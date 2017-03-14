package bizapps.com.healthforusDoc.helper;

import android.content.Intent;
import android.util.Log;
import bizapps.com.healthforusDoc.BZAppApplication;
import bizapps.com.healthforusDoc.activity.WebActivity;
import bizapps.com.healthforusDoc.utills.Constants;
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
    String url;

    Log.i(TAG, "notificationOpened() payload = " + (data != null ? data.toString() : null));
    if (data != null) {
      url = data.optString("url", null);//TODO: launch url... additional payload {'key':'url'}
      if (url != null) {
        Log.i("OneSignalExample", "customkey set with value: " + url);
        Intent intent = new Intent(BZAppApplication.getInstance(), WebActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Constants.IntentExtra.URL, url);
        intent.putExtra(Constants.IntentExtra.TITLE, data.optString("title"));
        BZAppApplication.getInstance().startActivity(intent);
      }
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