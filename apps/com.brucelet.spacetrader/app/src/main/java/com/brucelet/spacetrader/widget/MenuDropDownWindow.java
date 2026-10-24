package com.brucelet.spacetrader.widget;

import android.content.Context;
import android.graphics.Typeface;
//import android.support.v7.widget.ListPopupWindow;
//import android.widget.ListPopupWindow;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.brucelet.spacetrader.R;

import java.util.ArrayList;
import java.util.List;

public class MenuDropDownWindow extends ListPopupWindow implements View.OnKeyListener{
	private OnDropDownItemClickListener mListener;
	private Menu mMenu;

	public MenuDropDownWindow(Context context, View anchor, Menu menu) {
		super(context, null, R.attr.listPopupWindowStyle);

		mMenu = menu;

		ListAdapter adapter = new MenuListAdapter(context, mMenu, new MenuListAdapter.OnWidthChangedListener() {
			@Override
			public void onWidthChanged(int width) {
				setContentWidth(width);
			}
		});
		setAdapter(adapter);
		setAnchorView(anchor);
		setModal(true);
		setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mListener != null) {
					MenuListAdapter adapter = (MenuListAdapter) parent.getAdapter();
					mListener.onDropDownItemClick(adapter.getItem(position));
					MenuDropDownWindow.this.dismiss();
				}
			}
		});
		anchor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MenuDropDownWindow.this.show();
			}
		});

		// XXX There are some bugs in createDragToOpenListener() including occasional misdrawing of selector, and omiting text color change in Palm theme
		// See AOSP issues 76371, 82905, and 176016 at https://code.google.com/p/android/
		final View.OnTouchListener dragListener = createDragToOpenListener(anchor);
		View.OnTouchListener wrapper = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean out = dragListener.onTouch(v, event);
				if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
					dismiss();
				}
				return out;
			}
		};
		anchor.setOnTouchListener(wrapper);
	}

	@Override
	public void show() {
		super.show();
		getListView().setOnKeyListener(this);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP && mListener != null) {
			int shortcut = event.getUnicodeChar();
			if (shortcut > 0) {
				for (int i = 0, s = mMenu.size(); i < s; i++) {
					MenuItem item = mMenu.getItem(i);
					if (shortcut == item.getAlphabeticShortcut()) {
						if (mListener.onDropDownItemClick(item)) {
							dismiss();
							return true;
						}
					}
				}
			}
		}
		if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_MENU) {
			dismiss();
			return true;
		}
		return false;
	}

	public void setOnDropDownItemClickListener(OnDropDownItemClickListener listener) {
		mListener = listener;
	}

	private static class MenuListAdapter extends BaseAdapter {

		private static class ViewHolder {
			TextView text;
			TextView shortcut;

			ViewHolder(View view) {
				text = (TextView) view.findViewById(R.id.spinner_text);
				shortcut = (TextView) view.findViewById(R.id.spinner_shortcut);
				shortcut.setTypeface(Typeface.MONOSPACE);
			}
		}

		private static final int DUMMY_MEASURE_SPEC = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

		private List<MenuItem> mMenuItems = new ArrayList<>();
		private int mMaxWidth;
		private OnWidthChangedListener mListener;

		public MenuListAdapter(Context context, Menu menu, OnWidthChangedListener listener) {
			int j = 0;
			int currentGroupId = 0;
			for (int i = 0, n = menu.size(); i < n; i++,j++) {
				MenuItem menuItem = menu.getItem(i);
				int groupId = menuItem.getGroupId();
				if (groupId != currentGroupId) {
					if (currentGroupId != 0) {
						mMenuItems.add(null);
					}
					currentGroupId = groupId;
				}
				mMenuItems.add(menuItem);
			}

			mListener = listener;
			mMaxWidth = context.getResources().getDimensionPixelSize(R.dimen.menu_dropdown_min_width);
			mListener.onWidthChanged(mMaxWidth);
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return getItemViewType(position) == 0;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return mMenuItems.get(position) == null? 1 : 0;
		}

		@Override
		public int getCount() {
			return mMenuItems.size();
		}

		@Override
		public MenuItem getItem(int position) {
			return mMenuItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			MenuItem item = getItem(position);
			return item != null? item.getItemId() : 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (getItemViewType(position) != 0) {
				if (convertView == null) {
					return LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_dropdown_divider, parent, false);
				} else {
					return convertView;
				}
			}

			View listItem;
			if (convertView == null) {
				listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_dropdown_item, parent, false);
				listItem.setTag(R.id.viewholder, new ViewHolder(listItem));
			} else {
				listItem = convertView;
			}

			// Check view width and update if necessary
			listItem.measure(DUMMY_MEASURE_SPEC, DUMMY_MEASURE_SPEC);
			int width = listItem.getMeasuredWidth();
			if (width > mMaxWidth) {
				mMaxWidth = width;
				mListener.onWidthChanged(mMaxWidth);
			}

			ViewHolder holder = (ViewHolder) listItem.getTag(R.id.viewholder);
			MenuItem menuItem = mMenuItems.get(position);

			holder.text.setText(menuItem.getTitle());

			char shortcut = menuItem.getAlphabeticShortcut();
			if (shortcut > 0) {
				holder.shortcut.setText(String.valueOf(shortcut).toUpperCase());
				holder.shortcut.setVisibility(View.VISIBLE);
			} else {
				holder.shortcut.setText(R.string.shortcut_none);
				holder.shortcut.setVisibility(View.GONE);
			}

			return listItem;
		}

		private interface OnWidthChangedListener {
			void onWidthChanged(int width);
		}
	}

	public interface OnDropDownItemClickListener {
		boolean onDropDownItemClick(MenuItem item);
	}

}
