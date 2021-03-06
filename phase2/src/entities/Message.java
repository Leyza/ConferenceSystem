package entities;

import java.io.Serializable;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 * A message entity. Stores time of sending, the sender's Id, and the message content.
 */
public class Message implements Serializable {

    private UUID id;
    private LocalDateTime dateAndTime;
    private UUID senderId;
    private String content;

	/**
	 * The constructor for Message
	 * @param senderId The sender's UUID
	 * @param content The content of the message
	 */
	public Message(UUID senderId, String content){
        this.id = UUID.randomUUID();
        this.dateAndTime = LocalDateTime.now(); //Should the time be a parameter instead?
        this.senderId = senderId;
        this.content = content;
    }

	/**
	 * @return Returns this message's Id
	 */
	public UUID getId(){
        return id;
    }

	/**
	 * @return Returns this message's time of sending
	 */
	public LocalDateTime getTime(){
        return dateAndTime;
    }

	/**
	 * @return Returns this message's sender Id
	 */
	public UUID getSenderId(){
        return senderId;
    }

	/**
	 * @return Returns this message's content
	 */
	public String getContent(){
        return content;
    }

}
