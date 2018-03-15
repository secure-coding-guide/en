package org.jssec.android.sqlite.task;

import org.jssec.android.sqlite.CommonData;
import org.jssec.android.sqlite.DataValidator;
import org.jssec.android.sqlite.MainActivity;
import org.jssec.android.sqlite.R;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class DataUpdateTask extends  AsyncTask<String, Void, Void>  {
    private SQLiteDatabase  mSampleDb;
    private MainActivity    mActivity;

    public DataUpdateTask(SQLiteDatabase db, MainActivity activity) {
        mSampleDb = db;
        mActivity = activity;
    }

    @Override
    protected Void doInBackground(String... params) {
        String  idno = params[0];
        String  name = params[1];
        String  info = params[2];

        //*** POINT 3 *** Validate the input value according the application requirements.
        if (!DataValidator.validateData(idno, name, info)) {
            return null;
        }

        String whereArgs[] = {idno};
        ContentValues updateValues = new ContentValues();
        updateValues.put("idno", idno);
        updateValues.put("name", name);
        updateValues.put("info", info);

        try {
            //*** POINT 2 *** Use place holder.
            mSampleDb. update (CommonData.TABLE_NAME, updateValues, "idno = ?", whereArgs);
        } catch (SQLException e) {
            Log.e(DataUpdateTask.class.toString(), mActivity.getString(R.string.UPDATING_ERROR_MESSAGE));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        //Display database data
        mActivity.showDbData();
    }

}
