package bizapps.com.healthforusDoc;

import bizapps.com.healthforusDoc.helper.OneSignalNotificationOpenedHandler;
import bizapps.com.healthforusDoc.helper.OneSignalNotificationReceivedHandler;
import com.onesignal.OneSignal;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import bizapps.com.healthforusDoc.model.Appiontments;
import bizapps.com.healthforusDoc.utills.ConnectivityReceiver;
import bizapps.com.healthforusDoc.utills.LruBitmapCache;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class BZAppApplication extends Application {

  public static final String TAG = BZAppApplication.class.getSimpleName();

  private RequestQueue mRequestQueue;
  private ImageLoader mImageLoader;

  private static BZAppApplication mInstance;
  public static int selectedImage = 0;
  public static List<Appiontments> acceptedAppointmentsList;
  public static List<Appiontments> pendingAppointmentsList;
  public static List<Appiontments> canceledAppointmentsList;
  public static boolean isReqGPS;
  public static String mLocation;
  public static boolean certificate = false;
  public static HashMap<String, File> fileHashMap;
  private final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();

  @Override public void onCreate() {
    super.onCreate();
    mInstance = this;

    //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
    OneSignal.startInit(this)
        .setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler())
        .setNotificationReceivedHandler(new OneSignalNotificationReceivedHandler())
        .init();
  }

  public static synchronized BZAppApplication getInstance() {
    return mInstance;
  }

  public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
    ConnectivityReceiver.connectivityReceiverListener = listener;
  }

  public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    return mRequestQueue;
  }

  public ImageLoader getImageLoader() {
    getRequestQueue();
    if (mImageLoader == null) {
      mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
    }
    return this.mImageLoader;
  }

  public <T> void addToRequestQueue(Request<T> req, String tag) {
    // set the default tag if tag is empty
    Log.e("Request", "" + req);
    req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    getRequestQueue().add(req);
  }

  public <T> void addToRequestQueue(Request<T> req) {
    req.setTag(TAG);
    getRequestQueue().add(req);
  }

  public void cancelPendingRequests(Object tag) {
    if (mRequestQueue != null) {
      mRequestQueue.cancelAll(tag);
    }
  }

  public OkHttpClient getOkHttpClient() {
    return client;
  }
}
