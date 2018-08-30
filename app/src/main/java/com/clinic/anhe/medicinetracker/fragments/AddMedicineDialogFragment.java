package com.clinic.anhe.medicinetracker.fragments;



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import com.clinic.anhe.medicinetracker.R;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddMedicineDialogFragment extends DialogFragment {
     ImageButton mbutton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_medicine, container, false);
        if(getDialog() == null) {
            Log.d("dialog is null", "CHLOE!!!!");
        }
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        mbutton = view.findViewById(R.id.add_medicine_ok);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "it is clicked", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        Dialog d = super.onCreateDialog(savedInstanceState);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder
//        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
////        //dialog.setContentView(layoutResId);
////        getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView((View) d, )
//////                 Add action buttons
//                .setPositiveButton("confirm",null)
//// new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int id) {
////                        // sign in the user ...
////                    }
////                })
//                .setNegativeButton("cancel",null);
////  new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////                        LoginDialogFragment.this.getDialog().cancel();
////                    }
////                });
////        return builder.create();
//        return builder.create();
//    }

}
