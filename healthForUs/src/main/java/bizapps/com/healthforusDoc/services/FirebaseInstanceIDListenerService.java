package bizapps.com.healthforusDoc.services;

import android.content.Intent;
import android.util.Log;
import bizapps.com.healthforusDoc.utills.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by sudendra.kamble on 22/09/16.
 */
public class FirebaseInstanceIDListenerService  extends FirebaseInstanceIdService {

  static final private String TAG = FirebaseInstanceIDListenerService.class.getSimpleName();
  /**
   * Called if InstanceID token is updated. This may occur if the security of
   * the previous token had been compromised. Note that this is also called
   * when the InstanceID token is initially generated, so this is where
   * you retrieve the token.
   */
  // [START refresh_token]
  @Override
  public void onTokenRefresh() {
    // Get updated InstanceID token.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);
    // TODO: Implement this method to send any registration to your app's servers.
    // send this to server sendRegistrationToServer(refreshedToken);
    Intent intent = new Intent(this, RegistrationService.class);
    //intent.putExtra(Constants.IntentExtra.AUTH_TOKEN, auth_token); //TODO auth token got via app registration... not sure why its another api
    intent.putExtra(Constants.IntentExtra.GCM_KEY, refreshedToken);
    startService(intent);
  }

}