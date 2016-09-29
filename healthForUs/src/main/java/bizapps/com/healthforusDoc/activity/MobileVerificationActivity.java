package bizapps.com.healthforusDoc.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.healthforusDoc.BZAppApplication;
import bizapps.com.healthforusDoc.R;
import bizapps.com.healthforusDoc.utills.ConnectivityReceiver;
import bizapps.com.healthforusDoc.utills.PrefManager;
import bizapps.com.healthforusDoc.utills.URLConstants;

public class MobileVerificationActivity extends BaseActivity {

	private EditText mobileVerification;
	private TextView headerType;
	private Typeface custom_font;
	private Button submit, resendVerify;
	private PrefManager pref;
	
	private JSONObject params;
	private String tag_json_obj = "json_obj_req";
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_verification);

		pref = getAppSharedPreference();

		mobileVerification = (EditText) findViewById(R.id.mobile_verification_layout);
		headerType = (TextView) findViewById(R.id.header_text);
		custom_font = Typeface.createFromAsset(getAssets(), "fonts/KirstyBold.ttf");
		headerType.setTypeface(custom_font);
		headerType.setText("User Verification");

		submit = (Button) findViewById(R.id.submit_btn);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (ConnectivityReceiver.isConnected())
					doVerificationServiceRequest();
				else
					Toast.makeText(getApplicationContext(), "Please check your internet conection!", Toast.LENGTH_SHORT)
							.show();
			}
		});
		
		resendVerify = (Button) findViewById(R.id.verif_btn);
		resendVerify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (ConnectivityReceiver.isConnected())
					doResendVerificationServiceRequest();
				else
					Toast.makeText(getApplicationContext(), "Please check your internet conection!", Toast.LENGTH_SHORT)
							.show();
			}
		});
	}
	
	private void doVerificationServiceRequest(){
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.show();
		/* Post data */
		Map<String, String> jsonParams = new HashMap<String, String>();
		jsonParams.put("email", pref.getEmail());
		jsonParams.put("passcode", mobileVerification.getText().toString().trim());

		JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
//				URLConstants.getDoctormobileverficationUrl,
				URLConstants.DR_BASE_URL + "passcodeVerification.php",
				new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try{
							Log.e("Verification Response",""+response);
							if(response != null){
								if(response.getString("status").equalsIgnoreCase("fail"))
									Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
								else
									moveToNext();
							}
						} catch(JSONException e){
							e.printStackTrace();
						}
						
						pDialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "Something happend. Please try again...", Toast.LENGTH_SHORT).show();
						pDialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				headers.put("User-agent", System.getProperty("http.agent"));
				return headers;
			}
		};
		postRequest.setRetryPolicy(new DefaultRetryPolicy(
				100000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		BZAppApplication.getInstance().addToRequestQueue(postRequest, tag_json_obj);
	}
	
	private void doResendVerificationServiceRequest(){
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.show();
		/* Post data */
		Map<String, String> jsonParams = new HashMap<String, String>();
		jsonParams.put("email", pref.getEmail());

		JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URLConstants.getDoctorResendMobileCodeUrl,

				new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try{
							if(response != null){
								if(response.getString("status").equalsIgnoreCase("fail"))
									Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
								else
									Toast.makeText(getApplicationContext(), "Verification Code sent successfully.", Toast.LENGTH_SHORT).show();
							}
						} catch(JSONException e){
							e.printStackTrace();
						}
						
						pDialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "Something happend. Please try again...", Toast.LENGTH_SHORT).show();
						pDialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json; charset=utf-8");
				headers.put("User-agent", System.getProperty("http.agent"));
				return headers;
			}
		};
		postRequest.setRetryPolicy(new DefaultRetryPolicy(
				100000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		BZAppApplication.getInstance().addToRequestQueue(postRequest, tag_json_obj);
	}

	protected void moveToNext() {
		pref.setUserVerified(true);
		Intent verify = new Intent(this, LoginActivity.class);
		startActivity(verify);
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
		finish();
	}
}
