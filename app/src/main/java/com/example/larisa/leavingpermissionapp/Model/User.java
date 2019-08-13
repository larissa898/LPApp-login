/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {


    private String id;
    private String lastName;
    private String firstName;
    private String role;
    private String phoneNumber;
    private String registrationNumber;
    private String teamLeader;
    private List<LeavingPermission> leavingPermissionList;

    public User() {
    }

    public User(String lastName, String firstName, String role, String phoneNumber, String registrationNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.registrationNumber = registrationNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public List<LeavingPermission> getLeavingPermissionList() {
        return leavingPermissionList;
    }

    public void setLeavingPermissionList(List<LeavingPermission> leavingPermissionList) {
        this.leavingPermissionList = leavingPermissionList;
    }

    public String getFullName() {
        return this.lastName + " " + this.firstName;
    }

    @Override
    public String toString() {
        return "User{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", role='" + role + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", teamLeader='" + teamLeader + '\'' +
                '}';
    }
}
