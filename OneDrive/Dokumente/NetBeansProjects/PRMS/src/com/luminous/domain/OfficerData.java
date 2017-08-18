/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous.domain;

import com.luminous.Start;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.layout.HBox;

/**
 *
 * @author samth
 */
public class OfficerData {

    int sn;
    String name, phone, rank;
    Node details;
    Button viewOffBut;
    
    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Node getDetails() {
        return details;
    }

    public void setDetails(Node details) {
        this.details = details;
    }
    
    
    public OfficerData(Start app, int x, ObservableMap<String, Object> officers) {
        this.sn = x;
        this.name = (String) officers.get("name");
        this.phone = (String) officers.get("phone");
        this.rank = (String) officers.get("rank");
        
        details = new HBox(viewOffBut = ButtonBuilder.create().text("View Details").prefWidth(150).onAction(ae -> {
                    app.regdOfficers.showDetailsPane(sn - 1);
                }).build() );
    }
    
}
