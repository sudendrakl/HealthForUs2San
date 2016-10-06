package bizapps.com.healthforusDoc;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.healthforusDoc.activity.BaseActivity;
import bizapps.com.healthforusDoc.activity.ForgotPasswordActivity;
import bizapps.com.healthforusDoc.activity.LoginActivity;
import bizapps.com.healthforusDoc.activity.SchuduleActivity;
import bizapps.com.healthforusDoc.utills.ConnectivityReceiver;
import bizapps.com.healthforusDoc.utills.URLConstants;
import bizapps.com.healthforusDoc.utills.materialdatetimepicker.date.DatePickerDialog;

public class ScheduleVactionActivity extends BaseActivity
		implements OnClickListener, OnMenuItemClickListener, DatePickerDialog.OnDateSetListener {
	private ImageView backarrow, settingsIcon;
	private boolean isFromDate;
	private TextView select_from_date, select_to_date;
	private Button add_schedule_btn;
	private ProgressDialog pDialog;
	private String tag_json_obj = "json_obj_req";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_vaction);

		backarrow = (ImageView) findViewById(R.id.backarrow_icon);
		backarrow.setOnClickListener(this);
		settingsIcon = (ImageView) findViewById(R.id.settings_icon);
		settingsIcon.setOnClickListener(this);

		select_from_date = (TextView) findViewById(R.id.select_from_date);
		select_to_date = (TextView) findViewById(R.id.select_to_date);
		select_from_date.setOnClickListener(this);
		select_to_date.setOnClickListener(this);

		add_schedule_btn = (Button) findViewById(R.id.add_schedule_btn);
		add_schedule_btn.setOnClickListener(this);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.item_change_password:
//			Intent register = new Intent(this, ForgotPasswordActivity.class);
//			startActivity(register);
//			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//			return true;
		case R.id.item_schedule_timings:
			Intent scheduleIntentTimings = new Intent(this, SchuduleActivity.class);
			startActivity(scheduleIntentTimings);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			return true;
//		case R.id.item_schedule_date:
//			return true;
		case R.id.item_logout:
			getAppSharedPreference().setUserRemembered(false);
			Intent login = new Intent(this, LoginActivity.class);
			startActivity(login);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			finish();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backarrow_icon) {
			finish();
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
		} else if (v.getId() == R.id.settings_icon) {
			PopupMenu popupMenu = new PopupMenu(ScheduleVactionActivity.this, v);
			popupMenu.setOnMenuItemClickListener(ScheduleVactionActivity.this);
			popupMenu.inflate(R.menu.menu_item);
			popupMenu.show();
		} else if (v.getId() == R.id.select_from_date) {
			isFromDate = true;
			Calendar now = Calendar.getInstance();
			DatePickerDialog dpd = DatePickerDialog.newInstance(ScheduleVactionActivity.this, now.get(Calendar.YEAR),
					now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
			dpd.setThemeDark(true);
			dpd.vibrate(true);
			dpd.dismissOnPause(true);
			dpd.showYearPickerFirst(true);
			dpd.setAccentColor(Color.parseColor("#607D8B"));
			dpd.setMinDate(now);
			dpd.setTitle("From Date");
			Calendar[] datess = new Calendar[13];
			for (int i = -6; i <= 6; i++) {
				Calendar date = Calendar.getInstance();
				date.add(Calendar.WEEK_OF_YEAR, i);
				datess[i + 6] = date;
			}
			dpd.setHighlightedDays(datess);
			dpd.show(getFragmentManager(), "Datepickerdialog");
		} else if (v.getId() == R.id.select_to_date) {
			isFromDate = false;
			Calendar now = Calendar.getInstance();
			DatePickerDialog dpd = DatePickerDialog.newInstance(ScheduleVactionActivity.this, now.get(Calendar.YEAR),
					now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
			dpd.setThemeDark(true);
			dpd.vibrate(true);
			dpd.dismissOnPause(true);
			dpd.showYearPickerFirst(true);
			dpd.setAccentColor(Color.parseColor("#607D8B"));
			dpd.setMinDate(now);
			dpd.setTitle("To Date");
			Calendar[] datess = new Calendar[13];
			for (int i = -6; i <= 6; i++) {
				Calendar date = Calendar.getInstance();
				date.add(Calendar.WEEK_OF_YEAR, i);
				datess[i + 6] = date;
			}
			dpd.setHighlightedDays(datess);
			dpd.show(getFragmentManager(), "Datepickerdialog");
		} else if (v.getId() == R.id.add_schedule_btn) {
			if (ConnectivityReceiver.isConnected() && checkValidDates())
				doServiceRequest();
			else{
					Toast.makeText(getApplicationContext(), "Please check your internet conection!", Toast.LENGTH_SHORT)
							.show();
			}
		}
	}
	
	private boolean checkValidDates(){
		boolean isValid = true;
		if(select_from_date.getText().toString().equalsIgnoreCase("Start Date")
					|| select_to_date.getText().toString().equalsIgnoreCase("End Date")){
			Toast.makeText(getApplicationContext(), "Please select the dates", Toast.LENGTH_SHORT).show();
			return false;
		}
		return isValid;
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
		if (isFromDate) {
			select_from_date.setText(date);
		} else {
			String fromdate[] = select_from_date.getText().toString().split("/");
			if(Integer.parseInt(fromdate[2]) < year || (Integer.parseInt(fromdate[2]) == year || Integer.parseInt(fromdate[1]) < monthOfYear)
					|| (Integer.parseInt(fromdate[2]) == year || Integer.parseInt(fromdate[1]) == monthOfYear || Integer.parseInt(fromdate[0]) < dayOfMonth)
					|| (Integer.parseInt(fromdate[2]) == year || Integer.parseInt(fromdate[1]) == monthOfYear || Integer.parseInt(fromdate[0]) == dayOfMonth)){
				select_to_date.setText(date);
			} else 
				Toast.makeText(getApplicationContext(), "Make sure from date should be lesserthan to date", Toast.LENGTH_LONG).show();
		}
	}

	public void doServiceRequest() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.show();
		/* Post data */
		Map<String, String> jsonParams = new HashMap<String, String>();
		jsonParams.put("userid", getAppSharedPreference().getLoggedInUser().toString());
		jsonParams.put("date", select_from_date.getText().toString() + " - " + select_to_date.getText().toString());

		JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
				URLConstants.getDoctorCancelScheduleAppointmentUrl,

				new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response != null) {
								if (response.getString("status").equalsIgnoreCase("fail"))
									Toast.makeText(getApplicationContext(), response.getString("message"),
											Toast.LENGTH_SHORT).show();
								else {
									Toast.makeText(getApplicationContext(), "Scheduled Vaction are add successfully",
											Toast.LENGTH_SHORT).show();
									finish();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						pDialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "Something happend. Please try again...",
								Toast.LENGTH_SHORT).show();
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
}
