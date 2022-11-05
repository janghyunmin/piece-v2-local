package com.bsstandard.piece.view.passcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.MemberPinModel;
import com.bsstandard.piece.data.datamodel.dmodel.consent.ConsentList;
import com.bsstandard.piece.data.datamodel.dmodel.join.Consents;
import com.bsstandard.piece.data.datamodel.dmodel.join.DevieInfo;
import com.bsstandard.piece.data.datamodel.dmodel.join.JoinModel;
import com.bsstandard.piece.data.datamodel.dmodel.join.NotificationInfo;
import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.dto.AuthPinDTO;
import com.bsstandard.piece.data.dto.BaseDTO;
import com.bsstandard.piece.data.dto.JoinDTO;
import com.bsstandard.piece.data.dto.PostTokenDTO;
import com.bsstandard.piece.data.viewmodel.AuthPinPutViewModel;
import com.bsstandard.piece.data.viewmodel.AuthPinViewModel;
import com.bsstandard.piece.data.viewmodel.GetAuthTokenViewModel;
import com.bsstandard.piece.data.viewmodel.MemberJoinViewModel;
import com.bsstandard.piece.data.viewmodel.PutTokenViewModel;
import com.bsstandard.piece.databinding.ActivityPasscodeBinding;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.view.common.NetworkActivity;
import com.bsstandard.piece.view.join.JoinActivity;
import com.bsstandard.piece.view.join.JoinSuccessActivity;
import com.bsstandard.piece.view.main.MainActivity;
import com.bsstandard.piece.view.purchase.PurchaseLoadingActivity;
import com.bsstandard.piece.widget.utils.CustomDialogListener;
import com.bsstandard.piece.widget.utils.DeviceInfoUtil;
import com.bsstandard.piece.widget.utils.DialogManager;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.NetworkConnection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.view.passcode
 * fileName       : PassCodeActivity
 * author         : piecejhm
 * date           : 2022/06/26
 * description    : 간편비밀번호 등록 및 재등록 activity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/26        piecejhm       최초 생성
 */
public class PassCodeActivity extends AppCompatActivity {


    /**
     * 공통 - jhm 2022/05/09
     **/
    private Context context;
    private ActivityPasscodeBinding binding;
    private PassCodeViewModel passCodeViewModel;
    private ArrayList<String> passCodeModelList = new ArrayList<>(); // 첫번째 간편비밀번호 저장 ArrayList
    private ArrayList<String> passCodeVerifyList = new ArrayList<>(); // 두번째 간편비밀번호 저장 ArrayList
    public String PushToken = "";


    // 회원가입 진행 Model - jhm 2022/06/30
    private MemberJoinViewModel memberJoinViewModel;
    private JoinModel joinModel;
    private ArrayList<Consents> consents = new ArrayList<>();

    // 회원가입 후 완료 화면시 넘겨줄 데이터 - jhm 2022/07/03
    private String name = "";
    private String pinNumber;
    private String inputPinNumber;
    private String memberId;

    // 재로그인시 accessToken 검증 - jhm 2022/08/08
    private GetAuthTokenViewModel getAuthTokenViewModel;
    // put refreshToken - jhm 2022/08/11
    private PutTokenViewModel putTokenViewModel;


    // 비밀번호 재설정 이후 핀번호 검증 - jhm 2022/07/04
    private AuthPinViewModel authPinViewModel;

    // 간편비밀번호 변경 - jhm 2022/08/08
    private AuthPinPutViewModel authPinPutViewModel;
    private MemberPinModel memberPinModel;



