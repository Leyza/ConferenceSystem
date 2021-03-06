package use_cases;

import java.util.*;

import entities.User;
import entities.Vip;
import enums.UserType;

/**
 * A use case class to interact with User objects
 * @author Sophie and Irene
 */
public class UserManager {
    private ArrayList<User> allUsers;
    private User activeUser = null;

    /**
     * Constructor for UserManager
     * @param loadedUsers a list of all users in the system
     */
    public UserManager(List<User> loadedUsers){
        this.allUsers = new ArrayList<>(loadedUsers);
    }

    /**
     * Gets the full list of all registered users in the system
     * @return A List of users
     */
    public List<User> getAllUsers() {
        return allUsers;
    }

    /**
     * Finds a user based on his/her user ID in the allUser arraylist
     * @param id the search key
     * @return -1 when the user does not exist; otherwise return the index in the allUser arraylist
     */
    public int findUserIndexById(UUID id) {
        for (User u: allUsers){
            if (u.getId().equals(id)){
                return allUsers.indexOf(u);
            }
        }
        return -1;
    }

    /**
     * Finds a user based on his/her user email in the allUser arraylist
     * @param userEmail the search key
     * @return -1 when the user does not exist; otherwise return the index in the allUser arraylist
     */
    public int findUserByEmail(String userEmail) {
        for (User u: allUsers){
            if (u.getEmail().equals(userEmail)){
                return allUsers.indexOf(u);
            }
        }
        return -1;
    }

    /**
     * Gets the User object based on index in allUsers list
     * @param index the location in the allUsers list
     * @return a User object
     */
    public User getUserByIndex(int index) {
        return allUsers.get(index);
    }

    /**
     * Creates a user account and the minimal amount of information required is email, password and type
     * @param name the full name of the user
     * @param email a valid email which will also serves as the username when logging in
     * @param password a combination of characters defined by user
     * @param type a code to differentiate user types.
     */
    public void createUser(String name, String email, String password, UserType type) {
        if (type == UserType.VIP) {
            Vip newVipUser = new Vip(name, email, password);
            allUsers.add(newVipUser);
        }
        else {
            User newUser = new User(name, email, password, type);
            allUsers.add(newUser);
        }
    }

    /**
     * Adds another user's ID to current user's friendsList so current user can message him/her
     * @param id the UUID of a user
     */
    public void addFriendById(UUID id) {
        activeUser.addToFriendsList(id);
    }

    /**
     * Removes another user's ID from current user's friendsList so current user cannot message him/her
     * @param id the UUID of a user
     */
    public void removeFriendById(UUID id){
        activeUser.removeFromFriendsList(id);
    }

    /**
     * Get list of events that the current user has signed up to.
     * @return a List of events ID, each representing an unique event, that the current user has signed up to.
     */
    public List<UUID> getEventList() {
        return activeUser.getEventsList();
    }

    /**
     * Adds an event ID to current user so he/she keeps track of registered events
     * @param id the UUID of an event
     */
    public void registerEventById(UUID id){
        activeUser.addToEventsList(id);
    }


    /**
     * Removes an event ID from current user so he/she updates the registered event
     * @param id the UUID of an event
     */
    public void cancelEventById(UUID id){
        activeUser.removeFromEventsList(id);
    }


    /**
     * Adds a conversation ID to current user so he/she starts and keep track of the conversation
     * @param id the UUID of a conversation
     */
    public void startConversationById(UUID id) {
        activeUser.addToConversationsList(id);
        archiveConversation(id, 0,getIndexOfActiveUser());
        deleteConversation(id, 0,getIndexOfActiveUser());
    }

    /**
     * Method added by Nathan to return a user's UUID.
     * @param index The index of the user in allUsers
     * @return The user's UUID.
     */
    public UUID getUserIdByIndex(int index){
        return getUserByIndex(index).getId();
    }

