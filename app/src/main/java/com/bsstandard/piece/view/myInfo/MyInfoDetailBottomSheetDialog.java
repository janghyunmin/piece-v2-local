package com.bsstandard.piece.view.myInfo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.consent.UpdateConsentList;
import com.bsstandard.piece.data.datamodel.dmodel.member.MemberModifyModel;
import com.bsstandard.piece.data.datamodel.dmodel.member.NotificationModel;
import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.dto.MemberPutDTO;
import com.bsstandard.piece.data.viewmodel.ConsentViewModel;
import com.bsstandard.piece.data.viewmodel.GetUserViewModel;
import com.bsstandard.piece.data.viewmodel.MemberPutViewModel;
import com.bsstandard.piece.databinding.SlideDetailAddressBinding;
import com.bsstandard.piece.widget.utils.CustomDialog;
import com.bsstandard.piece.widget.utils.CustomDialogListener;
import com.bsstandard.piece.widget.utils.CustomDialogPassCodeListener;
import com.bsstandard.piece.widget.utils.Division;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;

/**
 * packageName    : com.bsstandard.piece.view.myInfo
 * fileName       : MyInfoDetailBottomSheetDialog
 * author         : piecejhm
 * date           : 2022/09/07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/07        piecejhm       최초 생성
 */
public class MyInfoDetailBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    // 공통 변수 - jhm 2022/09/04
    public Context context;
    public Window window;
    public Typeface typeface;
    public String ViewType = "";
    public ProgressDialog progressDialog;

    SlideDetailAddressBinding slideDetailAddressBinding;

    // 초기 변수 설정
    private View view;

    // 재사용 데이터 - jhm 2022/09/07
    private String roadAddr;
    private String jibunAddr;
    private String getDetailAddress;

    ArrayList<UpdateConsentList> consentList = new ArrayList<>(); // 약관 리스트 - jhm 2022/09/06

    // 회원 정보 수정 요청 Model - jhm 2022/09/07
    private MemberPutViewModel memberPutViewModel;
    private ConsentViewModel consentViewModel;
    private MemberModifyModel memberModifyModel;
    private GetUserViewModel getUserViewModel;

    // 주소변경 완료 후 Dialog - jhm 2022/09/07
    public CustomDialog customDialog;

    public MyInfoDetailBottomSheetDialog(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @SuppressLint("NotifyDataSetChanged")
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        slideDetailAddressBinding = SlideDetailAddressBinding.inflate(getLayoutInflater());
        slideDetailAddressBinding.setLifecycleOwner(this);
        view = slideDetailAddressBinding.getRoot();
        slideDetailAddressBinding.confirmBtn.setOnClickListener(this::onClick);


        memberPutViewModel = new ViewModelProvider(this).get(MemberPutViewModel.class);
        consentViewModel = new ViewModelProvider(this).get(ConsentViewModel.class);
        getUserViewModel = new ViewModelProvider(this).get(GetUserViewModel.class);

        roadAddr = PrefsHelper.read("roadAddr", "");
        jibunAddr = PrefsHelper.read("jibunAddr", "");

        // MyInfoBottomSheetDialog 에서 선택한 도로명 , 지번 주소 setting - jhm 2022/09/07
        slideDetailAddressBinding.roadAddr.setText(roadAddr);
        slideDetailAddressBinding.jibunAddr.setText(jibunAddr);

        // 상세주소 입력값 변수 set - jhm 2022/09/07
//        getDetailAddress = Objects.requireNonNull(slideDetailAddressBinding.detailAdress.getText()).toString();

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_btn:
                LogUtil.logE("확인 onClick..");
                String memberId = PrefsHelper.read("memberId", "");
                String name = PrefsHelper.read("name", "");
                String pinNumber = PrefsHelper.read("pinNumber", "");
                String cellPhoneNo = PrefsHelper.read("cellPhoneNo", "");
                String cellPhoneIdNo = PrefsHelper.read("cellPhoneIdNo", "");
                String birthDay = PrefsHelper.read("birthDay", "");
                String zipCode = PrefsHelper.read("zipCode", "");
                String baseAddress = roadAddr;
                String detailAddress = slideDetailAddressBinding.detailAdress.getText().toString();
                String ci = PrefsHelper.read("ci", "");
                String di = PrefsHelper.read("di", "");
                String gender = PrefsHelper.read("gender", "");
                String email = PrefsHelper.read("email", "");
                String isFido = PrefsHelper.read("isFido", "");

                NotificationModel notification = new NotificationModel(
                        PrefsHelper.read("memberId", ""),
                        "Y",
                        "Y",
                        "Y",
                        "Y"
                );

                consentList.clear();
                consentViewModel.getConsentData("SIGN").observe(this, response -> {
                    for (int index = 0; index < response.getData().size(); index++) {
                        consentList.add(new UpdateConsentList(
                                PrefsHelper.read("memberId", ""),
                                response.getData().get(index).getConsentCode(),
                                "Y"));
                    }
                });


                memberModifyModel = new MemberModifyModel(memberId, name, "", cellPhoneNo, cellPhoneIdNo,
                        birthDay, zipCode, baseAddress, detailAddress, ci, di, gender, email, isFido, notification, consentList);
                memberPutViewModel.putCallMemberData(memberModifyModel);
                memberPutViewModel.memberPutData.observe(Objects.requireNonNull(slideDetailAddressBinding.getLifecycleOwner()), new Observer<MemberPutDTO>() {
                    @Override
                    public void onChanged(MemberPutDTO memberPutDTO) {
                        LogUtil.logE("회원 정보 변경 완료 " + memberPutDTO.getStatus());

                        PrefsHelper.write("roadAddr", roadAddr);
                        PrefsHelper.write("jibunAddr", slideDetailAddressBinding.detailAdress.getText().toString());
                        dismiss();

                        CustomDialogListener customDialogListener = new CustomDialogListener() {
                            @Override
                            public void onCancelButtonClicked() {

                            }

                            @Override
                            public void onOkButtonClicked() {
                                LogUtil.logE("주소 변경 완료");
                                getUserViewModel.getUserData();
                                customDialog.dismiss();
                            }
                        };
                        CustomDialogPassCodeListener customDialogPassCodeListener = new CustomDialogPassCodeListener() {
                            @Override
                            public void onCancleButtonClicked() {
                            }

                            @Override
                            public void onRetryPassCodeButtonClicked() {
                            }
                        };
                        customDialog = new CustomDialog(context, Division.DIALOG_ADDRESS_CONFIRM, customDialogListener, customDialogPassCodeListener);
                        customDialog.show();
                    }
                });
                break;

        }
    }
}
