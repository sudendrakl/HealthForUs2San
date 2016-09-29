package bizapps.com.healthforusDoc.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import bizapps.com.healthforusDoc.BuildConfig;
import bizapps.com.healthforusDoc.R;
import bizapps.com.healthforusDoc.data.GcmUpdateResponseDto;
import bizapps.com.healthforusDoc.data.TokenResponseDto;
import bizapps.com.healthforusDoc.utills.Constants;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

/**
 * Created by sudendra.kamble on 22/09/16.
 */

public class RegistrationService extends IntentService {
  static final private String TAG = RegistrationService.class.getSimpleName();
  public static final MediaType MEDIA_TYPE_MARKDOWN =
      MediaType.parse("text/x-markdown; charset=utf-8");

  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson = new Gson();

  public RegistrationService() {
    super("RegistrationService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    //assuming all are post api's....please handle for get or put
    String gcmKey = intent.getStringExtra(Constants.IntentExtra.GCM_KEY);

    try {
      String authToken = fetchTokenAndRegister();
      System.out.println("shit --- authToken: " + authToken);
      updateTokenToServer(authToken, gcmKey);

      //TODO: update UI to continue...throw a broadcast msg to MainActivity
    } catch (Exception e) {
      //TODO: failed to send data
      e.printStackTrace();
    }
  }

  private String fetchTokenAndRegister() throws Exception {
    String url = BuildConfig.BASE_URL + Constants.URLS.REGISTER_URL;

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("app_name", getAppName());
    String postParams = jsonObject.toString();


    TokenResponseDto response = sendRequest(url, null, postParams, TokenResponseDto.class);

    if(response.isSuccess()) {
      //TODO: success
      return response.getToken();
    } else {
      //TODO: failure
      throw new Exception("Failed to get token. Message : " + response.getMessage() + " Code : " + response.getCode());
    }

  }

  private void updateTokenToServer(String token, String gcmKey) throws Exception {

    String url = BuildConfig.BASE_URL + Constants.URLS.UPDATE_GCM_KEY_URL;

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("app_name", getAppName());
    jsonObject.put("gcm_key", gcmKey);
    String params = jsonObject.toString();

    HashMap<String, String> headerMap = new HashMap<>();
    headerMap.put("Authorization", token);
    headerMap.put("Content-Type", "application/json");
    Headers headers  = Headers.of(headerMap);

    GcmUpdateResponseDto response = sendRequest(url, headers, params, GcmUpdateResponseDto.class);

    System.out.println("shit updateTokenToServer()...response" + response.isStatus() + " ===== " + response.toString());

    if(response.isStatus()) {
      //TODO: success
      Log.i(TAG, "success ... " + response.getResponse());
    } else {
      //TODO: failure
      throw new Exception("Failed to get token. Message : " + response.getError() + " Code : " + response.getCode());
    }
  }

  private <T> T sendRequest(String url, Headers headers, String postParams, Class<T> clazz) throws IOException {
    Request request;
    if (headers != null) request = new Request.Builder().url(url)
        .headers(headers)
        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postParams))
        .build();
    else request = new Request.Builder().url(url)
        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postParams))
        .build();

    Response response = client.newCall(request).execute();
    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

    Log.i(TAG, response.body().string());

    T responseParse = gson.fromJson(response.body().charStream(), clazz);
    //TODO: updated response
    Log.i(TAG, response.toString());

    return responseParse;
  }

  public String getAppName() {
    return getString(R.string.app_name);
  }
}
