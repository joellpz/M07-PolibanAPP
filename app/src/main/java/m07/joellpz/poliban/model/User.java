package m07.joellpz.poliban.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the system.
 */
public class User {

    /**
     * User ID
     */
    private String uid;
    /**
     * User's profile name
     */
    private String profileName;
    /**
     * User's phone number
     */
    private String profilePhone;
    /**
     * User's address
     */
    private String profileDirection;
    /**
     * User's postal code
     */
    private String profileCP;
    /**
     * User's profile photo URL
     */
    private String profilePhoto;
    /**
     * List of user's bank accounts
     */
    private List<String> bankAccounts = new ArrayList<>();

    /**
     * Constructs a User object with the specified properties.
     *
     * @param uid              User ID
     * @param profileName      User's profile name
     * @param profilePhone     User's phone number
     * @param profileDirection User's address
     * @param profileCP        User's postal code
     * @param profilePhoto     User's profile photo URL
     * @param bankAccounts     List of user's bank accounts
     */
    public User(String uid, String profileName, String profilePhone, String profileDirection, String profileCP, String profilePhoto, List<String> bankAccounts) {
        this.uid = uid;
        this.profileName = profileName;
        this.profilePhone = profilePhone;
        this.profileDirection = profileDirection;
        this.profileCP = profileCP;
        this.profilePhoto = profilePhoto;
        this.bankAccounts = bankAccounts;
    }

    /**
     * Default constructor for the User class.
     */
    public User() {
    }

    /**
     * Retrieves the URL of the user's profile photo.
     *
     * @return The profile photo URL
     */
    public String getProfilePhoto() {
        return profilePhoto;
    }

    /**
     * Sets the URL of the user's profile photo.
     *
     * @param profilePhoto The profile photo URL
     */
    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    /**
     * Retrieves the user ID.
     *
     * @return The user ID
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the user ID.
     *
     * @param uid The user ID
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Retrieves the user's profile name.
     *
     * @return The profile name
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Sets the user's profile name.
     *
     * @param profileName The profile name
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * Retrieves the user's phone number.
     *
     * @return The phone number
     */
    public String getProfilePhone() {
        return profilePhone;
    }

    /**
     * Sets the user's phone number.
     *
     * @param profilePhone The phone number
     */
    public void setProfilePhone(String profilePhone) {
        this.profilePhone = profilePhone;
    }

    /**
     * Retrieves the user's address.
     *
     * @return The address
     */
    public String getProfileDirection() {
        return profileDirection;
    }

    /**
     * Sets the user's address.
     *
     * @param profileDirection The address
     */
    public void setProfileDirection(String profileDirection) {
        this.profileDirection = profileDirection;
    }

    /**
     * Retrieves the user's postal code.
     *
     * @return The postal code
     */
    public String getProfileCP() {
        return profileCP;
    }

    /**
     * Sets the user's postal code.
     *
     * @param profileCP The postal code
     */
    public void setProfileCP(String profileCP) {
        this.profileCP = profileCP;
    }

    /**
     * Retrieves the list of user's bank accounts.
     *
     * @return The list of bank accounts
     */
    public List<String> getBankAccounts() {
        return bankAccounts;
    }

    /**
     * Sets the list of user's bank accounts.
     *
     * @param bankAccounts The list of bank accounts
     */
    public void setBankAccounts(List<String> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
