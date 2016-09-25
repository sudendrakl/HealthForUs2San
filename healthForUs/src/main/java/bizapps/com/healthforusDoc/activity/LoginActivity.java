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
import bizapps.com.healthforusDoc.utills.URLConstants;

public class LoginActivity extends BaseActivity implements OnClickListener,  ConnectivityReceiver.ConnectivityReceiverListener {

	private Button login, register;
	private EditText userName, Pasword;
	private TextView forgotPassword;
	private ProgressDialog pDialog;
	private String tag_json_obj = "json_obj_req";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		login = (Button) findViewById(R.id.submitBtn);
		login.setOnClickListener(this);
		register = (Button) findViewById(R.id.registerBtn);
		register.setOnClickListener(this);
		userName = (EditText) findViewById(R.id.user_name);
		Pasword = (EditText) findViewById(R.id.password);
		forgotPassword = (TextView) findViewById(R.id.forgot_password);
		forgotPassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.submitBtn) {
			if (ConnectivityReceiver.isConnected())
				if(isValidData()){
					doServiceRequest();
				}
			else
				Toast.makeText(getApplicationContext(), "Please check your internet conection!", Toast.LENGTH_SHORT)
						.show();
		} else if (view.getId() == R.id.registerBtn) {
			Intent register = new Intent(this, RegisterActivity.class);
			register.putExtra("isRegister", true);
			startActivity(register);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		} else if (view.getId() == R.id.forgot_password) {
			Intent register = new Intent(this, ForgotPasswordActivity.class);
			startActivity(register);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		}
	}
	
	private void doServiceRequest() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.show();
		/* Post data */
		Map<String, String> jsonParams = new HashMap<String, String>();
		jsonParams.put("userid", userName.getText().toString().trim());
		jsonParams.put("password", Pasword.getText().toString().trim());

		JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
//				URLConstants.getDoctorLoginUrl,
				"http://sundareshln.com/test/login.php",
				new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try{
							Log.e("Login Response",""+response);
							if(response != null){
								if(response.getString("status").equalsIgnoreCase("fail"))
									Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
								else {
									getAppSharedPreference().setUserGuid(response.getString("userid"));
									getAppSharedPreference().setName(response.getString("username"));
									doLogin();
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

	protected void doLogin() {
		getAppSharedPreference().setLoggedInUser(userName.getText().toString().trim());
		getAppSharedPreference().setLoggedInPassword(Pasword.getText().toString().trim());
		getAppSharedPreference().setUserRemembered(true);
		Log.e("Userid",getAppSharedPreference().getUserGuid());
		Intent register = new Intent(this, DashboardActivity.class);
		startActivity(register);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {

	}
	
	public boolean isValidData(){
		boolean isValid = true;
		if(userName.getText().toString().trim().equalsIgnoreCase("")){
			isValid = false;
			userName.setError("Empty Field");
		}
		
		if(Pasword.getText().toString().trim().equalsIgnoreCase("")){
			isValid = false;
			Pasword.setError("Empty Field");
		} 
		return isValid;
	}
}
