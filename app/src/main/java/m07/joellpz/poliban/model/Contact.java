package m07.joellpz.poliban.model;

/**
 * Class representing a contact.
 */
public class Contact {
    /**
     * The name of the contact.
     */
    private String name;

    /**
     * The phone number of the contact.
     */
    private String phone;

    /**
     * Constructor for the Contact class.
     *
     * @param name  The name of the contact.
     * @param phone The phone number of the contact.
     */
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * Retrieves the name of the contact.
     *
     * @return The name of the contact.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the contact.
     *
     * @param name The name of the contact.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the phone number of the contact.
     *
     * @return The phone number of the contact.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the contact.
     *
     * @param phone The phone number of the contact.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
