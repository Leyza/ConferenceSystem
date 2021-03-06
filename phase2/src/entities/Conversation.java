package entities;

import enums.ConvoType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A conversation entity. Stores messages and conversation participants.
 */
public class Conversation implements Serializable {

    private UUID id;
    private ArrayList<Message> messageList;
    private List<UUID> participants;
    private ConvoType type;
    private String eventName;

	/**
	 * The constructor for Conversation.
	 * @param participants A list of the conversation's participant UUID's
	 * @param type The type of conversation, can be either 'DIRECT', 'EVENT', or 'ORGANIZER'.
	 */
	public Conversation(List<UUID> participants, ConvoType type){
        this.id = UUID.randomUUID();
        this.participants = participants;
        this.messageList = new ArrayList<Message>();
        this.type = type;
    }

	/**
	 * @param message The message to be added
	 */
	public void addMessage(Message message){
        messageList.add(message);
    }

	/**
	 * @return Returns this conversation's Id
	 */
	public UUID getId(){
        return id;
    }

	/**
	 * @return Returns this conversation's message list
	 */
	public List<Message> getMessageList(){
        return messageList;
    }

	/**
	 * @return Returns this conversation's participants.
	 */
	public List<UUID> getParticipants(){
        return participants;
    }

	/**
	 * @return Returns the conversation type
	 */
	public ConvoType getType(){return type;}

	/**
	 * Sets the event Name for messages of type event
	 * @param eventName The name of the event in Strong format
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return Returns event name
	 */
	public String getEventName() {
		return eventName;
	}
}
