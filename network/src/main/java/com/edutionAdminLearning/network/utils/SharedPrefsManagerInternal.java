package com.edutionAdminLearning.network.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.edutionAdminLearning.network.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SharedPrefsManagerInternal {


    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private boolean mBulkUpdate = false;
    private final String SPName = "NETWORK_SP";

    public SharedPrefsManagerInternal(Context appContext) {
        mPref = appContext.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    public SharedPrefsManagerInternal(Context appContext, String spName) {
        mPref = appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    public void putInt(String key, int val) {
        doEdit();
        mEditor.putInt(key, val);
        doCommit();
    }

    public void putString(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommit();

    }

    public void putStringSync(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommitSync();
    }

    public void putStringSet(String key, Set<String> stringSet) {
        doEdit();
        mEditor.putStringSet(key, stringSet);
        doCommit();
    }

    public void putBoolean(String key, Boolean val) {
        doEdit();
        mEditor.putBoolean(key, val != null && val);
        doCommit();
    }

    public void putLong(String key, Long val) {
        doEdit();
        mEditor.putLong(key, val == null ? 0L : val);
        doCommit();
    }


    public void put(Key key, String val) {
        doEdit();
        mEditor.putString(key.name(), val);
        doCommit();
    }

    public void putSync(Key key, String val) {
        doEdit();
        mEditor.putString(key.name(), val);
        doCommitSync();
    }

    public void put(Key key, int val) {
        doEdit();
        mEditor.putInt(key.name(), val);
        doCommit();
    }

    public void put(Key key, boolean val) {
        doEdit();
        mEditor.putBoolean(key.name(), val);
        doCommit();
    }

    public void put(Key key, float val) {
        doEdit();
        mEditor.putFloat(key.name(), val);
        doCommit();
    }

    public void put(Key key, double val) {
        doEdit();
        mEditor.putString(key.name(), String.valueOf(val));
        doCommit();
    }

    public void put(Key key, long val) {
        doEdit();
        mEditor.putLong(key.name(), val);
        doCommit();
    }

    public String getString(String key) {
        return mPref.getString(key, "");
    }

    public Set<String> getStringSet(String key) {
        return mPref.getStringSet(key, null);
    }

    public Long getLong(String key) {
        return mPref.getLong(key, 0L);
    }

    public String getMyKey(String endPoint) {
        String key = endPoint;
        Map<String, ?> allEntries = mPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains(endPoint)) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    public void removeAllApiCache() {
        Map<String, ?> allEntries = mPref.getAll();
        List<String> cacheKeys = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (!TextUtils.isEmpty(key) && key.contains(BuildConfig.BASE_URL)) {
                cacheKeys.add(key);
            }
        }
        remove(cacheKeys);

    }

    public Boolean getBoolean(String key) {
        return mPref.getBoolean(key, true);
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    public String getString(Key key, String defaultValue) {
        return mPref.getString(key.name(), defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public String getString(Key key) {
        return mPref.getString(key.name(), "");
    }

    public Boolean isKeyExist(String key) {
        return mPref.contains(key);
    }


    public int getInt(Key key) {
        int i;
        try {
            i = mPref.getInt(key.name(), 0);
        } catch (ClassCastException e) {
            i = Integer.parseInt(mPref.getString(key.name(), "0"));

        }
        return i;
    }

    public int getInt(Key key, int defaultValue) {
        return mPref.getInt(key.name(), defaultValue);
    }

    public long getLong(Key key) {
        long l;
        try {
            l = mPref.getLong(key.name(), 0);
        } catch (ClassCastException ex) {
            l = Long.parseLong(mPref.getString(key.name(), "0L"));
        }
        return l;
    }

    public long getLong(Key key, long defaultValue) {
        long l;
        try {
            l = mPref.getLong(key.name(), 0);
        } catch (ClassCastException ex) {
            l = Long.parseLong(mPref.getString(key.name(), String.valueOf(defaultValue)));
        }
        return l;
    }

    public float getFloat(Key key) {
        float f = 0f;
        try {
            f = mPref.getFloat(key.name(), 0);
        } catch (ClassCastException ex) {

            Map<String, ?> all = mPref.getAll();
            if (all.get("key_name") instanceof String) {
                f = Float.parseFloat(mPref.getString(key.name(), "0.0"));
            } else if (all.get("key_name") instanceof Integer) {
                f = (float) mPref.getInt(key.name(), 0);
            }
        }
        return f;
    }

    public float getFloat(Key key, float defaultValue) {

        return mPref.getFloat(key.name(), defaultValue);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The enum of the preference to fetch.
     */
    public double getDouble(Key key) {
        return getDouble(key, 0);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The enum of the preference to fetch.
     */
    public double getDouble(Key key, double defaultValue) {
        try {
            return Double.valueOf(mPref.getString(key.name(), String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public boolean getBoolean(Key key, boolean defaultValue) {
        return mPref.getBoolean(key.name(), defaultValue);
    }

    public boolean getBoolean(Key key) {
        boolean bool;
        try {
            bool = mPref.getBoolean(key.name(), false);
        } catch (Exception e) {
            bool = Boolean.parseBoolean(mPref.getString(key.name(), "false"));
        }
        return bool;
    }

    /**
     * Remove keys from SharedPreferences.
     *
     * @param keys The enum of the key(s) to be removed.
     */
    public void remove(List<String> keys) {
        doEdit();
        for (String key : keys) {
            mEditor.remove(key);
        }
        doCommit();
    }

    public void remove(Key key) {
        doEdit();
        if (mEditor != null && key != null && !key.name().isEmpty()) {
            mEditor.remove(key.name());
        }
        doCommit();
    }

    public void remove(String key) {
        doEdit();
        if (mEditor != null && key != null && !key.isEmpty()) {
            mEditor.remove(key);
        }
        doCommit();
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    // TODO : remove edit as it is not used
    public void edit() {
        mBulkUpdate = true;
        mEditor = mPref.edit();
    }

    public void commit() {
        mBulkUpdate = false;
        mEditor.commit();
        mEditor = null;
    }

    private void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.apply();
            mEditor = null;
        }
    }

    private void doCommitSync() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }


    public enum Key {
        ORGANISATION_ID("ORGANISATION_ID"),
        FCM_TOKEN("FCM_TOKEN"),
        FCM_INSTANCE_ID("FCM_INSTANCE_ID"),
        AUTH_TOKEN("AUTH_TOKEN"),
        PURCHASED_ANY_PRODUCT("IS_PURCHASED_ANY_PRODUCT"),
        REFRESH_TOKEN("REFRESH_TOKEN"),
        USER_ID("USER_ID"),
        USER_MOBILE("USER_MOBILE"),
        USER_COUNTRY_CODE("USER_COUNTRY_CODE"),
        USER_IMAGE("USER_IMAGE"),
        GUARDIAN_MOBILE("GUARDIAN_MOBILE"),
        USER_FIRST_NAME("USER_FIRST_NAME"),
        USER_NAME("USER_NAME"),
        USER_LAST_NAME("USER_LAST_NAME"),
        ADDRESS_LINE_1("ADDRESS_LINE_1"),
        ADDRESS_LINE_2("ADDRESS_LINE_2"),
        ALTERNATE_NUMBER("ALTERNATE_NUMBER"),
        RECIPIENT_NAME("RECIPIENT_NAME"),
        USER_LOCALITY("USER_LOCALITY"),
        USER_LANDMARK("USER_LANDMARK"),
        USER_DISTRICT("USER_DISTRICT"),
        USER_STATE("USER_STATE"),
        USER_PINCODE("USER_PINCODE"),
        USER_EMAIL("USER_EMAIL"),
        UNIQUE_KEY("UNIQUE_KEY"),
        USER_GENDER("USER_GENDER"),
        USER_CITY("USER_CITY"),
        USER_CLASS("USER_CLASS"),
        USER_LANGUAGE("USER_LANGUAGE"),
        USER_EXAM("USER_EXAM"),
        USER_STREAM("USER_STREAM"),
        USER_PROGRAM_NAME("USER_PROGRAM_NAME"),
        USER_BOARD("USER_BOARD"),
        USER_REFERRAL_CODE("USER_REFERRAL_CODE"),
        USER_PASSWORD("USER_PASSWORD"),
        ALL_EXAMS("ALL_EXAMS"),
        BATCH_FILTER("BATCH_FILTER"),
        SECURE_DOWNLOAD("SECURE_DOWNLOAD"),
        USER_CURRENT_WALLET("USER_CURRENT_WALLET"),
        USER_TOTAL_WALLET("USER_TOTAL_WALLET"),
        SHOW_OPTION("SHOW_OPTION"),
        COURSE_DATA("COURSE_DATA"),
        BATCH_ID("BATCH_ID"),
        BATCH_SUBJECT_ID("BATCH_SUBJECT_ID"),
        SCHEDULE_ID("SCHEDULE_ID"),
        PROFILE_POINTS("PROFILE_POINTS"),
        PROFILE_COMPLETED("PROFILE_COMPLETED"),
        IS_TEST_RESTART_NEEDED("IS_TEST_RESTART_NEEDED"),
        DEEP_DATA("DEEP_DATA"),
        ADD_ON_DATA("ADD_ON_DATA"),
        VIDEO_SOL_PROGRAM_ID("VIDEO_SOL_PROGRAM_ID"),
        PROGRAM_ID("PROGRAM_ID"),
        USER_QUESTION("USER_QUESTION"),
        TEST_CATEGORY_DATA("TEST_CATEGORY_DATA"),
        Q_BANK_CATEGORY_DATA("Q_BANK_CATEGORY_DATA"),
        TEST_ENTRY_POINT("TEST_ENTRY_POINT"),
        TEST_IS_FROM_NEW_TEST_SERIES("TEST_IS_FROM_NEW_TEST_SERIES"),
        QUIZ_ENTRY_POINT("QUIZ_ENTRY_POINT"),
        BATCH_CREDENTIAL("BATCH_CREDENTIAL"),
        COURSE_CREDENTIAL("COURSE_CREDENTIAL"),
        FIREBASE_TYPE("FIREBASE_TYPE"),
        FIREBASE_DATA("FIREBASE_DATA"),
        OTP("OTP"),
        LIVE_VIDEO_URL("LIVE_VIDEO_URL"),
        SHOW_VIDEO_QUALITY("SHOW_VIDEO_QUALITY"),
        CLIENT_VERSION("CLIENT_VERSION"),
        SET_PROFILE("SET_PROFILE"),
        BOOKMARKED_VIDEO_IDS("BOOKMARKED_VIDEO_IDS"),
        WATCH_TIMES("WATCH_TIMES"),
        BATCH_OVERVIEW_DATA("BATCH_OVERVIEW_DATA"),
        SUBJECT_DATA("SUBJECT_DATA"),
        TOPIC_DATA("TOPIC_DATA"),
        TOPIC_CONTENT_REQUIREMENTS("TOPIC_CONTENT_REQUIREMENTS"),
        QUIZ_REQUIREMENTS("QUIZ_REQUIREMENTS"),
        COURSE_NAME("COURSE_NAME"),
        DEMO_VIDEO_PLAY_COUNT("DEMO_VIDEO_PLAY_COUNT"),
        SHOW_QBANK("SHOW_QBANK"),
        FROM_PAYMENT_SUCCESSFUL("FROM_PAYMENT_SUCCESSFUL"),
        SHOW_RATING("SHOW_RATING"),
        SHOW_COMMUNITY_TAB("SHOW_COMMUNITY_TAB"),
        SHOW_RECOMMENDED_BATCH("SHOW_RECOMMENDED_BATCH"),
        TEST_ID("TEST_ID"),
        AWS_COOKIE("AWS_COOKIE"),
        TEST_EGISTRATION_FORM("TEST_EGISTRATION_FORM"),
        SHOW_REFER("SHOW_REFER"),
        LEAD_SQUARD_ID("LEAD_SQUARD_ID"),
        VIDEO_REPORT_BATCH_NAME("VIDEO_REPORT_BATCH_NAME"),
        VIDEO_REPORT_SUBJECT_NAME("VIDEO_REPORT_SUBJECT_NAME"),
        SHARED_REFERRAL_CODE("SHARED_REFERRAL_CODE"),
        VIDEO_REPORT_CHAPTER_NAME("VIDEO_REPORT_CHAPTER_NAME"),
        VIDEO_REPORT_TEST_ID("VIDEO_REPORT_TEST_ID"),
        VIDEO_REPORT_TEST_NAME("VIDEO_REPORT_TEST_NAME"),
        VIDEO_REPORT_TEST_SERIES_NAME("VIDEO_REPORT_TEST_SERIES_NAME"),
        VIDEO_REPORT_TEST_SECTION_NAME("VIDEO_REPORT_TEST_SECTION_NAME"),
        VIDEO_REPORT_QUESTION_ID("VIDEO_REPORT_QUESTION_ID"),
        VIDEO_SOLUTION_QUESTION_ID("VIDEO_SOLUTION_QUESTION_ID"),
        SELECTED_SUBJECT("SELECTED_SUBJECT"),
        LEAD_ID("LEAD_ID"),
        ADDRESS_CHANGE("ADDRESS_CHANGE"),
        PROGRAM_NAME("PROGRAM_NAME"),
        PROGRAM_SUBJECT("PROGRAM_SUBJECT"),
        VIDEO_REPORT_QUESTION_NUMBER("VIDEO_REPORT_QUESTION_NUMBER"),
        VIDEO_REPORT_TYPE("VIDEO_REPORT_TYPE"),
        PURCHASED_BOOK_RESPONSE("PURCHASED_BOOK_RESPONSE"),
        REQUEST_ID("REQUEST_ID"),
        PROGRAM_LANGUAGE("PROGRAM_LANGUAGE"),
        PROGRAM_EXAM("PROGRAM_EXAM"),
        PROGRAM_CLASS("PROGRAM_CLASS"),
        SEEK_TO("SEEK_TO"),
        SLIDE_DATA("SLIDE_DATA"),
        IS_LIVE("IS_LIVE"),
        PATH_NAME("PATH_NAME"),
        PATH_FNAME("PATH_FNAME"),
        PATH_EMAIL("PATH_EMAIL"),
        PATH_NUMBER("PATH_NUMBER"),
        PATH_GENDER("PATH_GENDER"),
        PATH_ALTERNATE("PATH_ALTERNATE"),
        PATH_CLASS("PATH_CLASS"),
        PATH_DEPARTMENT("PATH_DEPARTMENT"),
        LOCAL_VIDEO_STATS("LOCAL_VIDEO_STATS"),
        PATH_CENTER("PATH_CENTER"),
        CHAT_ACCESS_BLOCKED("CHAT_ACCESS_BLOCKED"),
        TODAY_CLASS_SELECTED_BATCH("TODAY_CLASS_SELECTED_BATCH"),
        POLL_DATA("POLL_DATA"),
        IS_VIDEO_LOADED("IS_VIDEO_LOADED"),
        PATHSHALA_DOUBT_ENABLED("PATHSHALA_DOUBT_ENABLED"),
        PATHSHALA_VOTE("PATHSHALA_VOTE"),
        PATHSHALA_POLL_DATA("PATHSHALA_POLL_DATA"),
        PATHSHALA_SCHEDULE("PATHSHALA_SCHEDULE"),
        APP_DATA_DATE("APP_DATA_DATE"),
        IS_LOG_DATA("IS_LOG_DATA"),
        BATCH_USER_SEGMENT("BATCH_USER_SEGMENT"),
        LANDING_PAGE("LANDING_PAGE"),
        SERVER_RETRY_TIME("SERVER_RETRY_TIME"),
        DOWNLOADED_PDF("DOWNLOADED_PDF"),
        SERVER_RETRY_COUNT("SERVER_RETRY_COUNT"),
        USER_CREATED_DATE("USER_CREATED_DATE"),
        JOINED_LIVE_CLASS_IDS("JOINED_LIVE_CLASS_IDS"),
        POLL_RESULT_DATA("POLL_RESULT_DATA"),
        PDF_STATS_DATA("PDF_STATS_DATA"),
        USER_ROLES("USER_ROLES"),
        KHAZANA_NEW_TAG_VISIBILITY("KHAZANA_NEW_TAG_VISIBILITY"),
        MY_DOUBT_BATCH("MY_DOUBT_BATCH"),
        MY_DOUBT_BATCH_NAME("MY_DOUBT_BATCH_NAME"),
        DOUBT_STATUS("DOUBT_STATUS"),
        BATCH_EXAM_DATA("BATCH_EXAM_DATA"),
        CONTINUE_LEARNING_TYPE_ID("CONTINUE_LEARNING_TYPE_ID"),
        CONTINUE_LEARNING_LAST_WATCH("CONTINUE_LEARNING_LAST_WATCH"),
        DATA_USAGE_TIMESTAMP("DATA_USAGE_TIME"),
        COHORT_ID("COHORT_ID"),
        COHORT_CONFIG("COHORT_CONFIG"),
        COHORT_UPDATE("COHORT_UPDATE"),
        COHORT_PROFILE("COHORT_PROFILE"),
        PW_STORE_ANIMATION("PW_STORE_ANIMATION"),
        TEST_SERIES_ANIMATION("TEST_SERIES_ANIMATION"),
        K8_VIDEO_COUNT("K8_VIDEO_COUNT"),
        K8_BATCH_DETAILS("K8_BATCH_DETAILS"),
        K8_BATCH_DATA("K8_BATCH_DATA"),
        FIREBASE_TOKEN_TO_FRESHCHAT("FIREBASE_TOKEN_TO_FRESHCHAT"),
        SME_ST_MASTER_BANNER("SME_ST_MASTER_BANNER"),
        USER_PLACED_REQUEST_A_CALL("USER_PLACED_REQUEST_A_CALL"),
        VIDEO_NOTIFICATION_NAME("VIDEO_NOTIFICATION_NAME"),
        IS_NAVIGATED_FROM_TEST("IS_NAVIGATED_FROM_TEST");


        private final String value;

        Key(String value) {
            this.value = value;
        }

        public static Key fromString(String text) {
            for (Key b : Key.values()) {
                if (b.value.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }

        public String getPropertyName() {
            return value;
        }
    }


}
