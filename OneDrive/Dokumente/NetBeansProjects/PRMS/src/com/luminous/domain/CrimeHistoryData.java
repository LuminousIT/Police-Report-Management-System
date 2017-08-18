/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous.domain;

import com.luminous.Start;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.ButtonBuilder;

/**
 *
 * @author Luminous
 */
public class CrimeHistoryData {
    int sn;
    String name, phone, gender, address, caseType, statement, caseId;
    String date;
    Node action;
//    private final int id;
//
//    public int getId() {
//        return id;
//    }

    public int getSn() {
        return sn;
    }
    
    public void setSn(int sn) {
        this.sn = sn;
    }
    
    public Node getAction() {
        return action;
    }

    public void setAction(Node action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String cDate) {
        this.date = cDate;
    }
    
    public CrimeHistoryData(Start app, int x, ObservableMap<String, Object> map) {
        this.name = (String) map.get("name");
        this.address = (String) map.get("address");
        this.date = ((Timestamp) map.get("cDate")).toLocalDateTime().format(DateTimeFormatter.ISO_DATE);
        this.caseId =(String)  map.get("caseId");
        this.caseType = (String) map.get("casetype");
        this.gender = (String) map.get("gender");
        this.phone = (String) map.get("phone");
        this.statement = (String) map.get("statement");
        this.sn = x;
        //this.id = (int) map.get("id");
        
        action = ButtonBuilder.create().text("View").styleClass("green-button").onAction((ae) -> {
            app.crmHistory.viewStatement(sn - 1); //view statement of a particular officer sn(numOfOffcicer)
        }).build();
    }
}
