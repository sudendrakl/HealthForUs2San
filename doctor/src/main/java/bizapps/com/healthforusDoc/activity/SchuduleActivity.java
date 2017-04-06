package bizapps.com.healthforusDoc.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.healthforusDoc.BZAppApplication;
import bizapps.com.healthforusDoc.R;
import bizapps.com.healthforusDoc.ScheduleVactionActivity;
import bizapps.com.healthforusDoc.utills.ConnectivityReceiver;
import bizapps.com.healthforusDoc.utills.URLConstants;
import bizapps.com.healthforusDoc.utills.materialdatetimepicker.time.RadialPickerLayout;
import bizapps.com.healthforusDoc.utills.materialdatetimepicker.time.TimePickerDialog;

public class SchuduleActivity extends BaseActivity
		implements OnClickListener, OnMenuItemClickListener, TimePickerDialog.OnTimeSetListener {

	private ImageView backarrow, settingsIcon;
	private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday, select_from_time, select_to_time, timings_text;
	private boolean isSunday, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSlectFromTime;
	private List<String> timeList;
	private Button addBtn, scheduleBtn;
	private ProgressDialog pDialog;
	private String tag_json_obj = "json_obj_req";
	public StringBuilder mon_start,mon_end,tue_start,tue_end,wed_start,wed_end,thu_start,thu_end,fri_start,fri_end,sat_start,sat_end,
	sun_start,sun_end;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schudule);
		mon_start = new StringBuilder();
		mon_end = new StringBuilder();
		tue_start = new StringBuilder();
		tue_end = new StringBuilder();
		wed_start = new StringBuilder();
		wed_end = new StringBuilder();
		thu_start = new StringBuilder();
		thu_end = new StringBuilder();
		fri_start = new StringBuilder();
		fri_end = new StringBuilder();
		sat_start = new StringBuilder();
		sat_end = new StringBuilder();
		sun_start = new StringBuilder();
		sun_end = new StringBuilder();

		backarrow = (ImageView) findViewById(R.id.backarrow_icon);
		backarrow.setOnClickListener(this);
		settingsIcon = (ImageView) findViewById(R.id.settings_icon);
		settingsIcon.setOnClickListener(this);
		sunday = (TextView) findViewById(R.id.sunday_tv);
		monday = (TextView) findViewById(R.id.monday_tv);
		tuesday = (TextView) findViewById(R.id.tuesday_tv);
		wednesday = (TextView) findViewById(R.id.wednesday_tv);
		thursday = (TextView) findViewById(R.id.thursday_tv);
		friday = (TextView) findViewById(R.id.friday_tv);
		saturday = (TextView) findViewById(R.id.saturday_tv);
		timings_text = (TextView) findViewById(R.id.timings_text);
		addBtn = (Button) findViewById(R.id.add_timings);
		addBtn.setOnClickListener(this);
		scheduleBtn = (Button) findViewById(R.id.add_schedule_btn);
		scheduleBtn.setOnClickListener(this);

		select_from_time = (TextView) findViewById(R.id.select_from_time);
		select_from_time.setOnClickListener(this);
		select_from_time.setEnabled(false);
		select_to_time = (TextView) findViewById(R.id.select_to_time);
		select_to_time.setOnClickListener(this);
		select_to_time.setEnabled(false);

		sunday.setOnClickListener(this);
		monday.setOnClickListener(this);
		tuesday.setOnClickListener(this);
		wednesday.setOnClickListener(this);
		thursday.setOnClickListener(this);
		friday.setOnClickListener(this);
		saturday.setOnClickListener(this);
		
		timeList = new ArrayList<String>();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backarrow_icon) {
			finish();
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
		} else if (v.getId() == R.id.settings_icon) {
			PopupMenu popupMenu = new PopupMenu(SchuduleActivity.this, v);
			popupMenu.setOnMenuItemClickListener(SchuduleActivity.this);
			popupMenu.inflate(R.menu.menu_item);
			popupMenu.show();
		} else if (v.getId() == R.id.sunday_tv) {
			if (isSunday) {
				isSunday = false;
				sunday.setTextColor(Color.parseColor("#ADAFB4"));
			} else {
				isSunday = true;
				sunday.setTextColor(Color.parseColor("#009688"));
			}
			checkAndEnableStartime();
		} else if (v.getId() == R.id.monday_tv) {
			if (isMonday) {
				isMonday = false;
				monday.setTextColor(Color.parseColor("#ADAFB4"));
			} else {
				isMonday = true;
				monday.setTextColor(Color.parseColor("#009688"));
			}
			checkAndEnableStartime();
		} else if (v.getId() == R.id.tuesday_tv) {
			if (isTuesday) {
				isTuesday = false;
				tuesday.setTextColor(Color.parseColor("#ADAFB4"));
			} else {
				isTuesday = true;
				tuesday.setTextColor(Color.parseColor("#009688"));
			}
			checkAndEnableStartime();
		} else if (v.getId() == R.id.wednesday_tv) {
			if (isWednesday) {
				isWednesday = false;
				wednesday.setTextColor(Color.parseColor("#ADAFB4"));
			} else {
				isWednesday = true;
				wednesday.setTextColor(Color.parseColor("#009688"));
			}
			checkAndEnableStartime();
		} else if (v.getId() == R.id.thursday_tv) {
			if (isThursday) {
				isThursday = false;
				thursday.setTextColor(Color.parseColor("#ADAFB4"));
			} else {
				isThursday = true;
				thursday.setTextColor(Color.parseColor("#009688"));
			}
			checkAndEnableStartime();
		} else if (v.getId() == R.id.friday_tv) {
			if (isFriday) {
				isFriday = false;
				friday.setTextColor(Color.parseColor("#ADAFB4"));
			} else {
				isFriday = true;
				friday.setTextColor(Color.parseColor("#009688"));
			}
			checkAndEnableStartime();
		} else if (v.getId() == R.id.saturday_tv) {
			if (isSaturday) {
				isSaturday = false;
				saturday.setTextColor(Color.parseColor("#ADAFB4"));
			} else {
				isSaturday = true;
				saturday.setTextColor(Color.parseColor("#009688"));
			}
			checkAndEnableStartime();
		} else if (v.getId() == R.id.select_from_time) {
			isSlectFromTime = true;
			select_to_time.setEnabled(true);
			Calendar now = Calendar.getInstance();
			TimePickerDialog tpd = TimePickerDialog.newInstance(SchuduleActivity.this, now.get(Calendar.HOUR_OF_DAY),
					now.get(Calendar.MINUTE), true);
			tpd.setThemeDark(true);
			tpd.vibrate(true);
			tpd.dismissOnPause(true);
			tpd.enableSeconds(false);
			tpd.setAccentColor(Color.parseColor("#607D8B"));
			tpd.setTitle("Select Start Time");
			//tpd.setTimeInterval(2, 5, 10);
			tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialogInterface) {
					Log.d("TimePicker", "Dialog was cancelled");
				}
			});
			tpd.show(getFragmentManager(), "Timepickerdialog");
		} else if (v.getId() == R.id.select_to_time) {
			isSlectFromTime = false;
			Calendar now = Calendar.getInstance();
			TimePickerDialog tpd = TimePickerDialog.newInstance(SchuduleActivity.this, now.get(Calendar.HOUR_OF_DAY),
					now.get(Calendar.MINUTE), true);
			tpd.setThemeDark(true);
			tpd.vibrate(true);
			tpd.dismissOnPause(true);
			tpd.enableSeconds(false);
			tpd.setAccentColor(Color.parseColor("#607D8B"));
			tpd.setTitle("Select End Time");
			//tpd.setTimeInterval(2, 5, 10);
			tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialogInterface) {
					Log.d("TimePicker", "Dialog was cancelled");
				}
			});
			tpd.show(getFragmentManager(), "Timepickerdialog");
		} else if (v.getId() == R.id.add_timings){
			if(!select_from_time.getText().toString().equalsIgnoreCase("Start Time") && !select_to_time.getText().toString().equalsIgnoreCase("End Time")){
				timeList.add(select_from_time.getText().toString() + "-" +select_to_time.getText().toString());
				setTimeForDays();
				addTextBox();
				select_from_time.setText("Start Time");
				select_to_time.setEnabled(false);
				select_to_time.setText("End Time");
			} else {
				Toast.makeText(this, "Please Select day and time", Toast.LENGTH_SHORT).show();
			}
		} else if (v.getId() == R.id.add_schedule_btn){
			if (ConnectivityReceiver.isConnected())
				doServiceRequest();
			else
				Toast.makeText(getApplicationContext(), "Please check your internet conection!", Toast.LENGTH_SHORT)
						.show();
		}
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
		/*case R.id.item_schedule_date:
			Intent scheduleIntent = new Intent(this, ScheduleVactionActivity.class);
			startActivity(scheduleIntent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			return true;*/
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
	
	private void checkAndEnableStartime(){
		if(isSunday || isMonday || isTuesday || isWednesday || isThursday || isFriday || isSaturday){
			select_from_time.setEnabled(true);
		} else {
			select_from_time.setEnabled(false);
			select_from_time.setText("Start Time");
			select_to_time.setEnabled(false);
			select_to_time.setText("End Time");
			
			Toast.makeText(getApplicationContext(), "Please select atleast one day.", Toast.LENGTH_SHORT).show();
			
		}
	}
	public void setTimeForDays(){
		if(isMonday){
			mon_start.append(select_from_time.getText().toString());
			mon_start.append(",");
			mon_end.append(select_to_time.getText().toString());
			mon_end.append(",");
		}
		if(isTuesday){
			tue_start.append(select_from_time.getText().toString());
			tue_start.append(",");
			tue_end.append(select_to_time.getText().toString());
			tue_end.append(",");
		}
		if(isWednesday){
			wed_start.append(select_from_time.getText().toString());
			wed_start.append(",");
			wed_end.append(select_to_time.getText().toString());
			wed_end.append(",");
		}
		if(isThursday){
			thu_start.append(select_from_time.getText().toString());
			thu_start.append(",");
			thu_end.append(select_to_time.getText().toString());
			thu_end.append(",");
		}
		if(isFriday){
			fri_start.append(select_from_time.getText().toString());
			fri_start.append(",");
			fri_end.append(select_to_time.getText().toString());
			fri_end.append(",");
		}
		if(isSaturday){
			sat_start.append(select_from_time.getText().toString());
			sat_start.append(",");
			sat_end.append(select_to_time.getText().toString());
			sat_end.append(",");
		}
		if(isSunday){
			sun_start.append(select_from_time.getText().toString());
			sun_start.append(",");
			sun_end.append(select_to_time.getText().toString());
			sun_end.append(",");
		}
	}
	private void addTextBox(){
		if(timeList.isEmpty()){
			timings_text.setVisibility(View.GONE);
		} else {
			timings_text.setVisibility(View.VISIBLE);
			String str = "";
			for(int i = 0; i < timeList.size(); i++){
				if(i == 0){
					str = timeList.get(i);
				} else {
					str += "\n" + timeList.get(i);
				}
			}
			
			timings_text.setText(str);
		}
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
		String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
		String minuteString = minute < 10 ? "0" + minute : "" + minute;
		String secondString = second < 10 ? "0" + second : "" + second;
		//String time = hourString + " : " + minuteString + " : " + secondString;
		String time = hourString + ":" + minuteString;
		if (isSlectFromTime)
			select_from_time.setText(time);
		else {
			String fromtime[] = select_from_time.getText().toString().split(":");
			if (Integer.parseInt(fromtime[0]) < hourOfDay
					|| (Integer.parseInt(fromtime[0]) == hourOfDay && Integer.parseInt(fromtime[1]) < minute)
					|| (Integer.parseInt(fromtime[0]) == hourOfDay && Integer.parseInt(fromtime[1]) == minute
							)) {
				select_to_time.setText(time);
			} else {
				if (Integer.parseInt(fromtime[0]) == hourOfDay && Integer.parseInt(fromtime[1]) == minute
						) {
					Toast.makeText(getApplicationContext(),
							"Invalid Selection! Please make sure from date and to date should not be same.",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Invalid Selection! Please make sure from date should be less then to date.",
							Toast.LENGTH_LONG).show();
				}

			}
		}
	}
	
	public void doServiceRequest(){
		if(timeList.isEmpty()){
			Toast.makeText(getApplicationContext(),
					"Noting scheduled please select timings.",
					Toast.LENGTH_SHORT).show();
		} else {
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Please wait...");
			pDialog.show();
			/* Post data */
			JSONObject jsonParams = new JSONObject();
			String startTime = "", endTime = "";
			/*for(String str : timeList){
				String[] timings = str.split("-");
				startTime += timings[0]+",";
				endTime += timings[1]+",";
 			}*/
			try {
				jsonParams.put("userid", getAppSharedPreference().getUserGuid().toString());
				jsonParams.put("monday_start", removeLastChar(mon_start.toString()));
				jsonParams.put("Tuesday_start", removeLastChar(tue_start.toString()));
				jsonParams.put("Wednesday_start", removeLastChar(wed_start.toString()));
				jsonParams.put("thrusday_start", removeLastChar(thu_start.toString()));
				jsonParams.put("Friday_start", removeLastChar(fri_start.toString()));
				jsonParams.put("saterday_start", removeLastChar(sat_start.toString()));
				jsonParams.put("Sunday_start", removeLastChar(sun_start.toString()));
				jsonParams.put("saterday_end", removeLastChar(sat_end.toString()));
				jsonParams.put("monday_end", removeLastChar(mon_end.toString()));
				jsonParams.put("Tuesday_end",removeLastChar(tue_end.toString()));
				jsonParams.put("Wednesday_end", removeLastChar(wed_end.toString()));
				jsonParams.put("thrusday_end", removeLastChar(thu_end.toString()));
				jsonParams.put("Friday_end", removeLastChar(fri_end.toString()));
				jsonParams.put("Sunday_end", removeLastChar(sun_end.toString()));
			} catch (JSONException jEx) {
				Log.e("schedule", "failed to register schedule");
			}

			JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
					URLConstants.getDoctorScheduleAppointmentUrl,
					jsonParams, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try{
								if(response != null){
									if(response.getString("status").equalsIgnoreCase("fail"))
										Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
									else {
										Toast.makeText(getApplicationContext(), "Scheduled Timings are add successfully", Toast.LENGTH_SHORT).show();
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
			Log.e("Post Request",""+postRequest.toString());
			postRequest.setRetryPolicy(new DefaultRetryPolicy(
					100000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			BZAppApplication.getInstance().addToRequestQueue(postRequest, tag_json_obj);
		}
	}

	private static String removeLastChar(String str) {
		String strnew="";
		if(str.length()!=0) {
			strnew = str.substring(0, str.length() - 1);
		}

		return strnew;
	}
}
