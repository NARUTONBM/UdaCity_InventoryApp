package com.naruto.udacity_inventory.utils;

/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-23
 * Time: 17:18
 * Desc: UdaCity_InventoryApp
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DbBitmapUtil {

	/**
	 * 将图片文件的bitmap转换成字节数组
	 * 
	 * @param bitmap
	 *            要转换的bitmap
	 * @return 以字节数组返回字节流
	 */
	public static byte[] getBytes(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

		return stream.toByteArray();
	}

	/**
	 * 将字节数组形式的图片转换成bitmap
	 * 
	 * @param image
	 *            需要转换的自己数组形式的image
	 * @return 返回转换后的bitmap
	 */
	public static Bitmap getImage(byte[] image) {

		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}
}
