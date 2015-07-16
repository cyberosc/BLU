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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView pickupAdress;
        public TextView arrivalAddress;
        public CardView serviceCard;

        public ViewHolder(View itemView) {

            super(itemView);
            serviceCard=(CardView)itemView.findViewById(R.id.service_card);
            username=(TextView)itemView.findViewById(R.id.lbl_user_name);
            pickupAdress=(TextView)itemView.findViewById(R.id.lbl_pickup_address);
            arrivalAddress=(TextView)itemView.findViewById(R.id.lbl_arrival_address);

        }

    }

    public ServiceAdapter(List<Service> services){

        this.services=services;
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
        viewHolder.pickupAdress.setText(services.get(i).pickupAdrress);
        viewHolder.arrivalAddress.setText(services.get(i).arrivalAdress);
    }

}
