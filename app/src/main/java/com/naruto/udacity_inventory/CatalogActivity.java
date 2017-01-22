package com.naruto.udacity_inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class CatalogActivity extends AppCompatActivity {

	private ListView lv_in_stock;
	private FloatingActionButton fab_add_goods;
	private Context mContext;

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
		setTitle(R.string.catalog_activity_title);
		lv_in_stock = (ListView) findViewById(R.id.lv_in_stock);
		fab_add_goods = (FloatingActionButton) findViewById(R.id.fab_add_goods);
	}

	public void AddGoods(View v) {
		Intent intent = new Intent(mContext, EditorActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu options from the res/menu/menu_catalog.xml file.
		// This adds menu items to the app bar.
		getMenuInflater().inflate(R.menu.menu_catalog, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// User clicked on a menu option in the app bar overflow menu
		switch (item.getItemId()) {
		// Respond to a click on the "Insert dummy data" menu option
		case R.id.action_insert_dummy_data:
			// insertGoods();

			return true;
		// Respond to a click on the "Delete all entries" menu option
		case R.id.action_delete_all_entries:
			// deleteAllGoods();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}