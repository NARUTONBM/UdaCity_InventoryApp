package com.naruto.udacity_inventory.adapter;

/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-22
 * Time: 22:18
 * Desc: UdaCity_InventoryApp
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.naruto.udacity_inventory.EditorActivity;
import com.naruto.udacity_inventory.R;
import com.naruto.udacity_inventory.data.GoodsContract.GoodsEntry;
import com.naruto.udacity_inventory.utils.DbBitmapUtil;

import static com.naruto.udacity_inventory.R.id.bt_cancel_order;
import static com.naruto.udacity_inventory.R.id.bt_cancel_update;
import static com.naruto.udacity_inventory.R.id.bt_ensure_order;
import static com.naruto.udacity_inventory.R.id.bt_ensure_update;

public class GoodsCursorAdapter extends CursorAdapter implements View.OnClickListener {

	private PopupWindow mPopupWindow;
	private Context mContext;
	private EditText et_input;
	private int mNum;
	private AlertDialog mDialog;
	private int mQuantity;
	private TextView mTv_goods_quantity;
	private int mRowId;
	private String mSupplier;

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
		mContext = context;
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
	public void bindView(View view, final Context context, Cursor cursor) {
		// 找到控件
		ImageView iv_goods_icon = (ImageView) view.findViewById(R.id.ib_goods_icon);
		TextView tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
		mTv_goods_quantity = (TextView) view.findViewById(R.id.tv_goods_quantity);
		TextView tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);
		TextView tv_goods_supplier = (TextView) view.findViewById(R.id.tv_goods_supplier);
		// 得到需要的列的索引
		int idIndex = cursor.getColumnIndex(GoodsEntry._ID);
		mRowId = cursor.getInt(idIndex);
		int iconIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_ICON);
		int nameIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_NAME);
		int quantityIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_QUANTITY);
		int priceIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_PRICE);
		int supplierIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_SUPPLIER);
		// 根据索引拿到相应的数据,并设置给相应的控件
		// 设置图标
		Bitmap image = DbBitmapUtil.getImage(cursor.getBlob(iconIndex));
		iv_goods_icon.setImageBitmap(image);
		// 设置name
		tv_goods_name.setText(cursor.getString(nameIndex));
		// 根据数量，分别显示in stock;out stock
		mQuantity = cursor.getInt(quantityIndex);
		if (mQuantity > 0) {
			mTv_goods_quantity.setText("in stock: " + mQuantity);
		} else {
			mTv_goods_quantity.setText("out stock");
		}
		// 设置价格
		tv_goods_price.setText("$" + cursor.getDouble(priceIndex));
		// 设置供应商
		mSupplier = cursor.getString(supplierIndex);
		tv_goods_supplier.setText("Supplied by " + mSupplier);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 创建意图
				Intent intent = new Intent(mContext, EditorActivity.class);
				// 配置uri
				Uri uri = ContentUris.withAppendedId(GoodsEntry.CONTENT_URI, mRowId);
				// 将uri通过意图传递到editoractivity
				intent.setData(uri);
				// 启动editoractivity
				mContext.startActivity(intent);
			}
		});
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {

				View popupView = View.inflate(mContext, R.layout.item_list_goods_expend, null);

				TextView tv_goods_sale = (TextView) popupView.findViewById(R.id.tv_goods_sale);
				TextView tv_goods_order = (TextView) popupView.findViewById(R.id.tv_goods_order);

				tv_goods_sale.setOnClickListener(GoodsCursorAdapter.this);
				tv_goods_order.setOnClickListener(GoodsCursorAdapter.this);

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
				mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
				// 2.设置一个透明背景
				mPopupWindow.setBackgroundDrawable(new ColorDrawable());
				// 3.指定窗体位置
				mPopupWindow.showAsDropDown(view, 180, -(view.getHeight() + 20));

				// 启动动画
				popupView.startAnimation(animationSet);

				return true;
			}
		});
	}

	private void updateQuantity() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		mDialog = builder.create();
		View view = View.inflate(mContext, R.layout.dialog_update_quantity, null);
		// 设置自定义的对话框并显示
		mDialog.setView(view, 0, 0, 0, 0);
		mDialog.show();

		// 获取控件
		TextView tv_goods_sale_title = (TextView) view.findViewById(R.id.tv_goods_sale_title);
		Button bt_add = (Button) view.findViewById(R.id.bt_add);
		et_input = (EditText) view.findViewById(R.id.et_input);
		et_input.setText("1");
		Button bt_remove = (Button) view.findViewById(R.id.bt_remove);
		Button bt_cancel_update = (Button) view.findViewById(R.id.bt_cancel_update);
		Button bt_ensure_update = (Button) view.findViewById(R.id.bt_ensure_update);
		// 设置对话框标题
		tv_goods_sale_title.setText("Update quantity");
		// 设置按钮的点击事件
		bt_add.setOnClickListener(GoodsCursorAdapter.this);
		bt_remove.setOnClickListener(GoodsCursorAdapter.this);
		bt_cancel_update.setOnClickListener(GoodsCursorAdapter.this);
		bt_ensure_update.setOnClickListener(GoodsCursorAdapter.this);
	}

	private void orderMore() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		mDialog = builder.create();
		View view = View.inflate(mContext, R.layout.dialog_order_more, null);
		// 设置自定义的对话框并显示
		mDialog.setView(view, 0, 0, 0, 0);
		mDialog.show();

		// 获取控件
		TextView tv_goods_order_title = (TextView) view.findViewById(R.id.tv_goods_order_title);
		Button bt_add = (Button) view.findViewById(R.id.bt_add);
		et_input = (EditText) view.findViewById(R.id.et_input);
		et_input.setText("1");
		Button bt_remove = (Button) view.findViewById(R.id.bt_remove);
		TextView tv_goods_order_supplier = (TextView) view.findViewById(R.id.tv_goods_order_supplier);
		Button bt_cancel_order = (Button) view.findViewById(R.id.bt_cancel_order);
		Button bt_ensure_order = (Button) view.findViewById(R.id.bt_ensure_order);
		// 设置对话框标题
		tv_goods_order_title.setText("Order more");
		tv_goods_order_supplier.setText("Order from " + mSupplier);
		// 设置按钮的点击事件
		bt_add.setOnClickListener(GoodsCursorAdapter.this);
		bt_remove.setOnClickListener(GoodsCursorAdapter.this);
		bt_cancel_order.setOnClickListener(GoodsCursorAdapter.this);
		bt_ensure_order.setOnClickListener(GoodsCursorAdapter.this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_goods_sale:
			updateQuantity();

			break;

		case R.id.tv_goods_order:
			orderMore();

			break;

		case R.id.bt_add:
			mNum = Integer.parseInt(et_input.getText().toString());
			mNum += 1;
			et_input.setText("" + mNum);

			break;

		case R.id.bt_remove:
			mNum = Integer.parseInt(et_input.getText().toString());
			if (mNum > 0) {
				mNum -= 1;
				et_input.setText("" + mNum);
			} else {
				Toast.makeText(mContext, "Quantity must bigger than 0", Toast.LENGTH_SHORT).show();
			}

			break;

		case bt_cancel_update:
			mDialog.dismiss();

			break;

		case bt_ensure_update:
			updateQuantityDb(false);

			break;

		case bt_cancel_order:
			mDialog.dismiss();

			break;

		case bt_ensure_order:
			updateQuantityDb(true);

			break;

		default:

			break;
		}

		// 点了窗体之后关闭窗体
		if (mPopupWindow != null) {

			mPopupWindow.dismiss();
		}
	}

	private void updateQuantityDb(boolean isAdd) {
		mNum = Integer.parseInt(et_input.getText().toString());
		int newquantity;
		if (isAdd) {
			newquantity = mQuantity + mNum;
		} else {
			newquantity = mQuantity - mNum;
		}
		mTv_goods_quantity.setText("" + newquantity);
		ContentValues values = new ContentValues();
		values.put(GoodsEntry.COLUMN_GOODS_QUANTITY, newquantity);
		Uri currentUri = ContentUris.withAppendedId(GoodsEntry.CONTENT_URI, mRowId);
		int rowUpdated = mContext.getContentResolver().update(currentUri, values, null, null);
		if (rowUpdated == 0) {
			if (isAdd) {
				Toast.makeText(mContext, "Order more failed", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Update quantity failed", Toast.LENGTH_SHORT).show();
			}
		} else {
			if (isAdd) {
				Toast.makeText(mContext, "Order more successfully", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Update quantity successfully", Toast.LENGTH_SHORT).show();
			}
		}

		mDialog.dismiss();
	}
}