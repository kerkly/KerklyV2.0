package com.app.kerkly.utils

import android.app.Dialog
import android.content.Context
import com.app.kerkly.R

object ProgresCust {


    fun showLoadingDialog(context: Context): Dialog {
        val progressDialog = Dialog(context)

        progressDialog.let {
            it.show()
          //  it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.progress_layout)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }
 
}