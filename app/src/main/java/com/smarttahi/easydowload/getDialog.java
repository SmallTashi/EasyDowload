package com.smarttahi.easydowload;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

public class getDialog extends AlertDialog {

    protected getDialog(Context context) {
        super(context);
    }
    public static Builder getInputDialog( Inflater inflater, String title, String hint, @Nullable View layoutId, OnClickListener actionNext,OnClickListener actionCancel){
        Builder builder = new Builder(MyApplication.getThisContext());
            if(layoutId!=null){
                builder.setTitle(title)
                        .setView(layoutId)
                        .setPositiveButton("Next",actionNext)
                        .setNegativeButton("Cancel",actionCancel)
                        .create();
            }else {

                //TODO
//                View view = inflater(R.layout.input_dialog,null)
                builder.setTitle(title)
                        .setPositiveButton("Next",actionNext)
                        .setNegativeButton("Cancel",actionCancel)
                        .create();
            }

            return builder;
    }
}
