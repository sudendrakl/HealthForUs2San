package bizapps.com.healthforusDoc.utills;

public interface URLConstants {
  String DR_BASE_URL = "http://medico4us.in/";
	String baseUrl = "http://doctorapp.rakyow.com/userapp/";
	//String patient_baseUrl = baseUrl + "patientapi/";
	String doctor_base_url = baseUrl + "api/";

	//String mobile_verification_url = "verifycode";
	//String login_url = "login";
	//String register_url = "register";
	//String resend_verification_code_url = "resendcode";
	//String forgot_password_url = "resetpassword";
	//String change_password_url = "changepassword";
	//String add_user_url = "add_user";
	String update_profile = "update";
	//String appointment = "apidoc/appointment";
	//String appointment_accept = "apidoc/appointment_accept";
	//String appointment_reject = "apidoc/appointment_reject";
	//String schedule_appointments = "apidoc/appointment_timing";
	String cancel_scheduled_appointments = "apidoc/appointment_avability";
	String blog_list = "apidoc/blog_api_list";
	String specializations = "apidoc/specializations";

	String getDoctorSpecializationsUrl = baseUrl + specializations;
	//String getDoctorLoginUrl = doctor_base_url + login_url;
	//String getDoctormobileverficationUrl = doctor_base_url + mobile_verification_url;
	//String getDoctorregisterUrl = doctor_base_url + register_url;
	String getDoctorResendMobileCodeUrl = DR_BASE_URL + "doctorResendOTP.php";
	//String getDoctorForgotPasswordUrl = doctor_base_url + forgot_password_url;
	//String getDoctorChangePasswordUrl = doctor_base_url + change_password_url;
	//String getDoctorAddUserUrl = doctor_base_url + add_user_url;
	String getDoctorUpdateProfileUrl = doctor_base_url + update_profile;
	//String getDoctorAppointmentsUrl = baseUrl + appointment;
	//String getDoctorAppointmentAcceptUrl = baseUrl + appointment_accept;
	String getDoctorScheduleAppointmentUrl = URLConstants.DR_BASE_URL + "appointment_timings.php";
	//String getDoctorAppointmentRejectUrl = baseUrl + appointment_reject;
	String getDoctorCancelScheduleAppointmentUrl = baseUrl + cancel_scheduled_appointments;
	String getDoctorBlogListUrl = baseUrl + blog_list;

	//String getPatientLoginUrl = patient_baseUrl + login_url;
	//String getPatientmobileverficationUrl = patient_baseUrl + mobile_verification_url;
	//String getPatientregisterUrl = patient_baseUrl + register_url;
	//String getPatientResendMobileCodeUrl = patient_baseUrl + resend_verification_code_url;
	//String getPatientForgotPasswordUrl = patient_baseUrl + forgot_password_url;
	//String getPatientChangePasswordUrl = patient_baseUrl + change_password_url;
	//String getPatientAddUserUrl = patient_baseUrl + add_user_url;
	//String getPatientUpdateProfileUrl = patient_baseUrl + update_profile;
}
