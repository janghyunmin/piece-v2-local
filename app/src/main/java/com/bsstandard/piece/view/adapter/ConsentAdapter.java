package com.bsstandard.piece.view.adapter;

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
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * packageName    : com.bsstandard.piece.view.adapter
 * fileName       : ConsetAdapter
 * author         : piecejhm
 * date           : 2022/06/19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/19        piecejhm       최초 생성
 */
public class ConsentAdapter extends RecyclerView.Adapter<ConsentAdapter.ViewHolder>{
    private LinearLayoutManager mLinearLayoutManager;

    ArrayList<ConsentList> consentList;
    ConsentList itemList;
    Context context;
    boolean isAllChk;

    public ConsentAdapter(Context context,ArrayList<ConsentList> consentList ){
        this.context = context;
        this.consentList = consentList;
    }

    private ItemClick itemClick;
    public interface ItemClick{
        public void onClick(int position , boolean isCheck);
    }
    public void setItemClick(ItemClick itemClick){
        this.itemClick = itemClick;
    }

    private ConcentDetail itemDetail;
    public interface ConcentDetail{
        public void onClick(int position);
    }
    public void setDetailClick(ConcentDetail itemDetail) { this.itemDetail = itemDetail;}

    public void selectAll(){
        isAllChk = true;
        notifyDataSetChanged();
    }
    public void unselectAll(){
        isAllChk = false;
        notifyDataSetChanged();
    }

    public interface onClickListener {
        void onCheck(Boolean isCheck);
    }
    onClickListener onClickListener;

    public void setOnClickListener(onClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType){
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.consent_item,parent,false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        itemList = consentList.get(position);

        if(holder instanceof ViewHolder){
            ((ViewHolder) holder).item_text.setText(consentList.get(position).getConsentTitle());
            ((ViewHolder) holder).item_agree.setOnCheckedChangeListener(null); // 체크박스 초기화 - jhm 2022/06/20
            holder.item_agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemList.setChk(holder.item_agree.isChecked());
                    onClickListener.onCheck(holder.item_agree.isChecked());
                }
            });
            if(!isAllChk) ((ViewHolder) holder).item_agree.setChecked(false);
            else ((ViewHolder) holder).item_agree.setChecked(true);


            RxView.clicks(((ViewHolder)holder).item_detail).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(empty -> {
                if(itemDetail != null){
                    itemDetail.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() { return consentList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox item_agree;
        ImageView item_detail;
        TextView item_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_agree = itemView.findViewById(R.id.item_agree);
            item_text = itemView.findViewById(R.id.item_text);
            item_detail = itemView.findViewById(R.id.item_detail);
        }
    }
    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;
    }


}
