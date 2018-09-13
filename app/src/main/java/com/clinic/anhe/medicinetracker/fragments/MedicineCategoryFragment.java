package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineCategoryPagerAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;


public class MedicineCategoryFragment extends Fragment implements View.OnKeyListener{

    private TabLayout mMedicineCategoryTabLayout;
    private Context mContext;
    private ViewPager mMedicineCategoryViewPager;
    private MedicineCategoryPagerAdapter mMedicineCategoryPagerAdapter;
    private CounterFab mCounterfab;
    private CartViewModel cartViewModel;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String cartSelectedPatientName;


    public static MedicineCategoryFragment newInstance(){
        MedicineCategoryFragment fragment = new MedicineCategoryFragment();
        return fragment;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: medicineType could be null....
        outState.putString(ArgumentVariables.ARG_CART_SELECTED_PATIENT_NAME, cartSelectedPatientName);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_medicine_category, container, false);

        mContext = getContext();
        volleyController.getInstance(mContext);
        if(savedInstanceState != null) {
            cartSelectedPatientName = savedInstanceState.getString(ArgumentVariables.ARG_CART_SELECTED_PATIENT_NAME);
        }
        if(cartSelectedPatientName == null) {
            cartSelectedPatientName = getArguments().getString(ArgumentVariables.ARG_CART_SELECTED_PATIENT_NAME);
        }
        findPatient(cartSelectedPatientName, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {

            }
        });
//        globalVariable.getInstance(getActivity().getApplicationContext());

        //set up view model
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        Log.d(getParentFragment()== null ?"null": "not null", "parent frag"+ cartSelectedPatientName);
//        //TODO:get medicinelist from the database
//        category = "dialysis";
//        String url = "http://192.168.0.9:8080/anhe/medicine/all?category" + category;
//        final List<MedicineCardViewModel> dialysisList = new ArrayList<>();
//        JsonArrayRequest jsonArrayRequest =
//                new JsonArrayRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        for(int i = 0; i < response.length(); i++){
//                            JSONObject object = null;
//                            try {
//                                object = response.getJSONObject(i);
//                                String name = object.getString("name");
//                                Integer id = object.getInt("mid");
//                                Integer price = object.getInt("price");
//                                String dose = object.getString("dose");
//                                Integer stock = object.getInt("stock");
//                                dialysisList.add(new MedicineCardViewModel(id, name, String.valueOf(price), dose, stock));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            cartViewModel.initDialysisList(dialysisList);
//                            Log.d("getting data from database", "CHLOE");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("VOLLEY", error.toString());
//                    }
//                } );
//


       // cartViewModel.initMedicineList(mContext);
        //cartViewModel.initEdibleList();

        //observe addtocart and removefrom cart to update ui
        cartViewModel.getCountLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer i) {
//                Log.d("counter fab get count is: " + mCounterfab.getCount(), "live data is: "+ i.intValue());
                    if(mCounterfab.getCount() + 1 == i.intValue()) {
                        mCounterfab.increase();
                    } else if (mCounterfab.getCount() - 1 == i.intValue()) {
                        mCounterfab.decrease();
                    }

            }
        });

//        cartViewModel.decreaseCount().observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                mCounterfab.decrease();
//            }
//        });


        mMedicineCategoryTabLayout = (TabLayout) view.findViewById(R.id.medicine_category_tabLayout);
        mCounterfab = view.findViewById(R.id.medicine_category_fab);

        //set up tab
        TabLayout.Tab dialysis = mMedicineCategoryTabLayout.newTab();
        mMedicineCategoryTabLayout.addTab(dialysis);

        TabLayout.Tab edible = mMedicineCategoryTabLayout.newTab();
        mMedicineCategoryTabLayout.addTab(edible);

        TabLayout.Tab needle = mMedicineCategoryTabLayout.newTab();
        mMedicineCategoryTabLayout.addTab(needle);

        TabLayout.Tab bandaid = mMedicineCategoryTabLayout.newTab();
        mMedicineCategoryTabLayout.addTab(bandaid);


        mMedicineCategoryTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mMedicineCategoryViewPager = (ViewPager) view.findViewById(R.id.medicine_category_pager);
        mMedicineCategoryTabLayout.setupWithViewPager(mMedicineCategoryViewPager);

        mMedicineCategoryPagerAdapter = new MedicineCategoryPagerAdapter(
                getChildFragmentManager(), mMedicineCategoryTabLayout.getTabCount(), mCounterfab, mContext);

        mMedicineCategoryViewPager.setAdapter(mMedicineCategoryPagerAdapter);
//        mMedicineCategoryViewPager.setCurrentItem(1);
        highLightCurrentTab(0);

        mMedicineCategoryViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMedicineCategoryTabLayout) {
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

        mMedicineCategoryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mMedicineCategoryViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mCounterfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cannot use getSupportFragmentManger(), it is for calling from activity, use getChildFragmentManager
                //https://stackoverflow.com/questions/7508044/android-fragment-no-view-found-for-id
               // FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
               FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                SummaryFragment summaryFragment = SummaryFragment.newInstance();
                Bundle args = new Bundle();
                args.putString(ArgumentVariables.ARG_CART_SELECTED_PATIENT_NAME, cartSelectedPatientName);
                summaryFragment.setArguments(args);
//                ArrayList<String> cartlist = new ArrayList<>();
//                for(MedicineCardViewModel item :medicineList.getMedicineList()) {
//                    if(item.getIsAddToCart() == true) {
//                        cartlist.add(item.getMedicinName());
//                    }
//                }
//                args.putStringArrayList(ArgumentVariables.ARG_CARTLIST, cartlist);
//                selectPatientFragment.setArguments(args);
                transaction.replace(R.id.medicine_category_layout, summaryFragment)
                        .addToBackStack("summary")
                        .commit();
            }
        });


        setRetainInstance(true);
        return view;
    }


    private void highLightCurrentTab(int position) {
        for (int i = 0; i < mMedicineCategoryPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mMedicineCategoryTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mMedicineCategoryPagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mMedicineCategoryTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mMedicineCategoryPagerAdapter.getSelectedTabView(position));
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //use getChildFragmentManager instead of getSupportedFragmentManager
                getChildFragmentManager().popBackStack();
                return true;
            }
        }

        return false;
    }

    private void findPatient(String name, final VolleyCallBack volleyCallBack) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://" + globalVariable.getInstance().getIpaddress() +
                ":" + globalVariable.getInstance().getPort() + "/anhe/patient/name?name=" + name;
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        Integer pid = object.getInt("pid");
                                        String name = object.getString("name");
                                        String shift = object.getString("shift");
                                        String ic = object.getString("ic");
                                        String day = object.getString("day");
                                        PatientsCardViewModel p = new PatientsCardViewModel(pid, name, ic, shift, day);
                                        cartViewModel.getCartSelectedPatientLiveData().setValue(p);
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

}
