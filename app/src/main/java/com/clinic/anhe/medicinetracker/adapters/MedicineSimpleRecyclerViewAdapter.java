package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.MedicineManageViewModel;
import com.clinic.anhe.medicinetracker.fragments.AddInventoryDialogFragment;
import com.clinic.anhe.medicinetracker.fragments.MedicineDetailFragment;
import com.clinic.anhe.medicinetracker.fragments.MedicineSimpleFragment;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.MedicineType;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineSimpleRecyclerViewAdapter extends RecyclerView.Adapter<MedicineSimpleRecyclerViewAdapter.MedicineSimpleViewHolder> {

    private MedicineType medicineType;
    private Context mContext;
    private List<MedicineCardViewModel> medicineList;
    private Fragment mFragment;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;

    public MedicineSimpleRecyclerViewAdapter( MedicineType medicineType, List<MedicineCardViewModel> medicineList, Fragment frag) {
        this.medicineType = medicineType;
        this.medicineList = medicineList;
        this.mFragment = frag;
//        currentList = new ArrayList<>();
//        for(MedicineCardViewModel m : medicineList) {
//            if(medicineType.toString().equals(m.getMedicineCategory())) {
//                currentList.add(m);
//            }
//        }
    }

    @NonNull
    @Override
    public MedicineSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_medicine_simple, parent, false);
        mContext = parent.getContext();
        MedicineSimpleViewHolder medicineSimpleViewHolder = new MedicineSimpleViewHolder(view);
        return medicineSimpleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineSimpleViewHolder holder, int position) {
        holder.name.setText(medicineList.get(position).getMedicinName());

        int monthlyused = 0;
        MedicineManageViewModel viewModel = ((MedicineSimpleFragment)mFragment).getMedicineManageViewModel();
        for(MedicineRecordCardViewModel r :viewModel.getMedicineListLiveData().getValue()){
            if(r.getMid().equals(medicineList.get(position).getMedicineId())){
                monthlyused ++;
            }
        }
        holder.stock.setText(monthlyused + "");
//        int stockNum = medicineList.get(position).getMedicineStock();
//        if (stockNum < 0) {
//            holder.stock.setTextColor(mContext.getResources().getColor(R.color.shortOfStock));
//        } else {
//            holder.stock.setTextColor(mContext.getResources().getColor(R.color.menuTextIconColor));
//        }
//        holder.stock.setText(stockNum + "");

    }

//    private void calcuteMonthlyUse(){
//        MedicineManageViewModel viewModel = ((MedicineSimpleFragment)mFragment).getMedicineManageViewModel();
//        for(MedicineRecordCardViewModel r :viewModel.getMedicineListLiveData().getValue()){
//
//        }
//    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MedicineSimpleViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
//        private Button addStock;
        private TextView stock;
        private ImageButton mDeleteButton;

        public MedicineSimpleViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medicine_simple_name);
//            addStock = itemView.findViewById(R.id.medicine_simple_add_inventory);
            stock = itemView.findViewById(R.id.medicine_simple_stock);
            mDeleteButton = itemView.findViewById(R.id.medicine_simple_delete);

//            addStock.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MedicineCardViewModel current = medicineList.get(getAdapterPosition());
//                    //TODO: sending medicine.mid and medicine.name to addinventoryDialog
//                    AddInventoryDialogFragment addInventoryDialogFragment =
//                            AddInventoryDialogFragment.newInstance(current.getMedicinName(), current.getMedicineId(), current.getMedicineStock(), mFragment);
//                    addInventoryDialogFragment.show(mFragment.getFragmentManager(), "addStock");
//                    notifyDataSetChanged();
//                }
//            });


            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int deletePosition = getAdapterPosition();
                    MedicineCardViewModel m = medicineList.get(deletePosition);
                    SweetAlertDialog deleteAlert = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                    deleteAlert.setTitleText("確定刪除" + m.getMedicinName() +"嗎?");
                    deleteAlert.setConfirmText("確定");
                    deleteAlert.setCancelText("取消");
                    deleteAlert.show();

                    deleteAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            deleteMedicine(m.getMedicineId(), new VolleyCallBack() {
                                @Override
                                public void onResult(VolleyStatus status) {
                                    if(status == VolleyStatus.SUCCESS) {
                                        medicineList.remove(deletePosition);
                                        notifyItemRemoved(deletePosition);
                                        Toast.makeText(mContext,"刪除" + m.getMedicinName() +"成功",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mContext, "刪除" + m.getMedicinName() +"失敗", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            deleteAlert.dismiss();
                        }
                    });

                    deleteAlert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            deleteAlert.dismiss();
                        }
                    });
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicineDetailFragment medicineDetailFragment = MedicineDetailFragment.newInstance(name.getText().toString());
                    FragmentTransaction transaction = mFragment.getActivity().getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.medicine_manage_layout, medicineDetailFragment)
                            .addToBackStack(ArgumentVariables.TAG_MEDICINE_DETAIL_FRAGMENT)
                            .commit();

                }
            });
        }
    }

    public void deleteMedicine(int mid, final  VolleyCallBack volleyCallBack) {

        String url = "http://" + globalVariable.getInstance().getIpaddress() +
                ":" + globalVariable.getInstance().getPort() + "/anho/medicine/delete?mid=" + mid;
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("saved")) {
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
                        } ){/**
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
                }};

        volleyController.getInstance(mContext).addToRequestQueue(stringRequest);
    }
}
