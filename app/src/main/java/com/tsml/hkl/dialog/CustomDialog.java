package com.tsml.hkl.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tsml.hkl.R;


public class CustomDialog extends Dialog
{
    //    style引用style样式
	private boolean ation;
    public CustomDialog(Context context, View layout, int style, boolean ation)
	{
        super(context, style);
        setContentView(layout);
		this.ation = ation;
        Window window = getWindow();
        /*WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);*/
		window.setWindowAnimations(R.style.mystyle);

    }
	@Override
    public void show()
	{
        super.show();
		if (ation)
		{
			WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
			layoutParams.gravity = Gravity.BOTTOM;
			layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
			layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			getWindow().getDecorView().setPadding(0, 0, 0, 0);
			getWindow().setAttributes(layoutParams);
		}
    }
}
