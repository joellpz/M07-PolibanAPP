package m07.joellpz.poliban.model;

import java.util.HashMap;
import java.util.Objects;


public class User {


    private String uid, profileName, profilePhone, profileDirection, profileCP, profilePhoto;
    private HashMap<String,Boolean> bankAccounts = new HashMap<String, Boolean>();

    public User(String uid, String profileName, String profilePhone, String profileDirection, String profileCP, String profilePhoto,HashMap<String,Boolean> bankAccounts) {
        this.uid = uid;
        this.profileName = profileName;
        this.profilePhone = profilePhone;
        this.profileDirection = profileDirection;
        this.profileCP = profileCP;
        this.profilePhoto = profilePhoto;
        this.bankAccounts = bankAccounts;
    }

    public User() {
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setProfilePhone(String profilePhone) {
        this.profilePhone = profilePhone;
    }

    public void setProfileDirection(String profileDirection) {
        this.profileDirection = profileDirection;
    }

    public void setProfileCP(String profileCP) {
        this.profileCP = profileCP;
    }

    public String getUid() {
        return uid;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getProfilePhone() {
        return profilePhone;
    }

    public String getProfileDirection() {
        return profileDirection;
    }

    public String getProfileCP() {
        return profileCP;
    }
}
