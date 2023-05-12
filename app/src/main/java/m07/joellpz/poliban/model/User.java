package m07.joellpz.poliban.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class User {


    private String uid, profileName, profilePhone, profileDirection, profileCP, profilePhoto;
    private List<String> bankAccounts = new ArrayList<>();

    public User(String uid, String profileName, String profilePhone, String profileDirection, String profileCP, String profilePhoto,List<String> bankAccounts) {
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

    public List<String> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<String> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
