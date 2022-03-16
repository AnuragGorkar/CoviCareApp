package com.example.covicareapp.helpers;

import android.provider.BaseColumns;

public class VitalsContract {

    public static final class OnlineUsersData implements BaseColumns {
        public static final String ONLINE_USERS_DATA = "ONLINE_USERS_DATA";
        public static final String EMAIL_ID = "EMAIL_ID";
        public static final String RASPI_UID = "RASPI_UID";
        public static final String RASPI_ID = "RASPI_ID";
        public static final String GROUP_ID = "GROUP_ID";
        public static final String O_2_VAL = "O2_VAL";
        public static final String HB_VAL = "HB_VAL";
        public static final String TEMP_VAL = "TEMP_VAL";
        public static final String COUGH_VAL = "COUGH_VAL";
        public static final String DATE_REC = "DATE_REC";
        public static final String ANALYSIS_RES = "ANALYSIS_RES";
    }

    public static final class LocalUsers implements BaseColumns {
        public static final String LOCAL_USERS = "LOCAL_USERS";
        public static final String LUID = "LUID";
        public static final String NAME = "NAME";
        public static final String GENDER = "GENDER";
        public static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";
        public static final String DATE_ADDED = "DATE_ADDED";
    }

    public static final class LocalUsersData implements BaseColumns {
        public static final String LOCAL_USERS_DATA = "LOCAL_USERS_DATA";
        public static final String LUID = "LUID";
        public static final String RASPI_ID = "RASPI_ID";
        public static final String GROUP_ID = "GROUP_ID";
        public static final String O_2_VAL = "O2_VAL";
        public static final String HB_VAL = "HB_VAL";
        public static final String TEMP_VAL = "TEMP_VAL";
        public static final String COUGH_VAL = "COUGH_VAL";
        public static final String DATE_REC = "DATE_REC";
        public static final String ANALYSIS_RES = "ANALYSIS_RES";
    }

    public static final class LocalUsersProfilesRelation implements BaseColumns {
        public static final String LOCAL_USERS_PROFILES = "LOCAL_USERS_PROFILES";
        public static final String LUID = "LUID";
        public static final String GROUP_ID = "GROUP_ID";

    }

}
