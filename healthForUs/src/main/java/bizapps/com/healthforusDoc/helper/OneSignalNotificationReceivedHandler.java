package bizapps.com.healthforusDoc.helper;

import android.util.Log;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import org.json.JSONObject;

/**
 * Created by sudendra.kamble on 22/09/16.
 */
//when app is in bg
public class OneSignalNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
  static final private String TAG = OneSignalNotificationReceivedHandler.class.getSimpleName();

  @Override
  public void notificationReceived(OSNotification notification) {
    JSONObject data = notification.payload.additionalData;
    String customKey;

    Log.i(TAG, "notificationReceived() payload = " + (data != null ? data.toString() : null));

    if (data != null) {
      customKey = data.optString("customkey", null);
      if (customKey != null)
        Log.i("OneSignalExample", "customkey set with value: " + customKey);
    }

    //TODO: do something, app is already opened, redirect to proper screen or webpage


    //TODO intent.putExtra("KEY", "VALUE");
    //TODO broadcast message or have listener.received() interface thing

  }
}