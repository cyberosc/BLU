package com.acktos.blu.presentation.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acktos.blu.R;
import com.acktos.blu.models.Service;

import java.util.List;

/**
 * Created by Acktos on 7/15/15.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {


    private List<Service> services;
    private static OnRecyclerViewClickListener onRecyclerViewClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView username;
        public TextView pickupAddress;
        public TextView arrivalAddress;
        public CardView serviceCard;

        public ViewHolder(View itemView) {

            super(itemView);
            serviceCard=(CardView)itemView.findViewById(R.id.service_card);
            username=(TextView)itemView.findViewById(R.id.lbl_user_name);
            pickupAddress=(TextView)itemView.findViewById(R.id.lbl_pickup_address);
            arrivalAddress=(TextView)itemView.findViewById(R.id.lbl_arrival_address);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onRecyclerViewClickListener.onRecyclerViewClick(view, this.getLayoutPosition());
        }
    }

    public ServiceAdapter(List<Service> services,OnRecyclerViewClickListener onRecyclerViewClick){

        this.services=services;
        this.onRecyclerViewClickListener=onRecyclerViewClick;
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_service, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.username.setText(services.get(i).username);
        viewHolder.pickupAddress.setText(services.get(i).pickupAddress);
        viewHolder.arrivalAddress.setText(services.get(i).arrivalAddress);
    }



    public interface OnRecyclerViewClickListener
    {

        public void onRecyclerViewClick(View v, int position);
    }

}
