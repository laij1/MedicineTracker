package com.clinic.anhe.medicinetracker.fragments;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.adapters.DashboardPatientAssignViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.PatientsPagerAdapter;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.DayType;
import com.clinic.anhe.medicinetracker.utils.Shift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SelectPatientsDialogFragment extends DialogFragment {
    private ViewPager mPatientsViewPager;
    private TabLayout mPatientsTabLayout;
    private PatientsPagerAdapter mPatientsPagerAdapter;
    private TextView addButton;
    private TextView cancelButton;
    private Context mContext;
    private Shift shift;
    private String nurseName;
    private DayType dayType;
    private DashboardViewModel dashboardViewModel;
    private static List<String> list;
    private static DashboardPatientAssignViewAdapter dashboardPatientAssignViewAdapter;
    private VolleyController volleyController;



    public static SelectPatientsDialogFragment newInstance(String name, DashboardPatientAssignViewAdapter mAdapter, List<String> patientList) {
        SelectPatientsDialogFragment fragment = new SelectPatientsDialogFragment();
        dashboardPatientAssignViewAdapter = mAdapter;
        list = patientList;
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_NURSE_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
//        Log.d("OnDestoryView", "Chloe");
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ArgumentVariables.ARG_NURSE_NAME, nurseName);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            nurseName = savedInstanceState.getString(ArgumentVariables.ARG_NURSE_NAME);
        }

        if(nurseName == null) {
            nurseName = getArguments().getString(ArgumentVariables.ARG_NURSE_NAME);
        }

        Log.d(nurseName, "chloein sleect");
        View view = inflater.inflate(R.layout.fragment_dashboard_patients, container, false);
        //right here shift does not matter
        shift = Shift.morning;
        //get livedata
        dashboardViewModel = ViewModelProviders.of(getParentFragment()).get(DashboardViewModel.class);


        addButton = view.findViewById(R.id.dashboard_patients_confirmbutton);
        cancelButton = view.findViewById(R.id.dashboard_patients_cancelbutton);
        mPatientsTabLayout = (TabLayout) view.findViewById(R.id.dashboard_patients_tabLayout);

        //set up tab
        TabLayout.Tab OddDays = mPatientsTabLayout.newTab();
        mPatientsTabLayout.addTab(OddDays);

        TabLayout.Tab EvenDays = mPatientsTabLayout.newTab();
        mPatientsTabLayout.addTab(EvenDays);

        mContext = getContext();

        mPatientsTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPatientsViewPager = (ViewPager) view.findViewById(R.id.dashboard_patients_pager);
        mPatientsTabLayout.setupWithViewPager(mPatientsViewPager);
        mPatientsPagerAdapter = new PatientsPagerAdapter
                (getChildFragmentManager(), mPatientsTabLayout.getTabCount(), mContext, shift, ArgumentVariables.KIND_DASHBOARD_PATIENTS, nurseName);
        mPatientsViewPager.setAdapter(mPatientsPagerAdapter);

        highLightCurrentTab(0);


        mPatientsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mPatientsTabLayout) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });
        mPatientsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPatientsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);

        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.addAll(dashboardViewModel.getSelectedPatientsList());
                //get current day of the week
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                switch (day) {
                    case Calendar.MONDAY:
                    case Calendar.WEDNESDAY:
                    case Calendar.FRIDAY:
                        dayType = DayType.oddDay;
                        break;
                    case Calendar.TUESDAY:
                    case Calendar.THURSDAY:
                    case Calendar.SATURDAY:
                        dayType = DayType.evenDay;
                        break;
                }
                //add to database
                addShiftRecordToDatabase(new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        Toast.makeText(mContext, "設定完成", Toast.LENGTH_LONG).show();
                        //clear data
                        dashboardViewModel.getSelectedPatientsList().removeAll(dashboardViewModel.getSelectedPatientsList());
                        dashboardViewModel.getSelectedPatientsLiveData().setValue(dashboardViewModel.getSelectedPatientsList());
                        dashboardViewModel.getNurseLiveData().setValue("");
                    }
                });
                dashboardPatientAssignViewAdapter.notifyDataSetChanged();
                dismiss();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardViewModel.getSelectedPatientsList().removeAll(dashboardViewModel.getSelectedPatientsList());
                dismiss();
//                Toast.makeText(getActivity(), "cancel is clicked", Toast.LENGTH_LONG).show();
            }
        });

        setRetainInstance(true);
        return view;
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < mPatientsPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mPatientsTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mPatientsPagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mPatientsTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mPatientsPagerAdapter.getSelectedTabView(position));
    }

    private void addShiftRecordToDatabase(final VolleyCallBack volleyCallBack) {
        //TODO: needs to modified create_by and subtotal
        String url = "http://192.168.0.4:8080/anhe/shift/record/addlist";
        JSONArray jsonArray = new JSONArray();
        try {
            for(String item : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nurse", nurseName);
                jsonObject.put("patient", item);
                jsonObject.put("shift", shift.toString());
                jsonObject.put("day", dayType==DayType.oddDay? "一三五":"二四六");
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

        volleyController.getInstance(mContext).addToRequestQueue(stringRequest);
    }


}