    /**
     * Method added by Nathan to add a conversation to the list of a user other than currentUser.
     * @param conversationId The UUID of the conversation being added
     * @param userIndex The user's index in the allUsers list.
     */
    public void addConversation(UUID conversationId, int userIndex){
        User user = getUserByIndex(userIndex);
        user.addToConversationsList(conversationId);
        archiveConversation(conversationId, 0,userIndex);
        deleteConversation(conversationId, 0,userIndex);


    }

    /**
     * Method added by Laxan to mark a message as unread.
     * @param conversationId The UUID of the conversation being marked as unread
     * @param userIndex The user's index in the allUsers list.
     */
    public void markConversationAsUnread(UUID conversationId, int userIndex){
        User user = getUserByIndex(userIndex);

        if (!user.getUnreadConversationsList().contains(conversationId)) {user.addToConversationsList(conversationId);}
    }
    /**
     * Method added by Laxan to mark a message as read.
     * @param conversationId The UUID of the conversation being marked as unread
     */
    public void markConversationAsRead(UUID conversationId){
        if (activeUser.getUnreadConversationsList().contains(conversationId))
        {activeUser.removeFromConversationsList(conversationId);}
    }

    /**
     * Method added by Laxan to archive a message .
     * @param conversationId The UUID of the conversation being archived
     *@param archiveIndex number of messages in the list to be archived
     *@param userIndex index of user
     */
    public void archiveConversation(UUID conversationId, Integer archiveIndex, int userIndex){
        User user1 = getUserByIndex(userIndex);
        user1.addToHashMap(user1.getArchivedConversationsList(), conversationId, archiveIndex);
    }
    /**
     * Method added by Laxan to delete a message .
     * @param conversationId The UUID of the conversation being deleted
     * @param deleteIndex number of messages in the list to be deleted
     * @param userIndex index of user
     */
    public void deleteConversation(UUID conversationId, Integer deleteIndex, int userIndex){
        User user1 = getUserByIndex(userIndex);
        user1.addToHashMap(user1.getDeletedConversationsList(), conversationId, deleteIndex);
    }

    /**
     * Gets the user's list of conversation and the index at which it was Archived
     * @return HashMap of archived conversation IDs and indexes at which they were archived
     */
    public HashMap<UUID, Integer> getArchivedConversationsList() {
        return activeUser.getArchivedConversationsList();
    }

    /**
     * Gets the user's list of conversation and the index at which it was deleted
     * @return HashMap of deleted conversation IDs and indexes at which they were deleted
     */
    public HashMap<UUID, Integer> getDeletedConversationsList() {
        return activeUser.getDeletedConversationsList();
    }

    /**
     * Gets the user's list of unread conversations
     * @return presenter.print of unread conversation UUID's
     */
    public List<UUID> getUnreadConversationList() {
        return activeUser.getUnreadConversationsList();
    }

    /**
     * Gets the index of the active user
     * @return User index of active user
     */
    public int getIndexOfActiveUser(){
        return allUsers.indexOf(activeUser);

    }

    /**
     * Method added by Nathan to determine if the currentUser has a conversation in its list.
     * @param conversationId The conversation ID to be checked
     * @return Returns whether the user has the conversation in its conversationList
     */
    public boolean hasConversation(UUID conversationId){
        if(activeUser.getConversationList().contains(conversationId)){
            return true;
        }
        return false;
    }

    /**
     * Method added by Nathan to get the current user's conversation list.
     * @return The current user's conversation list
     */
    public List<UUID> getConversations(){
        return activeUser.getConversationList();
    }


    /**
     * Gets the name of the user with the provided index
     * @param index the index of the user
     * @return the name of the user
     */
    public String getUserName(int index) {
        return getUserByIndex(index).getName();
    }

    /**
     * Gets user's list of Ids of friends
     * @return list of friend ID
     */
    public List<UUID> getFriends(){
        return activeUser.getFriendsList();
    }

