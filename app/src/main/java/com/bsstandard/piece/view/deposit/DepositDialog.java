package com.bsstandard.piece.view.deposit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bsstandard.piece.R;
import com.bsstandard.piece.databinding.SlideupDepositBinding;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * packageName    : com.bsstandard.piece.view.deposit
 * fileName       : DepositDialog
 * author         : piecejhm
 * date           : 2022/09/29
 * description    : 거래내역 Bottom Sheet Dialog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/29        piecejhm       최초 생성
 */


public class DepositDialog extends BottomSheetDialogFragment {
    private final String TAG = DepositDialog.class.getSimpleName();
    private final Context mContext;
    private String msg;

    private SlideupDepositBinding binding;
    private View view;

    private ConstraintLayout all_selected_layout, purchase_selected_layout , deposit_selected_layout;
    private ImageView arrow_down_1,arrow_down_2,arrow_down_3;



    // interface
    Listener listener;

    public DepositDialog(Context context, String msg, Listener listener) {
        this.mContext = context;
        this.msg = msg;
        this.listener = listener;
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

        binding = SlideupDepositBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        view = binding.getRoot();

        // 취소 불가 처리 - jhm 2022/09/29
        setCancelable(true);

        binding.allSelectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.logE(TAG + "전체 선택");
                if (listener != null) {
                    binding.arrowDown1.setVisibility(View.VISIBLE);
                    binding.arrowDown2.setVisibility(View.INVISIBLE);
                    binding.arrowDown3.setVisibility(View.INVISIBLE);
                    listener.changeText(binding.allTitle.getText().toString());
                }
                dismiss();
            }
        });

        binding.purchaseSelectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.logE(TAG + "조각 거래 내역 선택");
                if (listener != null) {
                    binding.arrowDown1.setVisibility(View.INVISIBLE);
                    binding.arrowDown2.setVisibility(View.VISIBLE);
                    binding.arrowDown3.setVisibility(View.INVISIBLE);
                    listener.changeText(binding.purchaseTitle.getText().toString());
                }
                dismiss();
            }
        });

        binding.depositSelectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.logE(TAG + "예치금 입출금 내역 선택");
                if (listener != null) {
                    binding.arrowDown1.setVisibility(View.INVISIBLE);
                    binding.arrowDown2.setVisibility(View.INVISIBLE);
                    binding.arrowDown3.setVisibility(View.VISIBLE);
                    listener.changeText(binding.depositTitle.getText().toString());


                }
                dismiss();
            }
        });
        return view;
    }


}
