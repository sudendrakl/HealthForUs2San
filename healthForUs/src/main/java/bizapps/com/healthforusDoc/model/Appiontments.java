package bizapps.com.healthforusDoc.model;

public class Appiontments {
	private int id;
	private String doctors_id;
	private int appointment_schedule_id;
	private String patients_id;
	private String diseases_description;
	private String datetime_start;
	private String datetime_end;
	private int status_id;
	private String pat_name;
	private String status;

	public String getContact() {
		return contact;
	}

	private String contact;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDoctors_id() {
		return doctors_id;
	}
	public void setDoctors_id(String doctors_id) {
		this.doctors_id = doctors_id;
	}
	public int getAppointment_schedule_id() {
		return appointment_schedule_id;
	}
	public void setAppointment_schedule_id(int appointment_schedule_id) {
		this.appointment_schedule_id = appointment_schedule_id;
	}
	public String getPatients_id() {
		return patients_id;
	}
	public void setPatients_id(String patients_id) {
		this.patients_id = patients_id;
	}
	public String getDiseases_description() {
		return diseases_description;
	}
	public void setDiseases_description(String diseases_description) {
		this.diseases_description = diseases_description;
	}
	public String getDatetime_start() {
		return datetime_start;
	}
	public void setDatetime_start(String datetime_start) {
		this.datetime_start = datetime_start;
	}
	public String getDatetime_end() {
		return datetime_end;
	}
	public void setDatetime_end(String datetime_end) {
		this.datetime_end = datetime_end;
	}
	public int getStatus_id() {
		return status_id;
	}
	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}
	public String getPat_name() {
		return pat_name;
	}
	public void setPat_name(String pat_name) {
		this.pat_name = pat_name;
	}
}