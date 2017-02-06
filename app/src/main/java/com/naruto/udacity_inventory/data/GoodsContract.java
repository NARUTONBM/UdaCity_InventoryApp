package com.naruto.udacity_inventory.data;

/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-22
 * Time: 19:04
 * Desc: UdaCity_InventoryApp
 */

import android.content.ContentResolver;
import android.content.ContentValues;
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

		public static void isVaildGoods(ContentValues values) {
			// 对货物名称进行完整性检查
			String name = values.getAsString(GoodsEntry.COLUMN_GOODS_NAME);
			if (name == null) {
				throw new IllegalArgumentException("Goods requires a name");
			}
			// 对货物价格进行完整性检查
			double price = values.getAsDouble(GoodsEntry.COLUMN_GOODS_PRICE);
			if (price <= 0) {
				throw new IllegalArgumentException("Goods requires vaild price");
			}
			// 对货物数量进行完整性检查
			Integer quantity = values.getAsInteger(GoodsEntry.COLUMN_GOODS_QUANTITY);
			if (quantity < 0) {
				throw new IllegalArgumentException("Goods requires vaild quantity");
			}
			// 对货物供货商进行完整性检查
			String supplier = values.getAsString(GoodsEntry.COLUMN_GOODS_SUPPLIER);
			if (supplier == null) {
				throw new IllegalArgumentException("Goods requires vaild supplier");
			}
		}
	}
}