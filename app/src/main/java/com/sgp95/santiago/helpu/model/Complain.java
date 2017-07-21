package com.sgp95.santiago.helpu.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private String Headquarter;
    private String Image;
    private String FullName;
    private String comCounter;
    private DatabaseReference mFirebaseDatabase;

    public Complain(){}
    public Complain(String complain, String complainImage, String complaintId,
                    String dateCreated, String userCode, String privacy, String state, String category, String headquarter,
                    String userimg , String fullname, FirebaseDatabase mFirebaseDatabase) {
        Complain = complain;
        ComplainImage = complainImage;
        ComplaintId = complaintId;
        DateCreated = dateCreated;
        UserCode = userCode;
        Privacy = privacy;
        State = state;
        Category = category;
        Headquarter = headquarter;
        Image = userimg;
        FullName = fullname;
        mFirebaseDatabase = mFirebaseDatabase;
    }


    public DatabaseReference getmFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    public void setmFirebaseDatabase(DatabaseReference mFirebaseDatabase) {
        this.mFirebaseDatabase = mFirebaseDatabase;
    }

    public String getHeadquarter() {
        return Headquarter;
    }

    public void setHeadquarter(String headquarter) {
        Headquarter = headquarter;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUserImg() {
        return Image;
    }

    public void setUserImg(String userImg) {
        Image = userImg;
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

    public void setComCounter(String comCounter) {
        this.comCounter = comCounter;
    }

    public String getComCounter() {
        return comCounter;
    }
}
