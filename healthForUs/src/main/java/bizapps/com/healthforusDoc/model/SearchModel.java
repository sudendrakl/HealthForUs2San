package bizapps.com.healthforusDoc.model;

import java.util.List;

/**
 * Created by venkatat on 7/3/2016.
 */
public class SearchModel {
    public String status;

    public String getMessage() {
        return message;
    }

    public String message;
    public GetData getData() {
        return data;
    }

    public void setData(GetData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GetData data;

    public class GetData{
        public String id;
        public String name;
        public String guid;
        public String mobile;
        public String email;
        public String specialization;
        public String location;
        public String associalted_hospitals;
        public String other_information;
        public String hospital_pic;
        public int rating;
        public String category;
        public String ambulace_no;
        public String certification_no;
        public String ownwrship;
        public String our_doctors;
        public String opd_timing;

        public String getDesignation() {
            return designation;
        }

        public String designation;

        public String getAffiliated_by() {
            return affiliated_by;
        }

        public String affiliated_by;

        public String getType() {
            return type;
        }

        public String type;

        public String getFacilities() {
            return facilities;
        }

        public String getOpt_timing() {
            return opd_timing;
        }

        public String getOur_doctor() {
            return our_doctors;
        }

        public String getOwnwrship() {
            return ownwrship;
        }

        public String getCertification_no() {
            return certification_no;
        }

        public String getAmbulace_no() {
            return ambulace_no;
        }

        public String getCategory() {
            return category;
        }

        public String facilities;
        public String getFacebook() {
            return facebook;
        }

        public String getLinkin() {
            return linkin;
        }

        public String facebook,linkin;
        public List<GetReview> getReview() {
            return review;
        }

        public int getRating() {
            return rating;
        }

        public List<GetReview> review;

        public List<String> getDates() {
            return dates;
        }

        public List<String> dates;
        public String getDr_name() {
            return dr_name;
        }

        public String getSpaceification() {
            return spaceification;
        }

        public String getExperence() {
            return experence;
        }

        public String getConsultation_fee() {
            return consultation_fee;
        }

        public String getContact() {
            return contact;
        }

        public String getFax() {
            return fax;
        }

        public String getWebsite() {
            return website;
        }

        public String getSpecialties() {
            return specialties;
        }

        public String getHospital_affiliation() {
            return hospital_affiliation;
        }

        public String getLeadership() {
            return leadership;
        }

        public String getFellowship() {
            return fellowship;
        }

        public String getCertifications() {
            return certifications;
        }

        public String getReceived_award() {
            return received_award;
        }

        public String dr_name;
        public String spaceification;
        public String experence;
        public String consultation_fee;
        public String contact;
        public String fax;
        public String website;
        public String specialties;
        public String hospital_affiliation;
        public String leadership;
        public String fellowship;
        public String certifications;
        public String received_award;

        public String getAddress() {
            return address;
        }

        public String getDescription() {
            return description;
        }

        public String getEmergency() {
            return emergency;
        }

        public String getPaycard() {
            return paycard;
        }

        public String getHomevisit() {
            return homevisit;
        }

        public String getHealthtip() {
            return healthtip;
        }

        public String address;
        public String description;
        public String emergency;
        public String paycard;
        public String homevisit;
        public String healthtip;

        public List<String> getImage() {
            return image;
        }

        public List<String> image;

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getHospital_pic() {
            return hospital_pic;
        }

        public void setHospital_pic(String hospital_pic) {
            this.hospital_pic = hospital_pic;
        }

        public String getOther_information() {
            return other_information;
        }

        public void setOther_information(String other_information) {
            this.other_information = other_information;
        }

        public String getAssocialted_hospitals() {
            return associalted_hospitals;
        }

        public void setAssocialted_hospitals(String associalted_hospitals) {
            this.associalted_hospitals = associalted_hospitals;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        public String clinic_name;

        public String getAccredity() {
            return accredity;
        }

        public void setAccredity(String accredity) {
            this.accredity = accredity;
        }

        public String accredity;

        public String getImmg_contact() {
            return immg_contact;
        }

        public void setImmg_contact(String immg_contact) {
            this.immg_contact = immg_contact;
        }

        public String immg_contact;

        public String getDescription_full() {
            return description_full;
        }

        public void setDescription_full(String description_full) {
            this.description_full = description_full;
        }

        public String getClinic_name() {
            return clinic_name;
        }

        public void setClinic_name(String clinic_name) {
            this.clinic_name = clinic_name;
        }

        public String description_full;
        public String video;
    }

    public class GetReview{
        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public String name,comment;
    }
}
