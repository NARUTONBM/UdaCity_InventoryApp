package com.naruto.udacity_inventory;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.naruto.udacity_inventory.adapter.GoodsCursorAdapter;
import com.naruto.udacity_inventory.data.GoodsContract.GoodsEntry;

public class CatalogActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LoaderCallbacks<Cursor> {

	private static final int GOODS_LOADR = 0;
	private ListView lv_in_stock;
	private FloatingActionButton fab_add_goods;
	private Context mContext;
	private TextView tv_empty_view;
	private GoodsCursorAdapter mCursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalog);

		mContext = this;

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		// 给当前activity设置标题
		setTitle(R.string.catalog_activity_title);
		// 找到控件
		lv_in_stock = (ListView) findViewById(R.id.lv_in_stock);
		tv_empty_view = (TextView) findViewById(R.id.tv_empty_view);
		// 给listview设置空view
		lv_in_stock.setEmptyView(tv_empty_view);
		fab_add_goods = (FloatingActionButton) findViewById(R.id.fab_add_goods);
		// 给listview创建并设置适配器
		mCursorAdapter = new GoodsCursorAdapter(mContext, null);
		lv_in_stock.setAdapter(mCursorAdapter);
		// 给listview设置条目点击事件
		lv_in_stock.setOnItemClickListener(this);
		// 启动加载程序
		getLoaderManager().initLoader(GOODS_LOADR, null, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 创建意图
		Intent intent = new Intent(mContext, EditorActivity.class);
		// 配置uri
		Uri uri = ContentUris.withAppendedId(GoodsEntry.CONTENT_URI, id);
		// 将uri通过意图传递到editoractivity
		intent.setData(uri);
		// 启动editoractivity
		startActivity(intent);
	}

	/**
	 * 点击右下角按钮的点击操作
	 * 
	 * @param v
	 *            被点击的控件
	 */
	public void AddGoods(View v) {
		Intent intent = new Intent(mContext, EditorActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 在appbar上插入菜单按钮
		getMenuInflater().inflate(R.menu.menu_catalog, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 用户点击了某个菜单按钮
		switch (item.getItemId()) {
		// 点击了"Delete all entries"按钮的反馈
		case R.id.action_delete_all_entries:
			deleteAllGoods();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 删除数据库中所有信息
	 */
	private void deleteAllGoods() {
		int rowDeleted = getContentResolver().delete(GoodsEntry.CONTENT_URI, null, null);
		// rowDeleted！=0说明删除成功，弹出吐司，提醒用户
		if (rowDeleted != 0) {
			Toast.makeText(mContext, "All inventory data are wiped", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {GoodsEntry._ID, GoodsEntry.COLUMN_GOODS_ICON, GoodsEntry.COLUMN_GOODS_NAME, GoodsEntry.COLUMN_GOODS_QUANTITY,
						GoodsEntry.COLUMN_GOODS_PRICE, GoodsEntry.COLUMN_GOODS_SUPPLIER};

		return new CursorLoader(mContext, GoodsEntry.CONTENT_URI, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.swapCursor(null);
	}
}