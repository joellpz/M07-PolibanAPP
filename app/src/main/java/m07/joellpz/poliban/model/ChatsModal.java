package m07.joellpz.poliban.model;

/**
 * Class representing a chat message.
 */
public class ChatsModal {

    private String message; // Stores the message content.
    private String sender; // Indicates the sender of the message (bot or human).

    /**
     * Constructor for the ChatsModal class.
     *
     * @param message The message content.
     * @param sender  The sender of the message.
     */
    public ChatsModal(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    /**
     * Retrieves the message content.
     *
     * @return The message content.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content.
     *
     * @param message The message content to be set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieves the sender of the message.
     *
     * @return The sender of the message.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender of the message.
     *
     * @param sender The sender of the message.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
}
