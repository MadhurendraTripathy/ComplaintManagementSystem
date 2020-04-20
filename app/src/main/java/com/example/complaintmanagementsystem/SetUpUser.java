package com.example.complaintmanagementsystem;

public class SetUpUser {

    String UnitID;
    String Fname;
    String Lname;

    public SetUpUser(String UnitID,String Fname,String Lname) {
        this.UnitID = UnitID;
        this.Fname = Fname;
        this.Lname = Lname;
    }


    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }
}
