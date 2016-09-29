package bizapps.com.healthforusDoc.utills;

public class URLConstants {
  public static final String DR_BASE_URL = "http://medico4us.in/";
	private static final String baseUrl = "http://doctorapp.rakyow.com/userapp/";
	private static final String patient_baseUrl = baseUrl + "patientapi/";
	private static final String doctor_base_url = baseUrl + "api/";

	private static final String mobile_verification_url = "verifycode";
	private static final String login_url = "login";
	private static final String register_url = "register";
	private static final String resend_verification_code_url = "resendcode";
	private static final String forgot_password_url = "resetpassword";
	private static final String change_password_url = "changepassword";
	private static final String add_user_url = "add_user";
	private static final String update_profile = "update";
	private static final String appointment = "apidoc/appointment";
	private static final String appointment_accept = "apidoc/appointment_accept";
	private static final String appointment_reject = "apidoc/appointment_reject";
	private static final String schedule_appointments = "apidoc/appointment_timing";
	private static final String cancel_scheduled_appointments = "apidoc/appointment_avability";
	private static final String blog_list = "apidoc/blog_api_list";
	private static final String specializations = "apidoc/specializations";

	public static final String getDoctorSpecializationsUrl = baseUrl + specializations;
	public static final String getDoctorLoginUrl = doctor_base_url + login_url;
	public static final String getDoctormobileverficationUrl = doctor_base_url + mobile_verification_url;
	public static final String getDoctorregisterUrl = doctor_base_url + register_url;
	public static final String getDoctorResendMobileCodeUrl = doctor_base_url + resend_verification_code_url;
	public static final String getDoctorForgotPasswordUrl = doctor_base_url + forgot_password_url;
	public static final String getDoctorChangePasswordUrl = doctor_base_url + change_password_url;
	public static final String getDoctorAddUserUrl = doctor_base_url + add_user_url;
	public static final String getDoctorUpdateProfileUrl = doctor_base_url + update_profile;
	public static final String getDoctorAppointmentsUrl = baseUrl + appointment;
	public static final String getDoctorAppointmentAcceptUrl = baseUrl + appointment_accept;
	public static final String getDoctorScheduleAppointmentUrl = baseUrl + schedule_appointments;
	public static final String getDoctorAppointmentRejectUrl = baseUrl + appointment_reject;
	public static final String getDoctorCancelScheduleAppointmentUrl = baseUrl + cancel_scheduled_appointments;
	public static final String getDoctorBlogListUrl = baseUrl + blog_list;

	public static final String getPatientLoginUrl = patient_baseUrl + login_url;
	public static final String getPatientmobileverficationUrl = patient_baseUrl + mobile_verification_url;
	public static final String getPatientregisterUrl = patient_baseUrl + register_url;
	public static final String getPatientResendMobileCodeUrl = patient_baseUrl + resend_verification_code_url;
	public static final String getPatientForgotPasswordUrl = patient_baseUrl + forgot_password_url;
	public static final String getPatientChangePasswordUrl = patient_baseUrl + change_password_url;
	public static final String getPatientAddUserUrl = patient_baseUrl + add_user_url;
	public static final String getPatientUpdateProfileUrl = patient_baseUrl + update_profile;
}
