package com.vkassin;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class QAActivity extends ListActivity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// Create an array of Strings, that will be put to our ListActivity
		String[] names = new String[] { "Linux", "Windows7", "Eclipse", "Suse",
				"Ubuntu", "Solaris", "Android", "iPhone" };
		// Use your own layout and point the adapter to the UI elements which
		// contains the label
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.qalayout,
				R.id.label, names));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG)
				.show();

	}

}