    /**
     * Gets user's email given a user ID
     * @param ID a search key
     * @return a string that represents an email address
     */
    public String getEmail(UUID ID){
        User f = getUserByIndex(findUserIndexById(ID));
        return f.getEmail();
    }

    /**
     * Checks whether the user with index is a friend of current user or not
     * @param index the index of the user
     * @return true if the user with index is a friend of current user, false otherwise
     */
    public boolean isFriend(int index){
        User f = getUserByIndex(index);
        return(activeUser.getFriendsList().contains(f.getId()));
    }

	/**
	 * Checks if the user with index has the active user added as a friend. Used in messenger.
	 * @param index Index of the user whose friends list is being checked
	 * @return Boolean of whether the active user is on the user's friends list.
	 */
	public boolean userHasMeAsFriend(int index){
    	User f = getUserByIndex(index);
    	return f.getFriendsList().contains(getActiveUserId());
    }

    /**
     * Return the ID of the user that is currently logged in.
     * @return The ID of the user that is currently logged in.
     */
    public UUID getActiveUserId() {
        return activeUser.getId();
    }

    /**
     * A method that returns the UserType of the currently logged in user.
     * @return The active user's type.
     */
    public UserType getActiveUserType() {
        return activeUser.getType();
    }

    /**
     * A method to change an existing user's usertype.
     * @param index the index that corresponds to a user
     * @param newType the new type the user will be set to
     */
    public void setUserType(int index, UserType newType){ getUserByIndex(index).setType(newType);}

    /**
     * Returns whether or not a user is currently logged in.
     * @return true if a user is logged in and false if not.
     */
    public boolean getIsUserLoggedIn() {
        return activeUser != null;
    }

    /**
     * A method that checks if 'index' is a valid index for allUsers
     * @param index Index to be checked
     * @return Whether index is valid for allUsers
     */
    public boolean isValidUserIndex(int index) {
        if((index >= 0) && (index < allUsers.size())){
            return true;
        }
        return false;
    }

    /**
     * Logs out the currently active user.
     */
    public void logout() {
        activeUser = null;
    }

    /**
     * Logs in a user for the provided credentials. Returns true if successful and false if not.
     * @return a string that instructs what is allowed and encourages login as a user.
     */
    public String loginAsAGuest() {
        activeUser = new User ("guest", null, null, UserType.GUEST);
        return "Guests are only allowed to explore. Login for further access!";
    }

