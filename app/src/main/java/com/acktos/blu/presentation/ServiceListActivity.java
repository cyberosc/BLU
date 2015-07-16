package com.acktos.blu.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.acktos.blu.controller.BaseController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.acktos.blu.R;
import com.acktos.blu.controller.DriversController;
import com.acktos.blu.controller.ServicesController;
import com.acktos.blu.models.Driver;
import com.acktos.blu.models.Service;
import com.acktos.blu.presentation.adapters.ServiceAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServiceListActivity extends AppCompatActivity {

    //ATRIBUTTES
    private List<Service> services;
    private RecyclerView.Adapter recyclerAdapter;

    //APP COMPONENTS
    private ServicesController servicesController;
    private DriversController driversController;

    //UI REFERENCES
    private RecyclerView servicesRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        //initialize attributes
        services=new ArrayList<>();

        //initialize componentes
        servicesController=new ServicesController(this);
        driversController=new DriversController(this);

        servicesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_services);
        servicesRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        servicesRecyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new ServiceAdapter(services);
        servicesRecyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Driver driver=driversController.getDriver();
        servicesController.getAvailableServices(driver.id, new Response.Listener<List<Service>>() {
            @Override
            public void onResponse(List<Service> responseServices) {

                if(services!=null){

                    services.clear();
                    services.addAll(responseServices);
                    recyclerAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(BaseController.TAG, volleyError.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
