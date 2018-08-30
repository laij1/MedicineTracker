package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.MedicineSimpleRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.MedicineType;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MedicineSimpleFragment extends Fragment {

    private MedicineType medicineType;
    private RecyclerView mRecyclerView;
    private MedicineSimpleRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MedicineCardViewModel> medicineList;
    private VolleyController volleyController;
    private Context mContext;

    public static MedicineSimpleFragment newInstance(MedicineType medicineType) {
        MedicineSimpleFragment fragment = new MedicineSimpleFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_MEDICINE_TYPE, medicineType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: medicineType could be null....
        outState.putString(ArgumentVariables.ARG_MEDICINE_TYPE, medicineType.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view  = inflater.inflate(R.layout.fragment_medicine_simple, container, false);

        mContext = view.getContext();

        if(savedInstanceState != null) {
            medicineType = medicineType.fromString(savedInstanceState.getString(ArgumentVariables.ARG_MEDICINE_TYPE));
        }

        if(medicineType == null) {
            medicineType = medicineType.fromString(getArguments().getString(ArgumentVariables.ARG_MEDICINE_TYPE));
        }
//
        medicineList = new ArrayList<>();
        prepareMedicineData();

        mRecyclerView = view.findViewById(R.id.medicine_simple_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(getContext());
         mLayoutManager = new GridLayoutManager(getContext(), 2);

        mAdapter = new MedicineSimpleRecyclerViewAdapter(medicineType, medicineList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

//setRetainInstance to true is important so that onSaveInstanceState will work
        setRetainInstance(true);
        return view;
    }

    private void prepareMedicineData() {
        String url = "http://192.168.0.4:8080/anhe/medicine?category=" + medicineType.toString();
               parseMedicineList(url, new VolleyCallBack() {
                   @Override
                   public void onResult(VolleyStatus status) {
                       if (status == VolleyStatus.SUCCESS) {
                           mAdapter.notifyDataSetChanged();
                       }
                   }
               });
    }


    private void parseMedicineList(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String category = object.getString("category");
                                        String name = object.getString("name");
                                        Integer id = object.getInt("mid");
                                        Integer price = object.getInt("price");
                                        String dose = object.getString("dose");
                                        Integer stock = object.getInt("stock");
                                        Log.d("jason object" , name + id +price +dose + stock);

                                        medicineList.add(new MedicineCardViewModel(id, name, Integer.toString(price), dose, stock, category));
//
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // Log.d("getting patient data from database", "CHLOE");
                                    volleyCallBack.onResult(VolleyStatus.SUCCESS);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }
}
