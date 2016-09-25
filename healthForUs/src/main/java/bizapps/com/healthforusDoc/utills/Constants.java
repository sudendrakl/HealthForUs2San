package bizapps.com.healthforusDoc.utills;

/**
 * Created by sudendra.kamble on 22/09/16.
 */

public interface Constants {
  public interface IntentExtra {
    String FORM_BODY = "form_body";
    String URL = "url";
    String AUTH_TOKEN = "auth_token";
    String GCM_KEY = "gck_key";
  }

  interface URLS {
    String REGISTER_URL = "bizapps/api/register";
    String UPDATE_GCM_KEY_URL = "bizapps/api/onesignal/createApp/android";
  }
}
