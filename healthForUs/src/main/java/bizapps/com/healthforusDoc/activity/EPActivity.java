package bizapps.com.healthforusDoc.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bizapps.com.healthforusDoc.BZAppApplication;
import bizapps.com.healthforusDoc.R;
import bizapps.com.healthforusDoc.model.IDDetail;
import bizapps.com.healthforusDoc.model.SearchModel;
import bizapps.com.healthforusDoc.utills.ConnectivityReceiver;
import bizapps.com.healthforusDoc.utills.CustomMultipartRequest;
import bizapps.com.healthforusDoc.utills.PrefManager;
import bizapps.com.healthforusDoc.utills.URLConstants;

public class EPActivity extends BaseActivity implements View.OnClickListener{

    private PrefManager pref;
    private ProgressDialog pDialog;
    private String tag_json_obj = "json_obj_req",locName;
    public SearchModel epData;
    TextView tvSpecilization,tvSpecilizationHos,browse_txt_hos,browse_txt_doc;
    private EditText name, password, email, confirmpassword, hospital_name, mobile, experence,designation,
            consultation_fee, website, leadership,  certifications,doc_desc,hos_desc,doc_clinic;
    private EditText hos_name, hos_specification, hos_location,hos_location1, hos_contact, hos_immg_contact, hos_email,
            hos_website,  hos_received_award, hos_average_length_stay, received_award,
            hos_accredity,  hos_our_doctors, hos_opd_timing,associated_hos,
            video_url,linkedin_url,fb_url,locationAddr,locationAddr1;
    private CheckBox lab, xray, blood_bank, opd, tv, wifi, health_tips, ambulance_contact, multiple_branches,
            house_medicine, immergency, room_services, hour_open, ac_rooms, health_blogs, nimber_of_beds,pay_cards,home_visit,health_tip,cb_emergency;
    private Spinner specialization, hos_specialties, category;
    private Button submitBtn, browseBtn,browseBtnDoc,browseBtnHos;
    private boolean isRegister = true;
    private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private int SELECT_PHOTO = 200;
    private TextView browse_txt,locationDoc,location_hosNew;
    private LinearLayout doctor_layout, hospital_layout;
    private RadioButton doctorType, hospitalType;
    List<String> specailiation;
    protected ArrayList<CharSequence> pselectedReceivers = new ArrayList<>();
    protected CharSequence[] preceivers;
    public static final int CALLED_ACTIVITY = 1;
    public boolean isDoctor;
    private String[] specs = { "Cardiology", "Cardiovascular surgery", "Clinical laboratory sciences", "Dermatology",
            "Allergy and immunology", "pathology" };
    RadioGroup radioGroup;
    SearchModel.GetData mydata;
    List<File> mFiles;
    private File[] arrayFile = new File[5];
    File mFile;
    ImageView img01,img02,img03,img04,img05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup.setVisibility(View.GONE);
        mFiles = new ArrayList<File>();
        pref = getAppSharedPreference();
        doctor_layout = (LinearLayout) findViewById(R.id.doctor_layout);
        hospital_layout = (LinearLayout) findViewById(R.id.hospital_layout);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        browse_txt_doc = (TextView) findViewById(R.id.browse_txt_doc);
        browseBtnDoc = (Button) findViewById(R.id.browse_btn_doc);
        browseBtnDoc.setOnClickListener(this);

        img01 = (ImageView) findViewById(R.id.img_01);
        img02 = (ImageView) findViewById(R.id.img_02);
        img03 = (ImageView) findViewById(R.id.img_03);
        img04 = (ImageView) findViewById(R.id.img_04);
        img05 = (ImageView) findViewById(R.id.img_05);
        img01.setOnClickListener(this);
        img02.setOnClickListener(this);
        img03.setOnClickListener(this);
        img04.setOnClickListener(this);
        img05.setOnClickListener(this);

