package com.linburgtech.collegeapp_linburg2019;

public class Guardian extends FamilyMember {
    String firstName;
    String lastName;


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

    public Guardian(){
        super();
    }

    public Guardian(String firstName, String lastName){
        super(firstName, lastName);
    }



}
