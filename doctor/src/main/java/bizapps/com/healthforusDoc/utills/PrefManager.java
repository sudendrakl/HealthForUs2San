package bizapps.com.healthforusDoc.utills;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "HealthForUs";

    // All Shared Preferences Keys
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_TYPE = "isDoctor";
    private static final String KEY_IS_USER_VERFIED = "isUserVerified";
    
    private static final String KEY_EMAIL = "email";
    private static final String KEY_SPECIALIZATION = "specialization";
    private static final String KEY_ASSOCIATED_HOSPITAL = "associated_hospital";
    private static final String KEY_NAME = "name";
    private static final String KEY_OTHER_INFORMATION = "other_information";
    
    
    private static final String LOGGED_IN_USER = "logged_in_user";
    private static final String LOGGED_IN_PASSWORD = "logged_in_password";
    private static final String LOGGED_USER_IS_REMEMBERED = "is_remembered";
    
    private static final String LOGGED_USER_IS_PROFILE_IMAGE = "is_user_profile_image";



    public void setUserGuid(String guid){
        editor.putString(USER_GUID, guid);
        editor.commit();
    }
    private static final String USER_GUID = "guid";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    
    public void setLoggedInUserImage(String loggedInUser) {
        editor.putString(LOGGED_USER_IS_PROFILE_IMAGE, loggedInUser);
        editor.commit();
    }

    public String getUserGuid() {
        return pref.getString(USER_GUID, "");
    }

    public String getLoggedInUserImage() {
        return pref.getString(LOGGED_USER_IS_PROFILE_IMAGE, "");
    }

    public void setLoggedInUser(String loggedInUser) {
        editor.putString(LOGGED_IN_USER, loggedInUser);
        editor.commit();
    }

    public String getLoggedInUser() {
        return pref.getString(LOGGED_IN_USER, "");
    }
    
    public void setLoggedInPassword(String loggedInPassword) {
        editor.putString(LOGGED_IN_PASSWORD, loggedInPassword);
        editor.commit();
    }

    public String getLoggedInPassword() {
        return pref.getString(KEY_ASSOCIATED_HOSPITAL, "");
    }

    
    public void setUserRemembered(Boolean userVerified) {
        editor.putBoolean(LOGGED_USER_IS_REMEMBERED, userVerified);
        editor.commit();
    }

    public Boolean getUserRemembered() {
        return pref.getBoolean(LOGGED_USER_IS_REMEMBERED, false);
    }

    
    public void setUserVerified(Boolean userVerified) {
        editor.putBoolean(KEY_IS_USER_VERFIED, userVerified);
        editor.commit();
    }

    public Boolean getUserVerified() {
        return pref.getBoolean(KEY_IS_USER_VERFIED, false);
    }
    
    public void setSpecialization(String specialization) {
        editor.putString(KEY_SPECIALIZATION, specialization);
        editor.commit();
    }

    public String getSpecialization() {
        return pref.getString(KEY_SPECIALIZATION, "");
    }
    
    public void setAssociatedHospital(String associatedHospital) {
        editor.putString(KEY_ASSOCIATED_HOSPITAL, associatedHospital);
        editor.commit();
    }

    public String getAssociatedHospital() {
        return pref.getString(KEY_ASSOCIATED_HOSPITAL, "");
    }
    
    public void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public String getName() {
        return pref.getString(KEY_NAME, "");
    }
    
    public void setOtherInformation(String otherInformation) {
        editor.putString(KEY_OTHER_INFORMATION, otherInformation);
        editor.commit();
    }

    public String getOtherInformation() {
        return pref.getString(KEY_OTHER_INFORMATION, "");
    }
    
    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }
    
    public void setUserType(boolean userType) {
        editor.putBoolean(USER_TYPE, userType);
        editor.commit();
    }

    public boolean getUserType() {
        return pref.getBoolean(USER_TYPE, false);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, null);
    }
    
    public void clearMobileNumber(){
    	editor.putString(KEY_MOBILE_NUMBER, null);
        editor.commit();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

}
