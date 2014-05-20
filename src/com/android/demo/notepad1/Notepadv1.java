/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.demo.notepad1;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

public class Notepadv1 extends ListActivity implements OnItemClickListener {
    private int mNoteNumber = 1;
    private NotesDbAdapter mDbHelper;
    public static final int INSERT_ID = Menu.FIRST;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_list);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();
        
        getListView().setOnItemClickListener(this);        
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_ID:
            createNote();
            return true;
        }
       
        return super.onOptionsItemSelected(item);
    }

    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_BODY };
        int[] to = new int[] { R.id.textView1, R.id.textView2 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to);
        setListAdapter(notes);
    }
    
    private void createNote() {
		
		LayoutInflater factory = LayoutInflater.from(this);
		final View dialogView = factory.inflate(R.layout.notes_dialog, null);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("New Notes");
		alert.setMessage("Input note details :");

		alert.setView(dialogView);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				EditText et1 = (EditText) dialogView.findViewById(R.id.editText1);
				String txtTitle = et1.getText().toString();		
				
				EditText et2 = (EditText) dialogView.findViewById(R.id.editText2);
				String txtBody = et2.getText().toString();
				
				
				mDbHelper.createNote(txtTitle, txtBody);
				fillData();
				return;
			}
		});


		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				return;
			}
		});


		alert.show();
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mDbHelper.deleteNote(id);
		fillData();
	}
	
	
}
