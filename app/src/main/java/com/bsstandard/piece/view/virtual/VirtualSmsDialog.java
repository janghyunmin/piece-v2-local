package com.bsstandard.piece.view.virtual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.occupation.OccupationVerifyModel;
import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.dto.OccupationDTO;
import com.bsstandard.piece.data.viewmodel.OccupationViewModel;
import com.bsstandard.piece.data.viewmodel.SmsViewModel;
import com.bsstandard.piece.databinding.SmsDialogBinding;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.view.virtual
 * fileName       : VirtualSmsDialog
 * author         : piecejhm
 * date           : 2022/10/06
 * description    : 가상 계좌 생성시 인증번호 입력 BottomDialog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/06        piecejhm       최초 생성
 */


public class VirtualSmsDialog extends BottomSheetDialogFragment {
    // 공통 변수 - jhm 2022/10/06
    public Context context;
    public Window window;
    private View view;

    // ViewBinding - jhm 2022/10/06
    SmsDialogBinding smsDialogBinding;
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; // 총 3분 - jhm 2022/10/06
    final int COUNT_DONW_INTERVAL = 1000; // onTick 메소드를 호출할 간격 (1초) - jhm 2022/10/06

    // 인증번호 담는 변수 - jhm 2022/10/06
    private String authNo = ""; // 인증번호를 초기화해준다. - jhm 2022/10/06
    private String mchtTrdNo = "";
    private String chargeMoney = ""; // 넘겨받은 충전 금액값 - jhm 2022/10/06

    private SmsViewModel smsViewModel;
    private OccupationVerifyModel occupationVerifyModel;


