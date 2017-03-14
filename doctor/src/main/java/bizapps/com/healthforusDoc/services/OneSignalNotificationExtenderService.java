package bizapps.com.healthforusDoc.services;

import android.util.Log;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

/**
 * Created by sudendra.kamble on 22/09/16.
 */

public class OneSignalNotificationExtenderService extends NotificationExtenderService {
  static final private String TAG = OneSignalNotificationExtenderService.class.getSimpleName();
  @Override
  protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
    // Read properties from result.
    Log.d(TAG, receivedResult.toString());
    // Return true to stop the notification from displaying.
    return false;
  }
}