package com.naruto.udacity_inventory.data;

/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-23
 * Time: 13:13
 * Desc: UdaCity_InventoryApp
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.naruto.udacity_inventory.data.GoodsContract.GoodsEntry;

public class GoodsProvider extends ContentProvider {

	public static final String LOG_TAG = GoodsProvider.class.getSimpleName();
	private static final int GOODS = 100;
	private static final int GOODS_ID = 101;
	/**
	 * UriMatcher对象将传入的uri匹配至对应的代码
	 */
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private GoodsDbHelper mDbHelper;

	static {
		// 匹配获取所有货物信息的uri
		sUriMatcher.addURI(GoodsContract.CONTENT_AUTHORITY, GoodsContract.PATH_GOODS, GOODS);
		// 匹配获取单行货物信息的uri
		sUriMatcher.addURI(GoodsContract.CONTENT_AUTHORITY, GoodsContract.PATH_GOODS + "/#", GOODS_ID);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new GoodsDbHelper(getContext());

		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// 创建一个游标，放置查询的结果
		Cursor cursor;
		// 建立数据库连接
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		// 根据传入的uri做不同处理
		int match = sUriMatcher.match(uri);
		switch (match) {
		case GOODS:
			// 获取整张表所有的货物信息
			cursor = db.query(GoodsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

			break;

		case GOODS_ID:
			// 获取单行货物信息
			selection = GoodsEntry._ID + "=?";
			selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
			cursor = db.query(GoodsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

			break;

		default:

			throw new IllegalArgumentException("Cannot query unknomn URI " + uri);
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case GOODS:
			return insertGoods(uri, contentValues);
		default:
			throw new IllegalArgumentException("Insertion is not supported for " + uri);
		}
	}

	private Uri insertGoods(Uri uri, ContentValues values) {
		// 对货物信息做校验
		GoodsContract.GoodsEntry.isVaildGoods(values);
		// 建立数据库连接
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// 根据传入的值，插入一条新的货物数据
		long id = db.insert(GoodsEntry.TABLE_NAME, null, values);
		// 如果id=-1，说明插入失败，打印失败的log信息
		if (id == -1) {
			Log.e(LOG_TAG, "Failed to insert row for " + uri);
			return null;
		}
		// 传入的uri的内容发现变化时，重新加载
		getContext().getContentResolver().notifyChange(uri, null);

		return ContentUris.withAppendedId(uri, id);
	}

	@Override
	public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// 根据传入的uri，采取不同的操作
		int match = sUriMatcher.match(uri);
		switch (match) {
		case GOODS:

			return updateGoods(uri, values, selection, selectionArgs);

		case GOODS_ID:
			selection = GoodsEntry._ID + "=?";
			selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

			return updateGoods(uri, values, selection, selectionArgs);

		default:

			throw new IllegalArgumentException("Update is not supported for " + uri);
		}
	}

	private int updateGoods(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// 对更新值做非空判断
		if (values.size() == 0) {
			return 0;
		}
		// 对货物名称进行完整性判断
		if (values.containsKey(GoodsEntry.COLUMN_GOODS_NAME)) {
			String name = values.getAsString(GoodsEntry.COLUMN_GOODS_NAME);
			if (name == null) {
				throw new IllegalArgumentException("Goods requires a name");
			}
		}
		// 对货物数量进行完整性判断
		if (values.containsKey(GoodsEntry.COLUMN_GOODS_QUANTITY)) {
			Integer quantity = values.getAsInteger(GoodsEntry.COLUMN_GOODS_QUANTITY);
			if (quantity == null) {
				throw new IllegalArgumentException("Goods requires vaild quantity");
			}
		}
		// 对货物价格进行完整性判断
		if (values.containsKey(GoodsEntry.COLUMN_GOODS_PRICE)) {
			String price = values.getAsString(GoodsEntry.COLUMN_GOODS_NAME);
			if (price == null) {
				throw new IllegalArgumentException("Goods requires vaild price");
			}
		}
		// 对货物供应商进行完整性判断
		if (values.containsKey(GoodsEntry.COLUMN_GOODS_SUPPLIER)) {
			String supplier = values.getAsString(GoodsEntry.COLUMN_GOODS_NAME);
			if (supplier == null) {
				throw new IllegalArgumentException("Goods requires vaild supplier");
			}
		}
		// 建立数据库连接
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// 更新数据
		int rowUpdated = db.update(GoodsEntry.TABLE_NAME, values, selection, selectionArgs);

		// 只要被更新的行号！=0，就说明更新成功，并对其观察，当其内容发生改变时，重新加载
		if (rowUpdated != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowUpdated;
	}

	@Override
	public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
		// 建立数据库连接
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// 初始化被删除的行号；
		int rowDeleted;
		int match = sUriMatcher.match(uri);
		switch (match) {
		case GOODS:
			// 删除数据库中的所有行
			rowDeleted = db.delete(GoodsEntry.TABLE_NAME, selection, selectionArgs);

			break;
		case GOODS_ID:
			// 删除数据库中的指定行
			selection = GoodsEntry._ID + "=?";
			selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
			rowDeleted = db.delete(GoodsEntry.TABLE_NAME, selection, selectionArgs);

			break;

		default:

			throw new IllegalArgumentException("Deletion is not supported for " + uri);
		}
		// 只要被删除的行号！=0，就说明删除操作成功，并对其观察，当其内容发生改变时，重新加载
		if (rowDeleted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowDeleted;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		int match = sUriMatcher.match(uri);
		switch (match) {
		case GOODS:

			return GoodsEntry.CONTENT_LIST_TYPE;

		case GOODS_ID:

			return GoodsEntry.CONTENT_ITEM_TYPE;

		default:

			throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
		}
	}
}