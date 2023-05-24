package m07.joellpz.poliban.model;

/**
 * Class representing a message.
 */
public class MsgModal {
    /**
     * The content of the message.
     */
    private String cnt;

    /**
     * Constructor for the MsgModal class.
     *
     * @param cnt The content of the message.
     */
    public MsgModal(String cnt) {
        this.cnt = cnt;
    }

    /**
     * Retrieves the content of the message.
     *
     * @return The content of the message.
     */
    public String getCnt() {
        return cnt;
    }

    /**
     * Sets the content of the message.
     *
     * @param cnt The content of the message.
     */
    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}
