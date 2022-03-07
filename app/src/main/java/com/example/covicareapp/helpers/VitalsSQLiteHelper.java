package com.example.covicareapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.covicareapp.models.OfflineUserModel;
import com.example.covicareapp.models.OnlineUserVitalsModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VitalsSQLiteHelper extends SQLiteOpenHelper {


    public static final String ONLINE_USERS_DATA = "ONLINE_USERS_DATA";
    public static final String RASPI_UID = "RASPI_UID";
    public static final String DATE_REC = "DATE_REC";
    public static final String RASPI_ID = "RASPI_ID";
    public static final String UID = "UID";
    public static final String PID = "PID";
    public static final String O_2_VAL = "O2_VAL";
    public static final String HB_VAL = "HB_VAL";
    public static final String TEMP_VAL = "TEMP_VAL";
    public static final String COUGH_VAL = "COUGH_VAL";
    public static final String ANALYSIS_RES = "ANALYSIS_RES";

    public static final String OFFLINE_USERS = "OFFLINE_USERS";
    public static final String OFFLINE_USERS_DATA = "OFFLINE_USERS_DATA";
    public static final String OFFLINE_USERS_PROFILES = "OFFLINE_USERS_PROFILES";
    public static final String LUID = "LUID";
    public static final String NAME = "NAME";
    public static final String GENDER = "GENDER";
    public static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";
    public static final String DATE_ADDED = "DATE_ADDED";


    public VitalsSQLiteHelper(@Nullable Context context) {
        super(context, "covicareData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createOnlineUsersDataTable =
                "CREATE TABLE " + ONLINE_USERS_DATA + " ("
                        + UID + " TEXT NOT NULL , "
                        + RASPI_UID + " TEXT NOT NULL , "
                        + RASPI_ID + " TEXT NOT NULL, "
                        + PID + " TEXT NOT NULL, "
                        + O_2_VAL + " REAL NOT NULL, "
                        + HB_VAL + " REAL NOT NULL, "
                        + TEMP_VAL + " REAL NOT NULL, "
                        + COUGH_VAL + " INTEGER NOT NULL, "
                        + DATE_REC + " NUMERIC NOT NULL, "
                        + ANALYSIS_RES + " TEXT NOT NULL, "
                        + " PRIMARY KEY(" + RASPI_UID + "," + DATE_REC + "))";

        sqLiteDatabase.execSQL(createOnlineUsersDataTable);

        String createOfflineUsersTable =
                "CREATE TABLE " + OFFLINE_USERS + " ("
                        + LUID + " TEXT NOT NULL , "
                        + NAME + " TEXT NOT NULL , "
                        + GENDER + " TEXT NOT NULL , "
                        + DATE_OF_BIRTH + " NUMERIC NOT NULL , "
                        + DATE_ADDED + " NUMERIC NOT NULL , "
                        + " PRIMARY KEY(" + LUID + "))";

        sqLiteDatabase.execSQL(createOfflineUsersTable);

        String createOfflineUsersDataTable =
                "CREATE TABLE " + OFFLINE_USERS_DATA + " ("
                        + LUID + " TEXT NOT NULL , "
                        + RASPI_ID + " TEXT NOT NULL , "
                        + PID + " TEXT NOT NULL, "
                        + O_2_VAL + " REAL NOT NULL ,  "
                        + HB_VAL + " REAL NOT NULL ,  "
                        + TEMP_VAL + " REAL NOT NULL ,  "
                        + COUGH_VAL + " INTEGER NOT NULL ,  "
                        + DATE_REC + " NUMERIC NOT NULL ,  "
                        + ANALYSIS_RES + " TEXT NOT NULL ,  "
                        + "FOREIGN KEY (" + LUID + ")"
                        + "REFERENCES " + OFFLINE_USERS
                        + "(" + LUID + "), "
                        + "PRIMARY KEY(" + LUID + "," + DATE_REC + "))";

        sqLiteDatabase.execSQL(createOfflineUsersDataTable);

        String createOfflineUsersProfilesRelationTable =
                "CREATE TABLE " + OFFLINE_USERS_PROFILES + " ("
                        + LUID + " TEXT NOT NULL , "
                        + PID + " TEXT NOT NULL , "
                        + "PRIMARY KEY(" + LUID + "," + PID + "))";

        sqLiteDatabase.execSQL(createOfflineUsersProfilesRelationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //    For Online Users
    public boolean addOneVitalsEntryOnline(OnlineUserVitalsModel onlineUserVitalsModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestamp = onlineUserVitalsModel.getRecDateTime();
        Date date = new Date(timestamp.getTime());

        contentValues.put(UID, onlineUserVitalsModel.getUserId());
        contentValues.put(RASPI_UID, onlineUserVitalsModel.getRaspiUId());
        contentValues.put(RASPI_ID, onlineUserVitalsModel.getRaspiId());
        contentValues.put(PID, onlineUserVitalsModel.getProfileId());
        contentValues.put(O_2_VAL, onlineUserVitalsModel.getO2Saturation());
        contentValues.put(HB_VAL, onlineUserVitalsModel.getHbRating());
        contentValues.put(TEMP_VAL, onlineUserVitalsModel.getBodyTemp());
        contentValues.put(COUGH_VAL, onlineUserVitalsModel.getCoughAnalysis());
        contentValues.put(DATE_REC, dateFormat.format(date));
        contentValues.put(ANALYSIS_RES, onlineUserVitalsModel.getAnalysisResult());

        boolean returnVal;

        try {
            long insert = sqLiteDatabase.insert(ONLINE_USERS_DATA, null, contentValues);
            returnVal = insert != -1;
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            returnVal = false;
        }

        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteAllUserVitalsOnline() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        boolean returnVal;

        try {
            sqLiteDatabase.execSQL("delete from " + ONLINE_USERS_DATA);
            returnVal = true;
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            returnVal = false;
        }
        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteUserVitalsOnline(String userUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + ONLINE_USERS_DATA + " WHERE " + UID + " = '" + userUniqueId + "'";

        boolean returnVal;

        try {
            sqLiteDatabase.rawQuery(queryString, null);
            returnVal = true;
        } catch (Exception e) {
            Log.i("Exception while deleting user   ", e.toString());
            returnVal = false;
        }
        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteUserVitalsForProfileOnline(String userUniqueId, String profileUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + ONLINE_USERS_DATA + " WHERE " + UID + " = '" + userUniqueId + "' AND " + PID + " = '" + profileUniqueId + "'";

        boolean returnVal;

        try {
            sqLiteDatabase.rawQuery(queryString, null);
            returnVal = true;
        } catch (Exception e) {
            Log.i("Exception while deleting user   ", e.toString());
            returnVal = false;
        }
        sqLiteDatabase.close();
        return returnVal;
    }

    public List<OnlineUserVitalsModel> getAllVitalsListOnline() {
        List<OnlineUserVitalsModel> allVitalsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + ONLINE_USERS_DATA;

        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(0);
                String raspiUId = cursor.getString(1);
                String raspiId = cursor.getString(2);
                String profileId = cursor.getString(3);
                float hbRating = cursor.getFloat(4);
                float o2Saturation = cursor.getFloat(5);
                float bodyTemp = cursor.getFloat(6);
                int coughAnalysis = cursor.getInt(7);
                String recDateTime = cursor.getString(8);
                String analysisResult = cursor.getString(9);

                Timestamp recTimestamp = Timestamp.valueOf(recDateTime);

                Log.i("userId : ", userId);
                Log.i("raspiUId : ", raspiUId);
                Log.i("raspiId : ", raspiId);
                Log.i("profileId : ", profileId);
                Log.i("hbRating : ", String.valueOf(hbRating));
                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
                Log.i("recDateTime : ", recDateTime);
                Log.i("recTimestamp : ", String.valueOf(recTimestamp));

                OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiUId, raspiId, profileId, hbRating, o2Saturation, bodyTemp, coughAnalysis, recTimestamp, analysisResult);
                allVitalsList.add(onlineUserVitalsModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allVitalsList;
    }

    public List<OnlineUserVitalsModel> getVitalsForUserListOnline(String userUniqueId) {
        List<OnlineUserVitalsModel> allVitalsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + ONLINE_USERS_DATA + " WHERE " + UID + " = '" + userUniqueId + "'";

        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(0);
                String raspiUId = cursor.getString(1);
                String raspiId = cursor.getString(2);
                String profileId = cursor.getString(3);
                float hbRating = cursor.getFloat(4);
                float o2Saturation = cursor.getFloat(5);
                float bodyTemp = cursor.getFloat(6);
                int coughAnalysis = cursor.getInt(7);
                String recDateTime = cursor.getString(8);
                String analysisResult = cursor.getString(9);

                Timestamp recTimestamp = Timestamp.valueOf(recDateTime);

                Log.i("userId : ", userId);
                Log.i("raspiUId : ", raspiUId);
                Log.i("raspiId : ", raspiId);
                Log.i("profileId : ", profileId);
                Log.i("hbRating : ", String.valueOf(hbRating));
                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
                Log.i("recDateTime : ", recDateTime);
                Log.i("recTimestamp : ", String.valueOf(recTimestamp));

                OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiUId, raspiId, profileId, hbRating, o2Saturation, bodyTemp, coughAnalysis, recTimestamp, analysisResult);
                allVitalsList.add(onlineUserVitalsModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allVitalsList;
    }

    //    For Offline Users
    public boolean addUserOffline(String profileUniqueId, OfflineUserModel offlineUserModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestamp = offlineUserModel.getDateOfBirth();
        Date date = new Date(timestamp.getTime());

        Date dateAdded = new Date(System.currentTimeMillis());

        contentValues.put(LUID, offlineUserModel.getLocalUserId());
        contentValues.put(NAME, offlineUserModel.getName());
        contentValues.put(DATE_OF_BIRTH, dateFormat.format(date));
        contentValues.put(GENDER, offlineUserModel.getGender());
        contentValues.put(DATE_ADDED, dateFormat.format(dateAdded));

        boolean returnVal;

        try {
            long insert = sqLiteDatabase.insert(OFFLINE_USERS, null, contentValues);
            if (insert == -1) {
                returnVal = false;
            } else {
                contentValues = new ContentValues();

                contentValues.put(LUID, offlineUserModel.getLocalUserId());
                contentValues.put(PID, profileUniqueId);

                try {
                    insert = sqLiteDatabase.insert(OFFLINE_USERS_PROFILES, null, contentValues);
                    if (insert == -1) {
                        deleteUserOffline(offlineUserModel.getLocalUserId());
                        returnVal = false;
                    } else {
                        returnVal = true;
                    }
                } catch (Exception e) {
                    deleteUserOffline(offlineUserModel.getLocalUserId());
                    Log.i("Exception while adding offline user data   ", e.toString());
                    returnVal = false;
                }
            }
        } catch (Exception e) {
            Log.i("Exception while adding offline user data   ", e.toString());
            returnVal = false;
        }

        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean updateUserOffline(OfflineUserModel offlineUserModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestamp = offlineUserModel.getDateOfBirth();
        Date date = new Date(timestamp.getTime());

        contentValues.put(NAME, offlineUserModel.getName());
        contentValues.put(DATE_OF_BIRTH, dateFormat.format(date));
        contentValues.put(GENDER, offlineUserModel.getGender());

        boolean returnVal;

        try {
            long insert = sqLiteDatabase.update(OFFLINE_USERS, contentValues, "LUID=?", new String[]{offlineUserModel.getLocalUserId()});
            returnVal = insert != -1;
        } catch (Exception e) {
            Log.i("Exception while updating local users data   ", e.toString());
            returnVal = false;
        }
        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteUserOffline(String userUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + OFFLINE_USERS + " WHERE " + UID + " = '" + userUniqueId + "'";

        boolean returnVal;

        try {
            sqLiteDatabase.rawQuery(queryString, null);
            queryString = "DELETE FROM " + OFFLINE_USERS_PROFILES + " WHERE " + UID + " = '" + userUniqueId + "'";
            returnVal = true;
        } catch (Exception e) {
            Log.i("Exception while deleting user   ", e.toString());
            returnVal = false;
        }
        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteUserOfflineFromProfile(String userUniqueId, String profileUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + ONLINE_USERS_DATA + " WHERE " + UID + " = '" + userUniqueId + "'";

        boolean returnVal;

        try {
            sqLiteDatabase.rawQuery(queryString, null);
            returnVal = true;
        } catch (Exception e) {
            Log.i("Exception while deleting user   ", e.toString());
            returnVal = false;
        }
        return returnVal;
    }

    public boolean deleteAllUsersOffline() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            sqLiteDatabase.execSQL("delete from " + ONLINE_USERS_DATA);
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            return false;
        }
        return true;
    }


    public boolean addOneVitalsEntryOffline(OnlineUserVitalsModel onlineUserVitalsModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestamp = onlineUserVitalsModel.getRecDateTime();
        Date date = new Date(timestamp.getTime());

        contentValues.put(UID, onlineUserVitalsModel.getUserId());
        contentValues.put(RASPI_UID, onlineUserVitalsModel.getRaspiUId());
        contentValues.put(RASPI_ID, onlineUserVitalsModel.getRaspiId());
        contentValues.put(O_2_VAL, onlineUserVitalsModel.getO2Saturation());
        contentValues.put(HB_VAL, onlineUserVitalsModel.getHbRating());
        contentValues.put(TEMP_VAL, onlineUserVitalsModel.getBodyTemp());
        contentValues.put(COUGH_VAL, onlineUserVitalsModel.getCoughAnalysis());
        contentValues.put(DATE_REC, dateFormat.format(date));
        contentValues.put(ANALYSIS_RES, onlineUserVitalsModel.getAnalysisResult());

        try {
            long insert = sqLiteDatabase.insert(ONLINE_USERS_DATA, null, contentValues);
            return insert != -1;
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            return false;
        }
    }

    public boolean deleteAllUserVitalsOffline() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            sqLiteDatabase.execSQL("delete from " + ONLINE_USERS_DATA);
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            return false;
        }
        return true;
    }

    public boolean deleteUserVitalsOffline(String userUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + ONLINE_USERS_DATA + " WHERE " + UID + " = '" + userUniqueId + "'";

        try {
            sqLiteDatabase.rawQuery(queryString, null);
        } catch (Exception e) {
            Log.i("Exception while deleting user   ", e.toString());
            return false;
        }
        return true;
    }

//    public List<OnlineUserVitalsModel> getAllVitalsListOffline() {
//        List<OnlineUserVitalsModel> allVitalsList = new ArrayList<>();
//
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        String queryString = "SELECT * FROM " + ONLINE_USERS_DATA;
//
//        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                String userId = cursor.getString(0);
//                String raspiUId = cursor.getString(1);
//                String raspiId = cursor.getString(2);
//                float hbRating = cursor.getFloat(3);
//                float o2Saturation = cursor.getFloat(4);
//                float bodyTemp = cursor.getFloat(5);
//                int coughAnalysis = cursor.getInt(6);
//                String recDateTime = cursor.getString(7);
//                String analysisResult = cursor.getString(8);
//
//                Timestamp recTimestamp = Timestamp.valueOf(recDateTime);
//
//                Log.i("userId : ", userId);
//                Log.i("raspiUId : ", raspiUId);
//                Log.i("raspiId : ", raspiId);
//                Log.i("hbRating : ", String.valueOf(hbRating));
//                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
//                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
//                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
//                Log.i("recDateTime : ", recDateTime);
//                Log.i("recTimestamp : ", String.valueOf(recTimestamp));
//
//                OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiId, raspiUId, hbRating, o2Saturation, bodyTemp, coughAnalysis, recTimestamp, analysisResult);
//                allVitalsList.add(onlineUserVitalsModel);
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        sqLiteDatabase.close();
//        return allVitalsList;
//    }

//    public List<OnlineUserVitalsModel> getVitalsForUserListOffline(String userUniqueId) {
//        List<OnlineUserVitalsModel> allVitalsList = new ArrayList<>();
//
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        String queryString = "SELECT * FROM " + ONLINE_USERS_DATA + " WHERE " + UID + " = '" + userUniqueId + "'";
//
//        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                String userId = cursor.getString(0);
//                String raspiUId = cursor.getString(1);
//                String raspiId = cursor.getString(2);
//                float hbRating = cursor.getFloat(3);
//                float o2Saturation = cursor.getFloat(4);
//                float bodyTemp = cursor.getFloat(5);
//                int coughAnalysis = cursor.getInt(6);
//                String recDateTime = cursor.getString(7);
//                String analysisResult = cursor.getString(8);
//
//                Timestamp recTimestamp = Timestamp.valueOf(recDateTime);
//
//                Log.i("userId : ", userId);
//                Log.i("raspiUId : ", raspiUId);
//                Log.i("raspiId : ", raspiId);
//                Log.i("hbRating : ", String.valueOf(hbRating));
//                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
//                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
//                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
//                Log.i("recDateTime : ", recDateTime);
//                Log.i("recTimestamp : ", String.valueOf(recTimestamp));
//
//                OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiId, raspiUId, hbRating, o2Saturation, bodyTemp, coughAnalysis, recTimestamp, analysisResult);
//                allVitalsList.add(onlineUserVitalsModel);
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        sqLiteDatabase.close();
//        return allVitalsList;
//    }
}
