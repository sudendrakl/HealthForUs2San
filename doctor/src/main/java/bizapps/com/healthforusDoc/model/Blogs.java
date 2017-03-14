package bizapps.com.healthforusDoc.model;

public class Blogs {

	private int id;
	private String guid, description, userid, file_record;
	
	public String getFile_record() {
		return file_record;
	}
	public void setFile_record(String file_record) {
		this.file_record = file_record;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
