package com.naruto.udacity_inventory;
/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-22
 * Time: 13:17
 * Desc: UdaCity_InventoryApp
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.naruto.udacity_inventory.data.GoodsContract.GoodsEntry;

public class EditorActivity extends AppCompatActivity {

	private EditText et_goods_name, et_sale, et_stock, et_supply;
	private Spinner sn_category;
	private ImageButton ib_icon;
	private int mCategory = GoodsEntry.CATEGORY_OTHER;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		initUI();
	}

	private void initUI() {
		// 设置页面标题
		setTitle(R.string.editor_activity_title_new_goods);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// 找到控件
		et_goods_name = (EditText) findViewById(R.id.et_goods_name);
		sn_category = (Spinner) findViewById(R.id.sn_category);
		et_sale = (EditText) findViewById(R.id.et_sale);
		et_stock = (EditText) findViewById(R.id.et_stock);
		et_supply = (EditText) findViewById(R.id.et_supply);
		ib_icon = (ImageButton) findViewById(R.id.ib_icon);
		// 设置下拉选择
		setupSpinner();
	}

	/**
	 * 设置下拉选择的内容与之相应的点击选择操作
	 */
	private void setupSpinner() {
		// 未下来选择框创建适配器
		ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R.layout.simple_spinner_item);

		// 指定下来选择框的样式
		genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		// 配置适配器
		sn_category.setAdapter(genderSpinnerAdapter);

		// 设置选择的条目与对应的
		sn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selection = (String) parent.getItemAtPosition(position);
				if (!TextUtils.isEmpty(selection)) {
					if (selection.equals(getString(R.string.category_arts_crafts_sewing))) {
						mCategory = GoodsEntry.CATEGORY_ARTS_CRAFTS_SEWING;
					} else if (selection.equals(getString(R.string.category_automotive_parts_accessories))) {
						mCategory = GoodsEntry.CATEGORY_AUTOMOTIVE_PARTS_ACCESSORIES;
					} else if (selection.equals(getString(R.string.category_beauty_personal_care))) {
						mCategory = GoodsEntry.CATEGORY_BEAUTY_PERSONAL_CARE;
					} else if (selection.equals(getString(R.string.category_baby))) {
						mCategory = GoodsEntry.CATEGORY_BABY;
					} else if (selection.equals(getString(R.string.category_books))) {
						mCategory = GoodsEntry.CATEGORY_BOOKS;
					} else if (selection.equals(getString(R.string.category_cds_vinyl))) {
						mCategory = GoodsEntry.CATEGORY_CDS_VINYL;
					} else if (selection.equals(getString(R.string.category_cell_phone_accessories))) {
						mCategory = GoodsEntry.CATEGORY_CELL_PHONE_ACCESSORIES;
					} else if (selection.equals(getString(R.string.category_clothing_shoes_jewelry))) {
						mCategory = GoodsEntry.CATEGORY_CLOTHING_SHOES_JEWELRY;
					} else if (selection.equals(getString(R.string.category_collectibles_fine_art))) {
						mCategory = GoodsEntry.CATEGORY_COLLECTIBLES_FINE_ART;
					} else if (selection.equals(getString(R.string.category_computers))) {
						mCategory = GoodsEntry.CATEGORY_COMPUTERS;
					} else if (selection.equals(getString(R.string.category_courses))) {
						mCategory = GoodsEntry.CATEGORY_COURSES;
					} else if (selection.equals(getString(R.string.category_credit_payment_cards))) {
						mCategory = GoodsEntry.CATEGORY_CREDIT_PAYMENT_CARDS;
					} else if (selection.equals(getString(R.string.category_digital_music))) {
						mCategory = GoodsEntry.CATEGORY_DIGITAL_MUSIC;
					} else if (selection.equals(getString(R.string.category_electronics))) {
						mCategory = GoodsEntry.CATEGORY_ELECTRONICS;
					} else if (selection.equals(getString(R.string.category_gift_cards))) {
						mCategory = GoodsEntry.CATEGORY_GIFT_CARDS;
					} else {
						mCategory = GoodsEntry.CATEGORY_OTHER;
					}
				}
			}

			// Because AdapterView is an abstract class, onNothingSelected must
			// be defined
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				mCategory = GoodsEntry.CATEGORY_OTHER;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu options from the res/menu/menu_editor.xml file.
		// This adds menu items to the app bar.
		getMenuInflater().inflate(R.menu.menu_editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// User clicked on a menu option in the app bar overflow menu
		switch (item.getItemId()) {
		// Respond to a click on the "Save" menu option
		case R.id.action_save:
			// Save pet to database
			// saveGoods();
			// Exit activity
			finish();
			return true;
		// Respond to a click on the "Delete" menu option
		case R.id.action_delete:
			// Pop up confirmation dialog for deletion
			showDeleteConfirmationDialog();
			return true;
		// Respond to a click on the "Up" arrow button in the app bar
		case android.R.id.home:
			// If the pet hasn't changed, continue with navigating up to parent
			// activity
			// which is the {@link CatalogActivity}.
			/*
			 * if (!mGoodsHasChanged) {
			 * NavUtils.navigateUpFromSameTask(EditorActivity.this); return
			 * true; }
			 */

			// Otherwise if there are unsaved changes, setup a dialog to warn
			// the user.
			// Create a click listener to handle the user confirming that
			// changes should be discarded.
			DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					// User clicked "Discard" button, navigate to parent
					// activity.
					NavUtils.navigateUpFromSameTask(EditorActivity.this);
				}
			};

			// Show a dialog that notifies the user they have unsaved changes
			showUnsavedChangesDialog(discardButtonClickListener);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method is called when the back button is pressed.
	 */
	@Override
	public void onBackPressed() {
		// If the pet hasn't changed, continue with handling back button press
		/*
		 * if (!mGoodsHasChanged) { super.onBackPressed();
		 * 
		 * return; }
		 */

		// Otherwise if there are unsaved changes, setup a dialog to warn the
		// user.
		// Create a click listener to handle the user confirming that changes
		// should be discarded.
		DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				// User clicked "Discard" button, close the current activity.
				finish();
			}
		};

		// Show dialog that there are unsaved changes
		showUnsavedChangesDialog(discardButtonClickListener);
	}

	/**
	 * Show a dialog that warns the user there are unsaved changes that will be
	 * lost if they continue leaving the editor.
	 *
	 * @param discardButtonClickListener
	 *            is the click listener for what to do when the user confirms
	 *            they want to discard their changes
	 */
	private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
		// Create an AlertDialog.Builder and set the message, and click
		// listeners
		// for the postivie and negative buttons on the dialog.
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.unsaved_changes_dialog_msg);
		builder.setPositiveButton(R.string.discard, discardButtonClickListener);
		builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked the "Keep editing" button, so dismiss the dialog
				// and continue editing the pet.
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});

		// Create and show the AlertDialog
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	/**
	 * Prompt the user to confirm that they want to delete this pet.
	 */
	private void showDeleteConfirmationDialog() {
		// Create an AlertDialog.Builder and set the message, and click
		// listeners
		// for the postivie and negative buttons on the dialog.
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_dialog_msg);
		builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked the "Delete" button, so delete the pet.
				// deleteGoods();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked the "Cancel" button, so dismiss the dialog
				// and continue editing the pet.
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});

		// Create and show the AlertDialog
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
}