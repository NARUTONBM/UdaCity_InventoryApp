package com.naruto.udacity_inventory.utils;

/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-02-06
 * Time: 13:23
 * Desc: UdaCity_InventoryApp
 */

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PopularViewUtil {

	public static PopupWindow setPopupView(View view, View popupView) {
		// 透明动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(500);
		alphaAnimation.setFillAfter(true);
		// 缩放动画
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(500);
		scaleAnimation.setFillAfter(true);
		// 动画集合set--设置为true表示公用同一个插补器，也就是说两个动画的运动模式是一样
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);

		// 1.创建窗体对象，指定宽高
		PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
		// 2.设置一个透明背景
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		// 3.指定窗体位置
		popupWindow.showAsDropDown(view, 180, -(view.getHeight() + 20));

		// 启动动画
		popupView.startAnimation(animationSet);

		return popupWindow;
	}
}