    // 인증번호 재발송  - jhm 2022/10/07
    private static RetrofitService mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);


    // 인증번호 검증 ViewModel - jhm 2022/10/06
    private OccupationViewModel occupationViewModel;

    String accessToken = PrefsHelper.read("accessToken", "");
    String deviceId = PrefsHelper.read("deviceId", "");
    String memberId = PrefsHelper.read("memberId", "");

    public VirtualSmsDialog(Context context) {
        this.context = context;
    }


    @Override
    public void onStart() {
        super.onStart();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        smsDialogBinding = SmsDialogBinding.inflate(getLayoutInflater());
        smsDialogBinding.setLifecycleOwner(this);
        view = smsDialogBinding.getRoot();

        smsViewModel = new ViewModelProvider(this).get(SmsViewModel.class);

        // 인증번호 값 check - jhm 2022/10/06
        smsViewModel = new ViewModelProvider(this).get(SmsViewModel.class);
        smsDialogBinding.setSms(smsViewModel);


        // 인증번호 검증 ViewModel - jhm 2022/10/06
        occupationViewModel = new ViewModelProvider(this).get(OccupationViewModel.class);


        countDownTimer(); // 인증번호 입력 시간 countDown - jhm 2022/10/06
        verifyNumRetry(); // 인증번호 재발송 로직 - jhm 2022/10/06
        verifySetText(); // 인증번호 입력 후 확인 button 로직 - jhm 2022/10/06

        mchtTrdNo = getArguments().getString("mchtTrdNo");
        chargeMoney = getArguments().getString("chargeMoney");

        return view;
    }

    // 인증번호 입력 시간 countDown - jhm 2022/10/06
    public void countDownTimer() {
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DONW_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                smsDialogBinding.smsTime.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
            }
        }.start();
    }

    // 인증번호 재발송 로직 - jhm 2022/10/06
    public void verifyNumRetry() {
        // 재전송 버튼 - jhm 2022/10/06
        smsDialogBinding.smsRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
                countDownTimer();
                LogUtil.logE("인증번호 재발송");

                Call<OccupationDTO> reqSms = mInstance.postOccupation(
                        "Bearer " + accessToken,
                        deviceId,
                        memberId);
                reqSms.enqueue(new Callback<OccupationDTO>() {
                    @Override
                    public void onResponse(Call<OccupationDTO> call, Response<OccupationDTO> response) {
                        try {
                            if(response.isSuccessful()) {
                                if(response.body().getData() != null) {
                                    LogUtil.logE("재발송 성공 ! " + response.message());
                                } else {
                                    LogUtil.logE("재발송 성공 했지만 data null ! " + response.message());
                                }
                            } else {
                                LogUtil.logE("재발송 실패 ! " + response.message());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<OccupationDTO> call, Throwable t) {
                        t.printStackTrace();
                        LogUtil.logE("재발송 Error ! " + t.getMessage());
                    }
                });
            }
        });


        // 닫기 버튼 - jhm 2022/10/06
        smsDialogBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
                dismiss();
            }
        });
    }

    // 인증문자 입력 - jhm 2022/10/06
    public void verifySetText() {
        Observer<String> smsObserver = smsNumber -> authNo = smsNumber;
        smsViewModel.getVerifyNum().observe(this, smsObserver);
        smsViewModel.getVerifyNum().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String number) {
                authNo = number;
                LogUtil.logE("number : " + number);
                ConfirmOnclick();
                smsDialogBinding.smsError.setVisibility(View.GONE);
            }
        });
    }


    // 인증문자 입력 후 확인버튼 클릭 로직 - jhm 2022/10/06
    public void ConfirmOnclick() {
        // 입력한 인증번호 있으면 - jhm 2022/10/06
        if (smsDialogBinding.smsNumber.getText().toString().length() > 0) {
            smsDialogBinding.confirmBtn.setSelected(true);

            // 점유인증 검증시 필요 모델 - jhm 2022/10/06
            occupationVerifyModel = new OccupationVerifyModel(
                    authNo,
                    mchtTrdNo);


            // 확인 버튼 클릭 - jhm 2022/10/06
            smsDialogBinding.confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 검증 요청 - jhm 2022/10/06
                    occupationViewModel.postVerifyData(
                            "Bearer " + accessToken,
                            deviceId,
                            memberId,
                            occupationVerifyModel
                    );

                    // 인증번호 검증 리턴 데이터를 받는다 - jhm 2022/10/06
                    occupationViewModel.data.observe(getViewLifecycleOwner(), new Observer<OccupationDTO>() {
                        @Override
                        public void onChanged(OccupationDTO occupationDTO) {
                            try {
                                if (occupationDTO.getData() != null) {
                                    LogUtil.logE("status : " + occupationDTO.getStatus() + " | message : " + occupationDTO.getMessage());

                                    String rsMsg = occupationDTO.getMessage();
                                    // 점유 인증 검증 완료 - jhm 2022/10/06
                                    if (rsMsg.equals("휴대전화 점유 인증 검증 성공")) {
                                        LogUtil.logE("휴대폰 점유 인증 검증 성공");
                                        smsDialogBinding.smsError.setVisibility(View.GONE);
                                        dismiss();

                                        // 충전 신청 화면으로 이동 - jhm 2022/10/06
                                        Intent intent = new Intent(context, VirtualSuccessActivity.class);
                                        intent.putExtra("chargeMoney", chargeMoney);
                                        startActivity(intent);
                                        dismiss();

                                    }
                                    // 검증 실패시 재전송 - jhm 2022/10/06
                                    else {
                                        LogUtil.logE("휴대폰 점유 인증 검증 실패");
                                        smsDialogBinding.smsError.setText("유효하지 않은 인증번호입니다.");
                                        smsDialogBinding.smsError.setVisibility(View.VISIBLE);
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.logE("휴대폰 점유 인증 검증 실패 재시도 해야함");
                                smsDialogBinding.smsError.setText("유효하지 않은 인증번호입니다.");
                                smsDialogBinding.smsError.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }
            });

        }
        // 입력한 인증번호가 없으면 - jhm 2022/10/06
        else {
            smsDialogBinding.confirmBtn.setSelected(false);
        }
    }
}
