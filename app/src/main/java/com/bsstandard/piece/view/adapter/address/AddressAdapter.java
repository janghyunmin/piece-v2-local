package com.bsstandard.piece.view.adapter.address;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datamodel.dmodel.address.AddressList;

import java.util.ArrayList;

/**
 * packageName    : com.bsstandard.piece.view.adapter.address
 * fileName       : AddressAdapter
 * author         : piecejhm
 * date           : 2022/09/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/04        piecejhm       최초 생성
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private LinearLayoutManager linearLayoutManager;
    ArrayList<AddressList> addressList;
    Context context;
    AddressList itemList;

    public AddressAdapter(Context context , ArrayList<AddressList> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    public onClickListener onClickListener;
    public interface onClickListener {
        void onClick(String roadAddr,String jibunAddr);
    }

    public void setOnClickListener(onClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType){
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.address_item,parent,false);
        return new ViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        itemList = addressList.get(position);

        ((ViewHolder) holder).roadAddr.setText(addressList.get(position).getRoadAddr());
        ((ViewHolder) holder).jibunAddr.setText(addressList.get(position).getJibunAddr());
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(addressList.get(position).getRoadAddr(),addressList.get(position).getJibunAddr());
            }
        });
    }

    @Override
    public int getItemCount() { return addressList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout parent_layout;
        TextView roadAddr,jibunAddr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            roadAddr = itemView.findViewById(R.id.roadAddr);
            jibunAddr = itemView.findViewById(R.id.jibunAddr);
        }
    }
    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.linearLayoutManager = linearLayoutManager;
    }
}
