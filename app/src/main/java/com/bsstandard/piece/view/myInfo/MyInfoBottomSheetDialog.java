package com.bsstandard.piece.view.myInfo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.address.AddressList;
import com.bsstandard.piece.data.datamodel.dmodel.consent.UpdateConsentList;
import com.bsstandard.piece.data.datamodel.dmodel.member.MemberModifyModel;
import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.viewmodel.AddressViewModel;
import com.bsstandard.piece.data.viewmodel.ConsentViewModel;
import com.bsstandard.piece.data.viewmodel.MemberPutViewModel;
import com.bsstandard.piece.databinding.SlideupAddressBinding;
import com.bsstandard.piece.view.adapter.address.AddressAdapter;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * packageName    : com.bsstandard.piece.view.myInfo
 * fileName       : MyInfoBottomSheetDialog
 * author         : piecejhm
 * date           : 2022/09/04
 * description    : 주소변경 및 이메일 변경 BottomSheetDialog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/04        piecejhm       최초 생성
 */
public class MyInfoBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    // 공통 변수 - jhm 2022/09/04
    public Context context;
    public Window window;
    public Typeface typeface;
    public String ViewType = "";
    public ProgressDialog progressDialog;

    SlideupAddressBinding slideupAddressBinding;

    // 초기 변수 설정
    private View view;
    // 인터페이스 변수
    private BottomSheetListener bottomSheetListener;


    // ViewModel ( 주소찾기 , 이메일 등록 ) - jhm 2022/09/04
    private AddressViewModel addressViewModel;
    private ArrayList<AddressList> addressList = new ArrayList<>();
    private AddressAdapter addressAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<UpdateConsentList> consentList = new ArrayList<>(); // 약관 리스트 - jhm 2022/09/06

    // 회원 정보 수정 요청  - jhm 2022/09/05
    private MemberPutViewModel memberPutViewModel;
    private ConsentViewModel consentViewModel;
    private MemberModifyModel memberModifyModel;

    // 상세 주소 bottomDialog - jhm 2022/09/07
    private MyInfoDetailBottomSheetDialog myInfoDetailBottomSheetDialog;

    public MyInfoBottomSheetDialog(Context context) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        slideupAddressBinding = SlideupAddressBinding.inflate(getLayoutInflater());
        slideupAddressBinding.setLifecycleOwner(this);
        view = slideupAddressBinding.getRoot();


        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        memberPutViewModel = new ViewModelProvider(this).get(MemberPutViewModel.class);
        consentViewModel = new ViewModelProvider(this).get(ConsentViewModel.class);


        slideupAddressBinding.addressSearchText.setText(null);
        slideupAddressBinding.searchBtn.setOnClickListener(this);
        slideupAddressBinding.closeBtn.setOnClickListener(this);
        slideupAddressBinding.clear.setOnClickListener(this);


        // KeyPad 확인 클릭시 실행 - jhm 2022/09/05
        slideupAddressBinding.addressSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (slideupAddressBinding.addressSearchText.getText().length() == 0 || slideupAddressBinding.addressSearchText.getText().length() < 2) {
                    LogUtil.logE("주소 입력값 없음..");
                } else {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        addressModel();
                    }
                }
                return false;
            }
        });


        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                if (slideupAddressBinding.addressSearchText.getText().length() == 0 || slideupAddressBinding.addressSearchText.getText().length() < 2) {
                    LogUtil.logE("주소 입력값 없음..");
                } else {
                    addressModel();
                }
                break;

            case R.id.clear:
                slideupAddressBinding.addressSearchText.setText("");
                break;

            // 닫기  - jhm 2022/09/05
            case R.id.close_btn:
                dismiss();
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addressModel() {
        consentList.clear();
        consentViewModel.getConsentData("SIGN").observe(this, response -> {
            for(int index = 0; index < response.getData().size(); index++) {
                consentList.add(new UpdateConsentList(
                        PrefsHelper.read("memberId",""),
                        response.getData().get(index).getConsentCode(),
                        "Y"));
            }
        });

        addressViewModel.getAddressData(slideupAddressBinding.addressSearchText.getText().toString() , 100).observe(
                getViewLifecycleOwner(),
                reponse -> {
                    addressList.clear();
                    if (reponse.getData().getResults().getJuso() != null) {
                        for (int index = 0; index < reponse.getData().getResults().getJuso().size(); index++) {
                            addressList.add(new AddressList(
                                    reponse.getData().getResults().getJuso().get(index).getJibunAddr(),
                                    reponse.getData().getResults().getJuso().get(index).getRoadAddr()
                            ));
                        }
                        addressAdapter = new AddressAdapter(getContext(), addressList);
                        layoutManager = new LinearLayoutManager(getContext());
                        slideupAddressBinding.addressRv.setLayoutManager(layoutManager);
                        slideupAddressBinding.addressRv.setHasFixedSize(true);
                        slideupAddressBinding.addressRv.setAdapter(addressAdapter);
                        addressAdapter.notifyDataSetChanged();

                        addressAdapter.setOnClickListener(new AddressAdapter.onClickListener() {
                            @Override
                            public void onClick(String roadAddr, String jibunAddr) {
                                LogUtil.logE("roadAddr : " + roadAddr);
                                LogUtil.logE("jibunAddr : " + jibunAddr);


                                PrefsHelper.write("roadAddr",roadAddr);
                                PrefsHelper.write("jibunAddr",jibunAddr);

                                // 상세 주소 입력 Dialog 호출 - jhm 2022/09/08
                                myInfoDetailBottomSheetDialog = new MyInfoDetailBottomSheetDialog(context);
                                myInfoDetailBottomSheetDialog.show(getActivity().getSupportFragmentManager(),"addressDetail");
                                
                                dismiss(); // 현재 주소검색 BottomDialog 종료 - jhm 2022/09/08
                            }
                        });
                    } else {
                        LogUtil.logE("검색주소 결과 data null..");
                    }


                }
        );


    }


    // 액티비티와 연결하기위한 인터페이스
    public interface BottomSheetListener {
        void onButtonClick(String selected);
    }
}
