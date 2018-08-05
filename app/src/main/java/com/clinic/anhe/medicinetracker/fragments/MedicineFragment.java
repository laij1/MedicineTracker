package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.CounterFab;

import java.util.ArrayList;
import java.util.List;



public class MedicineFragment extends Fragment implements View.OnKeyListener {

    private CartViewModel medicineList;
    private RecyclerView mRecyclerView;
    private MedicineRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CounterFab mCounterFab;
   // private List<MedicineCardViewModel> list;
   // private ImageView mBottomImageView;
   // private RadioGroup mRadioGroup;


    public static MedicineFragment newInstance() {
        MedicineFragment fragment = new MedicineFragment();
        return fragment;
    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        FragmentManager manager = getFragmentManager();
//        if (savedInstanceState != null) {
//            medicineFragment = (MedicineFragment) manager.getFragment(savedInstanceState, "MedicineFragment");
//        }
//        Log.d("on restore instance is called", "CHLOE!!");
//        //restore will be called!
//
//    }

//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        onSaveInstanceState(new Bundle());
//        //this will be called
//        Log.d("the view has been destoried!", "CHLOE!!!");
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_medicine, container, false);


       // prepareMedicineData();
        mRecyclerView = view.findViewById(R.id.medicine_recyclerview);
        mRecyclerView.setHasFixedSize(true);
       // mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mCounterFab = view.findViewById(R.id.medicine_fab);

        // Set up the WordViewModel.
        medicineList = ViewModelProviders.of(this).get(CartViewModel.class);

       // medicineList.setMedicineList(list);
       // medicineList.getMedicineLiveData().setValue(list);

        // and associate them to the adapter.
        medicineList.getMedicineLiveData().observe(this, new Observer<List<MedicineCardViewModel>>() {
            @Override
            public void onChanged(@Nullable final List<MedicineCardViewModel> medicineList) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setList(medicineList);
            }
        });

       // mBottomImageView = view.findViewById(R.id.medicine_add_button);
       // mRadioGroup = view.findViewById(R.id.payment_radiogroup);

        mAdapter = new MedicineRecyclerViewAdapter(medicineList, mCounterFab);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        mCounterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //cannot use getSupportFragmentManger(), it is for calling from activity, use getChildFragmentManager
                //https://stackoverflow.com/questions/7508044/android-fragment-no-view-found-for-id
               // FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
               FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                SelectPatientFragment selectPatientFragment = SelectPatientFragment.newInstance();
//                Bundle args = new Bundle();
//                ArrayList<String> cartlist = new ArrayList<>();
//                for(MedicineCardViewModel item :medicineList.getMedicineList()) {
//                    if(item.getIsAddToCart() == true) {
//                        cartlist.add(item.getMedicinName());
//                    }
//                }
//                args.putStringArrayList(ArgumentVariables.ARG_CARTLIST, cartlist);
//                selectPatientFragment.setArguments(args);
                transaction.replace(R.id.medicine_layout, selectPatientFragment)
                        .addToBackStack("medicine")
                        .commit();
            }


        });


        return view;
    }





//    private void prepareMedicineData(){
//        list = new ArrayList<>();
//
//        list.add(new MedicineCardViewModel("HDF", "", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("NESP", "20ug", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("NESP", "40ug", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("EPO", "2000", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("Carnitine", "原廠", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("Carnitine", "台廠", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("Provita", " ", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("循利寧", "", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("Nephrosteril", "", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("IDPN", "", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("Methionin-B", "", R.drawable.ic_pills));
//        list.add(new MedicineCardViewModel("Anol", "", R.drawable.ic_pills));
//
//
//
//
//    }

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
}
