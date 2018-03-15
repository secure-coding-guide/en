package org.jssec.android.sqlite;

import org.jssec.android.sqlite.task.DataDeleteTask;
import org.jssec.android.sqlite.task.DataInsertTask;
import org.jssec.android.sqlite.task.DataSearchTask;
import org.jssec.android.sqlite.task.DataUpdateTask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity {
    private SampleDbOpenHelper       mSampleDbOpenHelper;   //Database open helper
    private Cursor                   mCursor = null;        //Cursor to use for the screen display
    
    //Search conditions being set
    private String  mItemIdNoSearch = null;
    private String  mItemNameSearch = null;
    private String  mItemInfoSearch = null;   
    
    //Selecting row's information
    private String  mItemIdNo = null;
    private String  mItemName = null;
    private String  mItemInfo = null;   
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Prepare the openHelper
        mSampleDbOpenHelper = SampleDbOpenHelper.newHelper(this);
        mSampleDbOpenHelper.openDatabaseWithHelper();
    }
    
    @Override 
    protected void onStart() {
        super.onStart();
        
        //Listener used when a list is selected
        ListView lv = (ListView)findViewById(R.id.DataList);
        lv.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setCurrentData(arg1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //clear data of selected row
                clearCurrentData();
            }
        });

        //Listener used when a item is clicked
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Set data of selected row
                setCurrentData(arg1);
                //Call for edit screen
                startEditActivity();
            }
        });

        //Listener used when a item is longclicked
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Set data of selected row
                setCurrentData(arg1);
                return false;
            }
        });

        //Prepare context menu
        lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu arg0, View arg1, ContextMenuInfo arg2) {
                //Set a title
                String strTitle = mItemIdNo + "/" + mItemName; 
                arg0.setHeaderTitle(strTitle);
                //Create a menu
                arg0.add(0, EDIT_MENUITEM_ID, 1, R.string.EDIT_MENUITEM);
                arg0.add(0, DELETE_MENUITEM_ID, 2, R.string.DELETE_MENUITEM);
            }
        });
        
        //Create initial data
        initData();

        //Show Database data
        showDbData();
    }

    //Set information of selected row
    private void setCurrentData(View currentLine) {
        mItemIdNo   = ((TextView)currentLine.findViewById(R.id.dlc_IdNo)).getText().toString();
        mItemName = ((TextView)currentLine.findViewById(R.id.dlc_Name)).getText().toString();
        mItemInfo = ((TextView)currentLine.findViewById(R.id.dlc_Info)).getText().toString();      
    }

    //Clear information of selected row
    private void clearCurrentData() {
        mItemIdNo   = null;
        mItemName = null;
        mItemInfo = null;       
    }

    //Create initial data for DataBase (for test)
    private void initData() {
        //Set no data when data exists
        Cursor cur = mSampleDbOpenHelper.getDb().rawQuery("SELECT * FROM " + CommonData.TABLE_NAME + " LIMIT 1", null);
        if (cur.getCount() > 0) {
            cur.close();
            return;   //Set no data
        }

        //Create initial data
        ContentValues insertValues = new ContentValues();
        for (int i = 1; i <= 31; i++) {
            insertValues.put("idno", String.valueOf(i));
            insertValues.put("name", "Name of User-" + String.valueOf(i));
            insertValues.put("info", "Info of User-" + String.valueOf(i));
            mSampleDbOpenHelper.getDb().insert(CommonData.TABLE_NAME, null, insertValues);
        }
    }
    
    //Pick up after
    @Override
    protected void onPause() {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
            mCursor = null;
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mSampleDbOpenHelper.closeDatabase();
        super.onDestroy();
    } 

    //-----------------------------------------------------------------------------
    // Menu
    //-----------------------------------------------------------------------------
    private static final int INSERT_MENUITEM_ID = Menu.FIRST + 1;
    private static final int SEARCH_MENUITEM_ID = Menu.FIRST + 2;
    private static final int EDIT_MENUITEM_ID   = Menu.FIRST + 3;
    private static final int DELETE_MENUITEM_ID = Menu.FIRST + 4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.add(0, INSERT_MENUITEM_ID, 0, R.string.INSERT_MENUITEM);
        item.setIcon(android.R.drawable.ic_menu_add);
        item = menu.add(0, SEARCH_MENUITEM_ID, 0, R.string.SEARCH_MENUITEM);
        item.setIcon(android.R.drawable.ic_menu_search);
        return true;
    }
 
    /**
     * When a menu is selected
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {           
        switch (item.getItemId()) {
        case INSERT_MENUITEM_ID:
            //Call an edit screen with registration mode
            startAddActivity();
            break;
        case SEARCH_MENUITEM_ID:
            //Call a search screen with registration mode
             startSearchActivity();
            break;
        case EDIT_MENUITEM_ID:
            //Call an edit screen with edit mode
            startEditActivity();
            break;
        case DELETE_MENUITEM_ID:
            //Call a confirming deletion screen
            startDeleteActivity();
            break;
        default:
            break;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    //-----------------------------------------------------------------------------
    //  Call child screens
    //-----------------------------------------------------------------------------
    //Call a new screen
    private void startAddActivity() {
        Intent intentAdd = new Intent(this, EditActivity.class);
        intentAdd.putExtra(CommonData.EXTRA_REQUEST_MODE, CommonData.REQUEST_NEW);
        startActivityForResult(intentAdd, CommonData.REQUEST_NEW);
    }

    private Intent newIntent(Context context, Class<?> cls,
            String idno, String name, String info) {
        Intent intent = new Intent(context, cls);

        //Set data to the intent
        intent.putExtra(CommonData.EXTRA_ITEM_IDNO, idno);
        intent.putExtra(CommonData.EXTRA_ITEM_NAME, name);
        intent.putExtra(CommonData.EXTRA_ITEM_INFO, info);            

        return intent;
    }
    
    //Call a search start screen
    private void startSearchActivity() {
        Intent intentSearch = newIntent(this, SearchActivity.class, 
                                    mItemIdNoSearch, mItemNameSearch, mItemInfoSearch);
           
        startActivityForResult(intentSearch, CommonData.REQUEST_SEARCH);
    }
    
    //Call an edit screen
    private void startEditActivity() {
        Intent intentEdit = newIntent(this, EditActivity.class, 
                                            mItemIdNo, mItemName, mItemInfo);
        intentEdit.putExtra(CommonData.EXTRA_REQUEST_MODE, CommonData.REQUEST_EDIT);

        startActivityForResult(intentEdit, CommonData.REQUEST_EDIT);
    }

    //Call delete screen
    private void startDeleteActivity() {
        Intent intentDelete = newIntent(this, DeleteActivity.class, 
                                            mItemIdNo, mItemName, mItemInfo);

        startActivityForResult(intentDelete, CommonData.REQUEST_DELETE);
    }

    /* Called when returning from edit, delete or search screen */
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        //Do nothing when cancel is selected
        if (resultCode == Activity.RESULT_CANCELED) {
            showDbData();            //Display user information
            return;
        }

        //Processing of each request code
        switch(requestCode) {
        case CommonData.REQUEST_NEW:
            addUserData(data.getStringExtra(CommonData.EXTRA_ITEM_IDNO), 
                        data.getStringExtra(CommonData.EXTRA_ITEM_NAME), 
                        data.getStringExtra(CommonData.EXTRA_ITEM_INFO));
            break;
        case CommonData.REQUEST_SEARCH:
              searchUserData(data.getStringExtra(CommonData.EXTRA_ITEM_IDNO), 
                      data.getStringExtra(CommonData.EXTRA_ITEM_NAME), 
                      data.getStringExtra(CommonData.EXTRA_ITEM_INFO));
            break;
        case CommonData.REQUEST_EDIT:
            editUserData(data.getStringExtra(CommonData.EXTRA_ITEM_IDNO), 
                         data.getStringExtra(CommonData.EXTRA_ITEM_NAME), 
                         data.getStringExtra(CommonData.EXTRA_ITEM_INFO));
            break;
        case CommonData.REQUEST_DELETE:
            deleteUserData(data.getStringExtra(CommonData.EXTRA_ITEM_IDNO));
            break;
        default:
            break;
        }
    }
    
    //Reflect data to a screen
    public void updateCursor(Cursor cur) {
        //Create an adapter
        String  cols2[]  =   {"idno","name","info"};
        int     views[] = {R.id.dlc_IdNo, R.id.dlc_Name, R.id.dlc_Info};

        //Get a listView
        ListView lv = (ListView)findViewById(R.id.DataList);

        //Close cursor if previous data exists
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        mCursor = cur;

        //Set data to display screen
        CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.data_list_contents, mCursor, cols2, views);
        lv.setAdapter(adapter);
    }

   //---------------------------------------
    //Processes to DataBase
    //---------------------------------------
    //DataBase data displaying process
    public void showDbData() {
        //Display screen process (asynchronous task)
        DataSearchTask task = new DataSearchTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(mItemIdNoSearch, mItemNameSearch, mItemInfoSearch);
    }
    
    //Addition process
    private void addUserData(String idno, String name, String info) {
        //add data process
        DataInsertTask task = new DataInsertTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(idno, name, info);        
    }
    
    //Search process
    private void searchUserData(String idno, String name, String info) {
        mItemIdNoSearch   = idno; 

        mItemNameSearch = name;
        mItemInfoSearch = info;

        //Search data process
        DataSearchTask task = new DataSearchTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(idno, name, info);
        }
    
    //Update process
    private void editUserData(String idno, String name, String info) {
        DataUpdateTask task = new DataUpdateTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(idno, name, info);
    }

    //Delete process
    private void deleteUserData(String idno) {     
        DataDeleteTask task = new DataDeleteTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(idno);
    }
}
