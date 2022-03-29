package com.example.covicareapp.helpers;

import static com.example.covicareapp.helpers.VitalsContract.OnlineUsersData.DATE_REC;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.covicareapp.helpers.VitalsContract.LocalUsers;
import com.example.covicareapp.helpers.VitalsContract.LocalUsersData;
import com.example.covicareapp.helpers.VitalsContract.LocalUsersProfilesRelation;
import com.example.covicareapp.helpers.VitalsContract.OnlineUsersData;
import com.example.covicareapp.models.LocalUserModel;
import com.example.covicareapp.models.LocalUserVitalsModel;
import com.example.covicareapp.models.OnlineUserVitalsModel;

import java.util.ArrayList;
import java.util.List;

public class VitalsSQLiteHelper extends SQLiteOpenHelper {
    public static final String COVICARE_DATA_DB = "covicareData.db";
    public static final int DATABASE_VERSION = 1;


    public VitalsSQLiteHelper(@Nullable Context context) {
        super(context, COVICARE_DATA_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String createOnlineUsersDataTable =
                "CREATE TABLE " + OnlineUsersData.ONLINE_USERS_DATA + " ("
                        + OnlineUsersData.EMAIL_ID + " TEXT NOT NULL , "
                        + OnlineUsersData.RASPI_UID + " TEXT NOT NULL , "
                        + OnlineUsersData.RASPI_ID + " TEXT NOT NULL, "
                        + OnlineUsersData.GROUP_ID + " TEXT NOT NULL, "
                        + OnlineUsersData.O_2_VAL + " REAL NOT NULL, "
                        + OnlineUsersData.HB_VAL + " REAL NOT NULL, "
                        + OnlineUsersData.TEMP_VAL + " REAL NOT NULL, "
                        + OnlineUsersData.COUGH_VAL + " INTEGER NOT NULL, "
                        + DATE_REC + " LONG NOT NULL, "
                        + OnlineUsersData.ANALYSIS_RES + " TEXT NOT NULL, "
                        + OnlineUsersData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ");";
//                        + " PRIMARY KEY(" + OnlineUsersData.RASPI_UID + "," + OnlineUsersData.DATE_REC + "));";

        sqLiteDatabase.execSQL(createOnlineUsersDataTable);

        final String createLocalUsersTable =
                "CREATE TABLE " + LocalUsers.LOCAL_USERS + " ("
                        + LocalUsers.LUID + " TEXT NOT NULL , "
                        + LocalUsers.NAME + " TEXT NOT NULL , "
                        + LocalUsers.GENDER + " TEXT NOT NULL , "
                        + LocalUsers.DATE_OF_BIRTH + " TEXT NOT NULL , "
                        + LocalUsers.DATE_ADDED + " TEXT NOT NULL , "
                        + LocalUsers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ");";
//                        + " PRIMARY KEY(" + LocalUsers.LUID + "));";

        sqLiteDatabase.execSQL(createLocalUsersTable);

        final String createLocalUsersDataTable =
                "CREATE TABLE " + LocalUsersData.LOCAL_USERS_DATA + " ("
                        + LocalUsersData.LUID + " TEXT NOT NULL , "
                        + LocalUsersData.RASPI_ID + " TEXT NOT NULL , "
                        + LocalUsersData.GROUP_ID + " TEXT NOT NULL, "
                        + LocalUsersData.O_2_VAL + " REAL NOT NULL ,  "
                        + LocalUsersData.HB_VAL + " REAL NOT NULL ,  "
                        + LocalUsersData.TEMP_VAL + " REAL NOT NULL ,  "
                        + LocalUsersData.COUGH_VAL + " INTEGER NOT NULL ,  "
                        + LocalUsersData.DATE_REC + " LONG NOT NULL ,  "
                        + LocalUsersData.ANALYSIS_RES + " TEXT NOT NULL ,  "
                        + LocalUsersData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ");";
//                        + "FOREIGN KEY (" + LocalUsersData.LUID + ")"
//                        + "REFERENCES " + LocalUsers.LOCAL_USERS
//                        + "(" + LocalUsersData.LUID + "), "
//                        + "PRIMARY KEY(" + LocalUsersData.LUID + "," + LocalUsersData.DATE_REC + "));";

        sqLiteDatabase.execSQL(createLocalUsersDataTable);

        final String createLocalUsersProfilesRelationTable =
                "CREATE TABLE " + LocalUsersProfilesRelation.LOCAL_USERS_PROFILES + " ("
                        + LocalUsersProfilesRelation.LUID + " TEXT NOT NULL , "
                        + LocalUsersProfilesRelation.GROUP_ID + " TEXT NOT NULL , "
                        + LocalUsersProfilesRelation._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ");";
//                        + "PRIMARY KEY(" + LocalUsersProfilesRelation.LUID + "," + LocalUsersProfilesRelation.GROUP_ID + "));";

        sqLiteDatabase.execSQL(createLocalUsersProfilesRelationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OnlineUsersData.ONLINE_USERS_DATA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocalUsers.LOCAL_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocalUsersData.LOCAL_USERS_DATA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocalUsersProfilesRelation.LOCAL_USERS_PROFILES);
        onCreate(sqLiteDatabase);

    }

    //    For Online Users
    public boolean addOneVitalsEntryOnline(OnlineUserVitalsModel onlineUserVitalsModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Timestamp timestamp = onlineUserVitalsModel.getRecDateTime();
//        Date date = new Date(timestamp.getTime());

//        String timestampNow = String.valueOf(com.google.firebase.Timestamp.now().getSeconds());

        contentValues.put(OnlineUsersData.EMAIL_ID, onlineUserVitalsModel.getUserId());
        contentValues.put(OnlineUsersData.RASPI_UID, onlineUserVitalsModel.getRaspiUId());
        contentValues.put(OnlineUsersData.RASPI_ID, onlineUserVitalsModel.getRaspiId());
        contentValues.put(OnlineUsersData.GROUP_ID, onlineUserVitalsModel.getGroupId());
        contentValues.put(OnlineUsersData.O_2_VAL, onlineUserVitalsModel.getSp02());
        contentValues.put(OnlineUsersData.HB_VAL, onlineUserVitalsModel.getPulse());
        contentValues.put(OnlineUsersData.TEMP_VAL, onlineUserVitalsModel.getTemperature());
        contentValues.put(OnlineUsersData.COUGH_VAL, onlineUserVitalsModel.getCoughAnalysis());
        contentValues.put(DATE_REC, onlineUserVitalsModel.getRecDateTime());
        contentValues.put(OnlineUsersData.ANALYSIS_RES, onlineUserVitalsModel.getAnalysisResult());

        boolean returnVal;

        try {
            long insert = sqLiteDatabase.insert(OnlineUsersData.ONLINE_USERS_DATA, null, contentValues);
            returnVal = insert != -1;
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            returnVal = false;
        }
//TODO close
//        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteAllUserVitalsOnline() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        boolean returnVal;

        try {
            sqLiteDatabase.execSQL("DELETE FROM " + OnlineUsersData.ONLINE_USERS_DATA + " ;");
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

        String queryString = "DELETE FROM " + OnlineUsersData.ONLINE_USERS_DATA + " WHERE " + OnlineUsersData.EMAIL_ID + " = '" + userUniqueId + "'" + " ;";

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

    public boolean deleteUserVitalsForProfileOnline(String userUniqueId, String groupUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + OnlineUsersData.ONLINE_USERS_DATA + " WHERE " + OnlineUsersData.EMAIL_ID + " = '" + userUniqueId + "' AND " + OnlineUsersData.GROUP_ID + " = '" + groupUniqueId + "'" + " ;";

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

    public ArrayList<OnlineUserVitalsModel> getAllVitalsListOnline() {
        ArrayList<OnlineUserVitalsModel> allVitalsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + OnlineUsersData.ONLINE_USERS_DATA + " ;";

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

                Log.i("userId : ", userId);
                Log.i("raspiUId : ", raspiUId);
                Log.i("raspiId : ", raspiId);
                Log.i("profileId : ", profileId);
                Log.i("hbRating : ", String.valueOf(hbRating));
                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
                Log.i("recDateTime : ", recDateTime);
                Log.i("recTimestamp : ", String.valueOf(recDateTime));

                OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiUId, raspiId, profileId, Double.valueOf(hbRating), Double.valueOf(o2Saturation), Double.valueOf(bodyTemp), Integer.valueOf(coughAnalysis), Long.valueOf(recDateTime), analysisResult);
                allVitalsList.add(onlineUserVitalsModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allVitalsList;
    }

    public ArrayList<OnlineUserVitalsModel> getVitalsForUserListOnline(String userUniqueId) {
        ArrayList<OnlineUserVitalsModel> allVitalsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + OnlineUsersData.ONLINE_USERS_DATA + " WHERE " + OnlineUsersData.EMAIL_ID + " = '" + userUniqueId + "'" + " ;";

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
                long recDateTime = cursor.getLong(8);
                String analysisResult = cursor.getString(9);

//                Log.i("userId : ", userId);
//                Log.i("raspiUId : ", raspiUId);
//                Log.i("raspiId : ", raspiId);
//                Log.i("profileId : ", profileId);
//                Log.i("hbRating : ", String.valueOf(hbRating));
//                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
//                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
//                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
//                Log.i("recDateTime : ", recDateTime);
//                Log.i("recTimestamp : ", recDateTime);

                OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiUId, raspiId, profileId, hbRating, o2Saturation, bodyTemp, coughAnalysis, Long.valueOf(recDateTime), analysisResult);
                allVitalsList.add(onlineUserVitalsModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
//        sqLiteDatabase.close();
        return allVitalsList;
    }

    public  ArrayList<OnlineUserVitalsModel> getVitalsForUserListOnlineBetween(String userUniqueId, Long from, Long to) {
        ArrayList<OnlineUserVitalsModel> allVitalsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + OnlineUsersData.ONLINE_USERS_DATA + " WHERE " + OnlineUsersData.EMAIL_ID + " = '" + userUniqueId + "'" + " AND " +  OnlineUsersData.DATE_REC + " BETWEEN " + from  + " AND " + to + " ORDER BY " + DATE_REC + " ASC;";

        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(0);
                String raspiUId = cursor.getString(1);
                String raspiId = cursor.getString(2);
                String profileId = cursor.getString(3);
                float o2Saturation = cursor.getFloat(4);
                float hbRating = cursor.getFloat(5);
                float bodyTemp = cursor.getFloat(6);
                int coughAnalysis = cursor.getInt(7);
                long recDateTime = cursor.getLong(8);
                String analysisResult = cursor.getString(9);

//                Log.i("userId : ", userId);
//                Log.i("raspiUId : ", raspiUId);
//                Log.i("raspiId : ", raspiId);
//                Log.i("profileId : ", profileId);
//                Log.i("hbRating : ", String.valueOf(hbRating));
//                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
//                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
//                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
//                Log.i("recDateTime : ", recDateTime);
//                Log.i("recTimestamp : ", recDateTime);

                OnlineUserVitalsModel onlineUserVitalsModel = new OnlineUserVitalsModel(userId, raspiUId, raspiId, profileId, hbRating, o2Saturation, bodyTemp, coughAnalysis, recDateTime, analysisResult);
                allVitalsList.add(onlineUserVitalsModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
//        sqLiteDatabase.close();
        return allVitalsList;
    }

    //    For Local Users
    public Cursor getCursorForRecyclerViewForNotInGroup(String groupUniqueId) {
        Log.i("Group Unique Id", groupUniqueId);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> userIds = getLocalUsersIdsForGroupList(groupUniqueId);

        sqLiteDatabase = this.getReadableDatabase();

        String inQueryList = "";

        for (String userId : userIds) {
            inQueryList = inQueryList + "'" + userId + "',";
        }
        if (inQueryList.length() > 0)
            inQueryList = inQueryList.substring(0, inQueryList.length() - 1);

        String queryString = "SELECT * FROM " + LocalUsers.LOCAL_USERS + " WHERE " + LocalUsers.LUID + " NOT IN (" + inQueryList + ") ORDER BY " + LocalUsers.DATE_ADDED + " DESC;";
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        return cursor;
    }

    public Cursor getCursorForRecyclerViewForGroup(String groupUniqueId) {
        Log.i("Group Unique Id", groupUniqueId);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> userIds = getLocalUsersIdsForGroupList(groupUniqueId);

        sqLiteDatabase = this.getReadableDatabase();

        String inQueryList = "";

        for (String userId : userIds) {
            inQueryList = inQueryList + "'" + userId + "',";
        }
        if (inQueryList.length() > 0)
            inQueryList = inQueryList.substring(0, inQueryList.length() - 1);

        String queryString = "SELECT * FROM " + LocalUsers.LOCAL_USERS + " WHERE " + LocalUsers.LUID + " IN (" + inQueryList + ") ORDER BY " + LocalUsers.DATE_ADDED + " DESC;";
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        return cursor;
    }

    public String getCountForRecyclerViewForGroup(String groupUniqueId) {
        ArrayList<String> userIds = getLocalUsersIdsForGroupList(groupUniqueId);

        String inQueryList = "";

        for (String userId : userIds) {
            inQueryList = inQueryList + "'" + userId + "',";
        }
        if (inQueryList.length() > 0)
            inQueryList = inQueryList.substring(0, inQueryList.length() - 1);

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + LocalUsers.LOCAL_USERS + " WHERE " + LocalUsers.LUID + " IN (" + inQueryList + ") ORDER BY " + LocalUsers.DATE_ADDED + " DESC;";
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        return String.valueOf(cursor.getCount());
    }

    public ArrayList<String> getExistingLocalUsersIdList() {
        ArrayList<String> allLocalUsersIdsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT " + LocalUsers.LUID + " FROM " + LocalUsers.LOCAL_USERS + " ;";

        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(0);

                Log.i("userId : ", userId);

                allLocalUsersIdsList.add(userId);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allLocalUsersIdsList;
    }

    public ArrayList<String> getLocalUsersIdsForGroupList(String groupUniqueId) {
        ArrayList<String> allLocalUsersIdsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT DISTINCT " + LocalUsersProfilesRelation.LUID + " FROM " + LocalUsersProfilesRelation.LOCAL_USERS_PROFILES + " WHERE " + LocalUsersData.GROUP_ID + " = '" + groupUniqueId + "';";

        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(0);

                Log.i("userId : ", userId);

                allLocalUsersIdsList.add(userId);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allLocalUsersIdsList;
    }

    public boolean addExistingUserLocal(String groupUniqueId, String localUserId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LocalUsersProfilesRelation.LUID, localUserId);
        contentValues.put(LocalUsersProfilesRelation.GROUP_ID, groupUniqueId);

        boolean returnVal;

        try {
            long insert = sqLiteDatabase.insert(LocalUsersProfilesRelation.LOCAL_USERS_PROFILES, null, contentValues);
            returnVal = insert != -1;
        } catch (Exception e) {
            Log.i("Exception while adding Local user data   ", e.toString());
            returnVal = false;
        }

        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean addNewUserLocal(String groupUniqueId, LocalUserModel LocalUserModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        com.google.firebase.Timestamp timestamp = LocalUserModel.getDateOfBirth();

        String timestampNow = String.valueOf(com.google.firebase.Timestamp.now().getSeconds());

        contentValues.put(LocalUsers.LUID, LocalUserModel.getLocalUserId());
        contentValues.put(LocalUsers.NAME, LocalUserModel.getName());
        contentValues.put(LocalUsers.DATE_OF_BIRTH, String.valueOf(timestamp.getSeconds()));
        contentValues.put(LocalUsers.GENDER, LocalUserModel.getGender());
        contentValues.put(LocalUsers.DATE_ADDED, timestampNow);

        boolean returnVal;

        try {
            long insert = sqLiteDatabase.insert(LocalUsers.LOCAL_USERS, null, contentValues);
            if (insert == -1) {
                returnVal = false;
            } else {
                contentValues = new ContentValues();

                contentValues.put(LocalUsersProfilesRelation.LUID, LocalUserModel.getLocalUserId());
                contentValues.put(LocalUsersProfilesRelation.GROUP_ID, groupUniqueId);

                try {
                    insert = sqLiteDatabase.insert(LocalUsersProfilesRelation.LOCAL_USERS_PROFILES, null, contentValues);
                    if (insert == -1) {
                        deleteUserLocal(LocalUserModel.getLocalUserId());
                        returnVal = false;
                    } else {
                        returnVal = true;
                    }
                } catch (Exception e) {
                    deleteUserLocal(LocalUserModel.getLocalUserId());
                    Log.i("Exception while adding Local user data   ", e.toString());
                    returnVal = false;
                }
            }
        } catch (Exception e) {
            Log.i("Exception while adding Local user data   ", e.toString());
            returnVal = false;
        }

        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean updateUserLocal(LocalUserModel LocalUserModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        com.google.firebase.Timestamp timestamp = LocalUserModel.getDateOfBirth();

        contentValues.put(LocalUsers.NAME, LocalUserModel.getName());
        contentValues.put(LocalUsers.DATE_OF_BIRTH, timestamp.getSeconds());
        contentValues.put(LocalUsers.GENDER, LocalUserModel.getGender());

        boolean returnVal;

        try {
            long insert = sqLiteDatabase.update(LocalUsers.LOCAL_USERS, contentValues, "LUID=?", new String[]{LocalUserModel.getLocalUserId()});
            returnVal = insert != -1;
        } catch (Exception e) {
            Log.i("Exception while updating local users data   ", e.toString());
            returnVal = false;
        }
        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteUserLocal(String userUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + LocalUsers.LOCAL_USERS + " WHERE " + LocalUsers.LUID + " = '" + userUniqueId + "'" + " ;";

        boolean returnVal;

        try {
            sqLiteDatabase.rawQuery(queryString, null);
            queryString = "DELETE FROM " + LocalUsersProfilesRelation.LOCAL_USERS_PROFILES + " WHERE " + LocalUsersProfilesRelation.LUID + " = '" + userUniqueId + "'" + " ;";
            returnVal = true;
        } catch (Exception e) {
            Log.i("Exception while deleting user   ", e.toString());
            returnVal = false;
        }
        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteUserLocalFromProfile(String userUniqueId, String groupUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + LocalUsersProfilesRelation.LOCAL_USERS_PROFILES + " WHERE " + LocalUsersProfilesRelation.LUID + " = '" + userUniqueId + "'";

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

    public boolean deleteAllUsersLocal() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        boolean returnVal;

        try {
            sqLiteDatabase.execSQL("delete from " + OnlineUsersData.ONLINE_USERS_DATA);
            returnVal = true;
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            returnVal = false;
        }
        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean addOneVitalsEntryLocal(LocalUserVitalsModel LocalUserVitalsModel, String uniqueGroupId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Log.i("Unique Group Id:", uniqueGroupId);

        contentValues.put(LocalUsersData.LUID, LocalUserVitalsModel.getUserId());
        contentValues.put(LocalUsersData.RASPI_ID, LocalUserVitalsModel.getRaspiId());
        contentValues.put(LocalUsersData.GROUP_ID, uniqueGroupId);
        contentValues.put(LocalUsersData.O_2_VAL, LocalUserVitalsModel.getO2Saturation());
        contentValues.put(LocalUsersData.HB_VAL, LocalUserVitalsModel.getHbRating());
        contentValues.put(LocalUsersData.TEMP_VAL, LocalUserVitalsModel.getBodyTemp());
        contentValues.put(LocalUsersData.COUGH_VAL, LocalUserVitalsModel.getCoughAnalysis());
        contentValues.put(LocalUsersData.DATE_REC, LocalUserVitalsModel.getRecDateTime());
        contentValues.put(LocalUsersData.ANALYSIS_RES, LocalUserVitalsModel.getAnalysisResult());

        boolean returnVal;

        try {
            long insert = sqLiteDatabase.insert(LocalUsersData.LOCAL_USERS_DATA, null, contentValues);
            sqLiteDatabase.close();
            returnVal = insert != -1;
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            returnVal = false;
        }

        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteAllUserVitalsLocal() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        boolean returnVal;
        try {
            sqLiteDatabase.execSQL("delete from " + LocalUsersData.LOCAL_USERS_DATA);
            returnVal = true;
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            returnVal = false;
        }

        sqLiteDatabase.close();
        return returnVal;
    }

    public boolean deleteUserVitalsLocal(String userUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "DELETE FROM " + LocalUsersData.LOCAL_USERS_DATA + " WHERE " + LocalUsersData.LUID + " = '" + userUniqueId + "'";

        try {
            sqLiteDatabase.rawQuery(queryString, null);
        } catch (Exception e) {
            Log.i("Exception while deleting user   ", e.toString());
            return false;
        }
        return true;
    }

    public boolean addLocalUserToGroup(String userUniqueId, String groupUniqueId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LocalUsersProfilesRelation.LUID, userUniqueId);
        contentValues.put(LocalUsersProfilesRelation.GROUP_ID, groupUniqueId);

        try {
            long insert = sqLiteDatabase.insert(LocalUsersProfilesRelation.LOCAL_USERS_PROFILES, null, contentValues);
            return insert != -1;
        } catch (Exception e) {
            Log.i("Exception while adding vitals data   ", e.toString());
            return false;
        }

    }

    public List<LocalUserVitalsModel> getAllVitalsListLocal() {
        List<LocalUserVitalsModel> allVitalsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + LocalUsersData.LOCAL_USERS_DATA;

        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(0);
                String raspiId = cursor.getString(1);
                String groupId = cursor.getString(2);
                float o2Saturation = cursor.getFloat(3);
                float hbRating = cursor.getFloat(4);
                float bodyTemp = cursor.getFloat(5);
                int coughAnalysis = cursor.getInt(6);
                String recDateTime = cursor.getString(7);
                String analysisResult = cursor.getString(8);

                Log.i("userId : ", userId);
                Log.i("raspiId : ", raspiId);
                Log.i("groupId : ", groupId);
                Log.i("hbRating : ", String.valueOf(hbRating));
                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
                Log.i("recDateTime : ", recDateTime);
                Log.i("recTimestamp : ", recDateTime);

                LocalUserVitalsModel localUserVitalsModel = new LocalUserVitalsModel(hbRating, o2Saturation, bodyTemp, coughAnalysis, raspiId, userId, analysisResult, Long.valueOf(recDateTime));
                allVitalsList.add(localUserVitalsModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allVitalsList;
    }

    public List<LocalUserVitalsModel> getVitalsForUserListLocal(String userUniqueId) {
        List<LocalUserVitalsModel> allVitalsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + LocalUsersData.LOCAL_USERS_DATA + " WHERE " + LocalUsersData.LUID + " = '" + userUniqueId + "'";

        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(0);
                String raspiId = cursor.getString(1);
                String groupId = cursor.getString(2);
                float o2Saturation = cursor.getFloat(3);
                float hbRating = cursor.getFloat(4);
                float bodyTemp = cursor.getFloat(5);
                int coughAnalysis = cursor.getInt(6);
                String recDateTime = cursor.getString(7);
                String analysisResult = cursor.getString(8);

//                Log.i("userId : ", userId);
//                Log.i("raspiId : ", raspiId);
//                Log.i("groupId : ", groupId);
//                Log.i("hbRating : ", String.valueOf(hbRating));
//                Log.i("o2Saturation : ", String.valueOf(o2Saturation));
//                Log.i("bodyTemp : ", String.valueOf(bodyTemp));
//                Log.i("coughAnalysis : ", String.valueOf(coughAnalysis));
//                Log.i("recDateTime : ", recDateTime);
//                Log.i("recTimestamp : ", recDateTime);

                LocalUserVitalsModel localUserVitalsModel = new LocalUserVitalsModel(hbRating, o2Saturation, bodyTemp, coughAnalysis, raspiId, userId, analysisResult, Long.valueOf(recDateTime));
                allVitalsList.add(localUserVitalsModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allVitalsList;
    }
}
