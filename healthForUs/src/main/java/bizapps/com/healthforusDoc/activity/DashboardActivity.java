package bizapps.com.healthforusDoc.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bizapps.com.healthforusDoc.BZAppApplication;
import bizapps.com.healthforusDoc.R;
import bizapps.com.healthforusDoc.ScheduleVactionActivity;
import bizapps.com.healthforusDoc.model.Appiontments;
import bizapps.com.healthforusDoc.model.Blogs;
import bizapps.com.healthforusDoc.utills.CircularImageView;
import bizapps.com.healthforusDoc.utills.ConnectivityReceiver;
import bizapps.com.healthforusDoc.utills.URLConstants;

public class DashboardActivity extends BaseActivity implements OnClickListener, OnMenuItemClickListener{
	
	private ImageLoader imageLoader;
	private ProgressDialog pDialog;
	private String tag_json_obj = "json_obj_req";
	private FloatingActionButton fab;
	private CircularImageView mProfileImageView;
	private TextView userName;
	private TextView appointments, appointmentRequest, historyAppointments;
	private Button availability, blogs;
	private LinearLayout availability_layout, blog_layout;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    public final static String EXTRA_FILE_PATH = "file_path";
    
    public final static String[] morningHrs = {"07:00 - 08:00", "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "12:00 - 13:00", "12:00 - 12:30"};
    public final static String[] earlyHrs = {"01:00 - 02:00", "02:00 - 03:00", "03:00 - 04:00", "04:00 - 05:00", "05:00 - 06:00", "06:00 - 07:00"};
    public final static String[] aftnHrs = {"13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00"};
    public final static String[] lateHrs = {"19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00", "22:00 - 23:00", "23:00 - 00:00", "00:00 - 01:00"};
    
    private List<String> scheduleTimes; 
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	
    	if(getAppSharedPreference().getLoggedInUserImage() != null && !getAppSharedPreference().getLoggedInUserImage().equalsIgnoreCase(""))
			mProfileImageView.setImageURI(Uri.parse(getAppSharedPreference().getLoggedInUserImage()));
    
    	if (ConnectivityReceiver.isConnected())
			doServiceRequest();
		else
			Toast.makeText(getApplicationContext(), "Please check your internet conection!", Toast.LENGTH_SHORT)
					.show();
    }
    
    private long getTimeEndOfDay(){
    	Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        return now.getTimeInMillis();
    }
    private long getTimeStartOfDay(){
    	Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 00);
        now.set(Calendar.MINUTE, 00);
        now.set(Calendar.SECOND, 00);
        return now.getTimeInMillis();
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(this);
		userName = (TextView) findViewById(R.id.user_name);
		userName.setText(getAppSharedPreference().getName());
		mProfileImageView = (CircularImageView) findViewById(R.id.user_thumbnail);
		mProfileImageView.setOnClickListener(this);
		
		
		findViewById(R.id.popup_menu).setOnClickListener(this);
		
		appointments = (TextView) findViewById(R.id.appointments);
		appointments.setOnClickListener(this);
		appointmentRequest = (TextView) findViewById(R.id.appointment_requests);
		appointmentRequest.setOnClickListener(this);
		historyAppointments = (TextView) findViewById(R.id.history_appointment_request);
		historyAppointments.setOnClickListener(this);
		