        doServiceRequest();

//        getSpecFromServer();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitBtn) {
            if (ConnectivityReceiver.isConnected()) {
                if (isValidData()) {
                    doServiceRequestNew();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet conection!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        /*else if (v.getId() == R.id.browse_btn) {
            browseGallery();
        }
        else if (v.getId() == R.id.browse_btn) {
            browseGallery();
        }*/
        else if(v.getId() == R.id.img_01){
            BZAppApplication.selectedImage = 1;
            browseGallery();
        }
        else if(v.getId() == R.id.img_02){
            BZAppApplication.selectedImage = 2;
            browseGallery();
        }
        else if(v.getId() == R.id.img_03){
            BZAppApplication.selectedImage = 3;
            browseGallery();
        }
        else if(v.getId() == R.id.img_04){
            BZAppApplication.selectedImage = 4;
            browseGallery();
        }
        else if(v.getId() == R.id.img_05){
            BZAppApplication.selectedImage = 5;
            browseGallery();
        }
        else if(v.getId() == R.id.browse_btn_doc){
            BZAppApplication.certificate = true;
            browseGallery();
        }
    }

    private void browseGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(EPActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(EPActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {


                } else {
                    ActivityCompat.requestPermissions(EPActivity.this,
                            new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            } else {
                ActivityCompat.requestPermissions(EPActivity.this,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    }


    private void doServiceRequestNew() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.show();
		/* Post data */
        if(mFiles.size()!=0){
            mFiles.toArray(arrayFile);
        }
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("userid",getAppSharedPreference().getUserGuid());
        if (isDoctor)
            setDoctorMap(jsonParams);
        else
            setHospitalMap(jsonParams);

        CustomMultipartRequest multipartRequest = new CustomMultipartRequest(
//                "http://doctorapp.rakyow.com/userapp/api/doctor_edit",
                "http://sundareshln.com/test/updateDoctor.php",
                jsonParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        try {
                            JSONObject response = new JSONObject(result);
                            Log.e("EP Response",""+response);
                            if (response != null) {
                                if (response.getString("status").equalsIgnoreCase("fail")) {
                                    Toast.makeText(getApplicationContext(), response.getString("message"),
                                            Toast.LENGTH_SHORT).show();
//                                    if (response.getString("message").contains("Please Varify your Email ID")) {
//                                        addtoPrefs();
//                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), response.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                    addtoPrefs();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pDialog.dismiss();

                    }
                }, arrayFile,mFile, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
                if (pDialog != null)
                    pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server is not responding, Please try after sometime.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Volley.newRequestQueue(this).add(mr);


        BZAppApplication.getInstance().addToRequestQueue(multipartRequest, tag_json_obj);
    }

    private void doServiceRequest() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.show();
		/* Post data */
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("userid", getAppSharedPreference().getUserGuid());

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
//                "http://doctorapp.rakyow.com/userapp/api/get_doctor_edit",
                "http://sundareshln.com/test/doctorDetails.php",
                new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    Log.e("Ep Response",""+response);
                    Gson gson = new Gson();
                    epData = gson.fromJson(String.valueOf(response), SearchModel.class);
                    setLayout();
                    //doLogin();
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

    public void setLayout(){
        mydata = epData.getData();
        if(epData.getData().getType().equalsIgnoreCase("doctor")){
            doctor_layout.setVisibility(View.VISIBLE);
            hospital_layout.setVisibility(View.GONE);
            isDoctor = true;
            updateDoctorUILayout();
        }
        else{
            doctor_layout.setVisibility(View.GONE);
            hospital_layout.setVisibility(View.VISIBLE);
            isDoctor = false;
            updateHospitalUILayout();
        }
    }

    private void setHospitalMap(Map<String, String> jsonParams) {
        jsonParams.put("dr_name", hos_name.getText().toString().trim());
        jsonParams.put("spaceification", hos_specification.getText().toString().trim());
        jsonParams.put("address", hos_location.getText().toString().trim()+" "+hos_location1.getText().toString());
        jsonParams.put("website", hos_website.getText().toString().trim());
        jsonParams.put("email", hos_email.getText().toString().trim());
        jsonParams.put("contact", hos_contact.getText().toString().trim());
        //jsonParams.put("specialties", hos_specialties.getSelectedItem().toString().trim());
        jsonParams.put("received_award", hos_received_award.getText().toString().trim());
        jsonParams.put("location",location_hosNew.getText().toString().trim());
        jsonParams.put("type","hospital");
        jsonParams.put("description_full",hos_desc.getText().toString());
        StringBuilder sb= new StringBuilder();
        if(lab.isChecked()){
            sb.append("lab,");
        }
        if(xray.isChecked()){
            sb.append("xray,");
        }
        if(blood_bank.isChecked()){
            sb.append("blood bank,");
        }
        if(opd.isChecked()){
            sb.append("opd,");
        }
        if(room_services.isChecked()){
            sb.append("room service,");
        }
        if(tv.isChecked()){
            sb.append("tv,");
        }
        if(hour_open.isChecked()){
            sb.append("24 Hour open,");
        }
        if(ac_rooms.isChecked()){
            sb.append("AC rooms,");
        }
        if(wifi.isChecked()){
            sb.append("Wifi,");
        }
        if(immergency.isChecked()){
            sb.append("Immergency,");
        }
        if(multiple_branches.isChecked()){
            sb.append("Multiple branches,");
        }
        if(house_medicine.isChecked()){
            sb.append("In House Medicine Store,");
        }
        if(ambulance_contact.isChecked()){
            sb.append("Ambulance,");
        }
        if(nimber_of_beds.isChecked()){
            sb.append("CGHS Linked");
        }
        if(health_tips.isChecked()){
            sb.append("Health tips,");
        }
        if(health_blogs.isChecked()){
            sb.append("Insurance,");
        }
        String facilities = sb.toString();

        facilities = facilities.substring(0,facilities.length()-1);
        Log.e("facilities",""+facilities);
        jsonParams.put("facilities",facilities);
        jsonParams.put("immg_contact", hos_immg_contact.getText().toString().trim());
//        jsonParams.put("health_tips", name.getText().toString().trim());
//        jsonParams.put("health_blog", category.getSelectedItem().toString().trim());
        jsonParams.put("accredity", hos_accredity.getText().toString().trim());
        jsonParams.put("our_doctors", hos_our_doctors.getText().toString().trim());
        jsonParams.put("opd_timing", hos_opd_timing.getText().toString().trim());
    }

    private void updateHospitalUILayout() {
        hos_name = (EditText) findViewById(R.id.hospital_name);
        hos_name.setText(mydata.getDr_name());
        location_hosNew = (TextView) findViewById(R.id.location_hosNew);
        location_hosNew.setText(mydata.getLocation());
        location_hosNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EPActivity.this, PlaceActivity.class);
                startActivityForResult(intent, CALLED_ACTIVITY);
            }
        });
        hos_desc = (EditText) findViewById(R.id.hospital_description);
        hos_desc.setText(mydata.getDescription_full());
        tvSpecilizationHos = (TextView) findViewById(R.id.tv_specilizationhos);
        tvSpecilizationHos.setText(mydata.getSpecialization());
        tvSpecilizationHos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                splSelection();
            }
        });
        hos_specification = (EditText) findViewById(R.id.hospital_category);
        hos_location = (EditText) findViewById(R.id.hospital_address);
        hos_location.setText(mydata.getAddress());
        hos_location1 = (EditText) findViewById(R.id.hospital_address1);
        hos_contact = (EditText) findViewById(R.id.hospital_mobile_number);
        hos_contact.setText(mydata.getContact());
        hos_immg_contact = (EditText) findViewById(R.id.hospital_immg_mobile_number);
        hos_immg_contact.setText(mydata.getImmg_contact());
        hos_email = (EditText) findViewById(R.id.hospital_email_id);
        hos_email.setText(mydata.getEmail());
        hos_website = (EditText) findViewById(R.id.hospital_website);
        hos_website.setText(mydata.getWebsite());
        //hos_specialties = (Spinner) findViewById(R.id.hospital_specilization);
        hos_received_award = (EditText) findViewById(R.id.hospital_received_awards);
        hos_received_award.setText(mydata.getReceived_award());
        hos_accredity = (EditText) findViewById(R.id.hospital_accredity);
        hos_accredity.setText(mydata.getAccredity());
        hos_our_doctors = (EditText) findViewById(R.id.hospital_our_doctors);
        hos_our_doctors.setText(mydata.getOur_doctor());
        hos_opd_timing = (EditText) findViewById(R.id.hospital_opd_timing);
        hos_opd_timing.setText(mydata.getOpt_timing());

