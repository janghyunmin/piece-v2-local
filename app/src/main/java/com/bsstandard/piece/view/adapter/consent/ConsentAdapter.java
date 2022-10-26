package com.bsstandard.piece.view.adapter.consent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.consent.ConsentList;
import com.bsstandard.piece.widget.utils.Division;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * packageName    : com.bsstandard.piece.view.adapter
 * fileName       : ConsetAdapter
 * author         : piecejhm
 * date           : 2022/06/19
 * description    : 이용약관 및 마케팅 화면
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/19        piecejhm       최초 생성
 */
public class ConsentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LinearLayoutManager mLinearLayoutManager;

    ArrayList<ConsentList> consentList;
    ConsentList itemList;
    Context context;
    boolean isAllChk;

    public ConsentAdapter(Context context, ArrayList<ConsentList> consentList) {
        this.context = context;
        this.consentList = consentList;
    }

    private ItemClick itemClick;

    public interface ItemClick {
        public void onClick(int position, boolean isCheck);
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    private ConcentDetail itemDetail;

    public interface ConcentDetail {
        public void onClick(int position);
    }

    public void setDetailClick(ConcentDetail itemDetail) {
        this.itemDetail = itemDetail;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void selectAll() {
        isAllChk = true;
        notifyDataSetChanged();
    }

    public void unselectAll() {
        isAllChk = false;
        notifyDataSetChanged();
    }

    public interface onClickListener {
        void onCheck(Boolean isCheck);
    }

    onClickListener onClickListener;

    public void setOnClickListener(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 초기 약관 목록 리스트 or 가상계좌 생성시 약관 목록 리스트 - jhm 2022/09/22
        if (viewType == Division.CONSENT || viewType == Division.SETTLE_CONSET) {
            view = inflater.inflate(R.layout.consent_item, parent, false);
            return new ConsentViewHolder(view);
        }
        // 더보기 - 이용약관 및 마케팅 정보 수신 동의 - jhm 2022/09/22
        else {
            view = inflater.inflate(R.layout.more_consent_item, parent, false);
            return new MoreConsentViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return consentList.get(position).getViewType();
    }


    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        itemList = consentList.get(position);

        if (viewHolder instanceof ConsentViewHolder) {
            ((ConsentViewHolder) viewHolder).item_text.setText(consentList.get(position).getConsentTitle());
            ((ConsentViewHolder) viewHolder).item_agree.setOnCheckedChangeListener(null); // 체크박스 초기화 - jhm 2022/06/20
            ((ConsentViewHolder) viewHolder).item_agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemList.setChk(((ConsentViewHolder) viewHolder).item_agree.isChecked());
                    onClickListener.onCheck(((ConsentViewHolder) viewHolder).item_agree.isChecked());
                }
            });
            if (!isAllChk) ((ConsentViewHolder) viewHolder).item_agree.setChecked(false);
            else ((ConsentViewHolder) viewHolder).item_agree.setChecked(true);


            RxView.clicks(((ConsentViewHolder) viewHolder).item_detail).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(empty -> {
                if (itemDetail != null) {
                    itemDetail.onClick(position);
                }
            });
        }
        else if( viewHolder instanceof MoreConsentViewHolder) {
            ((MoreConsentViewHolder) viewHolder).title.setText(consentList.get(position).getConsentTitle());
            RxView.clicks(((MoreConsentViewHolder) viewHolder).item_detail).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(empty -> {
                if (itemDetail != null) {
                    itemDetail.onClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return consentList.size();
    }

    public class ConsentViewHolder extends RecyclerView.ViewHolder {
        CheckBox item_agree;
        ImageView item_detail;
        TextView item_text;

        public ConsentViewHolder(@NonNull View itemView) {
            super(itemView);
            item_agree = itemView.findViewById(R.id.item_agree);
            item_text = itemView.findViewById(R.id.item_text);
            item_detail = itemView.findViewById(R.id.item_detail);
        }
    }

    public class MoreConsentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView item_detail;

        public MoreConsentViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            item_detail = itemView.findViewById(R.id.item_detail);

        }
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }


}