    // 화면 진입시 필요한 분기 변수 - jhm 2022/10/17
    private String Step = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_passcode);
        binding.setLifecycleOwner(this);


        // accessToken 검증 - jhm 2022/08/08
        getAuthTokenViewModel = new ViewModelProvider(this).get(GetAuthTokenViewModel.class);
        // 간편 비밀번호 검증 - jhm 2022/08/11
        authPinViewModel = new ViewModelProvider(this).get(AuthPinViewModel.class);

        // 간편비밀번호 변경 - jhm 2022/10/18
        authPinPutViewModel = new ViewModelProvider(this).get(AuthPinPutViewModel.class);


        // refreshToken 발급 - jhm 2022/08/11
        //putTokenViewModel = new ViewModelProvider(this).get(PutTokenViewModel.class);


        passCodeViewModel = new ViewModelProvider(this).get(PassCodeViewModel.class);
        binding.setPasscode(passCodeViewModel);

        NetworkConnection networkConnection = new NetworkConnection(getApplicationContext());
        networkConnection.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                // 인터넷 연결되어있음 - jhm 2022/11/02
                if(isConnected) {
                    passCodeModelList.clear();
                    passCodeVerifyList.clear();

                    binding.backImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });

                    // 최초 로그인인지 , 최초 로그인이 아니고 재접속 인지 , 비밀번호 변경인지 분기 - jhm 2022/10/17
                    Intent intent = getIntent();
                    if (intent != null) {
                        Step = intent.getExtras().getString("Step");
                        LogUtil.logE("Step 진입 단계 :  " + Step);
                        switch (Step) {
                            // 최초 로그인일때 - jhm 2022/10/17
                            case "1":
                                PinNumRandom(); // 간편 비밀번호 0~9 위치 랜덤 - jhm 2022/06/27
                                PinNumberClear(); // 간편 비밀번호 입력값 모두 초기화 - jhm 2022/06/27
                                KeyPad("1"); // 키패드 입력 로직 - jhm 2022/06/27
                                break;

                            // 최초 로그인이 아닐때 - jhm 2022/10/18
                            case "2":
                                PinNumRandom(); // 간편 비밀번호 0~9 위치 랜덤 - jhm 2022/06/27
                                PinNumberClear(); // 간편 비밀번호 입력값 모두 초기화 - jhm 2022/06/27
                                KeyPad("2"); // 키패드 입력 로직 - jhm 2022/06/27
                                break;

                            // 내정보 - 인증 및 보안 - 비밀번호 재설정 케이스 일때 - jhm 2022/10/17
                            case "3":
                                PinNumRandom(); // 간편 비밀번호 0~9 위치 랜덤 - jhm 2022/06/27
                                PinNumberClear(); // 간편 비밀번호 입력값 모두 초기화 - jhm 2022/06/27
                                binding.passcodeTitleC.setText("현재 비밀번호를 입력해 주세요.");
                                KeyPad("3"); // 키패드 입력 로직 - jhm 2022/06/27
                                break;
                            // 포트폴리오 구매로 진입하였을 경우 - jhm 2022/10/21
                            case "5":
                                PinNumRandom(); // 간편 비밀번호 0~9 위치 랜덤 - jhm 2022/06/27
                                PinNumberClear(); // 간편 비밀번호 입력값 모두 초기화 - jhm 2022/06/27
                                KeyPad("5"); // 키패드 입력 로직 - jhm 2022/06/27
                                break;
                        }

                    }

                    binding.clear.setOnClickListener(new View.OnClickListener() {
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                            PinNumberDelete(); // 번호 뒤에서부터 지우는 로직 - jhm 2022/06/27
                        }
                    });

                }
                // 인터넷 연결 안되어있음 - jhm 2022/11/02
                else {
                    Intent intent = new Intent(context, NetworkActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    /***
     *
     * Step 1: 회원가입
     * Step 2: 재 로그인
     * Step 3: 간편 비밀번호 변경
     *
     ***/
    private void KeyPad(String Step) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        binding.code0.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code0.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code1.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code2.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code3.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code4.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code5.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code6.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code7.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code8.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
        binding.code9.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code9.getText().toString());
                }
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                PasswordChk(Step);
            }
        });
    }


    /**
     * 6자리 입력 후 한번 더 시도하는지 아닌지에 따라 분기 처리
     **/
    private void PasswordChk(String Step) {
        binding.passcodeTitleVerify.setVisibility(View.INVISIBLE);
        switch (Step) {
            case "1":
                setActiveChk(); // UI 변경 - jhm 2022/07/05

                // 입력한 값이 6자리일때
                if (passCodeModelList.size() == 6) {
                    // 입력받은 핀번호를 저장한다. - jhm 2022/07/06
                    inputPinNumber = passCodeModelList.get(0) + passCodeModelList.get(1) + passCodeModelList.get(2)
                            + passCodeModelList.get(3) + passCodeModelList.get(4) + passCodeModelList.get(5);

                    PrefsHelper.removeKey(context, "inputPinNumber");
                    PrefsHelper.write("inputPinNumber", inputPinNumber);


                    //  6자리가 되면 키패드 랜덤 배열 및 UI 초기화 진행 - jhm 2022/10/17
                    PinNumRandom(); // 키패드 초기화 및 숫자 재배열 - jhm 2022/06/28
                    setActiveReset(); // UI 변경 - jhm 2022/06/28

                    // 비밀번호 재입력 진행 - jhm 2022/10/17
                    if (passCodeVerifyList.size() == 0) {
                        passCodeVerifyList.addAll(passCodeModelList);
                        passCodeModelList.clear(); // 첫번째 입력 비밀번호 Array 초기화 - jhm 2022/10/18
                        KeyPad("1");
                        binding.passcodeTitleC.setText(context.getString(R.string.passcode_title_re));
                    } else {
                        // 첫번째 비밀번호 입력값 사이즈와 두번째 입력값 사이즈 비교 - jhm 2022/10/18
                        if (passCodeModelList.size() == passCodeVerifyList.size()) {
                            // 사이즈 동일하며, 서로 값이 일치하면 - jhm 2022/10/18
                            if (passCodeModelList.containsAll(passCodeVerifyList)) {
                                LogUtil.logE("첫번째 ArrayList : " + passCodeModelList.toArray());
                                LogUtil.logE("두번째 ArrayList : " + passCodeVerifyList.toArray());
                                LogUtil.logE("입력한 두 Array 가 모두 동일");

                                Intent intent = getIntent();
                                Bundle bundle = intent.getExtras();


                                pinNumber = PrefsHelper.read("inputPinNumber", "");

                                name = PrefsHelper.read("name", "");
//                                String deviceId = DeviceInfoUtil.getDeviceId(context);
                                String deviceId = DeviceInfoUtil.getUUID(context);
                                PrefsHelper.write("deviceId", deviceId);

                                LogUtil.logE("deviceId  : " + deviceId);
                                LogUtil.logE("fbToken  : " + PrefsHelper.read("fcmToken",""));


                                LogUtil.logE("최초 로그인 시도");
                                DevieInfo deviceInfo = new DevieInfo(
                                        deviceId,
                                        "MDO0101",
                                        "",
                                        PrefsHelper.read("fcmToken",""),
                                        PrefsHelper.read("fcmToken",""));

                                NotificationInfo notificationInfo = new NotificationInfo(
                                        "Y",
                                        "Y",
                                        "Y",
                                        "Y"
                                );

                                ArrayList<ConsentList> consentList = bundle.getParcelableArrayList("consentList");
                                for (int index = 0; index < consentList.size(); index++) {
                                    consents.add(new Consents(
                                            consentList.get(index).getConsentCode(),
                                            "Y"
                                    ));
                                    LogUtil.logE("code : " + consents.get(index).getConsentCode());
                                    LogUtil.logE("isAgreement : " + consents.get(index).getIsAgreement());

                                }

                                // 다음 회원가입 Join 진행 - jhm 2022/06/30
                                memberJoinViewModel = new ViewModelProvider(this).get(MemberJoinViewModel.class);

                                // db에 생년월일 insert 시 "-" 추가 - jhm 2022/07/06
                                String birthDay = bundle.getString("birthDay");
                                birthDay = birthDay.substring(0, 4) + "-" + birthDay.substring(4, 6) + "-" + birthDay.substring(6, 8);

                                joinModel = new JoinModel(
                                        name,
                                        pinNumber,
                                        bundle.getString("cellPhoneNo"),
                                        birthDay,
                                        bundle.getString("ci"),
                                        bundle.getString("di"),
                                        bundle.getString("gender"),
                                        bundle.getString("isFido"),
                                        deviceInfo,
                                        notificationInfo,
                                        consents
                                );

                                Gson gson = new Gson();

                                LogUtil.logE("name : " + name);
                                LogUtil.logE("pinNumber : " + pinNumber);
                                LogUtil.logE("cellPhoneNo : " + bundle.getString("cellPhoneNo"));
                                LogUtil.logE("birthDay : " + birthDay);
                                LogUtil.logE("ci : " + bundle.getString("ci"));
                                LogUtil.logE("di : " + bundle.getString("di"));
                                LogUtil.logE("gender : " + bundle.getString("gender"));
                                LogUtil.logE("isFido : " + bundle.getString("isFido"));

                                LogUtil.logE("deviceInfo" + gson.toJson(deviceInfo));
                                LogUtil.logE("notificationInfo" + gson.toJson(notificationInfo));
                                LogUtil.logE("consents" + gson.toJson(consents));

                                // 회원가입 진행 - jhm 2022/10/18
                                memberJoinViewModel.postCallJoinData(joinModel);
                                memberJoinViewModel.joinData.observe(Objects.requireNonNull(binding.getLifecycleOwner()), new Observer<JoinDTO>() {
                                    @Override
                                    public void onChanged(JoinDTO joinDTO) {
                                        try {
                                            if(joinDTO!=null) {
                                                // 성공 후 Shared를 통하여 저장한다. - jhm 2022/07/03
                                                PrefsHelper.write("name", name);
                                                PrefsHelper.write("inputPinNumber", pinNumber);

                                                PrefsHelper.write("pinNumChk", "false"); // 비밀번호 재설정 여부 - jhm 2022/07/06

                                                PrefsHelper.write("accessToken", joinDTO.getData().getAccessToken());
                                                PrefsHelper.write("deviceId", joinDTO.getData().getDeviceId());
                                                PrefsHelper.write("expiredAt", joinDTO.getData().getExpiredAt());
                                                PrefsHelper.write("memberId", joinDTO.getData().getMemberId()); // 화면 재진입시 해당값이 없으면 최초 로그인 시도 - jhm 2022/10/18
                                                PrefsHelper.write("refreshToken", joinDTO.getData().getRefreshToken());

                                                // 회원가입 성공 후 토큰 발급 진행완료 - jhm 2022/07/01
                                                Intent go_success = new Intent(context, JoinSuccessActivity.class);
                                                go_success.putExtra("name", name);
                                                startActivity(go_success);
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                finish();
                                            }
                                            else {
                                                LogUtil.logE("회원가입 실퍄");
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            LogUtil.logE("회원가입 실패 " + ex.getMessage());
                                        }
                                    }
                                });

                            }
                            // 사이즈는 같지만 서로 입력한 비밀번호 Array 값이 다름 - jhm 2022/10/18
                            else {
                                // 처음부터 새로 입력받는다. - jhm 2022/06/30
                                LogUtil.logE("사이즈는 같지만 값이 서로 다름");
                                binding.passcodeTitleVerify.setVisibility(View.VISIBLE);
                                passCodeModelList.clear();
                                passCodeVerifyList.clear();
                                setActiveReset();
                                KeyPad("1");
                            }
                        }
                    }
                }
                break;

            // 최초 로그인이 아니고 간편비밀번호 1번만 입력하는 로직 - jhm 2022/10/18
            case "2":
                setActiveChk(); // UI 변경 - jhm 2022/07/05

                LogUtil.logE("2");

                // 입력한 값이 6자리일때
                if (passCodeModelList.size() == 6) {
                    // 입력받은 핀번호를 저장한다. - jhm 2022/07/06
                    inputPinNumber = passCodeModelList.get(0) + passCodeModelList.get(1) + passCodeModelList.get(2)
                            + passCodeModelList.get(3) + passCodeModelList.get(4) + passCodeModelList.get(5);

                    // 저장되어있는 비밀번호와 입력한 비밀번호 일치 여부 판별 - jhm 2022/10/18
                    if (!PrefsHelper.read("inputPinNumber", "").equals(inputPinNumber)) {
                        // 검증 실패시 간편비밀번호 재설정 Dialog 출력 - jhm 2022/10/18
                        binding.passcodeTitleVerify.setVisibility(View.VISIBLE); // 비밀번호가 일치하지 않아요 text
                        PinNumberReset();
                    } else {
                        // 토큰 검증 - jhm 2022/10/18
                        getAuthTokenViewModel.getAuthTokenData().observe(this, new Observer<BaseDTO>() {
                            @Override
                            public void onChanged(BaseDTO baseDTO) {
                                try {
                                    if (baseDTO != null) {
                                        if (baseDTO.getStatus().equals("OK")) {
                                            // accessToken 검증 후 회원 PIN 번호 검증 - jhm 2022/10/18
                                            authPinViewModel.getAuthData().observe(Objects.requireNonNull(binding.getLifecycleOwner()), new Observer<AuthPinDTO>() {
                                                @Override
                                                public void onChanged(AuthPinDTO authPinDTO) {
                                                    try {
                                                        if (authPinDTO != null) {
                                                            if (authPinDTO.getStatus().equals("OK")) {
                                                                // 검증 완료 후 MainActivity 이동 - jhm 2022/10/18
                                                                Intent go_success = new Intent(context, MainActivity.class);
                                                                startActivity(go_success);
                                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                                finish();
                                                            } else {
                                                                // 검증 실패시 간편비밀번호 재설정 Dialog 출력 - jhm 2022/10/18
                                                                binding.passcodeTitleVerify.setVisibility(View.VISIBLE); // 비밀번호가 일치하지 않아요 text
                                                                PinNumberReset();
                                                            }
                                                        } else {
                                                            // 검증 실패시 간편비밀번호 재설정 Dialog 출력 - jhm 2022/10/18
                                                            binding.passcodeTitleVerify.setVisibility(View.VISIBLE); // 비밀번호가 일치하지 않아요 text
                                                            PinNumberReset();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        LogUtil.logE("Pin 검증 Error !");
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        // accessToken 검증 실패시 토큰 재발행 후 재검증 - jhm 2022/10/18
                                        String deviceId = PrefsHelper.read("deviceId", "");
                                        String grantType = "authorization_code";
                                        String memberId = PrefsHelper.read("memberId", "");

                                        Call<PostTokenDTO> postToken = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class).postAccessToken(
                                                deviceId,
                                                grantType,
                                                memberId
                                        );

                                        postToken.enqueue(new Callback<PostTokenDTO>() {
                                            @Override
                                            public void onResponse(Call<PostTokenDTO> call, Response<PostTokenDTO> response) {
                                                try {
                                                    if (response.body().getData() != null) {
                                                        PrefsHelper.removeToken("expiredAt");
                                                        PrefsHelper.removeToken("accessToken");
                                                        PrefsHelper.removeToken("refreshToken");

                                                        PrefsHelper.write("expiredAt", response.body().getData().getExpiredAt());
                                                        PrefsHelper.write("accessToken", response.body().getData().getAccessToken());
                                                        PrefsHelper.write("refreshToken", response.body().getData().getRefreshToken());


                                                        Intent go_success = new Intent(context, MainActivity.class);
                                                        startActivity(go_success);
                                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                        finish();
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                    // 에러호출페이지 생성 - jhm 2022/11/01
                                                    LogUtil.logE("Token 재 생성 실패..");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PostTokenDTO> call, Throwable t) {
                                                t.printStackTrace();
                                                LogUtil.logE("POST member/auth Fail..");
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtil.logE("토큰 검증 catch Error !");
                                }
                            }
                        });
                    }
                }

                break;

            case "3":
                setActiveChk(); // UI 변경 - jhm 2022/07/05
                // 입력한 값이 6자리일때
                if (passCodeModelList.size() == 6) {
                    // 입력받은 핀번호를 저장한다. - jhm 2022/07/06
                    inputPinNumber = passCodeModelList.get(0) + passCodeModelList.get(1) + passCodeModelList.get(2)
                            + passCodeModelList.get(3) + passCodeModelList.get(4) + passCodeModelList.get(5);

                    // 입력한 비밀번호와 등록되어있던 비밀번호 일치 여부 판별 - jhm 2022/10/18
                    if (!PrefsHelper.read("inputPinNumber", "").equals(inputPinNumber)) {
                        LogUtil.logE("비밀번호 불일치");
                        binding.passcodeTitleVerify.setVisibility(View.VISIBLE);
                        //  6자리가 되면 키패드 랜덤 배열 및 UI 초기화 진행 - jhm 2022/10/17
                        PinNumRandom(); // 키패드 초기화 및 숫자 재배열 - jhm 2022/06/28
                        setActiveReset(); // UI 변경 - jhm 2022/06/28
                        passCodeModelList.clear();
                        passCodeVerifyList.clear();
                    } else {
                        LogUtil.logE("현재 등록되어있는 비밀번호와 일치");
                        //  6자리가 되면 키패드 랜덤 배열 및 UI 초기화 진행 - jhm 2022/10/17
                        PinNumRandom(); // 키패드 초기화 및 숫자 재배열 - jhm 2022/06/28
                        setActiveReset(); // UI 변경 - jhm 2022/06/28
                        passCodeModelList.clear();
                        passCodeVerifyList.clear();

                        PinNumberUpdate();
                    }
                }
                break;


            // 비밀번호 변경처리 - jhm 2022/10/18
            case "4":
                LogUtil.logE("비밀번호 변경 처리");
                setActiveChk(); // UI 변경 - jhm 2022/07/05

                // 입력한 값이 6자리일때
                if (passCodeModelList.size() == 6) {
                    LogUtil.logE("변경할 비밀번호 6자리 입력 완료");

                    // 입력받은 핀번호를 저장한다. - jhm 2022/07/06
                    inputPinNumber = passCodeModelList.get(0) + passCodeModelList.get(1) + passCodeModelList.get(2)
                            + passCodeModelList.get(3) + passCodeModelList.get(4) + passCodeModelList.get(5);

                    //  6자리가 되면 키패드 랜덤 배열 및 UI 초기화 진행 - jhm 2022/10/17
                    PinNumRandom(); // 키패드 초기화 및 숫자 재배열 - jhm 2022/06/28
                    setActiveReset(); // UI 변경 - jhm 2022/06/28

                    // 비밀번호 재입력 진행 - jhm 2022/10/17
                    if (passCodeVerifyList.size() == 0) {
                        passCodeVerifyList.addAll(passCodeModelList);
                        passCodeModelList.clear(); // 첫번째 입력 비밀번호 Array 초기화 - jhm 2022/10/18
                        KeyPad("4");
                        binding.passcodeTitleC.setText(context.getString(R.string.passcode_title_re));
                    } else {
                        // 첫번째 비밀번호 입력값 사이즈와 두번째 입력값 사이즈 비교 - jhm 2022/10/18
                        if (passCodeModelList.size() == passCodeVerifyList.size()) {
                            // 사이즈 동일하며, 서로 값이 일치하면 - jhm 2022/10/18
                            if (passCodeModelList.containsAll(passCodeVerifyList)) {
                                LogUtil.logE("입력한 두 Array 가 모두 동일");

                                PrefsHelper.removeKey(context, "inputPinNumber");
                                PrefsHelper.write("inputPinNumber", inputPinNumber);

                                memberId = PrefsHelper.read("memberId","");
                                memberPinModel = new MemberPinModel(memberId, inputPinNumber);
                                authPinPutViewModel.putCallAuthPinData(memberPinModel);

                                DialogManager.INSTANCE.openDalog(context, "비밀번호 변경 완료", "확인을 누르면 설정으로 이동해요.", "확인",this);

                            }
                            // 사이즈는 같지만 서로 입력한 비밀번호 Array 값이 다름 - jhm 2022/10/18
                            else {
                                // 처음부터 새로 입력받는다. - jhm 2022/06/30
                                LogUtil.logE("사이즈는 같지만 값이 서로 다름");
                                binding.passcodeTitleC.setText(context.getString(R.string.passcode_title_c));
                                binding.passcodeTitleVerify.setVisibility(View.VISIBLE);
                                passCodeModelList.clear();
                                passCodeVerifyList.clear();
                                setActiveReset();
                                KeyPad("4");
                            }

                        }
                    }
                }
                break;

            //  - jhm 2022/10/21
            case "5":
                setActiveChk(); // UI 변경 - jhm 2022/07/05
                // 입력한 값이 6자리일때
                if (passCodeModelList.size() == 6) {
                    // 입력받은 핀번호를 저장한다. - jhm 2022/07/06
                    inputPinNumber = passCodeModelList.get(0) + passCodeModelList.get(1) + passCodeModelList.get(2)
                            + passCodeModelList.get(3) + passCodeModelList.get(4) + passCodeModelList.get(5);

                    // 입력한 비밀번호와 등록되어있던 비밀번호 일치 여부 판별 - jhm 2022/10/18
                    if (!PrefsHelper.read("inputPinNumber", "").equals(inputPinNumber)) {
                        LogUtil.logE("비밀번호 불일치");
                        binding.passcodeTitleVerify.setVisibility(View.VISIBLE);
                        //  6자리가 되면 키패드 랜덤 배열 및 UI 초기화 진행 - jhm 2022/10/17
                        PinNumRandom(); // 키패드 초기화 및 숫자 재배열 - jhm 2022/06/28
                        setActiveReset(); // UI 변경 - jhm 2022/06/28
                        passCodeModelList.clear();
                        passCodeVerifyList.clear();
                    } else {
                        LogUtil.logE("피스 구매 완료 처리 진행..");

                        Intent intent = new Intent(this , PurchaseLoadingActivity.class);
                        overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                        );
                        startActivity(intent);
                        finish();

                    }
                }
                break;
        }
    }

    // 회원 PIN 번호 변경 - jhm 2022/10/18
    public void PinNumberUpdate() {
        LogUtil.logE("회원 PIN 번호 변경");
        binding.passcodeTitleC.setText("새로운 비밀번호를 입력해 주세요.");
        setActiveChk(); // UI 변경 - jhm 2022/07/05
        setActiveReset();

        KeyPad("4");
    }

    // 간편비밀번호 재설정 Dialog - jhm 2022/07/05
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void PinNumberReset() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // 안드로이드 기본제공 진동패턴 효과 생성
        @SuppressLint({"NewApi", "LocalSuppress"})
        VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
        vibrator.vibrate(vibrationEffect);

        LogUtil.logE("간편비밀번호 재등록 필요..");
        // 실패시 비밀번호 재설정 - jhm 2022/07/05
        CustomDialogListener customDialogListener = new CustomDialogListener() {
            @Override
            public void onCancelButtonClicked() {
                // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                passCodeModelList.clear(); // 기존 ArrayList 초기화 - jhm 2022/06/28
                setActiveReset();
                binding.passcodeTitleVerify.setVisibility(View.GONE); // 비밀번호가 일치하지 않아요 text
            }

            @Override
            public void onOkButtonClicked() {

                LogUtil.logE("비밀번호 재설정");
                PrefsHelper.write("pinNumChk", "true");
                pinNumber = "";
                passCodeModelList.clear(); // 기존 ArrayList 초기화 - jhm 2022/06/28
                Intent go_join = new Intent(context, JoinActivity.class);
                context.startActivity(go_join);
                finish();

                binding.passcodeTitleVerify.setVisibility(View.GONE); // 비밀번호가 일치하지 않아요 text
            }
        };

        DialogManager.INSTANCE.openTwoBtnDialog(context, "비밀번호가 틀렸어요", "비밀번호를 잊으셨나요?", customDialogListener, "비밀번호 재설정");

    }


    // 입력비밀번호 뒤에서부터 한자리씩 지우기 - jhm 2022/06/27
    public void PinNumberDelete() {

        if (passCodeModelList.size() > 0) {
            LogUtil.logE("size 0 보다 큼");

            try {
                int index = passCodeModelList.size() - 1;
                passCodeModelList.remove(index);
                setActiveChk();
                System.out.println(passCodeModelList);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }
    }

    // 입력한 간편비밀번호 모두 초기화 - jhm 2022/06/27
    public void PinNumberClear() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        binding.clearText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                vibrator.vibrate(VibrationEffect.createOneShot(100,100));
                passCodeModelList.clear();
                setActiveChk();
            }
        });
    }

    // 키패드에 랜덤으로 숫자를 뽑아 넣어준다. - jhm 2022/06/28
    public void PinNumRandom() {
        List<String> padList = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            padList.add(String.valueOf(index));
        }
        Collections.shuffle(padList);
        passCodeViewModel.Code0.setValue(padList.get(0));
        passCodeViewModel.Code1.setValue(padList.get(1));
        passCodeViewModel.Code2.setValue(padList.get(2));
        passCodeViewModel.Code3.setValue(padList.get(3));
        passCodeViewModel.Code4.setValue(padList.get(4));
        passCodeViewModel.Code5.setValue(padList.get(5));
        passCodeViewModel.Code6.setValue(padList.get(6));
        passCodeViewModel.Code7.setValue(padList.get(7));
        passCodeViewModel.Code8.setValue(padList.get(8));
        passCodeViewModel.Code9.setValue(padList.get(9));
    }


    // 간편비밀번호 초록색 점 표시 - jhm 2022/08/04
    public void setActiveReset() {
        binding.passcode1.setActivated(false);
        binding.passcode2.setActivated(false);
        binding.passcode3.setActivated(false);
        binding.passcode4.setActivated(false);
        binding.passcode5.setActivated(false);
        binding.passcode6.setActivated(false);
    }

    // 간편비밀번호 Ui - jhm 2022/06/28
    public void setActiveChk() {
        if (passCodeModelList.size() == 0) {
            binding.passcode1.setActivated(false);
            binding.passcode2.setActivated(false);
            binding.passcode3.setActivated(false);
            binding.passcode4.setActivated(false);
            binding.passcode5.setActivated(false);
            binding.passcode6.setActivated(false);
        } else if (passCodeModelList.size() == 1) {
            binding.passcode1.setActivated(true);
            binding.passcode2.setActivated(false);
            binding.passcode3.setActivated(false);
            binding.passcode4.setActivated(false);
            binding.passcode5.setActivated(false);
            binding.passcode6.setActivated(false);
        } else if (passCodeModelList.size() == 2) {
            binding.passcode1.setActivated(true);
            binding.passcode2.setActivated(true);
            binding.passcode3.setActivated(false);
            binding.passcode4.setActivated(false);
            binding.passcode5.setActivated(false);
            binding.passcode6.setActivated(false);
        } else if (passCodeModelList.size() == 3) {
            binding.passcode1.setActivated(true);
            binding.passcode2.setActivated(true);
            binding.passcode3.setActivated(true);
            binding.passcode4.setActivated(false);
            binding.passcode5.setActivated(false);
            binding.passcode6.setActivated(false);
        } else if (passCodeModelList.size() == 4) {
            binding.passcode1.setActivated(true);
            binding.passcode2.setActivated(true);
            binding.passcode3.setActivated(true);
            binding.passcode4.setActivated(true);
            binding.passcode5.setActivated(false);
            binding.passcode6.setActivated(false);
        } else if (passCodeModelList.size() == 5) {
            binding.passcode1.setActivated(true);
            binding.passcode2.setActivated(true);
            binding.passcode3.setActivated(true);
            binding.passcode4.setActivated(true);
            binding.passcode5.setActivated(true);
            binding.passcode6.setActivated(false);
        } else {
            binding.passcode1.setActivated(true);
            binding.passcode2.setActivated(true);
            binding.passcode3.setActivated(true);
            binding.passcode4.setActivated(true);
            binding.passcode5.setActivated(true);
            binding.passcode6.setActivated(true);
        }
    }
}
