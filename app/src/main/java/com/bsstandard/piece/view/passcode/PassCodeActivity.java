package com.bsstandard.piece.view.passcode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.MemberPinModel;
import com.bsstandard.piece.data.datamodel.dmodel.consent.ConsentList;
import com.bsstandard.piece.data.datamodel.dmodel.join.ConsentModel;
import com.bsstandard.piece.data.datamodel.dmodel.join.DeviceInfoModel;
import com.bsstandard.piece.data.datamodel.dmodel.join.JoinModel;
import com.bsstandard.piece.data.datamodel.dmodel.join.NotificationInfoModel;
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
import com.bsstandard.piece.view.join.JoinActivity;
import com.bsstandard.piece.view.join.JoinSuccessActivity;
import com.bsstandard.piece.view.main.MainActivity;
import com.bsstandard.piece.widget.utils.CustomDialog;
import com.bsstandard.piece.widget.utils.CustomDialogListener;
import com.bsstandard.piece.widget.utils.CustomDialogPassCodeListener;
import com.bsstandard.piece.widget.utils.DeviceInfoUtil;
import com.bsstandard.piece.widget.utils.Division;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    public CustomDialog customDialog;


    // 회원가입 진행 Model - jhm 2022/06/30
    private MemberJoinViewModel memberJoinViewModel;
    private JoinModel joinModel;
    private ArrayList<ConsentModel> consent = new ArrayList<>();

    // 회원가입 후 완료 화면시 넘겨줄 데이터 - jhm 2022/07/03
    private String user_name = "";
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


    // 회원가입 유무 , 간편비밀번호 체크 변수 - jhm 2022/08/08
    public String isJoin;
    public String pinNumChk;


    // 더보기 - 인증 및 보안에서 간편 비밀번호 변경으로 들어왔는지 체크하기 위한 변수 - jhm 2022/09/16
    private String isRepassword;


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
        // refreshToken 발급 - jhm 2022/08/11
        //putTokenViewModel = new ViewModelProvider(this).get(PutTokenViewModel.class);


        passCodeViewModel = new ViewModelProvider(this).get(PassCodeViewModel.class);
        binding.setPasscode(passCodeViewModel);


        passCodeModelList.clear();
        passCodeVerifyList.clear();
        binding.passcodeTitleVerify.setVisibility(View.INVISIBLE); // 비밀번호가 일치하지 않아요 text

        InitFirebaseToken(); // Firebase Token - jhm 2022/07/01
        PinNumRandom(); // 간편 비밀번호 0~9 위치 랜덤 - jhm 2022/06/27
        PinNumberClear(); // 간편 비밀번호 입력값 모두 초기화 - jhm 2022/06/27
        KeyPadOnClick(); // 키패드 입력 로직 - jhm 2022/06/27
        AutoLogin(); // 회원가입 완료 후 자동로그인인지 판별하는 로직 - jhm 2022/07/03


        // 내정보 - 인증 및 보안 - 간편비밀번호의 경로로 들어왔을 경우 판별 변수 - jhm 2022/09/16

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                isRepassword = "";
            } else {
                isRepassword = extras.getString("isRepassword");
                isRepasswordLogic();
            }
        } else {
            isRepassword = (String) savedInstanceState.getSerializable("isRepassword");
            isRepasswordLogic();
        }

        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PinNumberDelete(); // 번호 뒤에서부터 지우는 로직 - jhm 2022/06/27
            }
        });

    }

    // 푸시토큰 발행 - jhm 2022/07/01
    private void InitFirebaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.d("TAG", "retrieve token successful : " + token);
                PushToken = token;
                PrefsHelper.removeKey(context, "firebaseToken");
                PrefsHelper.write("firebaseToken", PushToken);
            } else {
                Log.w("TAG", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task -> Log.v("TAG", "This is the token : " + task.getResult()));
    }


    private void isRepasswordLogic() {
        LogUtil.logE("isRepssword : " + isRepassword);
        if (isRepassword.equals("isRepassword")) {

            binding.passcodeTitleC.setText("현재 비밀번호를 입력해주세요.");
            MorePinUpdate();


        }
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

    public void KeyPadOnClick() {
        binding.code0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code0.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code1.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code2.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code3.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code4.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code5.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code6.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code7.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code8.getText().toString());
                }
                PinNumInput();
            }
        });
        binding.code9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCodeModelList.size() <= 6) {
                    passCodeModelList.add(binding.code9.getText().toString());
                }
                PinNumInput();
            }
        });
    }


    // 회원가입 진행시 키패드 입력 로직
    public void PinNumInput() {
        binding.passcodeTitleVerify.setVisibility(View.INVISIBLE);
        setActiveChk(); // UI Update - jhm 2022/06/28

        // 입력한 값이 6자리일때
        if (passCodeModelList.size() == 6) {
            isJoin = PrefsHelper.read("isJoin", "false");
            pinNumChk = PrefsHelper.read("pinNumChk", "false");

            // 입력받은 핀번호를 저장한다. - jhm 2022/07/06
            inputPinNumber = passCodeModelList.get(0) + passCodeModelList.get(1) + passCodeModelList.get(2)
                    + passCodeModelList.get(3) + passCodeModelList.get(4) + passCodeModelList.get(5);
            PrefsHelper.removeKey(context, "inputPinNumber");
            PrefsHelper.write("inputPinNumber", inputPinNumber); // 최초 저장 및 새로 저장 - jhm 2022/07/06
            LogUtil.logE("inputPinNumber : " + inputPinNumber);


            // 로그인 기록이 없다면 - jhm 2022/07/05
            if (isJoin.equals("false")) {
                // 비밀번호 최초 등록
                // 최초 회원가입 프로세스 실행 - jhm 2022/07/06
                PinNumTwoStep();

            } else {
                // 이전에 회원가입시 등록한 핀번호를 불러온다.
                LogUtil.logE("자동로그인 ");
                String sharedNum = PrefsHelper.read("inputPinNumber", "");
                LogUtil.logE("inputPinNumber : " + inputPinNumber); // 새로 입력한 핀번호 - jhm 2022/07/06
                LogUtil.logE("sharedNum : " + sharedNum);

                // 비밀번호 재설정 - jhm 2022/07/06
                if (pinNumChk.equals("true")) {
                    PinNumberUpdate();
                } else {
                    PinNumOneStep();
                }
            }



        }
    }

    // 더보기 -> 내정보 -> 인증 및 보안 으로 들어온 케이스에만 실행 - jhm 2022/09/16
    public void MorePinUpdate() {
        // 입력한 비밀번호와 이전에 등록되어있던 비밀번호가 다르다면 - jhm 2022/09/16
        if (!inputPinNumber.equals(pinNumber)) {
            binding.passcodeTitleVerify.setVisibility(View.VISIBLE);
        }
        // 그렇지 않다면 - jhm 2022/09/16
        else {
            // 변경할 비밀번호 입력 - jhm 2022/09/16
            PinNumberReUpdate();
        }
//        PinNumRandom(); // 간편 비밀번호 0~9 위치 랜덤 - jhm 2022/06/27
//        PinNumberClear(); // 간편 비밀번호 입력값 모두 초기화 - jhm 2022/06/27
//        KeyPadOnClick(); // 키패드 입력 로직 - jhm 2022/06/27



    }



    // 화면 재진입시 핀번호 입력 1회 - jhm 2022/07/05
    public void PinNumOneStep() {
        setActiveReset();
        setActiveChk();
        KeyPadOnClick();
        PinNumberClear();

        getAuthTokenViewModel.getAuthTokenData().observe(this, new Observer<BaseDTO>() {
            @Override
            public void onChanged(BaseDTO baseDTO) {
                if (baseDTO != null) {
                    LogUtil.logE("dto not null..");
                    if (baseDTO.getStatus().equals("OK")) {
                        // accessToken 검증 성공 후 pin 인증 - jhm 2022/08/11
                        // 회원 PIN 번호 검증
                        authPinViewModel.getAuthData().observe(Objects.requireNonNull(binding.getLifecycleOwner()), new Observer<AuthPinDTO>() {
                            @Override
                            public void onChanged(AuthPinDTO authPinDTO) {
                                if (authPinDTO != null) {
                                    if (authPinDTO.getStatus().equals("OK")) {
                                        Intent go_success = new Intent(context, MainActivity.class);
                                        startActivity(go_success);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                    } else {
                                        PinNumberReset();
                                    }
                                } else {
                                    PinNumberReset();
                                }
                            }
                        });
                    }
                } else {
                    // accessToken 검증 실패시 accessToken refresh - jhm 2022/08/11
                    LogUtil.logE("accessToken 검증 실패.. token refresh 진행");

                    String accessToken = PrefsHelper.read("accessToken", "");
                    String deviceId = PrefsHelper.read("deviceId", "");
                    String memberId = PrefsHelper.read("memberId", "");
                    String refreshToken = PrefsHelper.read("refreshToken", "");

                    LogUtil.logE("accessToken : " + accessToken);
                    LogUtil.logE("deviceId : " + deviceId);
                    LogUtil.logE("memberId : " + memberId);
                    LogUtil.logE("refreshToken : " + refreshToken);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("accessToken", "Bearer " + accessToken);
                    map.put("deviceId", deviceId);
                    map.put("grantType", "refresh_token");
                    map.put("memberId", memberId);
                    map.put("refreshToken", refreshToken);


                    Call<PostTokenDTO> postToken = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class).putRefreshToken(map);
                    postToken.enqueue(new Callback<PostTokenDTO>() {
                        @Override
                        public void onResponse(Call<PostTokenDTO> call, Response<PostTokenDTO> response) {
                            try {
                                if (response.isSuccessful()) {
                                    LogUtil.logE("isSuccess Refresh..");
                                    PrefsHelper.removeToken("expiredAt");
                                    PrefsHelper.removeToken("accessToken");
                                    PrefsHelper.removeToken("refreshToken");

                                    PrefsHelper.write("expiredAt", response.body().getData().getExpiredAt());
                                    PrefsHelper.write("accessToken", response.body().getData().getAccessToken());
                                    PrefsHelper.write("refreshToken", response.body().getData().getRefreshToken());

                                    LogUtil.logE("expiredAt : " + response.body().getData().getExpiredAt());
                                    LogUtil.logE("accessToken : " + response.body().getData().getAccessToken());
                                    LogUtil.logE("refreshToken : " + response.body().getData().getRefreshToken());

                                    Intent go_success = new Intent(context, MainActivity.class);
                                    startActivity(go_success);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                            } catch (Exception ex) {
                                // 에러발생시 초기 화면으로 return 시키기 - jhm 2022/08/22
                                LogUtil.logE("response fail.. " + response.body().getStatus());
                                LogUtil.logE("response fail.. " + response.body().getMessage());
                                PinNumberReset();
                                ex.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<PostTokenDTO> call, Throwable t) {
                            LogUtil.logE("isFail Refresh..");
                            PinNumberReset();
                        }
                    });
                }
            }
        });
    }


    // 간편비밀번호 재설정 Dialog - jhm 2022/07/05
    public void PinNumberReset() {
        // 실패시 비밀번호 재설정 - jhm 2022/07/05
        CustomDialogListener customDialogListener = new CustomDialogListener() {
            @Override
            public void onCancelButtonClicked() {

            }

            @Override
            public void onOkButtonClicked() {
            }
        };
        CustomDialogPassCodeListener customDialogPassCodeListener = new CustomDialogPassCodeListener() {
            @Override
            public void onCancleButtonClicked() {
                LogUtil.logE("닫기 onclick..");
                customDialog.dismiss();

                // 닫기 버튼 누른 후 로직 - jhm 2022/07/04
                passCodeModelList.clear(); // 기존 ArrayList 초기화 - jhm 2022/06/28
                setActiveReset();
                binding.passcodeTitleVerify.setVisibility(View.VISIBLE); // 비밀번호가 일치하지 않아요 text
                KeyPadOnClick();
            }

            @Override
            public void onRetryPassCodeButtonClicked() {
                LogUtil.logE("비밀번호 재설정");
                PrefsHelper.write("pinNumChk", "true");
                pinNumber = "";
                passCodeModelList.clear(); // 기존 ArrayList 초기화 - jhm 2022/06/28
                customDialog.dismiss();
                Intent go_join = new Intent(context, JoinActivity.class);
                context.startActivity(go_join);
                finish();
            }
        };
        customDialog = new CustomDialog(context, Division.DIALOG_C_PASSWORD, customDialogListener, customDialogPassCodeListener);
        customDialog.show();
        LogUtil.logE("간편비밀번호 재등록 필요..");
    }


    // 로그인 기록이 없어서 최초 pinNumber 등록 - jhm 2022/07/05
    public void PinNumTwoStep() {
        PinNumRandom(); // 키패드 초기화 및 숫자 재배열 - jhm 2022/06/28
        setActiveReset(); // UI 변경 - jhm 2022/06/28
        setActiveChk(); // UI 변경 - jhm 2022/07/05
        PinNumberValidate(Division.PIN_VIEWTYPE_1); // PinNumber - jhm 2022/07/05
    }

    // 로그인 기록이 있지만 비밀번호 변경으로 들어온 경우 - jhm 2022/07/05
    public void PinNumberUpdate() {
        PinNumRandom(); // 키패드 초기화 및 숫자 재배열 - jhm 2022/06/28
        setActiveReset(); // UI 변경 - jhm 2022/06/28
        setActiveChk(); // UI 변경 - jhm 2022/07/05
        PinNumberValidate(Division.PIN_VIEWTYPE_2); // PinNumber - jhm 2022/07/05
    }

    // 더보기 -> 비밀번호 재설정으로 들어온 경우 - jhm 2022/09/16
    private void PinNumberReUpdate() {
        PinNumRandom(); // 키패드 초기화 및 숫자 재배열 - jhm 2022/06/28
        setActiveReset(); // UI 변경 - jhm 2022/06/28
        setActiveChk(); // UI 변경 - jhm 2022/07/05
        PinNumberValidate(Division.PIN_VIEWTYPE_2); // PinNumber - jhm 2022/07/05
    }


    // 비밀번호 최초 입력 후 재입력 검증 로직 - jhm 2022/06/28
    public void PinNumberValidate(String division) {
        if (passCodeVerifyList.size() == 0) {
            passCodeVerifyList.addAll(passCodeModelList);
            passCodeModelList.clear(); // 기존 ArrayList 초기화 - jhm 2022/06/28
            setActiveReset();
            KeyPadOnClick();
            binding.passcodeTitleC.setText(context.getString(R.string.passcode_title_re));
        } else {
            System.out.println("passCodeVerifyList 사이즈가 0 이 아님");
            // 입력받은 Array 사이즈 동일한지 체크 - jhm 2022/06/30
            if (passCodeModelList.size() == passCodeVerifyList.size()) {
                // 사이즈가 동일하다면 전체 값을 비교 - jhm 2022/06/30
                // 값이 동일하다면 - jhm 2022/06/30
                if (passCodeModelList.containsAll(passCodeVerifyList)) {
                    System.out.println("두 값이 동일");

                    System.out.println(passCodeModelList.toString());
                    System.out.println(passCodeVerifyList.toString());

                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();

                    user_name = PrefsHelper.read("name", "");
                    PrefsHelper.write("inputPinNumber", inputPinNumber);
                    pinNumber = PrefsHelper.read("inputPinNumber", ""); // 입력받아 저장해놓은 핀번호를 불러온다.

                    LogUtil.logE("user_name : " + user_name);
                    LogUtil.logE("inputPinNumber : " + pinNumber);
                    // 기기 ID - jhm 2022/06/30
                    String deviceId = DeviceInfoUtil.getDeviceId(context);
                    PrefsHelper.write("deviceId", deviceId);


                    if (division.equals("최초")) {
                        LogUtil.logE("최초로 실행.." + division);
                        DeviceInfoModel deviceInfo = new DeviceInfoModel(
                                deviceId,
                                "MDO0101",
                                "",
                                "",
                                PushToken);

                        NotificationInfoModel notificationInfo = new NotificationInfoModel(
                                "Y",
                                "Y",
                                "Y",
                                "Y"
                        );

                        ArrayList<ConsentList> consentList = bundle.getParcelableArrayList("consentList");
                        for (int index = 0; index < consentList.size(); index++) {
                            consent.add(new ConsentModel(
                                    consentList.get(index).getConsentCode(),
                                    "Y"
                            ));
                        }

                        // 다음 회원가입 Join 진행 - jhm 2022/06/30
                        memberJoinViewModel = new ViewModelProvider(this).get(MemberJoinViewModel.class);

                        // db에 생년월일 insert 시 "-" 추가 - jhm 2022/07/06
                        String userBirth = bundle.getString("birthDay");
                        userBirth = userBirth.substring(0, 4) + "-" + userBirth.substring(4, 6) + "-" + userBirth.substring(6, 8);


                        joinModel = new JoinModel(
                                user_name,
                                pinNumber,
                                bundle.getString("cellPhoneNo"),
                                userBirth,
                                bundle.getString("ci"),
                                bundle.getString("di"),
                                bundle.getString("gender"),
                                bundle.getString("isFido"),
                                deviceInfo,
                                notificationInfo,
                                consent
                        );

                        Gson gson = new Gson();
                        System.out.println("joinModel : " + gson.toJson(joinModel));
                        // 회원가입 진행 - jhm 2022/06/30
                        memberJoinViewModel.postCallJoinData(joinModel);
                        // 진행 후 리턴 데이터를 받는다. - jhm 2022/06/30
                        memberJoinViewModel.joinData.observe(binding.getLifecycleOwner(), new Observer<JoinDTO>() {
                            @Override
                            public void onChanged(JoinDTO joinDTO) {
                                LogUtil.logE("joinVO : " + gson.toJson(joinDTO));


                                // 성공 후 Shared를 통하여 저장한다. - jhm 2022/07/03
                                PrefsHelper.init(context);
                                PrefsHelper.write("name", user_name);
                                PrefsHelper.write("inputPinNumber", pinNumber);

                                PrefsHelper.write("isJoin", "true"); // 자동로그인 여부 - jhm 2022/07/06
                                PrefsHelper.write("pinNumChk", "false"); // 비밀번호 재설정 여부 - jhm 2022/07/06

                                PrefsHelper.write("accessToken", joinDTO.getData().getAccessToken());
                                PrefsHelper.write("deviceId", joinDTO.getData().getDeviceId());
                                PrefsHelper.write("expiredAt", joinDTO.getData().getExpiredAt());
                                PrefsHelper.write("memberId", joinDTO.getData().getMemberId());
                                PrefsHelper.write("refreshToken", joinDTO.getData().getRefreshToken());


                            }
                        });

                        // 최초 실행 아니고 로그인 되어있는 상태에서 재 진입시 아래 로직 실행 - jhm 2022/08/08
                    } else {

                        authPinPutViewModel = new ViewModelProvider(this).get(AuthPinPutViewModel.class);
                        String _memberId = PrefsHelper.read("memberId", "");
                        pinNumber = PrefsHelper.read("inputPinNumber", "");
                        LogUtil.logE("최초로그인 아니고 핀번호 검증 실행 : " + pinNumber);


                        memberPinModel = new MemberPinModel(_memberId, pinNumber);
                        authPinPutViewModel.putCallAuthPinData(memberPinModel);
                        authPinPutViewModel.authPinData.observe(Objects.requireNonNull(binding.getLifecycleOwner()), new Observer<AuthPinDTO>() {
                            @Override
                            public void onChanged(AuthPinDTO authPinDTO) {
                                LogUtil.logE("authPinDTO : " + authPinDTO.getStatus());
                                PrefsHelper.write("inputPinNumber", pinNumber);

                            }
                        });
                        PrefsHelper.write("pinNumChk", "false");
                    }

                    // 회원가입 성공 후 토큰 발급 진행완료 - jhm 2022/07/01
                    Intent go_success = new Intent(context, JoinSuccessActivity.class);
                    go_success.putExtra("name", user_name);
                    startActivity(go_success);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
                // 사이즈는 동일하지만 값이 다르면 - jhm 2022/06/30
                else {
                    // 처음부터 새로 입력받는다. - jhm 2022/06/30
                    System.out.println("사이즈는 같지만 값이 다름");
                    binding.passcodeTitleC.setText(context.getString(R.string.passcode_title_c));
                    binding.passcodeTitleVerify.setVisibility(View.VISIBLE);
                    passCodeModelList.clear();
                    passCodeVerifyList.clear();
                    setActiveReset();
                    KeyPadOnClick();
                }
            }
        }
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
        binding.clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passCodeModelList.clear();
                setActiveChk();
            }
        });
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
        } else if (passCodeModelList.size() == 6) {
            binding.passcode1.setActivated(true);
            binding.passcode2.setActivated(true);
            binding.passcode3.setActivated(true);
            binding.passcode4.setActivated(true);
            binding.passcode5.setActivated(true);
            binding.passcode6.setActivated(true);
        }
    }


    public void AutoLogin() {
        memberId = PrefsHelper.read("memberId", "");
        LogUtil.logE("memberId : " + memberId);
        // memberId가 공백이 아니면 이미 회원가입 프로세스를 진행한 것이므로 다음 로직 실행 - jhm 2022/07/03
        if (!memberId.equals("")) {
            passCodeModelList.clear();
            setActiveReset();
            KeyPadOnClick();
        }
    }
}
