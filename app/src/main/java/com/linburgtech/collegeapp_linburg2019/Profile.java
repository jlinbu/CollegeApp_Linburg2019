package com.linburgtech.collegeapp_linburg2019;

import java.util.Date;

public class Profile {

    String lastName;
    String firstName;
    Date dateOfBirth;
    String objectId;
    String email;

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

    public Profile(){
        dateOfBirth = new Date();
        lastName = "Data";
        firstName = "New";
        email = "jlinburg@doversd.org";
    }

    public String getObjectId(){
        return objectId;
    }
    public void setObjectId(String objectID){
        this.objectId = objectID;
    }

}
