package com.linxmap.iplayerfeed.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.linxmap.iplayerfeed.R;

public class DialogManager {

    private static final String LOG_TAG = "DialogManager";
    private static final boolean LOG_ENABLED = true;
    private Context context;
    private AlertDialog networkStatusDialog;
    private boolean isNetworkStatusDialogShowing = false;

    public DialogManager(Context context) {
        this.context = context;
    }

    public void launchNoNetworkConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_network_connection_error_string))
                .setCancelable(true)
                .setTitle(context.getString(R.string.error_string))
                .setPositiveButton(context.getResources().getString(R.string.dialog_settings_button_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        } catch (Exception e) {
                            Toast.makeText(context, "Error, settings can not be opened.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        setNetworkStatusDialogShowing(false);
                    }
                })
                .setNegativeButton(context.getString(R.string.dialog_close_button_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        setNetworkStatusDialogShowing(false);
                    }
                });
        networkStatusDialog = builder.create();
        if (!networkStatusDialog.isShowing()) {
            networkStatusDialog.show();
            setNetworkStatusDialogShowing(true);
        }

    }

    public void hideNetworkConnectionDialog() {
        if (networkStatusDialog != null && networkStatusDialog.isShowing())
            networkStatusDialog.dismiss();
    }

    public boolean isNetworkStatusDialogShowing() {
        return isNetworkStatusDialogShowing;
    }

    private void setNetworkStatusDialogShowing(boolean showing) {
        this.isNetworkStatusDialogShowing = showing;
    }

}
