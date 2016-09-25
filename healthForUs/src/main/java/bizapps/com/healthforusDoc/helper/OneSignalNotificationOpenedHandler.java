package bizapps.com.healthforusDoc.helper;

import android.util.Log;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import org.json.JSONObject;

/**
 * Created by sudendra.kamble on 22/09/16.
 */
//when app is opened
public class OneSignalNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
  static final private String TAG = OneSignalNotificationOpenedHandler.class.getSimpleName();

  // This fires when a notification is opened by tapping on it.
  @Override public void notificationOpened(OSNotificationOpenResult result) {
    OSNotificationAction.ActionType actionType = result.action.type;
    JSONObject data = result.notification.payload.additionalData;
    String customKey;

    Log.i(TAG, "notificationOpened() payload = " + (data != null ? data.toString() : null));
    if (data != null) {
      customKey = data.optString("customkey", null);
      if (customKey != null) Log.i("OneSignalExample", "customkey set with value: " + customKey);
    }

    if (actionType == OSNotificationAction.ActionType.ActionTaken) {
      Log.i(TAG, "Button pressed with id: " + result.action.actionID);
    }

    //TODO: do something, notification clicked
    // The following can be used to open an Activity of your choice.

    //TODO intent.putExtra("KEY", "VALUE");
    //TODO broadcast message or have listener.opened() interface thing
    // Follow the instructions in the link below to prevent the launcher Activity from starting.
    // https://documentation.onesignal.com/docs/android-notification-customizations#changing-the-open-action-of-a-notification
  }
}