package use_cases;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import entities.Conversation;
import entities.Message;
import enums.ConvoType;
import value_holders.MessageInfo;

/**
 * A Use Case class for managing conversations.
 * @author Nathan Raymant
 */
public class ConversationManager {

    private ArrayList<Conversation> allConversations;

	/**
	 * The constructor for ConversationManager
	 * @param loadedConversation Conversations to be set from save data.
	 */
	public ConversationManager(List<Conversation> loadedConversation){
        this.allConversations = new ArrayList<>(loadedConversation);
    }

    /**
     * Creates a new conversation and returns its ID, or returns the ID of an existing conversation.
     * @param participants A list of participants for the conversation.
     * @param type The type of conversation, can be either 'DIRECT', 'EVENT', or 'ORGANIZER'.
     * @return If no conversation with the participants exists, returns the UUID of a new conversation. Else, return the UUID of an existing conversation.
     */
    public UUID createConversation(List<UUID> participants, ConvoType type) {
        Optional<UUID> potentialId = conversationExists(participants, type);
        if (potentialId.isPresent()) {
            return potentialId.get();
        } else {
            Conversation newConvo = new Conversation(participants, type);
            allConversations.add(newConvo);
            return newConvo.getId();
        }
    }

    /**
     * Sends a message by adding a new message object to an existing conversation.
     * @param content The content of the message.
     * @param sender The ID for the user sending the message.
     * @param conversation The id for the conversation the message is being added to. Can be found with createConversation().
     */
    public void sendMessage(String content, UUID sender, UUID conversation) {
        int convoIndex = findConversation(conversation);
        if(convoIndex != -1) {
            Conversation c = getConversation(convoIndex);
            c.addMessage(new Message(sender, content));
        }
    }

	/**
	 * A method for replying (adding a message) to an existing conversation. Similar to send message but takes an index
	 * for its conversation parameter instead. Index must be valid.
	 * @param content Content of the reply
	 * @param sender Sender of the reply
	 * @param index Index of the conversation being replied to
	 */
	public void replyToConversation(String content, UUID sender, int index) {
        getConversation(index).addMessage(new Message(sender, content));
    }

    /**
     * Find a conversation INDEX given a UUID.
     * @param id The UUID of the conversation being searched for.
     * @return The index of the conversation. If no such conversation exists, returns -1.
     */
    public int findConversation(UUID id) {
        for(Conversation c : allConversations) {
            if(c.getId().equals(id)){
                return allConversations.indexOf(c);
            }
        }
        return -1;
    }

    public ConvoType getType(UUID id){
        return getConversation(findConversation(id)).getType();
    }

    /**
     * Gets a conversation given its index. Should be preceded by a findConversation() call.
     * @param index The index of the conversation (MUST BE VALID).
     * @return A conversation object.
     */
    private Conversation getConversation(int index) {
        return allConversations.get(index);
    }

    /**
     * Get arraylist of all conversations data available up to the point of the method being called/
     * @return Arraylist of all conversations.
     */
    public List<Conversation> getAllConversations() {
        return allConversations;
    }

	/**
	 * Gets a conversation's participants given its index.
	 * @param index The index of the conversation whose participants are requested
	 * @return A list of this conversation's participants.
	 */
	public List<UUID> getConversationParticipants(int index){
        return getConversation(index).getParticipants();
    }

	/**
	 * Returns a conversation's Id given its index.
	 * @param index The index of the conversations whose Id is requested
	 * @return The UUID of the conversation with 'index'.
	 */
	public UUID getIdFromIndex(int index) {
		return getConversation(index).getId();
    }

    /**
     * A method that checks whether 'index' is a valid index in allConversations
     * @param index The index to be checked.
     * @return Boolean of whether the index is valid.
     */
    public boolean isValidConversationIndex(int index) {
        if((index >= 0) && (index < allConversations.size())){
            return true;
        }
        return false;
    }

    public void setEventNameForConvo(List<UUID> participantUUID, String eventName){
        Optional<UUID> potentialId = conversationExists(participantUUID, ConvoType.EVENT);
        if (potentialId.isPresent()){
            Conversation conversation = getConversation(findConversation(potentialId.get()));
            conversation.setEventName(eventName);
        }
    }

    public String getEventName (UUID id){
        return getConversation(findConversation(id)).getEventName();
    }

    /**
     * A method that returns a list of MessageInfo objects carrying message information to the controller. Made for
     * UserController.viewMessages()
     * @param index Index of the conversation to retrieve message info from
     * @return A list of MessageInfo objects
     */
    public List<MessageInfo> getMessagesInfo(int index) {
        ArrayList<MessageInfo> output = new ArrayList<MessageInfo>();
        List<Message> messages = getConversation(index).getMessageList();
        for(Message m : messages) {
            output.add(new MessageInfo(m.getSenderId(), m.getTime(), m.getContent()));
        }
        return output;
    }

    public Integer getMessageListSize(UUID id){ return getConversation(findConversation(id)).getMessageList().size(); }

    /**
     * Private method for determining if a conversation between participants exists. Used in createConversation() and AppPresenter.sendMessageToEventAttendees and. sendMessageToAll.
     * Order of participants in their list is ignored.
     * @param participants The participants being searched for.
     * @return An Optional which contains a conversation ID if one exists, otherwise contains null.
     */
    private Optional<UUID> conversationExists(List<UUID> participants, ConvoType type) {
        boolean status;
        for(Conversation c : allConversations) {
            status = true;
            if (c.getType() != type){
                status=false;}
            if(participants.size() != c.getParticipants().size()) {
            	status = false;
            } else {
	            for (UUID id : c.getParticipants()) {
		            if (!participants.contains(id)) {
			            status = false;
		            }
	            }
            }
            if(status) {
                return Optional.ofNullable(c.getId());
            }
        }
        return Optional.ofNullable(null);
    } // There are libraries that compare if lists have the same contents while ignoring order. Might be worthwhile.

}