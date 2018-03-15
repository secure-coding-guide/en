package org.jssec.android.sqlite;

public class CommonData {
    //Request codes for calling each screen.
    public static final int REQUEST_NEW           = 0;
    public static final int REQUEST_EDIT          = 1;
    public static final int REQUEST_DELETE        = 2;
    public static final int REQUEST_SEARCH        = 3;
    
    //Intent data identifier
    public static final String EXTRA_REQUEST_MODE   = "EXTRA_REQUEST_MODE";
    public static final String EXTRA_ITEM_IDNO      = "EXTRA_ITEM_IDNO";
    public static final String EXTRA_ITEM_NAME      = "EXTRA_ITEM_NAME";
    public static final String EXTRA_ITEM_INFO      = "EXTRA_ITEM_INFO";
    public static final String EXTRA_TABLE_NAME     = "EXTRA_TABLE_NAME";
    
    //Database file name
    public static final String DBFILE_NAME = "SQLiteDatabase.db";
    //Database version
    public static final int DB_VERSION = 1;
    
    public static final String TABLE_NAME = "SQLiteTable";
    
    public static final Integer TEXT_DATA_LENGTH_MAX       = 15;
    
}
