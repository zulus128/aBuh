package com.vkassin.abuh;

import java.util.ArrayList;

import com.vkassin.abuh.Common.item_type;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;


public class FavrActivity extends ListActivity {

	private static final String TAG = "aBuh.FavrActivity"; 
	private FavrArrayAdapter adapter;
	private static final int CONTEXTMENU_DELETEITEM = 1;
	private int selectedRowId;
	
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.fas);
		 
		ArrayList<RSSItem> items = Common.getFavrs();
		
		adapter = new FavrArrayAdapter(this, R.layout.newsitem, items);
    	setListAdapter(adapter);
		
		ListView listView = getListView();
		listView.setBackgroundColor(Color.WHITE);
		registerForContextMenu(listView);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		//Log.i(TAG,"click");
		RSSItem it = adapter.getItems().get(position);
		Intent i = new Intent(FavrActivity.this, (it.type == item_type.IT_QA)? QADetail.class : NewsDetail.class);
		i.putExtra("rssitem", it);
		startActivity(i);

	}
	
	public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
		   // record position/id/whatever here
		   return false;
		  }
	
	protected void onResume() {
		
		super.onResume();
		
		adapter.setItems(Common.getFavrs());
		adapter.notifyDataSetChanged();
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	
		super.onCreateContextMenu(menu, v, menuInfo);  
	    
	    AdapterView.AdapterContextMenuInfo info =
            (AdapterView.AdapterContextMenuInfo) menuInfo;
//	    selectedWord = ((TextView) info.targetView).getText().toString();
	    selectedRowId = (int)info.id;
    
		menu.setHeaderTitle("Меню");  
	    menu.add(0, CONTEXTMENU_DELETEITEM, 0, "Удалить");  
	}  
	
	   @Override  
	   public boolean onContextItemSelected(MenuItem item) {  
		   
		    // Delete row
		    //long rowId = getListView().getSelectedItemPosition();
		    if (selectedRowId >= 0) {
			Log.i(TAG, "Deleting row: " + selectedRowId);
			
			Common.delFavr(selectedRowId);
			
			adapter.setItems(Common.getFavrs());
			adapter.notifyDataSetChanged();
		    }
/*			AlertDialog ad = new AlertDialog.Builder(this)
			    .setIcon(android.R.drawable.ic_dialog_alert)
			    .setTitle(R.string.confirm_delete)
			    .setPositiveButton(R.string.yes, new
	DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
					// User clicked Yes so delete the contexts.
					deleteSelectedRow();
				    }
				})
			    .setNegativeButton(R.string.no, new
	DialogInterface.OnClickListener() {
	 			    public void onClick(DialogInterface dialog, int whichButton) {
	 				// User clicked No so don't delete (do nothing).
	 			    }
	 			})
			    .show();
		    }*/
	   return true;  
	   }  
}

