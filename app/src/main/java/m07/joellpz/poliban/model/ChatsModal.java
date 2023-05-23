package m07.joellpz.poliban.model;

public class ChatsModal {

    private String message; // tells the message.
    private String sender; // tells whether it is bot or human because of multiple recyclerViews.

    public ChatsModal(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
