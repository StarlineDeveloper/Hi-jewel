package com.hijewel.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
//import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hijewel.R;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class FilterFragment extends BottomSheetDialogFragment {

    OnApplyListener listener;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        listener = (OnApplyListener) getActivity();

        View contentView = View.inflate(getContext(), R.layout.for_whom, null);
        dialog.setContentView(contentView);

        TextView apply = contentView.findViewById(R.id.apply);
        final CheckBox male = contentView.findViewById(R.id.male);
        male.setChecked(getArguments().getBoolean("male"));
        final CheckBox female = contentView.findViewById(R.id.female);
        female.setChecked(getArguments().getBoolean("female"));

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean M = false, F = false;
                if (male.isChecked())
                    M = true;
                if (female.isChecked())
                    F = true;
                listener.onApply(M, F);
                dialog.dismiss();
            }
        });
    }

    public interface OnApplyListener {
        public void onApply(boolean male, boolean female);
    }
}