        String[] facility = mydata.getFacilities().split(",");
        List<String> wordList = Arrays.asList(facility);
        lab = (CheckBox) findViewById(R.id.lab);
        if(wordList.contains("lab")){
            lab.setChecked(true);
        }
        xray = (CheckBox) findViewById(R.id.xray);
        if(wordList.contains("xray")){
            xray.setChecked(true);
        }
        blood_bank = (CheckBox) findViewById(R.id.blood_bank);
        if(wordList.contains("blood bank")){
            blood_bank.setChecked(true);
        }
        opd = (CheckBox) findViewById(R.id.opd);
        if(wordList.contains("opd")){
            opd.setChecked(true);
        }
        tv = (CheckBox) findViewById(R.id.tv);
        if(wordList.contains("tv")){
            tv.setChecked(true);
        }
        wifi = (CheckBox) findViewById(R.id.wifi);
        if(wordList.contains("Wifi")){
            wifi.setChecked(true);
        }
        health_tips = (CheckBox) findViewById(R.id.health_tips);
        if(wordList.contains("Health tips")){
            health_tips.setChecked(true);
        }
        ambulance_contact = (CheckBox) findViewById(R.id.ambulace_contact);
        if(wordList.contains("Ambulance")){
            ambulance_contact.setChecked(true);
        }
        multiple_branches = (CheckBox) findViewById(R.id.multiple_branches);
        if(wordList.contains("Multiple branches")){
            multiple_branches.setChecked(true);
        }
        house_medicine = (CheckBox) findViewById(R.id.home_medicine);
        if(wordList.contains("In House Medicine Store")){
            house_medicine.setChecked(true);
        }
        immergency = (CheckBox) findViewById(R.id.immergency);
        if(wordList.contains("Immergency")){
            immergency.setChecked(true);
        }
        room_services = (CheckBox) findViewById(R.id.room_service);
        if(wordList.contains("room service")){
            room_services.setChecked(true);
        }
        hour_open = (CheckBox) findViewById(R.id.hour_open);
        if(wordList.contains("24 Hour open")){
            hour_open.setChecked(true);
        }
        ac_rooms = (CheckBox) findViewById(R.id.ac_room);
        if(wordList.contains("AC rooms")){
            ac_rooms.setChecked(true);
        }
        health_blogs = (CheckBox) findViewById(R.id.health_blog);
        if(wordList.contains("Insurance")){
            health_blogs.setChecked(true);
        }
        nimber_of_beds = (CheckBox) findViewById(R.id.number_of_bead);
        if(wordList.contains("CGHS Linked")){
            nimber_of_beds.setChecked(true);
        }
        health_tips = (CheckBox) findViewById(R.id.health_tips);
        if(wordList.contains("Health tips")){
            health_tips.setChecked(true);
        }

