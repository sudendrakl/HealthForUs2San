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
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bizapps.com.healthforusDoc.BZAppApplication;
import bizapps.com.healthforusDoc.R;
import bizapps.com.healthforusDoc.utills.ConnectivityReceiver;
import bizapps.com.healthforusDoc.utills.URLConstants;

public class ForgotPasswordActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

	private EditText emailId, password, conformPassword, verifyPassword;
	private Button submitBtn;

	private JSONObject params;
	private String tag_json_obj = "json_obj_req";
	private ProgressDialog pDialog;
	private boolean isForgotPassword = true;
	private boolean isVerifyCode = false;
	private String Email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);

		emailId = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		conformPassword = (EditText) findViewById(R.id.conformpassword);
		verifyPassword = (EditText) findViewById(R.id.verificationCode);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		
		if(isForgotPassword){
			password.setVisibility(View.GONE);
			conformPassword.setVisibility(View.GONE);
			verifyPassword.setVisibility(View.GONE);
		}/* else {
			emailId.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			emailId.setHint("Old Password");
			password.setVisibility(View.VISIBLE);
			conformPassword.setVisibility(View.VISIBLE);
			verifyPassword.setVisibility(View.VISIBLE);
		}*/

		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (ConnectivityReceiver.isConnected())
					if(isForgotPassword && !isVerifyCode)
						sendForgotServerRequest();
					else if(isForgotPassword && isVerifyCode)
						setNewPasswordRequest();
				else
					Toast.makeText(getApplicationContext(), "Please check your internet conection!", Toast.LENGTH_SHORT)
							.show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		BZAppApplication.getInstance().setConnectivityListener(this);
	}

	private void setNewPasswordRequest() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.show();
		/* Post data */
		Map<String, String> jsonParams = new HashMap<String, String>();
		jsonParams.put("email", emailId.getText().toString().trim());
		jsonParams.put("otp", verifyPassword.getText().toString().trim());
		jsonParams.put("password", password.getText().toString().trim());

		JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
//				URLConstants.getDoctorForgotPasswordUrl,
				URLConstants.DR_BASE_URL + "doctorUpdatePassword.php",
				new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try{
							Log.e("Verify Response",""+response);
							if(response != null){
								if(response.getString("status").equalsIgnoreCase("fail"))
									Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
								else {
									Toast.makeText(getApplicationContext(), "Password Changed Successfully.", Toast.LENGTH_SHORT).show();
//									enableNewPasswordRequest();
									Intent LoginIntent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
									startActivity(LoginIntent);
									finish();
								}
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
		BZAppApplication.getInstance().addToRequestQueue(postRequest, tag_json_obj);
	}
	
	
	private void sendForgotServerRequest() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.show();
		/* Post data */
		Map<String, String> jsonParams = new HashMap<String, String>();
		Email = emailId.getText().toString().trim();
		jsonParams.put("email", Email);

		JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
//				URLConstants.getDoctorForgotPasswordUrl,
				URLConstants.DR_BASE_URL + "doctorForgotPassword.php",
				new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try{
							if(response != null){
								Log.i("Forgot Password: ", response.toString());
								if(response.getString("status").equalsIgnoreCase("fail"))
									Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
								else {
									Toast.makeText(getApplicationContext(), "verification code sent successfully.", Toast.LENGTH_SHORT).show();
									enableNewPasswordRequest();
								}
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

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {

	}
	
	public void enableNewPasswordRequest(){
		isVerifyCode = true;
		emailId.setInputType(InputType.TYPE_CLASS_TEXT);
		emailId.setHint("Verification Code");
		emailId.setEnabled(false);
		emailId.setClickable(false);
		password.setVisibility(View.VISIBLE);
		conformPassword.setVisibility(View.VISIBLE);
		verifyPassword.setVisibility(View.VISIBLE);
		submitBtn.setText("Submit");
	}
}
