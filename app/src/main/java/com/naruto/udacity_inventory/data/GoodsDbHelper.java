package com.naruto.udacity_inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.naruto.udacity_inventory.data.GoodsContract.GoodsEntry;

/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-23
 * Time: 12:58
 * Desc: UdaCity_InventoryApp
 */

public class GoodsDbHelper extends SQLiteOpenHelper {

	public static final String LOG_TAG = GoodsDbHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "inventory.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文环境
	 */
	public GoodsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * 第一次创建数据库时调用
	 * 
	 * @param db
	 *            数据库对象
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建sql语句
		String SQL_CREATE_GOODS_TABLE = "CREATE TABLE " + GoodsContract.GoodsEntry.TABLE_NAME + "(" + GoodsEntry._ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + GoodsEntry.COLUMN_GOODS_NAME + " TEXT NOT NULL, "
						+ GoodsEntry.COLUMN_GOODS_CATEGORY + " INTEGER NOT NULL, " + GoodsEntry.COLUMN_GOODS_PRICE + " DOUBLE NOT NULL, "
						+ GoodsEntry.COLUMN_GOODS_QUANTITY + " INTEGER NOT NULL, " + GoodsEntry.COLUMN_GOODS_SUPPLIER + " TEXT NOT NULL, "
						+ GoodsEntry.COLUMN_GOODS_ICON + " BLOB);";
		// 执行SQL语句
		db.execSQL(SQL_CREATE_GOODS_TABLE);
	}

	/**
	 * 数据库结构发生变化时调用
	 * 
	 * @param db
	 *            数据库对象
	 * @param oldVersion
	 *            数据库旧版本号
	 * @param newVersion
	 *            数据库新版本号
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + GoodsEntry.TABLE_NAME);
		onCreate(db);
	}
}