        //getSpecFromServer();
    }

    private void setDoctorMap(Map<String, String> jsonParams) {
        jsonParams.put("dr_name", name.getText().toString().trim());
        //jsonParams.put("specialization", getSpecialization());
        jsonParams.put("experence", experence.getText().toString().trim());
        jsonParams.put("designation",designation.getText().toString().trim());
        jsonParams.put("consultation_fee", consultation_fee.getText().toString().trim());
        jsonParams.put("address",locationAddr.getText().toString()+" "+locationAddr1.getText().toString());
        jsonParams.put("location", locationDoc.getText().toString().trim());
        jsonParams.put("description_full",doc_desc.getText().toString());
        jsonParams.put("clinic_name",doc_clinic.getText().toString());
        jsonParams.put("website", website.getText().toString().trim());
        jsonParams.put("email", email.getText().toString().trim());
//        jsonParams.put("password", password.getText().toString().trim());
        jsonParams.put("contact", mobile.getText().toString().trim());
        jsonParams.put("type","doctor");
        //jsonParams.put("specialties", getSpecialization());
        jsonParams.put("hospital_affiliation", hospital_name.getText().toString().trim());
        jsonParams.put("leadership", leadership.getText().toString().trim());
        jsonParams.put("certifications", certifications.getText().toString().trim());
        jsonParams.put("received_award", received_award.getText().toString().trim());
        jsonParams.put("paycard",(pay_cards.isChecked())?"yes":"no");
        jsonParams.put("homevisit",(home_visit.isChecked())?"yes":"no");
        jsonParams.put("healthtip",(health_tip.isChecked())?"yes":"no");
        jsonParams.put("emergency",(cb_emergency.isChecked())?"yes":"no");
        jsonParams.put("associalted_hospitals",associated_hos.getText().toString().trim());
        jsonParams.put("linkin",linkedin_url.getText().toString().trim());
        jsonParams.put("facebook",fb_url.getText().toString().trim());
    }

    private void updateDoctorUILayout() {
        name = (EditText) findViewById(R.id.user_name);
        name.setText(mydata.getDr_name());
        locationDoc = (TextView) findViewById(R.id.locationDoc);
        locationDoc.setText(mydata.getLocation());
        locationDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EPActivity.this, PlaceActivity.class);
                startActivityForResult(intent, CALLED_ACTIVITY);
            }
        });
        doc_desc = (EditText) findViewById(R.id.doctor_description);
        doc_desc.setText(mydata.getDescription_full());
        doc_clinic = (EditText) findViewById(R.id.doc_clinic);
        doc_clinic.setText(mydata.getClinic_name());
        designation = (EditText) findViewById(R.id.designation);
        designation.setText(mydata.getDesignation());
        email = (EditText) findViewById(R.id.email_id);
        email.setText(mydata.getEmail());
        mobile = (EditText) findViewById(R.id.mobile_number);
        mobile.setText(mydata.getContact());
        received_award = (EditText) findViewById(R.id.received_awards);
        received_award.setText(mydata.getReceived_award());
        hospital_name = (EditText) findViewById(R.id.hospital_affiliations);
        hospital_name.setText(mydata.getHospital_affiliation());
        category = (Spinner) findViewById(R.id.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item_layout, getCategoryList());
        category.setAdapter(adapter);
        leadership = (EditText) findViewById(R.id.leadership_roles);
        leadership.setText(mydata.getLeadership());
        certifications = (EditText) findViewById(R.id.certifications);
        certifications.setText(mydata.getCertifications());
        tvSpecilization = (TextView) findViewById(R.id.tv_specilization);
        tvSpecilization.setText(mydata.getSpecialization());
        tvSpecilization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //splSelection();
            }
        });
        specialization = (Spinner) findViewById(R.id.specilization);
        experence = (EditText) findViewById(R.id.experience);
        experence.setText(mydata.getExperence());
        consultation_fee = (EditText) findViewById(R.id.cosultation_fee);
        consultation_fee.setText(mydata.getConsultation_fee());
        locationAddr = (EditText) findViewById(R.id.address);
        locationAddr.setText(mydata.getAddress());
        locationAddr1 = (EditText) findViewById(R.id.address1);
        website = (EditText) findViewById(R.id.website);
        website.setText(mydata.getWebsite());
        associated_hos = (EditText) findViewById(R.id.associated_hospital);
        associated_hos.setText(mydata.getAssocialted_hospitals());
        pay_cards = (CheckBox) findViewById(R.id.cb_payCard);
        cb_emergency = (CheckBox) findViewById(R.id.cb_Emergency);
        home_visit = (CheckBox) findViewById(R.id.cb_homevisit);
        health_tip = (CheckBox) findViewById(R.id.cb_healthtip);
        linkedin_url = (EditText) findViewById(R.id.linkedin_url);
        linkedin_url.setText(mydata.getLinkin());
        fb_url = (EditText) findViewById(R.id.fb_url);
        fb_url.setText(mydata.getFacebook());
        if(mydata.getEmergency().equalsIgnoreCase("yes")){
            cb_emergency.setChecked(true);
        }
        if(mydata.getPaycard().equalsIgnoreCase("yes")){
            pay_cards.setChecked(true);
        }
        if(mydata.getHealthtip().equalsIgnoreCase("yes")){
            health_tip.setChecked(true);
        }
        if(mydata.getHomevisit().equalsIgnoreCase("yes")){
            home_visit.setChecked(true);
        }
        //getSpecFromServer();
    }

    private List<String> getCategoryList(){
        List<String> categoryList = new ArrayList<String>();
        categoryList.add("Select Category");
        categoryList.add("Category1");
        categoryList.add("Category2");
        categoryList.add("Category3");
        categoryList.add("Category4");

        return categoryList;
    }

    private void getSpecFromServer() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.show();
		/* Post data */
        Map<String, String> jsonParams = new HashMap<String, String>();

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET,
                URLConstants.getDoctorSpecializationsUrl,

                new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        if (response.getString("status").equalsIgnoreCase("fail"))
                            Toast.makeText(getApplicationContext(), response.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        else {
                            Log.i("APPOINTMENTS", response.toString());
                            JSONArray jArray = response.getJSONArray("data");
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            List<IDDetail> posts = new ArrayList<IDDetail>();
                            posts = Arrays.asList(gson.fromJson(jArray.toString(), IDDetail[].class));
                            specailiation = new ArrayList<String>();
//									specailiation.add("Select Speciality");
                            for (IDDetail detail : posts) {
                                specailiation.add(detail.getTitle());
                            }
                            if (specailiation.isEmpty())
                                specailiation.addAll(Arrays.asList(specs));
                            preceivers = new CharSequence[specailiation.size()];
                            int i=0;
                            for(String str : specailiation){
                                preceivers[i] = str;
                                i++;
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.spinner_item_layout, specailiation);
                            if (isDoctor) {
                                Log.i("TYPE OF DOCTOR", "DOCTOR IS: " + isDoctor);
                                specialization.setAdapter(adapter);
                            } else {
                                //hos_specialties.setAdapter(adapter);
                                Log.i("TYPE OF DOCTOR", "DOCTOR IS: " + isDoctor);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pDialog.dismiss();
                doServiceRequest();
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

    public void splSelection(){
        boolean[] checkedReceivers = new boolean[preceivers.length];
        int count = preceivers.length;

        for(int i = 0; i < count-1; i++)
            checkedReceivers[i] = pselectedReceivers.contains(preceivers[i]);

        DialogInterface.OnMultiChoiceClickListener receiversDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    pselectedReceivers.add(preceivers[which]);
                }
                else{
                    pselectedReceivers.remove(preceivers[which]);
                }
                // onChangeSelectedReceivers();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(EPActivity.this);
        builder
                .setTitle("Select Specialization")
                .setMultiChoiceItems(preceivers, checkedReceivers, receiversDialogListener)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isDoctor) {
                            tvSpecilization.setText(" ");
                            tvSpecilization.setText(getSpecialization());
                        }
                        else{
                            tvSpecilizationHos.setText(" ");
                            tvSpecilizationHos.setText(getSpecialization());
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getSpecialization(){
        String splz="";

        StringBuilder tempSB = new StringBuilder();

        for(int i=0;i<pselectedReceivers.size();i++){
            tempSB.append(pselectedReceivers.get(i));
            if(i<pselectedReceivers.size()-1){
                tempSB.append(",");
            }
        }

        splz = tempSB.toString();

        return splz;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    File file = new File(getRealPathFromURI(getApplicationContext(), imageUri));

                    if(!BZAppApplication.certificate) {
                        //browse_txt.setText(file.getAbsolutePath().toString());
                        mFiles.add(file);
                        if(BZAppApplication.selectedImage==1){
                            Picasso.with(EPActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit().into((img01));
                        }
                        else if(BZAppApplication.selectedImage==2){
                            Picasso.with(EPActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit().into((img02));
                        }
                        else if(BZAppApplication.selectedImage==3){
                            Picasso.with(EPActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit().into((img03));
                        }
                        else if(BZAppApplication.selectedImage==4){
                            Picasso.with(EPActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit().into((img04));
                        }
                        else if(BZAppApplication.selectedImage==5){
                            Picasso.with(EPActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit().into((img05));
                        }
                    }
                    else{
                        browse_txt_doc.setText(file.getAbsolutePath().toString());
                        mFile = file;
                        BZAppApplication.certificate = false;
                    }


//					arrayFile[0] = file;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if(reqCode == CALLED_ACTIVITY){
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                locName = data.getStringExtra("locationDetails");
                BZAppApplication.mLocation = locName;
                Log.d("Location Address", "" + locName);
                //apiCall();

                if(isDoctor){
                    locationDoc.setText("");
                    locationDoc.setText(locName);
                }
                else{
                    location_hosNew.setText("");
                    location_hosNew.setText(locName);
                }
            }
        }
    }

    public void addtoPrefs() {
        if(isDoctor) {
            pref.setMobileNumber(mobile.getText().toString().trim());
            pref.setName(name.getText().toString().trim());
            pref.setEmail(email.getText().toString().trim());
            pref.setAssociatedHospital(hospital_name.getText().toString().trim());
        }
        else{
            pref.setMobileNumber(hos_contact.getText().toString().trim());
            pref.setName(hos_name.getText().toString().trim());
            pref.setEmail(hos_email.getText().toString().trim());
            pref.setAssociatedHospital(hos_name.getText().toString().trim());
//            hos_name
        }

       // pref.setAssociatedHospital(hospital_name.getText().toString().trim());
        pref.setSpecialization(getSpecialization());

//        Intent nextScreen = new Intent(this, MobileVerificationActivity.class);
//        startActivity(nextScreen);
//        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
//        finish();
    }

    public boolean isValidData() {
        boolean isValid = true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String webPattern = "[a-zA-Z0-9._-]+[a-z]+\\.+[a-z]+";
        if (hos_email != null && hos_email.getText().toString().trim().equalsIgnoreCase("")
                && !emailValidator(hos_email.getText().toString().trim())) {
            if (!hos_email.getText().toString().matches(emailPattern)) {
                isValid = false;
                hos_email.setError("Invalid email");
                Toast.makeText(getApplicationContext(),"Invalid Email Id",Toast.LENGTH_LONG);
            }
        }
        if (isDoctor && email != null && email.getText().toString().trim().equalsIgnoreCase("")) {
            if (!email.getText().toString().matches(emailPattern)) {
                isValid = false;
                email.setError("Invalid email");
                Toast.makeText(getApplicationContext(),"Invalid Email Id",Toast.LENGTH_LONG);
            }
        }
        if (isDoctor && email != null && !emailValidator(email.getText().toString().trim())) {
            isValid = false;
            email.setError("Invalid Email");
        }
        if (isDoctor && mobile != null && mobile.getText().toString().trim().equalsIgnoreCase("")) {
            isValid = false;
            mobile.setError("Empty Field");
        }
        if (isDoctor && mobile != null && mobile.getText().toString().trim().length() < 10) {
            isValid = false;
            mobile.setError("please enter correct mobile number");
        }
        if(!isDoctor && hos_contact!=null && hos_contact.getText().toString().trim().length()!=10){
            isValid = false;
            hos_contact.setError("Enter correct mobile number");
        }

        if (!isDoctor && hos_contact != null && hos_contact.getText().toString().trim().equalsIgnoreCase("")) {
            isValid = false;
            hos_contact.setError("Empty Field");
        }

		/*if(isDoctor && website.getText().toString()!=null){
			if(!android.util.Patterns.DOMAIN_NAME.matcher(website.getText().toString()).matches()){
				isValid = false;
				website.setError("Invalid website name");
				Toast.makeText(getApplicationContext(),"Invalid website name",Toast.LENGTH_LONG);
			}
		}*/
		/*if(!isDoctor && hos_website.getText().toString()!=null){
			if(!android.util.Patterns.DOMAIN_NAME.matcher(hos_website.getText().toString()).matches()){
				isValid = false;
				website.setError("Invalid website name");
				Toast.makeText(getApplicationContext(),"Invalid website name",Toast.LENGTH_LONG);
			}
		}*/

        if (hos_contact != null && hos_contact.getText().toString().trim().length() < 10) {
            isValid = false;
            hos_contact.setError("please enter correct mobile number");
        }
        if (hos_immg_contact != null && hos_immg_contact.getText().toString().trim().equalsIgnoreCase("")) {
            isValid = false;
            hos_immg_contact.setError("Empty Field");
        }
        if (hos_immg_contact != null && hos_immg_contact.getText().toString().trim().length() < 10) {
            isValid = false;
            hos_immg_contact.setError("please enter correct mobile number");
        }

        /*if (isRegister && password.getText().toString().trim().equalsIgnoreCase("")) {
            isValid = false;
            password.setError("Empty Field");
        }
        if (isRegister && confirmpassword.getText().toString().trim().equalsIgnoreCase("")) {
            isValid = false;
            confirmpassword.setError("Empty Field");
        }
        if (isRegister && !password.getText().toString().trim()
                .equalsIgnoreCase(confirmpassword.getText().toString().trim())) {
            password.setError("Password mismatch");
            confirmpassword.setError("Password mismatch");
            isValid = false;
        }*/
        /*if(pselectedReceivers.size()==0){
            isValid = false;
            Toast.makeText(getApplicationContext(),"Select specilization",Toast.LENGTH_LONG);
        }*/
        return isValid;
    }


    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
