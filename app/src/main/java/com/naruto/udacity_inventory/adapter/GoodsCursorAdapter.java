package com.naruto.udacity_inventory.adapter;

/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-22
 * Time: 22:18
 * Desc: UdaCity_InventoryApp
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naruto.udacity_inventory.R;
import com.naruto.udacity_inventory.data.GoodsContract.GoodsEntry;
import com.naruto.udacity_inventory.utils.DbBitmapUtil;

public class GoodsCursorAdapter extends CursorAdapter {

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文环境
	 * @param c
	 *            要去取的数据的游标
	 */
	public GoodsCursorAdapter(Context context, Cursor c) {
		super(context, c, 0);
	}

	/**
	 * 创建空的listview，用于展示获得的数据
	 * 
	 * @param context
	 *            上下文环境
	 * @param cursor
	 *            要去取的数据的游标
	 * @param parent
	 *            父控件
	 * @return 返回一个listview对象
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		return LayoutInflater.from(context).inflate(R.layout.item_list_goods, parent, false);
	}

	/**
	 * 对拿到的空listview绑定数据，以展示结果
	 * 
	 * @param view
	 *            拿到的控件
	 * @param context
	 *            上下文景
	 * @param cursor
	 *            要去取的数据的游标
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// 找到控件
		ImageView iv_goods_icon = (ImageView) view.findViewById(R.id.ib_goods_icon);
		TextView tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
		TextView tv_goods_quantity = (TextView) view.findViewById(R.id.tv_goods_quantity);
		TextView tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);
		TextView tv_goods_supplier = (TextView) view.findViewById(R.id.tv_goods_supplier);
		// 得到需要的列的索引
		int iconIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_ICON);
		int nameIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_NAME);
		int quantityIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_QUANTITY);
		int priceIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_PRICE);
		int supplierIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_SUPPLIER);
		// 根据索引拿到相应的数据,并设置给相应的控件
		iv_goods_icon.setImageBitmap(DbBitmapUtil.getImage(cursor.getBlob(iconIndex)));
		tv_goods_name.setText(cursor.getString(nameIndex));
		tv_goods_quantity.setText(cursor.getString(quantityIndex));
		tv_goods_price.setText(cursor.getString(priceIndex));
		tv_goods_supplier.setText(cursor.getString(supplierIndex));
	}
}