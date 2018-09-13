package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.MedicineType;

import java.util.List;



public class MedicineFragment extends Fragment {
//        implements View.OnKeyListener {

    private CartViewModel cartViewModel;
    private RecyclerView mRecyclerView;
    private MedicineRecyclerViewAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private static CounterFab mCounterFab;
    private MedicineType medicineType;
   // private List<MedicineCardViewModel> list;
   // private ImageView mBottomImageView;
   // private RadioGroup mRadioGroup;


    public static MedicineFragment newInstance(CounterFab counterFab, MedicineType medicineType) {
        MedicineFragment fragment = new MedicineFragment();
        mCounterFab = counterFab;
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_MEDICINE_TYPE, medicineType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("OnDestory", "CHLOE!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("OnDestoryView", "CHLOE!");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: medicineType could be null....
        outState.putString(ArgumentVariables.ARG_MEDICINE_TYPE, medicineType.toString());
    }

//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////        view.setFocusableInTouchMode(true);
////        view.requestFocus();
//        view.setOnKeyListener(this);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view  = inflater.inflate(R.layout.fragment_medicine, container, false);

        if(savedInstanceState != null) {
            medicineType = medicineType.fromString(savedInstanceState.getString(ArgumentVariables.ARG_MEDICINE_TYPE));
        }

        if(medicineType == null) {
            medicineType = medicineType.fromString(getArguments().getString(ArgumentVariables.ARG_MEDICINE_TYPE));
        }
        if(medicineType == null) {
            Log.d("medicineType is null in OnCreateView", "CHLOE!!!!");
        } else {
            Log.d("medicineType is NOT null in OnCreateView", "CHLOE");
        }

       // prepareMedicineData();
        mRecyclerView = view.findViewById(R.id.medicine_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
      //  mLayoutManager = new GridLayoutManager(getContext(), 2);
//        mCounterFab = view.findViewById(R.id.medicine_fab);

        // Set up the WordViewModel.
//        Fragment f = getParentFragment();
//        Log.d(f.getTag(), "WE NEED TO KNOW");
        cartViewModel = ViewModelProviders.of(
               getParentFragment()).get(CartViewModel.class);

        if(cartViewModel.getDialysisList() == null) {
            Log.d("cartViewModel- dialysis list in medicineFragment is null", "CHLOEE!");
        } else {
            Log.d("cartViewModel- dialysis list in medicineFragment is NOT null", "CHLOEE!WEEE!!");
        }


        //init all the medicine in livedata
//        if(medicineType == MedicineType.edible) {
//            cartViewModel.initEdibleList();
//        }

       // cartViewModel.setMedicineList(list);
       // cartViewModel.getMedicineLiveData().setValue(list);
       // cartViewModel.init(medicineType);


       // mBottomImageView = view.findViewById(R.id.medicine_add_button);
       // mRadioGroup = view.findViewById(R.id.payment_radiogroup);

        mAdapter = new MedicineRecyclerViewAdapter(cartViewModel, mCounterFab, medicineType);

        // and associate them to the adapter.
        if(medicineType == MedicineType.dialysis) {
            cartViewModel.getDialysisLiveData().observe(getParentFragment(), new Observer<List<MedicineCardViewModel>>() {
                @Override
                public void onChanged(@Nullable final List<MedicineCardViewModel> dialysisList) {
                    // Update the cached copy of the words in the adapter.
                    mAdapter.setList(dialysisList);
                }
            });
        } else if(medicineType == MedicineType.edible) {
            cartViewModel.getEdibleLiveData().observe(getParentFragment(), new Observer<List<MedicineCardViewModel>>() {
                @Override
                public void onChanged(@Nullable List<MedicineCardViewModel> edibleList) {
                    mAdapter.setList(edibleList);
                }
            });
        } else if(medicineType == MedicineType.needle) {
            cartViewModel.getNeedleLiveData().observe(getParentFragment(), new Observer<List<MedicineCardViewModel>>() {
                @Override
                public void onChanged(@Nullable List<MedicineCardViewModel> needleList) {
                    mAdapter.setList(needleList);
                }
            });
        } else {
            cartViewModel.getBandaidLiveData().observe(getParentFragment(), new Observer<List<MedicineCardViewModel>>() {
                @Override
                public void onChanged(@Nullable List<MedicineCardViewModel> bandaidList) {
                    mAdapter.setList(bandaidList);
                }
            });

        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



//        mCounterFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                //cannot use getSupportFragmentManger(), it is for calling from activity, use getChildFragmentManager
//                //https://stackoverflow.com/questions/7508044/android-fragment-no-view-found-for-id
//               // FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
//               FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//
//                SelectPatientFragment selectPatientFragment = SelectPatientFragment.newInstance();
////                Bundle args = new Bundle();
////                ArrayList<String> cartlist = new ArrayList<>();
////                for(MedicineCardViewModel item :cartViewModel.getMedicineList()) {
////                    if(item.getIsAddToCart() == true) {
////                        cartlist.add(item.getMedicinName());
////                    }
////                }
////                args.putStringArrayList(ArgumentVariables.ARG_CARTLIST, cartlist);
////                selectPatientFragment.setArguments(args);
//                transaction.replace(R.id.medicine_layout, selectPatientFragment)
//                        .addToBackStack("medicine")
//                        .commit();
//            }
//
//
//        });

//setRetainInstance to true is important so that onSaveInstanceState will work
        setRetainInstance(true);
        return view;
    }


//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_UP) {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                //use getChildFragmentManager instead of getSupportedFragmentManager
//                getChildFragmentManager().popBackStack();
//                return true;
//            }
//        }
//
//        return false;
//    }
}