		availability = (Button) findViewById(R.id.availability_today);
		availability.setOnClickListener(this);
		blogs = (Button) findViewById(R.id.blogs);
		blogs.setOnClickListener(this);
		availability_layout = (LinearLayout) findViewById(R.id.availability_layout);
		blog_layout = (LinearLayout) findViewById(R.id.blog_layout);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.fab){
			Intent editProfile = new Intent(this, EPActivity.class);
//			editProfile.putExtra("isRegister", false);
			startActivity(editProfile);
			overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
		} else if(v.getId() == R.id.user_thumbnail){
			selectImage();
		} else if(v.getId() == R.id.appointments){
			Intent appointments = new Intent(this, AppointmentActivity.class);
			appointments.putExtra("type", "Accepted");
			startActivity(appointments);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		} else if(v.getId() == R.id.history_appointment_request){
			Intent appointments = new Intent(this, AppointmentActivity.class);
			appointments.putExtra("type", "Rejected");
			startActivity(appointments);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		} else if(v.getId() == R.id.appointment_requests){
			Intent appointments = new Intent(this, AppointmentActivity.class);
			appointments.putExtra("type", "Pending");
			startActivity(appointments);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		} else if(v.getId() == R.id.popup_menu){
			PopupMenu popupMenu = new PopupMenu(DashboardActivity.this, v);
			popupMenu.setOnMenuItemClickListener(DashboardActivity.this);
			popupMenu.inflate(R.menu.menu_item);
			popupMenu.show();
		} else if(v.getId() == R.id.availability_today){
			/*if(scheduleTimes.isEmpty()){
				availability.setText("NO APPOINTMENTS TODAY");
				availability.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				availability_layout.setVisibility(View.GONE);
			} else {*/
				if(availability_layout.getVisibility() == View.VISIBLE){
					availability.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downarrow, 0);
					availability_layout.setVisibility(View.GONE);
				} else {
					availability.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uparrow, 0);
					availability_layout.setVisibility(View.VISIBLE);
					addScheduleDate();
				}
			// }
		} else if(v.getId() == R.id.blogs){
			if(blog_layout.getVisibility() == View.VISIBLE){
				blogs.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downarrow, 0);
				blog_layout.setVisibility(View.GONE);
			} else {
				blogs.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.uparrow, 0);
				blog_layout.setVisibility(View.VISIBLE);
				doBlogServiceRequest();
			}
		}
	}
	
	private void addScheduleDate(){
		if(availability_layout != null)
			availability_layout.removeAllViews();
		
		View morningLayout = LayoutInflater.from(this).inflate(R.layout.schedule_time_layout, availability_layout, false);
		TextView title = (TextView) morningLayout.findViewById(R.id.tittle_header);
		title.setText("Morning Hours");
		TextView time1 = (TextView) morningLayout.findViewById(R.id.time1);
		time1.setText(morningHrs[0]);
		time1.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView time2 = (TextView) morningLayout.findViewById(R.id.time2);
		time2.setText(morningHrs[1]);
		time2.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView time3 = (TextView) morningLayout.findViewById(R.id.time3);
		time3.setText(morningHrs[2]);
		time3.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView time4 = (TextView) morningLayout.findViewById(R.id.time4);
		time4.setText(morningHrs[3]);
		time4.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView time5 = (TextView) morningLayout.findViewById(R.id.time5);
		time5.setText(morningHrs[4]);
		time5.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView time6 = (TextView) morningLayout.findViewById(R.id.time6);
		time6.setText(morningHrs[5]);
		time6.setTextColor(getResources().getColor(R.color.primary_dark));
		availability_layout.addView(morningLayout);
		
		View afternoonLayout = LayoutInflater.from(this).inflate(R.layout.schedule_time_layout, availability_layout, false);
		TextView afttitle = (TextView) afternoonLayout.findViewById(R.id.tittle_header);
		afttitle.setText("Afternoon Hours");
		TextView afttime1 = (TextView) afternoonLayout.findViewById(R.id.time1);
		afttime1.setText(aftnHrs[0]);
		afttime1.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView afttime2 = (TextView) afternoonLayout.findViewById(R.id.time2);
		afttime2.setText(aftnHrs[1]);
		afttime2.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView afttime3 = (TextView) afternoonLayout.findViewById(R.id.time3);
		afttime3.setText(aftnHrs[2]);
		afttime3.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView afttime4 = (TextView) afternoonLayout.findViewById(R.id.time4);
		afttime4.setText(aftnHrs[3]);
		afttime4.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView afttime5 = (TextView) afternoonLayout.findViewById(R.id.time5);
		afttime5.setText(aftnHrs[4]);
		afttime5.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView afttime6 = (TextView) afternoonLayout.findViewById(R.id.time6);
		afttime6.setText(aftnHrs[5]);
		afttime6.setTextColor(getResources().getColor(R.color.primary_dark));
		availability_layout.addView(afternoonLayout);
		
		View eveningLayout = LayoutInflater.from(this).inflate(R.layout.schedule_time_layout, availability_layout, false);
		TextView evetitle = (TextView) eveningLayout.findViewById(R.id.tittle_header);
		evetitle.setText("Evening Hours");
		TextView evetime1 = (TextView) eveningLayout.findViewById(R.id.time1);
		evetime1.setText(lateHrs[0]);
		evetime1.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView evetime2 = (TextView) eveningLayout.findViewById(R.id.time2);
		evetime2.setText(lateHrs[1]);
		evetime2.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView evetime3 = (TextView) eveningLayout.findViewById(R.id.time3);
		evetime3.setText(lateHrs[2]);
		evetime3.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView evetime4 = (TextView) eveningLayout.findViewById(R.id.time4);
		evetime4.setText(lateHrs[3]);
		evetime4.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView evetime5 = (TextView) eveningLayout.findViewById(R.id.time5);
		evetime5.setText(lateHrs[4]);
		evetime5.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView evetime6 = (TextView) eveningLayout.findViewById(R.id.time6);
		evetime6.setText(lateHrs[5]);
		evetime6.setTextColor(getResources().getColor(R.color.primary_dark));
		availability_layout.addView(eveningLayout);
		
		View nightLayout = LayoutInflater.from(this).inflate(R.layout.schedule_time_layout, availability_layout, false);
		TextView nighttitle = (TextView) nightLayout.findViewById(R.id.tittle_header);
		nighttitle.setText("Late Hours");
		TextView nighttime1 = (TextView) nightLayout.findViewById(R.id.time1);
		nighttime1.setText(earlyHrs[0]);
		nighttime1.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView nighttime2 = (TextView) nightLayout.findViewById(R.id.time2);
		nighttime2.setText(earlyHrs[1]);
		nighttime2.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView nighttime3 = (TextView) nightLayout.findViewById(R.id.time3);
		nighttime3.setText(earlyHrs[2]);
		nighttime3.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView nighttime4 = (TextView) nightLayout.findViewById(R.id.time4);
		nighttime4.setText(earlyHrs[3]);
		nighttime4.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView nighttime5 = (TextView) nightLayout.findViewById(R.id.time5);
		nighttime5.setText(earlyHrs[4]);
		nighttime5.setTextColor(getResources().getColor(R.color.primary_dark));
		TextView nighttime6 = (TextView) nightLayout.findViewById(R.id.time6);
		nighttime6.setText(earlyHrs[5]);
		nighttime6.setTextColor(getResources().getColor(R.color.primary_dark));
		availability_layout.addView(nightLayout);
		
		for(String times : scheduleTimes){
			String split[] = times.trim().split(" ");
			String hrMin[] = split[1].toString().trim().split(":");
			
			if(Integer.parseInt(hrMin[0]) == 7){
				time1.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 8){
				time2.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 9){
				time3.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 10){
				time4.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 11){
				time5.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 12){
				time6.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			
			
			if(Integer.parseInt(hrMin[0]) == 13){
				afttime1.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 14){
				afttime2.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 15){
				afttime3.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 16){
				afttime4.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 17){
				afttime5.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 18){
				afttime6.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			
			
			if(Integer.parseInt(hrMin[0]) == 19){
				evetime1.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 20){
				evetime2.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 21){
				evetime3.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 22){
				evetime4.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 23){
				evetime5.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 00){
				evetime6.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			
			
			if(Integer.parseInt(hrMin[0]) == 01){
				nighttime1.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 02){
				nighttime2.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 03){
				nighttime3.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 04){
				nighttime4.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 05){
				nighttime5.setTextColor(getResources().getColor(R.color.reject_btn));
			}
			if(Integer.parseInt(hrMin[0]) == 06){
				nighttime6.setTextColor(getResources().getColor(R.color.reject_btn));
			}
		}
	}
	
	/**** Method for Setting the Height of the ListView dynamically.
	 **** Hack to fix the issue of not showing all the items of the ListView
	 **** when placed inside a ScrollView  ****/
	public void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	}
	
	private void doServiceRequest() {
		scheduleTimes = new ArrayList<String>();
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.show();
		/* Post data */
		Map<String, String> jsonParams = new HashMap<String, String>();
		Log.e("Userid",""+getAppSharedPreference().getUserGuid());
//		jsonParams.put("userid", getAppSharedPreference().getLoggedInUser());
//		getUserGuid
		jsonParams.put("userid", getAppSharedPreference().getUserGuid());
		JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
//				URLConstants.getDoctorAppointmentsUrl,
				URLConstants.DR_BASE_URL + "doctorSchedule.php",
				new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try{
							Log.isLoggable("AppointmentListResponse", Log.VERBOSE);

							Log.i("AppointmentListResponse",""+response);
							if(response != null){
								if(response.getString("status").equalsIgnoreCase("fail"))
									Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
								else {
									Log.i("AppointmentListResponse", response.toString());
									JSONArray jArray = response.getJSONArray("data");
									GsonBuilder gsonBuilder = new GsonBuilder();
									Gson gson = gsonBuilder.create();
									List<Appiontments> posts = new ArrayList<Appiontments>();
									posts = Arrays.asList(gson.fromJson(jArray.toString(), Appiontments[].class));
									BZAppApplication.pendingAppointmentsList = new ArrayList<Appiontments>();
									BZAppApplication.canceledAppointmentsList = new ArrayList<Appiontments>();
									BZAppApplication.acceptedAppointmentsList = new ArrayList<Appiontments>();
									for(Appiontments appointment : posts){
										if(appointment != null && appointment.getStatus() != null && appointment.getStatus().toString().trim().equalsIgnoreCase("Accepted")){
											BZAppApplication.acceptedAppointmentsList.add(appointment);
											
											// Date Conversion
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // 2016-07-27 08:30:00
											try {
												Log.i("Checking", "Checking: " + getTimeEndOfDay());
												Log.i("Checking", "Checking: " + getTimeStartOfDay());
												Date startDate = sdf.parse(appointment.getDatetime_start());
												Log.i("Checking", "Checking Start date: " + startDate.getTime());
												Date endDate = sdf.parse(appointment.getDatetime_end());
												Log.i("Checking", "Checking End Date: " + endDate.getTime());
												if(startDate.getTime() > getTimeStartOfDay() && startDate.getTime() < getTimeEndOfDay()){
													Log.i("Checking", "Checking: " + startDate.getTime());
													scheduleTimes.add(appointment.getDatetime_start());
												}
												
												if(endDate.getTime() > getTimeStartOfDay() && endDate.getTime() < getTimeEndOfDay()){
													Log.i("Checking", "Checking: " + endDate.getTime());
													scheduleTimes.add(appointment.getDatetime_end());
												}
											} catch (ParseException e) {
												e.printStackTrace();
											}
										} else if(appointment != null && appointment.getStatus() != null && appointment.getStatus().toString().trim().equalsIgnoreCase("Rejected")){
											BZAppApplication.canceledAppointmentsList.add(appointment);
										} else if(appointment != null && appointment.getStatus() != null && appointment.getStatus().toString().trim().equalsIgnoreCase("Pending")){
											BZAppApplication.pendingAppointmentsList.add(appointment);
										}
									}
									//Future Accepted Appointments
									appointmentRequest.setText(BZAppApplication.pendingAppointmentsList.size()+"");
									// Today's Accepted Appointments
									appointments.setText(BZAppApplication.acceptedAppointmentsList.size()+"");
									historyAppointments.setText(BZAppApplication.canceledAppointmentsList.size()+"");
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
	
	// Blog Request
	private void doBlogServiceRequest() {
		scheduleTimes = new ArrayList<String>();
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait blogs loading...");
		pDialog.show();
		/* Post data */
		Map<String, String> jsonParams = new HashMap<String, String>();
		jsonParams.put("userid", getAppSharedPreference().getLoggedInUser());

		JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URLConstants.getDoctorBlogListUrl,

				new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.i("Blog Response", response.toString());
						try{
							if(response != null){
								if(response.getString("status").equalsIgnoreCase("fail"))
									Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
								else {
									Log.i("BLOGS", response.toString());
									JSONArray jArray = response.getJSONArray("data");
									GsonBuilder gsonBuilder = new GsonBuilder();
									Gson gson = gsonBuilder.create();
									buildBlogLayout(Arrays.asList(gson.fromJson(jArray.toString(), Blogs[].class)));
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
						Log.i("Volley Error", error.getMessage());
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
	
	private void buildBlogLayout(List<Blogs> blogList){
		if(blogList != null){
			if(blog_layout != null)
				blog_layout.removeAllViews();
			
			for(Blogs blog : blogList){
				View blogLayout = LayoutInflater.from(this).inflate(R.layout.blog_item, blog_layout, false);
				TextView title = (TextView) blogLayout.findViewById(R.id.blog_name);
				NetworkImageView image = (NetworkImageView) blogLayout.findViewById(R.id.blog_img);
				title.setText(blog.getDescription());
				
				if (imageLoader == null)
					imageLoader = BZAppApplication.getInstance().getImageLoader();
				
				image.setImageUrl(blog.getFile_record(), imageLoader);
				
				blog_layout.addView(blogLayout);
			}
		}
	}
	
	// Camera code
	private void marshmallowCameraPermissionCheck(String camOrGal) {
        if(camOrGal.equalsIgnoreCase("Take Photo")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            	cameraIntent();
            } else {
                cameraIntent();
            }
        }else{
            galleryIntent();
          }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                 if (items[item].equals("Take Photo")) {
                        marshmallowCameraPermissionCheck("Take Photo");
                 } else if (items[item].equals("Choose from Gallery")) {
                        marshmallowCameraPermissionCheck("Choose from Gallery");
                 } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        // call android default camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
        // ******** code for crop image
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 150);

        try {

            intent.putExtra("return-data", true);
            startActivityForResult(intent, PICK_FROM_CAMERA);

        } catch (ActivityNotFoundException e) {
        // Do nothing for now
        }


    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // call android default gallery
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        // ******** code for crop image
        /*intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 150);*/

        try {

            intent.putExtra("return-data", true);
            startActivityForResult(Intent.createChooser(intent,
                    "Complete action using"), PICK_FROM_GALLERY);

        } catch (ActivityNotFoundException e) {
        // Do nothing for now
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FROM_CAMERA) {
           // Bundle extras = data.getExtras();
            if (data!=null && data.getExtras() != null) {
                Bitmap photo = data.getExtras().getParcelable("data");
                mProfileImageView.setImageBitmap(photo);
                getAppSharedPreference().setLoggedInUserImage(getRealPathFromURI(getApplicationContext(), data.getData()));
            }
        }

        if (requestCode == PICK_FROM_GALLERY) {
            if (data!=null && data.getExtras() != null) {
                String filepath = getRealPathFromURI(getApplicationContext(), data.getData());
                mProfileImageView.setImageURI(data.getData());
                getAppSharedPreference().setLoggedInUserImage(data.getData().toString());
            }
        }
    }

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_change_password:
			Intent register = new Intent(this, ForgotPasswordActivity.class);
			startActivity(register);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			return true;
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
	
	
	
	/*private class BlogsAdapter extends ArrayAdapter<Blogs>{
		private Context context;
		private int resource; 
		private List<Blogs> objects;
		
		private Typeface custom_font;
		
		public BlogsAdapter(Context context, int resource, List<Blogs> objects) {
			super(context, resource, objects);
			this.context = context;
			this.objects = objects;
			this.resource = resource;
			custom_font = Typeface.createFromAsset(getAssets(), "fonts/KirstyBold.ttf");
		}
		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        BlogHolder holder = null;
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((DashboardActivity)context).getLayoutInflater();
	            row = inflater.inflate(resource, parent, false);
	            
	            holder = new BlogHolder();
	            holder.imgIcon = (NetworkImageView)row.findViewById(R.id.blog_img);
	            holder.txtTitle = (TextView)row.findViewById(R.id.blog_name);
	            //holder.txtTitle.setTypeface(custom_font);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (BlogHolder)row.getTag();
	        }
	        
	        Blogs blogs = objects.get(position);
	        holder.txtTitle.setText(blogs.getDescription());
	        holder.imgIcon.setImageUrl(blogs.getFile_record(), imageLoader);
	        
	        return row;
	    }
	    
	    class BlogHolder
	    {
	        NetworkImageView imgIcon;
	        TextView txtTitle;
	    }
	}*/
}