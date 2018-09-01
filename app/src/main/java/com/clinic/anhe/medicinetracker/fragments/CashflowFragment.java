package com.clinic.anhe.medicinetracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;

public class CashflowFragment extends Fragment {

    BottomNavigationView mBottomNavigationView;

    public static CashflowFragment newInstance() {
        CashflowFragment fragment = new CashflowFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_cashflow, container, false);
        mBottomNavigationView = view.findViewById(R.id.cashflow_bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.cashflow_today:
                        CashflowTodayFragment cashflowTodayFragment = CashflowTodayFragment.newInstance();
                        transaction.replace(R.id.cashflow_fragment_container, cashflowTodayFragment)
                                   .commit();
                        break;
                    case R.id.cashflow_uncharged:
                        CashflowUnchargedFragment cashflowUnchargedFragment = CashflowUnchargedFragment.newInstance();
                        transaction.replace(R.id.cashflow_fragment_container, cashflowUnchargedFragment)
                                   .commit();
                        break;
                    case R.id.cashflow_search:
                        CashflowSearchFragment cashflowSearchFragment = CashflowSearchFragment.newInstance();
                        transaction.replace(R.id.cashflow_fragment_container, cashflowSearchFragment)
                                   .commit();
                        break;
                }
                return false;
            }
        });

        mBottomNavigationView.getMenu().performIdentifierAction(R.id.cashflow_today, 0);

        return view;
    }
}
