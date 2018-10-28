package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v4.view.MenuItemCompat;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.PatientListRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.DayType;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.MainActivity;
import com.clinic.anhe.medicinetracker.utils.Shift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class PatientListDayFragment extends Fragment implements ArgumentVariables,SearchView.OnQueryTextListener {

    //patients cardview
    private List<PatientsCardViewModel> patientList;
//    private EditText patientSearch;
    private RecyclerView mRecyclerView;
    private PatientListRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mAddPatient;
    private Shift shift;
    private DayType dayType;
    private VolleyController volleyController;
    private Context mContext;
    private GlobalVariable globalVariable;
    private String ip;

    private String port;
    private View view;
    private SearchView sv;

    public static PatientListDayFragment newInstance(Shift shift, DayType dayType) {
        PatientListDayFragment fragment = new PatientListDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATIENT_SHIFT, shift.toString());
        args.putString(ARG_DAY_TYPE, dayType.toString());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // What i have added is this
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu); // removed to not double the menu items
        MenuItem item = menu.findItem(R.id.action_search);
        sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item,  MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(this);
        sv.setIconifiedByDefault(false);

//        sv.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("Clicked: ", "Chloe");
//            }
//        });
//
//        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do something when collapsed
//                Log.d("Closed: ","Chloe");
//                return true;  // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do something when expanded
//                Log.d("Opened: ", "Chloe");
//                return true;  // Return true to expand action view
//            }
//        });


        //super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(patientList.size()==0) {
            preparePatientData();
            return false;
        }
        if(!query.equals("")) {
            filter(query.toString());
            sv.setQuery("", false);
            sv.clearFocus();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        if(patientList.size()==0) {
//            preparePatientData();
//            return false;
//        }
//        if(!newText.equals("")) {
//            filter(newText.toString());
//        }

        return false;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_PATIENT_SHIFT, shift.toString());
        outState.putString(ARG_DAY_TYPE, dayType.toString());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_patient_list_day, container, false);
        mAddPatient = view.findViewById(R.id.add_patient_fab);
        mContext = getContext();

        //set ip and port
        ip = globalVariable.getInstance().getIpaddress();
        port = globalVariable.getInstance().getPort();

        if(savedInstanceState != null) {
            shift = shift.fromString(savedInstanceState.getString(ARG_PATIENT_SHIFT));
            dayType = dayType.fromString(savedInstanceState.getString(ARG_DAY_TYPE));
        }

        if(shift == null) {
            shift = shift.fromString(getArguments().getString(ARG_PATIENT_SHIFT));
            dayType= dayType.fromString(getArguments().getString(ARG_DAY_TYPE));
        }


        patientList = new ArrayList<>();

        preparePatientData();
        mRecyclerView = view.findViewById(R.id.patient_list_day_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        //here we need to filter
        mAdapter = new PatientListRecyclerViewAdapter(patientList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPatientDialogFragment addPatientDialogFragment = AddPatientDialogFragment.newInstance(PatientListDayFragment.this);
                addPatientDialogFragment.show(getFragmentManager(), "addPatient");
                //Toast.makeText(mContext, "adding patient..", Toast.LENGTH_SHORT).show();
            }
        });


        setRetainInstance(true);
        return view;

    }

    private void filter(String filterString) {
        List<PatientsCardViewModel> filterPatientList = new ArrayList<>();
        for(PatientsCardViewModel p : patientList) {
            if(p.getPatientName().contains(filterString)) {
                filterPatientList.add(p);
            }
        }

        mAdapter.filterList(filterPatientList);

    }
    public void refreshrecyclerView() {
        preparePatientData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mAdapter != null && isVisibleToUser) {
            Log.d("we are in set user visble hint", "" + shift.toString() + " / " + dayType.toString());
            preparePatientData();
            mAdapter.filterList(patientList);
        }
    }

    private void preparePatientData() {
        String url = "";
        if(dayType == DayType.evenDay) {
        switch(shift) {
            case morning:
                url = "http://" + ip +
                        ":" + port + "/anho/patient?day=二四六&shift=早班";
                parsePatientList(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        if(status == VolleyStatus.SUCCESS) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            case afternoon:
                url = "http://" + ip +
                        ":" + port  + "/anho/patient?day=二四六&shift=中班";
                parsePatientList(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        if(status == VolleyStatus.SUCCESS) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            case night:
                url = "http://" + ip +
                        ":" + port + "/anho/patient?day=二四六&shift=晚班";
                parsePatientList(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        if(status == VolleyStatus.SUCCESS) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
        }
        } else {
            switch(shift) {
                case morning:
                    url = "http://" + ip +
                            ":" + port +  "/anho/patient?day=一三五&shift=早班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case afternoon:
                    url = "http://" + ip +
                            ":" + port +  "/anho/patient?day=一三五&shift=中班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case night:
                    url = "http://" + ip +
                            ":" + port + "/anho/patient?day=一三五&shift=晚班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;

            }
        }

    }

    private void parsePatientList(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        Integer pid = object.getInt("pid");
                                        String name = object.getString("name");
                                        String shift = object.getString("shift");
                                        String ic = object.getString("ic");
                                        String day = object.getString("day");
//                                        Log.d("patient jason object" , name + pid + shift + day + ic);
                                        PatientsCardViewModel patient = new PatientsCardViewModel(pid, name, ic, shift, day);
                                        if(!patientList.contains(patient)) {
                                            patientList.add(new PatientsCardViewModel(pid, name, ic, shift, day));
                                        }
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
                              //  Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } ){
                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        String credentials = "admin1:secret1";
                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }


}