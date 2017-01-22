package com.naruto.udacity_inventory.data;

/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-22
 * Time: 19:04
 * Desc: UdaCity_InventoryApp
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class GoodsContract {

	public static final String CONTENT_AUTHORITY = "com.naruto.udacity_inventory";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_GOODS = "goods";

	private GoodsContract() {
	}

	public static final class GoodsEntry implements BaseColumns {
		// 获取provider中的数据的uri
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GOODS);
		// 数据库中所有goods数组的MIME类型
		public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GOODS;
		// 数据库中单行goods的MIME类型
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GOODS;
		// 库存货物的数据库
		public static final String TABLE_NAME = "goods";
		// 库存数据库的货物id
		public static final String _ID = BaseColumns._ID;
		// 库存数据库的货物名称
		public static final String COLUMN_GOODS_NAME = "name";
		// 库存数据库的货物类别
		public static final String COLUMN_GOODS_CATEGORY = "category";
		// 库存数据库的货物售价
		public static final String COLUMN_GOODS_PRICE = "price";
		// 库存数据库的货物数量
		public static final String COLUMN_GOODS_QUANTITY = "quantity";
		// 库存数据库的货物供应商
		public static final String COLUMN_GOODS_SUPPLIER = "supplier";
		// 库存数据库的货物图标
		public static final String COLUMN_GOODS_ICON = "icon";
		// 库存数据的货物可能的商品类别
		public static final int CATEGORY_ARTS_CRAFTS_SEWING = 0;
		public static final int CATEGORY_AUTOMOTIVE_PARTS_ACCESSORIES = 1;
		public static final int CATEGORY_BABY = 2;
		public static final int CATEGORY_BEAUTY_PERSONAL_CARE = 3;
		public static final int CATEGORY_BOOKS = 4;
		public static final int CATEGORY_CDS_VINYL = 5;
		public static final int CATEGORY_CELL_PHONE_ACCESSORIES = 6;
		public static final int CATEGORY_CLOTHING_SHOES_JEWELRY = 7;
		public static final int CATEGORY_COLLECTIBLES_FINE_ART = 8;
		public static final int CATEGORY_COMPUTERS = 9;
		public static final int CATEGORY_COURSES = 10;
		public static final int CATEGORY_CREDIT_PAYMENT_CARDS = 11;
		public static final int CATEGORY_DIGITAL_MUSIC = 12;
		public static final int CATEGORY_ELECTRONICS = 13;
		public static final int CATEGORY_GIFT_CARDS = 14;
		public static final int CATEGORY_OTHER = 15;

		/**
		 * 判断当前类别值是否合法
		 * 
		 * @param category
		 *            当前类别值
		 * @return 判断的结果，true--符合；false--不符合
		 */
		public static boolean isVaildCategory(int category) {
			return category == CATEGORY_ARTS_CRAFTS_SEWING || category == CATEGORY_AUTOMOTIVE_PARTS_ACCESSORIES || category == CATEGORY_BABY
							|| category == CATEGORY_BEAUTY_PERSONAL_CARE || category == CATEGORY_BOOKS || category == CATEGORY_CDS_VINYL
							|| category == CATEGORY_CELL_PHONE_ACCESSORIES || category == CATEGORY_CLOTHING_SHOES_JEWELRY
							|| category == CATEGORY_COLLECTIBLES_FINE_ART || category == CATEGORY_COMPUTERS || category == CATEGORY_COURSES
							|| category == CATEGORY_CREDIT_PAYMENT_CARDS || category == CATEGORY_DIGITAL_MUSIC || category == CATEGORY_ELECTRONICS
							|| category == CATEGORY_GIFT_CARDS || category == CATEGORY_OTHER;

		}
	}
}