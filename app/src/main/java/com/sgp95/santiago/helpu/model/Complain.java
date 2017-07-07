package com.sgp95.santiago.helpu.model;

/**
 * Created by Hiraoka on 16/06/2017.
 */

public class Complain {
    private String Complain;
    private String ComplainImage;
    private String ComplaintId;
    private String DateCreated;
    private String UserCode;
    private String Privacy;
    private String State;
    private String Category;

    public Complain(){}
    public Complain(String complain, String complainImage, String complaintId,
                    String dateCreated, String userCode, String privacy, String state, String category) {
        Complain = complain;
        ComplainImage = complainImage;
        ComplaintId = complaintId;
        DateCreated = dateCreated;
        UserCode = userCode;
        Privacy = privacy;
        State = state;
        Category = category;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getComplain() {
        return Complain;
    }

    public void setComplain(String complain) {
        Complain = complain;
    }

    public String getComplainImage() {
        return ComplainImage;
    }

    public void setComplainImage(String complainImage) {
        ComplainImage = complainImage;
    }

    public String getComplaintId() {
        return ComplaintId;
    }

    public void setComplaintId(String complaintId) {
        ComplaintId = complaintId;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public String getPrivacy() {
        return Privacy;
    }

    public void setPrivacy(String privacy) {
        Privacy = privacy;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
