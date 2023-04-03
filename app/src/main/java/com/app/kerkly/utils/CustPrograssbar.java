package com.app.kerkly.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.app.kerkly.R;

public class CustPrograssbar {


    Dialog epicDialog2;

    public void prograssCreate(Context context){

        epicDialog2 = new Dialog(context);
        epicDialog2.setContentView(R.layout.progress_layout);
        epicDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog2.setCanceledOnTouchOutside(false);
        epicDialog2.show();

    }


    public void closePrograssBar() {

        epicDialog2.dismiss();
    }
}
