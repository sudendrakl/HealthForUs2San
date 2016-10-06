package bizapps.com.healthforusDoc.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.healthforusDoc.BZAppApplication;
import bizapps.com.healthforusDoc.R;
import bizapps.com.healthforusDoc.ScheduleVactionActivity;
import bizapps.com.healthforusDoc.model.Appiontments;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import static bizapps.com.healthforusDoc.utills.URLConstants.DR_BASE_URL;

public class AppointmentActivity extends BaseActivity implements OnClickListener, OnMenuItemClickListener {

	private GridView appointmentGrid;
	private int mYear, mMonth, mDay, mHour, mMinute;
	private TextView noAppointments, headerText;
	private ProgressDialog pDialog;
	private String tag_json_obj = "json_obj_req";
	private ImageView backarrow, settingsIcon;
	private List<Appiontments> itemList;
	private boolean isPending;
	private AppointmentsAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment);

		backarrow = (ImageView) findViewById(R.id.backarrow_icon);
		backarrow.setOnClickListener(this);
		settingsIcon = (ImageView) findViewById(R.id.settings_icon);
		settingsIcon.setOnClickListener(this);
		noAppointments = (TextView) findViewById(R.id.no_appointments);
		appointmentGrid = (GridView) findViewById(R.id.appointment_grid);

		headerText = (TextView) findViewById(R.id.header_text);

		String listType = getIntent().getExtras().getString("type", "");
		if (listType != null) {
			if (listType.equalsIgnoreCase("Accepted")) {
				itemList = BZAppApplication.acceptedAppointmentsList;
				isPending = false;
				headerText.setText("Appointments");
			} else if (listType.equalsIgnoreCase("Rejected")) {
				itemList = BZAppApplication.canceledAppointmentsList;
				isPending = false;
				headerText.setText("Appointments History");
			} else if (listType.equalsIgnoreCase("Pending")) {
				itemList = BZAppApplication.pendingAppointmentsList;
				isPending = true;
				headerText.setText("Appointments Requests");
			} else {
				itemList = new ArrayList<Appiontments>();
			}
		} else {
			itemList = new ArrayList<Appiontments>();
		}

		if (itemList != null && itemList.size() > 0) {
			noAppointments.setVisibility(View.GONE);
			appointmentGrid.setVisibility(View.VISIBLE);
			Collections.sort(itemList, new Comparator<Appiontments>() {
				@Override
				public int compare(Appiontments o1, Appiontments o2) {
					return o1.getDatetime_start().compareTo(o2.getDatetime_start());
				}
			});
			adapter = new AppointmentsAdapter(this, itemList, isPending);
			appointmentGrid.setAdapter(adapter);
		} else {
			noAppointments.setVisibility(View.VISIBLE);
			noAppointments.setText("No " + headerText.getText().toString());
			appointmentGrid.setVisibility(View.GONE);
			Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/KirstyBold.ttf");
			noAppointments.setTypeface(custom_font);
		}
	}


	private class AppointmentsAdapter extends BaseAdapter {
		Context ctx;
		List<Appiontments> items;
		LayoutInflater inflater;
		boolean isPending;

		public AppointmentsAdapter(Context ctx, List<Appiontments> items, boolean isPending) {
			this.ctx = ctx;
			this.items = items;
			this.isPending = isPending;
			inflater = LayoutInflater.from(this.ctx);
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int arg0) {
			return items.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			MyViewHolder mViewHolder;
			String appointmentStr = "";
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.appointments_list_item, parent, false);
				mViewHolder = new MyViewHolder(convertView);
				convertView.setTag(mViewHolder);
				convertView.setId(position);
			} else {
				mViewHolder = (MyViewHolder) convertView.getTag();
			}

			Appiontments currentData = (Appiontments) getItem(position);

			// Date Conversion
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // 2016-07-27 08:30:00
			SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy_HH:mm"); // 2016-07-27 08:30:00
			try {
				Date startDate = sdf.parse(currentData.getDatetime_start());
				Date endDate = sdf.parse(currentData.getDatetime_end());
				String startDateStr[] = df.format(startDate).split("_");
				appointmentStr = startDateStr[0] + " (" + startDateStr[1] +" - "+df.format(endDate).split("_")[1]+")";
				Log.i("Start Date", "Date: " + startDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			mViewHolder.patientName.setText("PATIENT NAME: " + currentData.getContact());
			mViewHolder.diseaseDiscription.setText("Contact: " + currentData.getPat_name());
			mViewHolder.appointmentTimings.setText(appointmentStr);
			mViewHolder.acceptBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doServiceRequest(((Appiontments) getItem(position)).getId(), true, position);
				}
			});
			mViewHolder.rejectBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doServiceRequest(((Appiontments) getItem(position)).getId(), false, position);
				}
			});

			if (!isPending) {
				mViewHolder.acceptBtn.setVisibility(View.GONE);
				mViewHolder.rejectBtn.setVisibility(View.GONE);
			} else {
				mViewHolder.acceptBtn.setVisibility(View.VISIBLE);
				mViewHolder.rejectBtn.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		private class MyViewHolder {
			TextView patientName, diseaseDiscription, appointmentTimings;
			Button acceptBtn, rejectBtn;

			public MyViewHolder(View item) {
				patientName = (TextView) item.findViewById(R.id.patient_name);
				diseaseDiscription = (TextView) item.findViewById(R.id.disease_text);
				appointmentTimings = (TextView) item.findViewById(R.id.appointment_timings_txt);

				acceptBtn = (Button) item.findViewById(R.id.accept_btn);
				rejectBtn = (Button) item.findViewById(R.id.reject_btn);
			}
		}

		private void doServiceRequest(int id, boolean isAccepted, final int posistion) {
			pDialog = new ProgressDialog(ctx);
			pDialog.setMessage("Please wait...");
			pDialog.show();
			/* Post data */
			Map<String, String> jsonParams = new HashMap<String, String>();
			jsonParams.put("appointemtn_id", id + "");

			final String msg, url;
			if (isAccepted) {
				msg = "Appointment is sucessfully added";
				url = DR_BASE_URL + "appointment_accept.php";
//						URLConstants.getDoctorAppointmentAcceptUrl;
			} else {
				msg = "Appointment is canceled";
				url = DR_BASE_URL + "appointment_reject.php";
//						URLConstants.getDoctorAppointmentRejectUrl;
			}

			JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,

					new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try{
								Log.i("AppointmentAcceptReject",""+response);
								if(response != null){
									if(response.getString("status").equalsIgnoreCase("fail"))
										Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
									else {
										Log.i("APPOINTMENTS", response.toString());
										itemList.remove(posistion);
										adapter.notifyDataSetChanged();
										Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
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
							Log.e("Appointment error",""+error.getMessage());
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
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backarrow_icon) {
			finish();
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
		} else if (v.getId() == R.id.settings_icon) {
			PopupMenu popupMenu = new PopupMenu(AppointmentActivity.this, v);
			popupMenu.setOnMenuItemClickListener(AppointmentActivity.this);
			popupMenu.inflate(R.menu.menu_item);
			popupMenu.show();
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
}
