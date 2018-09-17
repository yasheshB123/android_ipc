package com.droidipc.serviceProvider.data;

import com.google.gson.annotations.Expose;

public class CallInfoModel {

    @Expose
    private String number;

    @Expose
    private String name;

    @Expose
    private String callType;

    @Expose
    private String duration;

    @Expose
    private String date;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
