package com.clinic.anhe.medicinetracker.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Transaction;
import android.content.Context;
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
import com.android.volley.toolbox.JsonArrayRequest;
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
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ListIterator;
import java.util.Locale;


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
    private static VolleyStatus status= VolleyStatus.UNKNOWN;
    private Integer nurseEid = -1;


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

        if(status == VolleyStatus.SUCCESS) {
            final SweetAlertDialog successDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE);
            successDialog.setTitleText("Success!")
                    .setConfirmText("OK")
                    .show();
            successDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    SummaryFragment.status = VolleyStatus.UNKNOWN;
                    sweetAlertDialog.dismiss();
                    getActivity().getSupportFragmentManager().popBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });
        }
        else if(status == VolleyStatus.FAIL) {
            final SweetAlertDialog failDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE);
            failDialog.setTitleText("Fail!")
                    .setConfirmText("Try Again")
                    .show();
            failDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    SummaryFragment.status = VolleyStatus.UNKNOWN;
                    sweetAlertDialog.dismiss();
                }
            });
        }
        else if(status == VolleyStatus.UNSET) {
                 final SweetAlertDialog unsetDialog =  new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE);
                 unsetDialog.setTitleText("請設定負責護士")
                         .setConfirmText("Try Agian")
                         .show();
                 unsetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                     @Override
                     public void onClick(SweetAlertDialog sweetAlertDialog) {
                           SummaryFragment.status = VolleyStatus.UNKNOWN;
                           sweetAlertDialog.dismiss();
                           getActivity().getSupportFragmentManager().popBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                     }
                 });
        }

        volleyController.getInstance(getContext());
       // mQueue = Volley.newRequestQueue(getContext());

        cartViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(CartViewModel.class);
        selectedPatientViewModel = ViewModelProviders.of(getParentFragment()).get(SelectedPatientViewModel.class);


        patientName = view.findViewById(R.id.summary_patientname);
        patientId = view.findViewById(R.id.summary_patientid);
        summaryFab = view.findViewById(R.id.summary_fab);

        patientId.setText(selectedPatientViewModel.getPatient().getPatientIC());
        patientName.setText(selectedPatientViewModel.getPatient().getPatientName());

        summaryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch a sweetAlertDialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                final CountDownTimer timer = new CountDownTimer(500 * 7, 100) {
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
                        i=-1;
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                SummaryFragment.status = VolleyStatus.UNKNOWN;
                                if(sweetAlertDialog.getTitleText().equalsIgnoreCase("Success!")) {
                                    sweetAlertDialog.dismiss();
                                    getActivity().getSupportFragmentManager().popBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                } else if (sweetAlertDialog.getTitleText().equalsIgnoreCase("Fail!")) {
                                    sweetAlertDialog.dismiss();
                                } else {
                                    sweetAlertDialog.dismiss();
                                }   getActivity().getSupportFragmentManager().popBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                        });



                    }
                }.start();
                findNurse(new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        if(status == VolleyStatus.SUCCESS && nurseEid > 0 ) {
                            addRecordToDatabase(new VolleyCallBack() {
                                @Override
                                public void onResult(VolleyStatus status) {
                                    if(status == VolleyStatus.SUCCESS) {
                                        SummaryFragment.status = VolleyStatus.SUCCESS;
                                        pDialog.setTitleText("Success!")
                                                .setConfirmText("OK")
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    } else {
                                        SummaryFragment.status = VolleyStatus.FAIL;
                                        pDialog.setTitleText("Fail!")
                                                .setConfirmText("Try Again")
                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    }
                                    timer.onFinish();
                                }
                            });
                        } else {
                            //pop up an alter
                            SummaryFragment.status = VolleyStatus.UNSET;
                            pDialog.setTitleText("請設定負責護士")
                                    .setConfirmText("Try Again")
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
                    }
                });



            }
        });



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

    private void findNurse(final VolleyCallBack volleyCallBack) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://192.168.0.4:8080/anhe/shiftrecord/patient?patient=" + selectedPatientViewModel.getPatient().getPatientName()
                + "&createAt=" + date;
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        nurseEid = object.getInt("eid");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                volleyCallBack.onResult(VolleyStatus.SUCCESS);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

    private void addRecordToDatabase(final VolleyCallBack volleyCallBack) {
        //TODO: needs to modified create_by and subtotal
        String url = "http://192.168.0.4:8080/anhe/record/addlist";
        JSONArray jsonArray = new JSONArray();
        try {
            for(MedicineCardViewModel item : cartList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mid", item.getMedicineId());
                jsonObject.put("medicineName", item.getMedicinName());
                jsonObject.put("payment", item.isCashPayment().toString());
                jsonObject.put("pid", selectedPatientViewModel.getPatient().getPID());
                jsonObject.put("quantity", item.getQuantity());
                jsonObject.put("createBy", nurseEid);
                int subtotal = Integer.parseInt(item.getMedicinePrice()) * item.getQuantity();
                jsonObject.put("subtotal", subtotal);
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {

        }
        final String requestBody = jsonArray.toString();
        Log.d(requestBody, "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(response.equals("saved")) {
                           volleyCallBack.onResult(VolleyStatus.SUCCESS);
                       } else {
                           volleyCallBack.onResult(VolleyStatus.FAIL);
                       }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                        volleyCallBack.onResult(VolleyStatus.FAIL);
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

        };

        volleyController.getInstance().addToRequestQueue(stringRequest);
    }


    private void addToCartList(List<MedicineCardViewModel> list) {
        for(MedicineCardViewModel m : list) {
            if(m.getIsAddToCart()) {
                cartList.add(m);
            }
        }
    }
}
