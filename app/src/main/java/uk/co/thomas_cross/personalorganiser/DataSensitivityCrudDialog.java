package uk.co.thomas_cross.personalorganiser;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;

/**
 * Created by thomas on 19/03/18.
 */

public class DataSensitivityCrudDialog extends DialogFragment {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;

    DataSensitivity dataSensitivity = null;

    public DataSensitivityCrudDialog() {
    }

    public static DataSensitivityCrudDialog newInstance(int mode, DataSensitivity dataSensitivity) {

        DataSensitivityCrudDialog frag = new DataSensitivityCrudDialog();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("dataSensitivity", dataSensitivity);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.dataSensitivity = (DataSensitivity) getArguments().getSerializable("dataSensitivity");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.crud_form_data_sensitivity, null);

        final TextView recordNoLabel = view.findViewById(R.id.record_no_label);
        final TextView recordNoView = view.findViewById(R.id.record_no);
        final TextView ownerViewLabel = view.findViewById(R.id.owner_label);
        final TextView ownerView = view.findViewById(R.id.owner);
        final TextView ownerTypeLabel = view.findViewById(R.id.owner_type_label);
        final TextView ownerTypeView = view.findViewById(R.id.owner_type);
        final TextView titleTextView = view.findViewById(R.id.title_text_view);
        final EditText titleEditView = view.findViewById(R.id.title_edit_text);

        final View dialogHeader = inflater.inflate(R.layout.custom_view, null);
        final TextView dialogTitle = dialogHeader.findViewById(R.id.title);
        final ImageButton deleteButton = dialogHeader.findViewById(R.id.delete_button);
        final ImageButton updateButton = dialogHeader.findViewById(R.id.update_button);
        final ImageButton doneButton = dialogHeader.findViewById(R.id.done_button);
        final ImageButton deleteForeverButton = dialogHeader.findViewById(R.id.delete_forever_button);

        final POModel poModel = POModel.getInstance(this.getContext());
        recordNoView.setText(String.valueOf(dataSensitivity.getDatabaseRecordNo()));
        ownerView.setText(
                poModel.getUserTitle(
                        dataSensitivity.getOwner(),
                        dataSensitivity.getOwnerType()));

        ownerTypeView.setText(
                poModel.getOwnerType(dataSensitivity.getOwnerType())
        );
        titleTextView.setText(dataSensitivity.getTitle());
        titleEditView.setText(dataSensitivity.getTitle());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Delete");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.VISIBLE);

                titleEditView.setVisibility(View.GONE);
            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSensitivity.getDatabaseRecordNo() != 0)
                    poModel.deleteDataSensitivity(dataSensitivity.getDatabaseRecordNo());
                getDialog().dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataSensitivity.getDatabaseRecordNo() == 0)
                    return;

                String title = titleEditView.getText().toString();
                if (title.length() == 0)
                    return;
                dataSensitivity.setTitle(title);
                poModel.updateDataSensitivity(dataSensitivity);
                getDialog().dismiss();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Update");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);

                recordNoLabel.setVisibility(View.GONE);
                recordNoView.setVisibility(View.GONE);
                ownerViewLabel.setVisibility(View.GONE);
                ownerView.setVisibility(View.GONE);
                ownerTypeLabel.setVisibility(View.GONE);
                ownerTypeView.setVisibility(View.GONE);
                titleTextView.setVisibility(View.GONE);
                titleEditView.setVisibility(View.VISIBLE);
            }

        });

        switch (mode) {
            case CREATE_MODE:

                dialogTitle.setText("Create");

                recordNoLabel.setVisibility(View.GONE);
                recordNoView.setVisibility(View.GONE);
                ownerViewLabel.setVisibility(View.GONE);
                ownerView.setVisibility(View.GONE);
                ownerTypeLabel.setVisibility(View.GONE);
                ownerTypeView.setVisibility(View.GONE);
                titleTextView.setVisibility(View.GONE);
                titleEditView.requestFocus();

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titleEditView.getText().toString();
                        if (title.length() > 0) {
                            dataSensitivity.setTitle(title);
                            poModel.addDataSensitivity(dataSensitivity);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);

                break;

            case READ_MODE:
                dialogTitle.setText("View");
                titleEditView.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
        }
        builder.setCustomTitle(dialogHeader);
        builder.setView(view);
        return builder.create();
    }

}
