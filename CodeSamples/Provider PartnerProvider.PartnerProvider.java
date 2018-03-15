package org.jssec.android.provider.partnerprovider;

import java.util.List;

import org.jssec.android.shared.PkgCertWhitelists;
import org.jssec.android.shared.Utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;

public class PartnerProvider extends ContentProvider {

    public static final String AUTHORITY = "org.jssec.android.provider.partnerprovider";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.jssec.contenttype";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.org.jssec.contenttype";

    // Expose the interface that the Content Provider provides.
    public interface Download {
        public static final String PATH = "downloads";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }
    public interface Address {
        public static final String PATH = "addresses";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    }

    // UriMatcher
    private static final int DOWNLOADS_CODE = 1;
    private static final int DOWNLOADS_ID_CODE = 2;
    private static final int ADDRESSES_CODE = 3;
    private static final int ADDRESSES_ID_CODE = 4;
    private static UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, Download.PATH, DOWNLOADS_CODE);
        sUriMatcher.addURI(AUTHORITY, Download.PATH + "/#", DOWNLOADS_ID_CODE);
        sUriMatcher.addURI(AUTHORITY, Address.PATH, ADDRESSES_CODE);
        sUriMatcher.addURI(AUTHORITY, Address.PATH + "/#", ADDRESSES_ID_CODE);
    }

    // Since this is a sample program,
    // query method returns the following fixed result always without using database.
    private static MatrixCursor sAddressCursor = new MatrixCursor(new String[] { "_id", "city" });
    static {
        sAddressCursor.addRow(new String[] { "1", "New York" });
        sAddressCursor.addRow(new String[] { "2", "London" });
        sAddressCursor.addRow(new String[] { "3", "Paris" });
    }
    private static MatrixCursor sDownloadCursor = new MatrixCursor(new String[] { "_id", "path" });
    static {
        sDownloadCursor.addRow(new String[] { "1", "/sdcard/downloads/sample.jpg" });
        sDownloadCursor.addRow(new String[] { "2", "/sdcard/downloads/sample.txt" });
    }

    // *** POINT 2 *** Verify if the certificate of a requesting application has been registered in the own white list.
    private static PkgCertWhitelists sWhitelists = null;
    private static void buildWhitelists(Context context) {
        boolean isdebug = Utils.isDebuggable(context);
        sWhitelists = new PkgCertWhitelists();

        // Register certificate hash value of partner application org.jssec.android.provider.partneruser.
        sWhitelists.add("org.jssec.android.provider.partneruser", isdebug ?
                // Certificate hash value of "androiddebugkey" in the debug.keystore.
                "0EFB7236 328348A9 89718BAD DF57F544 D5CCB4AE B9DB34BC 1E29DD26 F77C8255" :
                // Certificate hash value of "partner key" in the keystore.
                "1F039BB5 7861C27A 3916C778 8E78CE00 690B3974 3EB8259F E2627B8D 4C0EC35A");

        // Register following other partner applications in the same way.
    }
    private static boolean checkPartner(Context context, String pkgname) {
        if (sWhitelists == null) buildWhitelists(context);
        return sWhitelists.test(context, pkgname);
    }
    // Get the package name of the calling application.
    private String getCallingPackage(Context context) {
        String pkgname;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pkgname = super.getCallingPackage();
        } else {
            pkgname = null;
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> procList = am.getRunningAppProcesses();
            int callingPid = Binder.getCallingPid();
            if (procList != null) {
                for (RunningAppProcessInfo proc : procList) {
                    if (proc.pid == callingPid) {
                        pkgname = proc.pkgList[proc.pkgList.length - 1];
                        break;
                    }
                }
            }
        }
        return pkgname;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
        case ADDRESSES_CODE:
            return CONTENT_TYPE;

        case DOWNLOADS_ID_CODE:
        case ADDRESSES_ID_CODE:
            return CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Invalid URI:" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        // *** POINT 2 *** Verify if the certificate of a requesting application has been registered in the own white list.
        if (!checkPartner(getContext(), getCallingPackage(getContext()))) {
            throw new SecurityException("Calling application is not a partner application.");
        }

        // *** POINT 3 *** Handle the received request data carefully and securely,
        // even though the data comes from a partner application.
        // Here, whether uri is within expectations or not, is verified by UriMatcher#match() and switch case.
        // Checking for other parameters are omitted here, due to sample.
        // Refer to "3.2 Handle Input Data Carefully and Securely."

        // *** POINT 4 *** Information that is granted to disclose to partner applications can be returned.
        // It depends on application whether the query result can be disclosed or not.
        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
        case DOWNLOADS_ID_CODE:
            return sDownloadCursor;

        case ADDRESSES_CODE:
        case ADDRESSES_ID_CODE:
            return sAddressCursor;

        default:
            throw new IllegalArgumentException("Invalid URI:" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // *** POINT 2 *** Verify if the certificate of a requesting application has been registered in the own white list.
        if (!checkPartner(getContext(), getCallingPackage(getContext()))) {
            throw new SecurityException("Calling application is not a partner application.");
        }

        // *** POINT 3 *** Handle the received request data carefully and securely,
        // even though the data comes from a partner application.
        // Here, whether uri is within expectations or not, is verified by UriMatcher#match() and switch case.
        // Checking for other parameters are omitted here, due to sample.
        // Refer to "3.2 Handle Input Data Carefully and Securely."

        // *** POINT 4 *** Information that is granted to disclose to partner applications can be returned.
        // It depends on application whether the issued ID has sensitive meaning or not.
        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
            return ContentUris.withAppendedId(Download.CONTENT_URI, 3);

        case ADDRESSES_CODE:
            return ContentUris.withAppendedId(Address.CONTENT_URI, 4);

        default:
            throw new IllegalArgumentException("Invalid URI:" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {

        // *** POINT 2 *** Verify if the certificate of a requesting application has been registered in the own white list.
        if (!checkPartner(getContext(), getCallingPackage(getContext()))) {
            throw new SecurityException("Calling application is not a partner application.");
        }

        // *** POINT 3 *** Handle the received request data carefully and securely,
        // even though the data comes from a partner application.
        // Here, whether uri is within expectations or not, is verified by UriMatcher#match() and switch case.
        // Checking for other parameters are omitted here, due to sample.
        // Refer to "3.2 Handle Input Data Carefully and Securely."

        // *** POINT 4 *** Information that is granted to disclose to partner applications can be returned.
        // It depends on application whether the number of updated records has sensitive meaning or not.
        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
            return 5;   // Return number of updated records

        case DOWNLOADS_ID_CODE:
            return 1;

        case ADDRESSES_CODE:
            return 15;

        case ADDRESSES_ID_CODE:
            return 1;

        default:
            throw new IllegalArgumentException("Invalid URI:" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // *** POINT 2 *** Verify if the certificate of a requesting application has been registered in the own white list.
        if (!checkPartner(getContext(), getCallingPackage(getContext()))) {
            throw new SecurityException("Calling application is not a partner application.");
        }

        // *** POINT 3 *** Handle the received request data carefully and securely,
        // even though the data comes from a partner application.
        // Here, whether uri is within expectations or not, is verified by UriMatcher#match() and switch case.
        // Checking for other parameters are omitted here, due to sample.
        // Refer to "3.2 Handle Input Data Carefully and Securely."

        // *** POINT 4 *** Information that is granted to disclose to partner applications can be returned.
        // It depends on application whether the number of deleted records has sensitive meaning or not.
        switch (sUriMatcher.match(uri)) {
        case DOWNLOADS_CODE:
            return 10;  // Return number of deleted records

        case DOWNLOADS_ID_CODE:
            return 1;

        case ADDRESSES_CODE:
            return 20;

        case ADDRESSES_ID_CODE:
            return 1;

        default:
            throw new IllegalArgumentException("Invalid URI:" + uri);
        }
    }
}
