package travelan.art.sangeun.travelan.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import travelan.art.sangeun.travelan.R;

public class Alert {
    public static void show(Context context, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(message);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
