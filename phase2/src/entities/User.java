package entities;

import enums.UserType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.io.Serializable;
import java.util.stream.Collectors;

/**
 * An entity class for users including attendees, organizers, and speakers.
 * @author Irene and Sophie
 */
public class User implements Serializable {
    private String name;
    private UUID id;
    private String email;
    private String password;
    private UserType type;
    private ArrayList<UUID> friendsList = new ArrayList<>();
    private ArrayList<UUID> eventsList = new ArrayList<>();
    private ArrayList<UUID> conversationList = new ArrayList<>();
    private ArrayList<UUID> unreadConversationsList= new ArrayList<>();
    private HashMap<UUID, Integer> archivedMessageIndexList = new HashMap<>();
    private HashMap<UUID, Integer> deletedMessageIndexList = new HashMap<>();
    private static final long serialVersionUID = 3896245514069491275L; // See: https://stackoverflow.com/a/8336144
    
    /**
     * Initializes an object of User and the minimal amount of information required is email, password and type
     * @param name the full name of the user
     * @param email a valid email which will also serves as the username when logging in
     * @param password a combination of characters defined by user
     * @param type a code to differentiate user types.
     */
    public User(String name, String email, String password, UserType type) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    /**
     * Gets the user's type
     * @return a string that represents user's type
     */
    public UserType getType() {
        return type;
    }

    /**
     * Sets user's type to a different type
     * @param newType The type to set the user to
     */
    public void setType(UserType newType){ this.type = newType;}

    /**
     * Adds a user's name as addtional informaiton.
     * @param name legal name or nick name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the user's list of IDs of registered events
     * @return the arraylist of event IDs
     */
    public ArrayList<UUID> getEventsList() {
        return eventsList;
    }

    /**
     * Gets the user's list of IDs of friends (other users to message)
     * @return the arraylist of user IDs
     */
    public ArrayList<UUID> getFriendsList() {
        return friendsList;
    }

    /**
     * Gets the user's list of IDs of conversations
     * @return the arraylist of conversation IDs
     */
    public ArrayList<UUID> getConversationList() {
        return conversationList.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets the user's list of IDs of unread conversations
     * @return the arraylist of unread conversation IDs
     */
    public ArrayList<UUID> getUnreadConversationsList() {
        return unreadConversationsList;
    }

    /**
     * Gets the user's list of conversation and the index at which it was Archived
     * @return HashMap of archived conversation IDs and indexes at which they were archived
     */
    public HashMap<UUID, Integer> getArchivedConversationsList() {
        return archivedMessageIndexList;
    }

    /**
     * Gets the user's list of conversation and the index at which it was deleted
     * @return HashMap of deleted conversation IDs and indexes at which they were deleted
     */
    public HashMap<UUID, Integer> getDeletedConversationsList() {
        return deletedMessageIndexList;
    }

    /**
     * Gets the user's name
     * @return a string that represents user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's unique ID
     * @return the UUID that separates each user
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the user's password
     * @return a string that represents user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's email
     * @return a string that represents user's email
     */
    public String getEmail(){
        return email;
    }


    /**
     * Adds a friend to the user's friend list
     * @param friend the UUID of the user to friend with
     */
    public void addToFriendsList(UUID friend) {
        friendsList.add(friend);
    }


    /**
     * Deletes the friend from the user' s friend list
     * @param friend the UUID of the user to unfriend with
     */
    public void removeFromFriendsList(UUID friend){
        friendsList.remove(friend);
    }


    /**
     * Adds an event to the user's event list
     * @param event the UUID of the event to add
     */
    public void addToEventsList(UUID event) {
        eventsList.add(event);
    }


    /**
     * Removes an event from the user's event list
     * @param event the UUID of the event to remove
     */
    public void removeFromEventsList(UUID event){
        eventsList.remove(event);
    }


    /**
     * Adds an event to the user's event list
     * @param con the UUID of the conversation to add
     */
    public void addToConversationsList(UUID con) {
        conversationList.add(con);
    }


    /**
     * Removes an event from the user's event list
     * @param con the UUID of the conversation to remove
     */
    public void removeFromConversationsList(UUID con){
        conversationList.remove(con);
    }


    /**
     * Adds an ID to user's list of IDs of registered events/friends/conversations
     * @param target one of eventLists/friendsList/conversationsList
     * @param ID the item to be added to the list
     * @param index number of messages in the conversation up to which will be deleted
     */
    public void addToHashMap(HashMap<UUID, Integer> target, UUID ID, Integer index){
        target.put(ID, index);
    }


    /**
     * Get a string representation of User information.
     * @return A string of User information.
     */
    @Override
    public String toString() {
        return name + " | Email: " + email;
    }
}
