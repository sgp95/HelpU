package com.sgp95.santiago.helpu.model;

/**
 * Created by Hiraoka on 22/06/2017.
 */

public class User {

    public String userCode;
    public String userPassword;

    public User(){}

    public User(String userCode, String userPassword) {
        this.userCode = userCode;
        this.userPassword = userPassword;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
