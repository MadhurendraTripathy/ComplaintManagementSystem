package com.example.complaintmanagementsystem;

import android.net.Uri;

import java.io.Serializable;

public class ComplaintParameters implements Serializable { // implements Serializable is to be able to use bundles to pass arraylists among activities
    String unitid;
    String category;
    String severity;
    String impact;
    String title;
    String description;
    String url;
    String assignedTo;
    String ComplainId;
    String assigneeContactNumber;

    ComplaintParameters(){}

    ComplaintParameters(String unitid,String category,String severity,String impact,String title,String description,String url,String assignedTo,String assigneeContactNumber){
        this.unitid = unitid;
        this.category = category;
        this.severity = severity;
        this.impact = impact;
        this.title = title;
        this.description = description;
        this.url = url;
        this.assignedTo = assignedTo;
        this.assigneeContactNumber = assigneeContactNumber;
    }

    ComplaintParameters(String ComplaintId){
        this.ComplainId = ComplaintId;
    }

    public String getUnitid() {
        return unitid;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getComplainId() {
        return ComplainId;
    }

    public String getCategory() {
        return category;
    }

    public String getAssigneeContactNumber() {
        return assigneeContactNumber;
    }

    public String getSeverity() {
        return severity;
    }

    public String getImpact() {
        return impact;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