    public boolean checkCredential(String email, String password){
        for (User user : allUsers) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Logs in a user for the provided credentials. Returns true if successful and false if not.
     * @param email - The email of the user trying to be logged into
     * @param password - The password of the user trying to be logged into
     * @return a boolean for whether or not the login was successful; true if it was, false if not.
     */
    public boolean login(String email, String password) {
        User foundUser = null;
        for (User user : allUsers) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                foundUser = user;
                break;
            }
        }
        if (foundUser != null) {
            activeUser = foundUser;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get a list of all users of a specific type.
     * @param type the type of user to filter for
     * @return A List of UUID where each UUID represents a unique user of the defined type in the system
     */
    public List<UUID> getAllUsersOfType(UserType type) {
        ArrayList<UUID> allThoseUsers = new ArrayList<>();
        for (User i : this.allUsers) {
            if (i.getType().equals(type)) {
                allThoseUsers.add(i.getId());
            }
        }
        return allThoseUsers;
    }

    /**
     * Get a list of strings from a list of UUID objects. Each UUID object represents a user and
     * each string is a string representation of the user.
     * @param selectUsers A List of UUID objects where each UUID represents a user.
     * @return a List of strings where each string is a string representation of a user.
     */
    public List<String> displayUsers(List<UUID> selectUsers){
        ArrayList<String> displayUsers = new ArrayList<>();
        for (UUID i : selectUsers){
            displayUsers.add(getUserByIndex(findUserIndexById(i)).toString());
        }
        return displayUsers;
    }

    /**
     * Set a new name for a given user.
     * @param user User object that represents the user that will have the new given name.
     * @param name String object that represents new name of the active user.
     */
    public void setName (User user, String name) {
        user.setName(name);
    }

    /**
     * Remove a given event for a given user.
     * @param user User object that represents the user that will have the event removed from their event list.
     * @param eventID UUID object that represents the event that will be removed from the user's event list.
     */
    public void removeEvent(User user, UUID eventID) {
        user.removeFromEventsList(eventID);
    }

    /***
     * Return all Vip users' ids.
     * @return a hashset of all VIP users' ids
     */
    public HashSet<UUID> getAllVipUsers() {
        HashSet<UUID> allVipIds = new HashSet<>();
        for (User user : allUsers) {
            if (user.getType() == UserType.VIP) {
                allVipIds.add(user.getId());
            }
        }
        return allVipIds;
    }

    /***
     * Add an event with eventId from the interested events of the user
     * @param eventId a speaker's id to be added from the favourites
     * @return if the process was successful
     */
    public boolean interestedInEvent(UUID eventId) {
        if (((Vip)activeUser).getInterestedEventsIds().contains(eventId)) {
            return false;
        } else {
            ((Vip)activeUser).interestedInEventId(eventId);
            return true;
        }
    }

    /***
     * Remove an event with eventId from the interested events of the user
     * @param eventId a speaker's id to be removed from the favourites
     * @return if the process was successful
     */
    public boolean uninterestedInEvent(UUID eventId) {
        if (!((Vip)activeUser).getInterestedEventsIds().contains(eventId)) {
            return false;
        } else {
            ((Vip)activeUser).getInterestedEventsIds().add(eventId);
            ((Vip)activeUser).uninterestedInEventId(eventId);
            return true;
        }
    }

    /***
     * Get the set of all interested event of active vip user
     * @return Hashset of UUID, which represents the interested events' ids
     */
    public HashSet<UUID> getInterestedEventsIds() {
        return ((Vip)activeUser).getInterestedEventsIds();
    }

    /***
     * Add a speaker with speakerId to the favourite speakers of active vip user
     * @param speakerId a speaker's id to be added to the favourites
     * @return if the process was successful
     */
    public boolean likeSpeakerById(UUID speakerId) {
        if (((Vip)activeUser).getFavouriteSpeakersId().contains(speakerId)) {
            return false;
        } else {
            ((Vip)activeUser).likeSpeakerById(speakerId);
            return true;
        }
    }

    /***
     * Remove a speaker with speakerId from the favourite speakers of active vip user
     * @param speakerId a speaker's id to be removed from the favourites
     * @return if the process was successful
     */
    public boolean dislikeSpeakerById(UUID speakerId) {
        if (!((Vip)activeUser).getFavouriteSpeakersId().contains(speakerId)) {
            return false;
        } else {
            ((Vip)activeUser).dislikeSpeakerById(speakerId);
            return true;
        }
    }

    /***
     * Returns all favourite speakers ids of the active (VIP) user.
     * @return List of UUID, which are the ids of the favourite speakers of active user.
     */
    public List<UUID> getFavouriteSpeakersId() {
        return new ArrayList<>(((Vip)activeUser).getFavouriteSpeakersId());
    }

    /***
     * Return true iff the speaker is one of the vip user's favourite speakers. This method checks before the
     * program performs more complex tasks to prevent errors.
     * @param vipId id of the vip who may have speaker with speakerId as their favourite
     * @param speakerId id of the speaker whom we want to check
     * @return a boolean value as explained above
     */
    public boolean isFavouriteSpeaker(UUID vipId, UUID speakerId) {
        Vip vip = (Vip) getUserByIndex(findUserIndexById(vipId));
        if (vip.getFavouriteSpeakersId().contains(speakerId)){
            return true;
        } else {
            return false;
        }
    }

}