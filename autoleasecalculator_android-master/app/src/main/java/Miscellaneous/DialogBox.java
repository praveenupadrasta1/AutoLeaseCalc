package Miscellaneous;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.autolease.autoleasecalculator.R;


public class DialogBox {

    public Dialog createInfoDialogBox(String msg, Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.info_dialog);
        TextView txtInfo = dialog.findViewById(R.id.txt_info);
        txtInfo.setText(msg);
        dialog.setTitle("Info...");
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public Dialog createErrorDialogBox(String msg, Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.error_dialog);
        TextView txtInfo = (TextView)dialog.findViewById(R.id.txt_error);
        txtInfo.setText(msg);
        dialog.setTitle("Error...");
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
