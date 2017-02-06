package com.naruto.udacity_inventory;
/*
 * Created with Android Studio.
 * User: narutonbm@gmail.com
 * Date: 2017-01-22
 * Time: 13:17
 * Desc: UdaCity_InventoryApp
 */

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.naruto.udacity_inventory.data.GoodsContract.GoodsEntry;
import com.naruto.udacity_inventory.utils.DbBitmapUtil;

import java.io.FileNotFoundException;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int EXISTING_GOODS_LOADER = 1;
	private static final int SELECT_PIC = 10;
	private static boolean NONE_SELECT = true;
	private EditText et_goods_name, et_goods_price, et_goods_quantity, et_goods_supplier;
	private Spinner sn_goods_category;
	private ImageButton ib_goods_icon;
	private int mCategory = GoodsEntry.CATEGORY_OTHER;
	private boolean mGoodsHasChanged = false;
	private Uri mCurrentGoodsUri;
	private static final String EDITOR_TAG = EditorActivity.class.getSimpleName();

	private View.OnTouchListener mListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			mGoodsHasChanged = true;
			return false;
		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		initData();
		initUI();
	}

	private void initData() {
		mCurrentGoodsUri = getIntent().getData();
	}

	private void initUI() {
		// 设置页面标题
		if (mCurrentGoodsUri == null) {
			setTitle(R.string.editor_activity_title_new_goods);
			invalidateOptionsMenu();
		} else {
			setTitle(R.string.editor_activity_title_edit_goods);
			getLoaderManager().initLoader(EXISTING_GOODS_LOADER, null, this);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// 找到控件,并设置相应点击事件
		et_goods_name = (EditText) findViewById(R.id.et_goods_name);
		et_goods_name.setOnTouchListener(mListener);
		sn_goods_category = (Spinner) findViewById(R.id.sn_goods_category);
		sn_goods_category.setOnTouchListener(mListener);
		et_goods_price = (EditText) findViewById(R.id.et_goods_price);
		et_goods_price.setOnTouchListener(mListener);
		et_goods_quantity = (EditText) findViewById(R.id.et_goods_quantity);
		et_goods_quantity.setOnTouchListener(mListener);
		et_goods_supplier = (EditText) findViewById(R.id.et_goods_supplier);
		et_goods_supplier.setOnTouchListener(mListener);
		ib_goods_icon = (ImageButton) findViewById(R.id.ib_goods_icon);
		ib_goods_icon.setOnTouchListener(mListener);
		// 设置下拉选择
		setupSpinner();
	}

	public void AddPhoto(View view) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select a picture"), SELECT_PIC);
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
		sn_goods_category.setAdapter(genderSpinnerAdapter);

		// 设置选择的条目与对应的内容
		sn_goods_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				mCategory = GoodsEntry.CATEGORY_OTHER;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 在appbar添加按钮
		getMenuInflater().inflate(R.menu.menu_editor, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		// 如果是一个新的货物，隐藏删除按钮
		if (mCurrentGoodsUri == null) {
			MenuItem menuItem = menu.findItem(R.id.action_delete);
			menuItem.setVisible(false);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 用户点击了一个appbar上的按钮
		switch (item.getItemId()) {
		// 点击了保存按钮的反馈
		case R.id.action_save:
			// 保存货物信息到数据库
			saveGoods();

			return true;
		// 点击了删除按钮的反馈
		case R.id.action_delete:
			// 弹出对话框提醒用户删除
			showDeleteConfirmationDialog();
			return true;
		// 点击appbar上的返回按钮的操作
		case android.R.id.home:
			// 如果货物信息没有被更改，继续处理返回父activity的操作
			if (!mGoodsHasChanged) {
				NavUtils.navigateUpFromSameTask(EditorActivity.this);
				return true;
			}

			// 提示用户，一旦退出，当前的更改不会被保存
			DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					// User clicked "Discard" button, navigate to parent
					// activity.
					NavUtils.navigateUpFromSameTask(EditorActivity.this);
				}
			};

			// 弹出对话框，还有未保存的更改
			showUnsavedChangesDialog(discardButtonClickListener);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void saveGoods() {
		// 获取输入的字符
		String name = et_goods_name.getText().toString().trim();
		String category = sn_goods_category.getSelectedItem().toString().trim();
		String price = et_goods_price.getText().toString().trim();
		String quantity = et_goods_quantity.getText().toString().trim();
		String supplier = et_goods_supplier.getText().toString().trim();
		Bitmap icon = (((BitmapDrawable) ib_goods_icon.getDrawable()).getBitmap());
		// 对输入的内容进行非空校验
		if (name.isEmpty() || category.isEmpty() || price.isEmpty() || quantity.isEmpty() || supplier.isEmpty()) {
			Toast.makeText(this, "请检查是否有未填写的内容", Toast.LENGTH_SHORT).show();
			return;
		}
		// 创建一个ContentValues对象
		ContentValues values = new ContentValues();
		values.put(GoodsEntry.COLUMN_GOODS_NAME, name);
		values.put(GoodsEntry.COLUMN_GOODS_CATEGORY, category);
		values.put(GoodsEntry.COLUMN_GOODS_PRICE, price);
		values.put(GoodsEntry.COLUMN_GOODS_QUANTITY, quantity);
		values.put(GoodsEntry.COLUMN_GOODS_SUPPLIER, supplier);
		if (NONE_SELECT) {
			values.put(GoodsEntry.COLUMN_GOODS_ICON,
							DbBitmapUtil.getBytes(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_broken_image)).getBitmap()));
		} else {
			values.put(GoodsEntry.COLUMN_GOODS_ICON, DbBitmapUtil.getBytes(icon));
		}

		if (mCurrentGoodsUri == null) {
			Uri newUri = getContentResolver().insert(GoodsEntry.CONTENT_URI, values);
			if (newUri == null) {
				Toast.makeText(this, R.string.editor_insert_fail, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.editor_insert_success, Toast.LENGTH_SHORT).show();
				// 退出当前activity
				finish();
			}
		} else {
			int rowUpdated = getContentResolver().update(mCurrentGoodsUri, values, null, null);
			if (rowUpdated == 0) {
				Toast.makeText(this, R.string.editor_update_fail, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.editor_update_success, Toast.LENGTH_SHORT).show();
				// 退出当前activity
				finish();
			}
		}
	}

	/**
	 * 当返回键被按钮时调用
	 */
	@Override
	public void onBackPressed() {
		// 如果货物信息没有被更改，继续处理返回键的操作
		if (!mGoodsHasChanged) {
			super.onBackPressed();
			return;
		}

		// 提示用户，一旦退出，当前的更改不会被保存
		DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				// 点击了放弃按钮，关闭当前activity
				finish();
			}
		};

		// 弹出对话框，还有未保存的更改
		showUnsavedChangesDialog(discardButtonClickListener);
	}

	/**
	 * 弹出对话框，退出时提醒用户还有未保存的更改
	 *
	 * @param discardButtonClickListener
	 *            用户需要取消更改时的监听器
	 */
	private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
		// 创建一个AlertDialog对象，并设置标题信息，以及按钮的点击事件
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.unsaved_changes_dialog_msg);
		builder.setPositiveButton(R.string.discard, discardButtonClickListener);
		builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// 点击了继续编辑按钮，关闭对话框，继续编辑
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});

		// 创建并展示对话框
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	/**
	 * 弹出对话框，提醒用户是否确认要删除当前货物信息
	 */
	private void showDeleteConfirmationDialog() {
		// 创建一个AlertDialog对象，并设置标题信息，以及按钮的点击事件
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_dialog_msg);
		builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// 点击了删除按钮，删除当前货物信息
				deleteGoods();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// 点击了取消按钮，关闭对话框，继续编辑
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});

		// 创建并展示对话框
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	private void deleteGoods() {
		if (mCurrentGoodsUri != null) {
			// uri不为空，根据uri删除内容
			int rowDeleted = getContentResolver().delete(mCurrentGoodsUri, null, null);
			// 判断返回的被删除行号是否为0，为0--删除失败，不为0--删除成功
			if (rowDeleted == 0) {
				Toast.makeText(this, "Delete goods failed", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Delete goods successfully", Toast.LENGTH_SHORT).show();
			}
		}

		// 关闭当前activity
		finish();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {GoodsEntry._ID, GoodsEntry.COLUMN_GOODS_NAME, GoodsEntry.COLUMN_GOODS_CATEGORY, GoodsEntry.COLUMN_GOODS_PRICE,
						GoodsEntry.COLUMN_GOODS_QUANTITY, GoodsEntry.COLUMN_GOODS_SUPPLIER, GoodsEntry.COLUMN_GOODS_ICON};

		return new CursorLoader(this, mCurrentGoodsUri, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// cursor为空或者curso少于一行，直接退出当前方法
		/*
		 * if (cursor == null || cursor.getCount() < 1) { return; }
		 */

		if (cursor.moveToNext()) {
			// 找到列的索引
			int nameColumnIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_NAME);
			int categoryColumnIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_CATEGORY);
			int priceColumnIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_PRICE);
			int quantityColumnIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_QUANTITY);
			int supplierColumnIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_SUPPLIER);
			int iconColumnIndex = cursor.getColumnIndex(GoodsEntry.COLUMN_GOODS_ICON);

			// 取出列中的值
			String name = cursor.getString(nameColumnIndex);
			int category = cursor.getInt(categoryColumnIndex);
			double price = cursor.getDouble(priceColumnIndex);
			int quantity = cursor.getInt(quantityColumnIndex);
			String supplier = cursor.getString(supplierColumnIndex);
			byte[] icon = cursor.getBlob(iconColumnIndex);

			// 将去除的内容展示到控件上
			et_goods_name.setText(name);
			et_goods_price.setText(String.format("$ %s", price));
			et_goods_quantity.setText(" " + quantity);
			et_goods_supplier.setText(supplier);
			ib_goods_icon.setImageBitmap(DbBitmapUtil.getImage(icon));

			// 设置下拉选择框的显示内容
			switch (category) {
			case GoodsEntry.CATEGORY_ARTS_CRAFTS_SEWING:
				sn_goods_category.setSelection(0);

				break;

			case GoodsEntry.CATEGORY_AUTOMOTIVE_PARTS_ACCESSORIES:
				sn_goods_category.setSelection(1);

				break;

			case GoodsEntry.CATEGORY_BABY:
				sn_goods_category.setSelection(2);

				break;

			case GoodsEntry.CATEGORY_BEAUTY_PERSONAL_CARE:
				sn_goods_category.setSelection(3);

				break;

			case GoodsEntry.CATEGORY_BOOKS:
				sn_goods_category.setSelection(4);

				break;

			case GoodsEntry.CATEGORY_CDS_VINYL:
				sn_goods_category.setSelection(5);

				break;

			case GoodsEntry.CATEGORY_CELL_PHONE_ACCESSORIES:
				sn_goods_category.setSelection(6);

				break;

			case GoodsEntry.CATEGORY_CLOTHING_SHOES_JEWELRY:
				sn_goods_category.setSelection(7);

				break;

			case GoodsEntry.CATEGORY_COLLECTIBLES_FINE_ART:
				sn_goods_category.setSelection(8);

				break;

			case GoodsEntry.CATEGORY_COMPUTERS:
				sn_goods_category.setSelection(9);

				break;

			case GoodsEntry.CATEGORY_COURSES:
				sn_goods_category.setSelection(10);

				break;

			case GoodsEntry.CATEGORY_CREDIT_PAYMENT_CARDS:
				sn_goods_category.setSelection(11);

				break;

			case GoodsEntry.CATEGORY_DIGITAL_MUSIC:
				sn_goods_category.setSelection(12);

				break;

			case GoodsEntry.CATEGORY_ELECTRONICS:
				sn_goods_category.setSelection(13);

				break;

			case GoodsEntry.CATEGORY_GIFT_CARDS:
				sn_goods_category.setSelection(14);

				break;

			default:
				sn_goods_category.setSelection(15);

				break;
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		et_goods_name.setText("");
		sn_goods_category.setSelection(15);
		et_goods_price.setText("");
		et_goods_quantity.setText("");
		et_goods_supplier.setText("");
		ib_goods_icon.setImageResource(R.mipmap.ic_add_a_photo);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_PIC) {
			Uri uri;
			if (data != null) {
				NONE_SELECT = false;
				uri = data.getData();
				try {
					ib_goods_icon.setImageBitmap(decodeUri(uri));
				} catch (FileNotFoundException e) {
					Log.e(EDITOR_TAG, "FileNotFound" + e);
				}
			} else {
				NONE_SELECT = true;
			}
		}
	}

	private Bitmap decodeUri(Uri uri) throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
		final int REQUIRED_SIZE = 144;
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		int scale = 1;
		while (true) {
			if (outWidth / 2 < REQUIRED_SIZE || outHeight / 2 < REQUIRED_SIZE) {
				break;
			}
			outWidth /= 2;
			outHeight /= 2;
			scale *= 2;
		}
		BitmapFactory.Options options1 = new BitmapFactory.Options();
		options1.inSampleSize = scale;

		return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options1);
	}
}