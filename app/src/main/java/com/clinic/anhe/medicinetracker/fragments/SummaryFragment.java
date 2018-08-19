package com.clinic.anhe.medicinetracker.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Transaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.NetworkResponse;
import com.android.volley.AuthFailureError;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineCategoryPagerAdapter;
import com.clinic.anhe.medicinetracker.adapters.SummaryRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;


public class SummaryFragment  extends Fragment {
    private View view;
    private TextView patientName;
    private TextView patientId;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SummaryRecyclerViewAdapter mAdapter;
    private FloatingActionButton summaryFab;
    private int i = -1;
    private VolleyController volleyController;
    private RequestQueue mQueue;

    //TODO
    private CartViewModel cartViewModel;
    private List<MedicineCardViewModel> medicineList;
    private List<MedicineCardViewModel> cartList;

    //TODO
    private SelectedPatientViewModel selectedPatientViewModel;

    public static SummaryFragment newInstance(){
       SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_summary, container, false);

        volleyController.getInstance(getContext());
       // mQueue = Volley.newRequestQueue(getContext());

        cartViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(CartViewModel.class);

        patientName = view.findViewById(R.id.summary_patientname);
        patientId = view.findViewById(R.id.summary_patientid);
        summaryFab = view.findViewById(R.id.summary_fab);

        summaryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch a sweetAlertDialog
                jasonParse();

                final SweetAlertDialog pDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                CountDownTimer timer = new CountDownTimer(500 * 7, 100) {
                    public void onTick(long millisUntilFinished) {
                        // you can change the progress bar color by ProgressHelper every 800 millis
                        i++;
                        switch (i){
                            case 0:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                break;
                            case 2:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                break;
                            case 6:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                        }
                    }

                    public void onFinish() {
                        i = -1;
                        pDialog.setTitleText("Success!")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
//                                getActivity().getSupportFragmentManager().popBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            }
                        });



                    }
                }.start();

            }
        });

        patientId.setText(getArguments().getString("patientId"));
        patientName.setText(getArguments().getString("patientName"));

        mRecyclerView = view.findViewById(R.id.summary_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        //TODO: only pass the meds that has been selected
        cartList = new ArrayList<>();
        //all four categories meds
        medicineList = cartViewModel.getDialysisList();
        addToCartList(medicineList);
        medicineList = cartViewModel.getEdibleList();
        addToCartList(medicineList);
        medicineList = cartViewModel.getNeedleList();
        addToCartList(medicineList);
        medicineList = cartViewModel.getBandaidList();
        addToCartList(medicineList);

        mAdapter = new SummaryRecyclerViewAdapter(cartList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    private boolean jasonParse(){
        String url= "http://192.168.0.9:8080/anhe/medicine/add/";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", "Android Volley Demo");
            jsonBody.put("dose", "BNK");
            jsonBody.put("price", 1);
            jsonBody.put("category", "test");
            jsonBody.put("stock", 0);
        } catch (JSONException e) {

        }
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        patientId.setText("Response is: "+ response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }
                })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                if (response != null) {
//                    responseString = String.valueOf(response.statusCode);
//                    // can get more details such as response.headers
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//            }
        };

        volleyController.getInstance(getContext()).addToRequestQueue(stringRequest);
        //mQueue.add(stringRequest);


//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        patientId.setText("Response is: "+ response.toString());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                patientId.setText((CharSequence) error.toString());
//            }
//        });

// Add the request to the RequestQueue.
//        mQueue.add(stringRequest);
        return false;
    }

    private void addToCartList(List<MedicineCardViewModel> list) {
        for(MedicineCardViewModel m : list) {
            if(m.getIsAddToCart()) {
                cartList.add(m);
            }
        }
    }
}
