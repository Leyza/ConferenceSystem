package entities;

import java.io.Serializable;
import java.util.UUID;

/**
 * A UserRequest entity. Stores UUID of the request sender, content of the request, status and associated event UUID.
 */
public class UserRequest implements Serializable {
    private final UUID id;
    private UUID senderId;
    private String content;
    private boolean isAddressed;
    private UUID associatedEventId;

    /**
     * Constructor for UserRequest
     * @param senderId UUID of request sender
     * @param content String representation of request
     * @param eventId UUID of associated event
     */
    public UserRequest(UUID senderId, String content, UUID eventId){
        this.id = UUID.randomUUID();
        this.senderId = senderId;
        this.content= content;
        this.isAddressed= false;
        this.associatedEventId = eventId;
    }

    /**
     * @return if the request is addressed
     */
    public boolean getIsAddressed(){
        return isAddressed;
    }

    /**
     * @return Returns sender's ID
     */
    public UUID getSenderId(){
        return senderId;
    }
    /**
     * @return Returns Event's ID
     */
    public UUID getAssociatedEventId(){
        return associatedEventId;
    }

    /**
     * @return Returns request content
     */
    public String getContent(){
        return content;
    }

    /**
     * @return Returns UserRequest UUID
     */
    public UUID getId(){
        return id;
    }

    /**
     * Sets isAddressed to the supplied parameter
     * @param isAddressed Whether or not the request has been addressed.
     */
    public void setIsAddressed(boolean isAddressed){
         this.isAddressed = isAddressed;
    }

}
