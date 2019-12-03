package com.tsml.hkl.Toast;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;


public class ToastS {
	private static Toast toast;//声明Toast对象
	private static TextView tv;//声明TextView对象
	private static Context c;//声明上下文
	private static int errorBkColor=0xFFFB7299;//erro边框颜色
	private static int errorPaddingColor=0xFFFFFFFF;//error内部填充颜色
	private static int errortextColor=0xFFFB7299;//error文本颜色
	private static int successBkColor=0xFF499A4E;//success边框颜色
	private static int successPaddingColor=0xFFFFFFFF;//success内部填充颜色
	private static int successtextColor=0xFF499A4E;//success文本颜色
	private static int infoBkColor=0xFF4F62BD;//info边框颜色
	private static int infoPaddingColor=0xFFFFFFFF;//info内部填充颜色
	private static int infotextColor=0xFF4F62BD;//info文本颜色
	private static int warningBkColor=0xFFFFB212;//warning边框颜色
	private static int warningPaddingColor=0xFFFFFFFF;//warning内部填充颜色
	private static int warningtextColor=0xFFFFB212;//warning文本颜色

	public ToastS(Context c) {
		this.c = c;
		this.tv = new TextView(c);
	}

	//设置控件圆角等属性
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private static void myDrawable(int bkColor, int paddingColor) {
		//创建GradientDrawable对象，设置控件圆角等属性
		GradientDrawable drawable=new GradientDrawable();
		drawable.setSize(500, 100);//设置长宽
		drawable.setStroke(5, bkColor);//设置边框宽度/颜色
		drawable.setColor(paddingColor);//设置内部填充颜色
		drawable.setCornerRadius(45);//设置圆角半径
		tv.setBackground(drawable);//将TextView背景设置为drawable
	}

	private static Toast msg(Context context, String content, int length, int textColor, int bkColor, int paddingColor) {
		//如果toast不为空
		if (toast != null) {
			toast.cancel();//防止Toast重复弹出
		}
		tv = new TextView(context);//创建TextView对象
		tv.setText(content);//设置文本内容
		tv.setTextColor(textColor);
		tv.setTextSize(18);//设置文本大小
		tv.setGravity(Gravity.CENTER);//设置文本位置
		tv.setPadding(10, 5, 10, 5);//设置文本内边距
		myDrawable(bkColor, paddingColor);
		toast = new Toast(context);//创建Toast对象
		toast.setView(tv);//设置视图
		toast.setDuration(length);//设置显示时长
		toast.setGravity(Gravity.BOTTOM, 0, 200);//设置位置
		toast.show();
		return toast;//返回Toast对象
	}

	private static Toast errorMsg(Context context, String content, int length) {
		return msg(context, content, length, errortextColor, errorBkColor, errorPaddingColor);
	}

	private static Toast successMsg(Context context, String content, int length) {
		return msg(context, content, length, successtextColor, successBkColor, successPaddingColor);
	}

	private static Toast infoMsg(Context context, String content, int length) {
		return msg(context, content, length, infotextColor, infoBkColor, infoPaddingColor);
	}

	private static Toast warningMsg(Context context, String content, int length) {
		return msg(context, content, length, warningtextColor, warningBkColor, warningPaddingColor);
	}

	/**
	 *错误Toast
	 *@param context 上下文
	 *@param content 弹出提示的内容
	 *@param length 显示时长
	 */
	public static Toast error(Context context, String content, int length) {
		return errorMsg(context, content, length);
	}

	/**
	 *成功Toast
	 *@param context 上下文
	 *@param content 弹出提示的内容
	 *@param length 显示时长
	 */
	public static Toast success(Context context, String content, int length) {
		return successMsg(context, content, length);
	}

	/**
	 *信息Toast
	 *@param context 上下文
	 *@param content 弹出提示的内容
	 *@param length 显示时长
	 */
	public static Toast info(Context context, String content, int length) {
		return infoMsg(context, content, length);
	}

	/**
	 *警告Toast
	 *@param context 上下文
	 *@param content 弹出提示的内容
	 *@param length 显示时长
	 */
	public static Toast warning(Context context, String content, int length) {
		return warningMsg(context, content, length);
	}
}

