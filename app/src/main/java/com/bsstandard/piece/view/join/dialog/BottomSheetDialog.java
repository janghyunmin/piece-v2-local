package com.bsstandard.piece.view.join.dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.consent.ConsentList;
import com.bsstandard.piece.data.datamodel.dmodel.sms.CallSmsAuthModel;
import com.bsstandard.piece.data.dto.CallSmsAuthDTO;
import com.bsstandard.piece.data.viewmodel.CallSmsAuthViewModel;
import com.bsstandard.piece.data.viewmodel.ConsentDetailViewModel;
import com.bsstandard.piece.data.viewmodel.ConsentViewModel;
import com.bsstandard.piece.databinding.SlideupConsentBinding;
import com.bsstandard.piece.databinding.SlideupPhoneBinding;
import com.bsstandard.piece.view.adapter.consent.ConsentAdapter;
import com.bsstandard.piece.view.webview.ConsentDetailWebView;
import com.bsstandard.piece.widget.utils.Division;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : BottomSheetDialog
 * author         : piecejhm
 * date           : 2022/05/09
 * description    : 통신사 선택 및 약관동의 BottomSheet 공통
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/09        piecejhm       최초 생성
 */
public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    // 공통변수 - jhm 2022/06/15
    public Context context;
    public Window window;
    public Typeface typeface;
    public String ViewType = "";
    public ProgressDialog progressDialog;

    // ViewType에 따른 화면 viewbinding - jhm 2022/06/22
    SlideupPhoneBinding slideupPhoneBinding;
    SlideupConsentBinding slideupConsentBinding;


    // 초기 변수 설정
    private View view;
    // 인터페이스 변수
    private BottomSheetListener bottomSheetListener;

    // selected String 변수
    private String selected_ = "";

    // 약관조회 리스트 - jhm 2022/06/20
    private String marketingTitle = "";
    private String marketingContent = "";
    ConsentViewModel consentViewModel;
    ConsentDetailViewModel consentDetailViewModel;
    ArrayList<ConsentList> consentList = new ArrayList<>();
    ConsentAdapter consentAdapter;
    RecyclerView.LayoutManager layoutManager;
    int chkCnt = 7;


    // 문자발송 - jhm 2022/06/22
    CallSmsAuthViewModel callSmsAuthViewModel;
    public String txSeqNo;
    public String name;
    public String birthday;
    public String sexCd;
    public String ntvFrnrCd;
    public String telComcd;
    public String telNo;

    // 문자발송 실패시 alert dialog - jhm 2022/06/23
    BottomSmsAlert bottomSmsAlert;

    // 문자발송 성공시 bottom dialog - jhm 2022/06/23
    BottomSheetSmsDialog bottomSheetSmsDialog;



    public BottomSheetDialog(Context context) {
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

        typeface = ResourcesCompat.getFont(getContext(), R.font.pretendard_bold);
        bottomSheetListener = (BottomSheetListener) getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        bottomSmsAlert = BottomSmsAlert.getInstance(getContext());


        ViewType = getArguments().getString("viewType");
        switch (ViewType) {
            // 휴대폰 선택 Dialog - jhm 2022/06/20
            case "phone_select":
                slideupPhoneBinding = SlideupPhoneBinding.inflate(getLayoutInflater());
                slideupPhoneBinding.setLifecycleOwner(this);
                view = slideupPhoneBinding.getRoot();

                slideupPhoneBinding.sktTv.setOnClickListener(this::onClick);
                slideupPhoneBinding.ktTv.setOnClickListener(this::onClick);
                slideupPhoneBinding.lgTv.setOnClickListener(this::onClick);
                slideupPhoneBinding.sktSubTv.setOnClickListener(this::onClick);
                slideupPhoneBinding.ktSubTv.setOnClickListener(this::onClick);
                slideupPhoneBinding.lgSubTv.setOnClickListener(this::onClick);
                slideupPhoneBinding.closeBtn.setOnClickListener(this::onClick);

                break;

            // 약관동의 Dialog - jhm 2022/06/20
            case "terms_select":
                slideupConsentBinding = SlideupConsentBinding.inflate(getLayoutInflater());
                slideupConsentBinding.setLifecycleOwner(this);
                view = slideupConsentBinding.getRoot();

                slideupConsentBinding.closeBtn.setOnClickListener(this::onClick);
                consentViewModel = new ViewModelProvider(this).get(ConsentViewModel.class); // 약관 조회 - jhm 2022/06/21
                consentDetailViewModel = new ViewModelProvider(this).get(ConsentDetailViewModel.class); // 약관 상세 조회 - jhm 2022/06/21
                callSmsAuthViewModel = new ViewModelProvider(this).get(CallSmsAuthViewModel.class); // req sms - jhm 2022/06/22

                consentViewModel.getConsentData("SIGN").observe(this, response -> {
                    progressDialog.show();
                    consentList.clear();
                    for (int index = 0; index < response.getData().size(); index++) {
                        consentList.add(new ConsentList(
                                true,
                                response.getData().get(index).getConsentCode(),
                                response.getData().get(index).getConsentGroup(),
                                response.getData().get(index).getConsentTitle(),
                                response.getData().get(index).getConsentContents(),
                                response.getData().get(index).getIsMandatory(),
                                response.getData().get(index).getDisplayOrder(),
                                response.getData().get(index).getCreatedAt(),
                                "Y",
                                Division.CONSENT
                        ));
                    }
                    // 마케팅 활용 Text - jhm 2022/06/20
                    marketingTitle = consentList.get(7).getConsentTitle();
                    marketingContent = consentList.get(7).getConsentContents();
                    slideupConsentBinding.cAgree8Tv.setText(consentList.get(7).getConsentTitle());
                    consentList.remove(consentList.size() - 1);
                    ConsentUI();
                });
                break;
        }
        return view;
    }

    // 약관 목록 데이터를 받아온 후 화면을 그려준다 - jhm 2022/06/21
    @SuppressLint("NotifyDataSetChanged")
    private void ConsentUI() {
        LogUtil.logE("ConsentUi call..");
        consentAdapter = new ConsentAdapter(getContext(), consentList);
        layoutManager = new LinearLayoutManager(getContext());
        slideupConsentBinding.consentRV.setLayoutManager(layoutManager);
        slideupConsentBinding.consentRV.setHasFixedSize(true);
        slideupConsentBinding.consentRV.setAdapter(consentAdapter);
        progressDialog.dismiss();

        // 모두 동의 초기 셋팅 로직 - jhm 2022/06/20
        slideupConsentBinding.allChk.setChecked(false);
//        consentAdapter.selectAll();
        setEnabledBtn(0);


        // 전체 체크박스 클릭시 - jhm 2022/06/21
        slideupConsentBinding.allChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideupConsentBinding.allChk.isChecked()) {
                    slideupConsentBinding.marketingAgree.setChecked(true);
                    consentAdapter.selectAll();
                    setEnabledBtn(7);
                } else {
                    slideupConsentBinding.marketingAgree.setChecked(false);
                    consentAdapter.unselectAll();
                    setEnabledBtn(0);
                }
            }
        });

        // 필수 개별체크시 모두동의 해제 및 선택 로직 - jhm 2022/06/20
        consentAdapter.setOnClickListener(new ConsentAdapter.onClickListener() {
            @Override
            public void onCheck(Boolean isCheck) {
                if (isCheck) chkCnt++;
                else chkCnt--;

                // chkCnt가 리스트 갯수와 같으면 전체선택도 체크 - jhm 2022/06/20
                slideupConsentBinding.allChk.setChecked(chkCnt == consentList.size());

                LogUtil.logE("chk : " + chkCnt);
                LogUtil.logE("consentList.size() : " + consentList.size());
                setEnabledBtn(chkCnt);
            }
        });

        // 마케팅 선택 체크박스 로직 - jhm 2022/06/21
        slideupConsentBinding.marketingAgree.setChecked(false);
        slideupConsentBinding.marketingAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.logE("marketing : " + slideupConsentBinding.marketingAgree.isChecked());
                if (slideupConsentBinding.marketingAgree.isChecked()) {
                    slideupConsentBinding.allChk.setChecked(true);
                } else {
                    slideupConsentBinding.allChk.setChecked(false);
                }
            }
        });
        consentAdapter.notifyDataSetChanged();

        // 약관 디테일 onClick - jhm 2022/06/21
        consentAdapter.setDetailClick(new ConsentAdapter.ConcentDetail() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(context, ConsentDetailWebView.class);
                intent.putExtra("consentTitle", consentList.get(position).getConsentTitle());
                intent.putExtra("consentContents", consentList.get(position).getConsentContents());
                getActivity().overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
                startActivity(intent);
            }
        });

        // 개인정보 수집 동의 detail - jhm 2022/06/22
        slideupConsentBinding.cAgree8Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConsentDetailWebView.class);
                intent.putExtra("consentTitle", marketingTitle);
                intent.putExtra("consentContents", marketingContent);
                getActivity().overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
                startActivity(intent);
            }
        });
    }

    // 버튼 활성화 / 비활성화 로직 - jhm 2022/06/21
    public void setEnabledBtn(int cnt) {
        if (cnt == consentList.size()) {
            slideupConsentBinding.confirmBtn.setSelected(true);


            // 약관 모두 동의 후 확인버튼 로직 - jhm 2022/06/22
            slideupConsentBinding.confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txSeqNo = getArguments().getString("");
                    name = getArguments().getString("name");
                    birthday = getArguments().getString("birthday");
                    sexCd = getArguments().getString("sexCd");
                    ntvFrnrCd = getArguments().getString("ntvFrnrCd");
                    telComcd = getArguments().getString("telComCd");
                    telNo = getArguments().getString("telNo");

                    // 인증번호 요청시 모델 - jhm 2022/06/22
                    CallSmsAuthModel callSmsAuthModel = new CallSmsAuthModel(
                            name,
                            birthday,
                            sexCd,
                            ntvFrnrCd,
                            telComcd,
                            telNo,
                            "Y",
                            "Y",
                            "Y",
                            "Y"
                    );
                    // 인증번호 요청을 한다. - jhm 2022/06/22
                    callSmsAuthViewModel.postCallSmsAuthData(callSmsAuthModel);

                    // 인증번호 요청 후 데이터를 받아온다. - jhm 2022/06/22
                    callSmsAuthViewModel.callSmsAuthData.observe(getViewLifecycleOwner(), new Observer<CallSmsAuthDTO>() {
                        @Override
                        public void onChanged(CallSmsAuthDTO callSmsAuthDTO) {
                            try {
                                if(callSmsAuthDTO.getData() != null) {
                                    LogUtil.logE("dto : " + callSmsAuthDTO.getData().getRsltMsg());
                                    LogUtil.logE("txSeqNo : " + callSmsAuthDTO.getData().getTxSeqNo());
                                    txSeqNo = callSmsAuthDTO.getData().getTxSeqNo();

                                    String Contents = callSmsAuthDTO.getData().getRsltMsg();
                                    GetResponseSMS(Contents); // 인증번호 dialog 호출
                                    dismiss(); // 인증번호 dialog 호출 후 약관동의 dialog 는 닫는다.
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtil.logE("인증번호 검증 실패 재시도 처리");
                            }


                        }
                    });
                }
            });
        } else {
            slideupConsentBinding.confirmBtn.setSelected(false);
        }
    }

    public void GetResponseSMS(String Contents){
        bottomSheetSmsDialog = new BottomSheetSmsDialog(context); // 인증번호 입력 dialog  - jhm 2022/06/24
        bottomSheetSmsDialog.setCancelable(false);
        bottomSmsAlert = new BottomSmsAlert(getContext(),Contents); // sms 인증 실패시 alert - jhm 2022/06/24

        if(Contents.equals("본인인증 완료")){
            Bundle retryData = new Bundle();
            retryData.putString("txSeqNo",txSeqNo);
            retryData.putString("name",name);
            retryData.putString("birthday",birthday);
            retryData.putString("sexCd",sexCd);
            retryData.putString("ntvFrnrCd",ntvFrnrCd);
            retryData.putString("telComCd",telComcd);
            retryData.putString("telNo",telNo);
            retryData.putString("agree1","Y");
            retryData.putString("agree1","Y");
            retryData.putString("agree1","Y");
            retryData.putString("agree1","Y");
            retryData.putString("otpNo","");
            retryData.putParcelableArrayList("consentList", (ArrayList<? extends Parcelable>) consentList);
            bottomSheetSmsDialog.setArguments(retryData);
            bottomSheetSmsDialog.show(getActivity().getSupportFragmentManager(), Division.DIALOG_J_SMS);
        }else{
            LogUtil.logE("본인인증 완료 실패..");
            bottomSmsAlert.show();
        }
    }



    @Override
    public void onViewStateRestored(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        switch (selected_) {
            case "SKT":
                slideupPhoneBinding.sktTv.setTextColor(Color.BLACK);
                slideupPhoneBinding.sktTv.setTypeface(typeface);
                break;
            case "KT":
                slideupPhoneBinding.ktTv.setTextColor(Color.BLACK);
                slideupPhoneBinding.ktTv.setTypeface(typeface);
                break;
            case "LG U+":
                slideupPhoneBinding.lgTv.setTextColor(Color.BLACK);
                slideupPhoneBinding.lgTv.setTypeface(typeface);
                break;
            case "SKT 알뜰폰":
                slideupPhoneBinding.sktSubTv.setTextColor(Color.BLACK);
                slideupPhoneBinding.sktSubTv.setTypeface(typeface);
                break;
            case "KT 알뜰폰":
                slideupPhoneBinding.ktSubTv.setTextColor(Color.BLACK);
                slideupPhoneBinding.ktSubTv.setTypeface(typeface);
                break;
            case "LG U+ 알뜰폰":
                slideupPhoneBinding.lgSubTv.setTextColor(Color.BLACK);
                slideupPhoneBinding.lgSubTv.setTypeface(typeface);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skt_tv:
                selected_ = slideupPhoneBinding.sktTv.getText().toString();
                break;
            case R.id.kt_tv:
                selected_ = slideupPhoneBinding.ktTv.getText().toString();
                break;
            case R.id.lg_tv:
                selected_ = slideupPhoneBinding.lgTv.getText().toString();
                break;
            case R.id.skt_sub_tv:
                selected_ = slideupPhoneBinding.sktSubTv.getText().toString();
                break;
            case R.id.kt_sub_tv:
                selected_ = slideupPhoneBinding.ktSubTv.getText().toString();
                break;
            case R.id.lg_sub_tv:
                selected_ = slideupPhoneBinding.lgSubTv.getText().toString();
                break;
            case R.id.close_btn:
                dismiss();
                break;
        }

        bottomSheetListener.onButtonClick(selected_);
        dismiss();
    }


    // 액티비티와 연결하기위한 인터페이스
    public interface BottomSheetListener {
        void onButtonClick(String selected);
    }


}
