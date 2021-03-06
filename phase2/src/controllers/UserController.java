package controllers;

import entities.Event;
import entities.User;
import entities.UserRequest;
import enums.ConvoType;
import enums.UserOption;
import enums.UserType;
import use_cases.*;
import value_holders.MessageInfo;

import java.time.LocalDate;
import java.util.*;

/**
 * Controller for users. Contains functionality available to all types of users.
 * @author Andrew
 */
public class UserController {

    /**
     * Returns a list of UserOption, which are things that the user can do.
     * @param userManager The UserManager to use.
     * @return List of UserOption
     */
    public List<UserOption> getUserOptions(UserManager userManager) {
        List<UserOption> options = new ArrayList<>();
        if (getIsUserLoggedIn(userManager)) {
            UserType type = getActiveUserType(userManager);
            options = UserType.optionsFor(type);
            if (getActiveUserType(userManager) != UserType.GUEST)
                options.add(UserOption.LOG_OUT);
        } else {
            options.add(UserOption.LOG_IN);
            options.add(UserOption.EXPLORE_AS_A_GUEST);
        }

        options.add(UserOption.EXIT);
        return options;
    }

    /**
     * Logs in a user as a guest.
     * @param userManager The UserManager to use.
     * @return a string encouraging to login for further access.
     */
    public String loginAsAGuest(UserManager userManager) {
        return userManager.loginAsAGuest();
    }

    /**
     * Logs in a user for the provided credentials. Returns true if successful and false if not.
     * @param userManager The UserManager to use.
     * @param email - The email of the user trying to be logged into
     * @param password - The password of the user trying to be logged into
     * @return a boolean for whether or not the login was successful; true if it was, false if not.
     */
    public boolean login(UserManager userManager, String email, String password) {
        return userManager.login(email, password);
    }

    /**
     * Logs out the current user.
     * @param userManager The UserManager to use.
     */
    public void logout(UserManager userManager) {
        userManager.logout();
    }

    /**
     * A method for sending a message. Messages can be sent to multiple users. Attendees should only be sending messages
     * to people in their friends list.
     * @param userManager The UserManager to use.
     * @param conversationManager The ConversationManager to use.
     * @param content The content of the message in String form.
     * @param recipientIndices The indices of the recipient users in the UserManager's list of all users.
     * @param type The type of conversation, can be either 'DIRECT', 'EVENT', or 'ORGANIZER'.
     * @return A string indicating whether the message was successfully sent.
     */
    public String sendMessage(UserManager userManager, ConversationManager conversationManager, String content, ArrayList<Integer> recipientIndices, ConvoType type) {
        ArrayList<UUID> participants = new ArrayList<UUID>();
        UUID sender = userManager.getActiveUserId();
        participants.add(sender);
        for(int r : recipientIndices){
            if(!userManager.isValidUserIndex(r)) {
                return "Invalid Recipient Index\n";
            }
            // If active user is attendee, checks if recipient and sender have each other added as friends.
            if(((userManager.getActiveUserType() == UserType.ATTENDEE) || (userManager.getActiveUserType() == UserType.VIP))
                    && ((!userManager.isFriend(r)) || (!userManager.userHasMeAsFriend(r)))) {
                return "You and the recipient(s) must have each other added as friends\n";
            }
            participants.add(userManager.getUserIdByIndex(r));
        }
        UUID conversation = conversationManager.createConversation(participants,type);

        // Checks if sender has conversation. If not, adds conversation to all participants' conversationList.
        if(!userManager.hasConversation(conversation)){
            userManager.startConversationById(conversation);
            for(int r:recipientIndices){
                userManager.addConversation(conversation, r);
            }
        }
        conversationManager.sendMessage(content, sender, conversation);

        for(UUID p : participants){
            int recipientIndex = userManager.findUserIndexById(p);
            userManager.markConversationAsUnread(conversation, recipientIndex);

        }
        return "Message Sent\n";
    }

    /**
     * A method that sends a reply to an active conversation.
     * @param conversationManager The ConversationManager to use.
     * @param userManager The UserManager to use.
     * @param content The content of the reply in String form
     * @param index The index of the conversation being replied to.
     * @return String indicating whether the reply was successful.
     */
    public String replyToConversation(ConversationManager conversationManager, UserManager userManager, String content, int index) {
        if(!conversationManager.isValidConversationIndex(index)) {
            return "Invalid Conversation Index\n";
        }
        if(!userManager.hasConversation(conversationManager.getIdFromIndex(index))) {
        	return "You are not a member of this conversation\n";
        }
        conversationManager.replyToConversation(content, userManager.getActiveUserId(), index);
        //marks conversations as unread for all participants except sender
        List<UUID> participants =conversationManager.getConversationParticipants(index);
        for (UUID p:participants){
            if(!p.equals(userManager.getActiveUserId())) {
                userManager.markConversationAsUnread(conversationManager.getIdFromIndex(index), userManager.findUserIndexById(p));
            }
        }
        return "Reply Sent\n";
    }

    /**
     * Method that deletes a conversation from a users conversations list
     * @param conversationManager The ConversationManager to use.
     * @param userManager The UserManager to use.
     * @param convoIndex index of conversation to be removed
     * @param userIndex index of user
     */
    public void deleteConversation(ConversationManager conversationManager, UserManager userManager, int convoIndex,int userIndex){
        UUID id = conversationManager.getIdFromIndex(convoIndex);
        if (userIndex ==-1){userIndex=userManager.getIndexOfActiveUser();}
        userManager.deleteConversation(id, conversationManager.getMessageListSize(id), userIndex);
    }
    /**
     * Method that Archived a conversation from a users conversations list
     * @param conversationManager The ConversationManager to use.
     * @param userManager The UserManager to use.
     * @param convoIndex index of conversation to be archived
     * @param userIndex index of user
     */
    public void archiveConversation(ConversationManager conversationManager, UserManager userManager, int convoIndex, int userIndex){
        UUID id = conversationManager.getIdFromIndex(convoIndex);
        if (userIndex ==-1){userIndex=userManager.getIndexOfActiveUser();}
        userManager.archiveConversation(id, conversationManager.getMessageListSize(id),userIndex);
    }
    /**
     * Method added by Laxan to mark a message as unread.
     * @param conversationManager The ConversationManager to use.
     * @param userManager The UserManager to use.
     * @param conversationIndex The index of the conversation being marked as unread
     */
    public void markConversationAsUnread(ConversationManager conversationManager, UserManager userManager, int conversationIndex){
       UUID conversationId =conversationManager.getIdFromIndex(conversationIndex);
       userManager.markConversationAsUnread(conversationId, userManager.findUserIndexById(userManager.getActiveUserId()));
    }

    /**
     * Sign up to given event as user if the event is not full, user has yet to signed up to the event and has no
     * schedule conflict with the event.
     * @param userManager The UserManager to use.
     * @param eventsManager The EventsManager to use.
     * @param roomManager The RoomManager to use.
     * @param eventId UUID object that represents the event.
     * @return A String representing the status of signing up to the event.
     */
    public String signUpToEvent(UserManager userManager, EventsManager eventsManager, RoomManager roomManager, UUID eventId) {
        UUID userID = userManager.getActiveUserId();
        if (eventsManager.getAllEvents().containsKey(eventId) && eventsManager.getNumAttendees(eventId) <
                roomManager.getRoomCapacity(eventsManager.getEventRoomId(eventId)) &&
                !eventsManager.isUserSignedUp(userID, eventId) &&
                eventsManager.getNumAttendees(eventId) < eventsManager.getCapacity(eventId))
        {
            eventsManager.addAttendee(eventId, userID);
            userManager.registerEventById(eventId);
            return "You've signed up to the event successfully.";
        }
        else {
            return "Sign up was unsuccessful. Either event is full, " +
                    "invalid or you've already signed up for the event.";
        }
    }
    /**
     * Disenroll to given event as user if user has already signed up to event.
     * @param userManager The UserManager to use.
     * @param eventsManager The EventsManager to use.
     * @param eventId UUID object that represents the event.
     * @return A String representing of disenrolling from the event.
     */
    public String disEnrollToEvent(UserManager userManager, EventsManager eventsManager, UUID eventId) {
        UUID userID = userManager.getActiveUserId();
        if (eventsManager.getAllEvents().containsKey(eventId)) {
            eventsManager.removeAttendee(eventId, userID);
            userManager.cancelEventById(eventId);
            return "You've disenrolled to the event successfully.";
        }
        else {
            return "No such event.";
        }
    }

    /**
     * Get a list of all event ids.
     * @param eventsManager The EventsManager to use.
     * @return A List of event ids.
     */
    public List<UUID> getAllEventIds(EventsManager eventsManager){
        return eventsManager.getAllEventIds();
    }

    /**
     * Get a list of all events.
     * @param eventsManager The EventsManager to use.
     * @return A List of all events.
     */
    public List<Event> getAllEvents(EventsManager eventsManager) {
        return new ArrayList<>(eventsManager.getAllEvents().values());
    }

    /**
     * Get a list of speakers who are speaking in the given event.
     * @param eventsManager The EventsManager to use.
     * @param eventID a UUID object that represents the event
     * @return A List of UUID objects where each UUID object represents a speakers who are speaking in the event
     */
    public List<UUID> getSpeakers(EventsManager eventsManager, UUID eventID) {
        return eventsManager.getSpeakers(eventID);
    }

    /**
     * Returns a shedule of events on a date
     * @param eventsManager The EventsManager to use.
     * @param roomManager The RoomManager to use.
     * @param date date for which events need to be found
     * @return schedule of events on a given date
     */
    public String viewEventsByDay(EventsManager eventsManager, RoomManager roomManager, LocalDate date){
        List<UUID> events= eventsManager.getEventsByDay(date);
        String output = "";
        if (events.isEmpty()){return "There are no scheduled talks on this day.";}
        for (UUID i : events) {
            output += eventsManager.getEventString(i) + " " +
                    roomManager.getRoomName(eventsManager.getEventRoomId(i)) + "\n";
        }
        return output;
    }

    /**
     * A method that returns a string displaying all the active user's conversations in the form:
     * "i - recipient1, recipient2, ..."
     * Where 'i' is the index of the conversation in ConversationManager's allConversations.
     * @param userManager The UserManager to use.
     * @param conversationManager The ConversationManager to use.
     * @return A string representation of the logged in user's conversations.
     */
    public String viewConversations(UserManager userManager, ConversationManager conversationManager){
        List<UUID> conversations = userManager.getConversations();
        List<UUID> unreadConversations = userManager.getUnreadConversationList();

        String output = "";
        if(conversations.isEmpty()) {
        	output += "You have no conversations. Start a new one by sending a message.\n";
        }else {
            String organizerOutput="\nOrganizer Notifications\n===========================\n";
            String eventOutput="\nEvent Notifications\n===========================\n";
            String directOutput="\nConversations\n===========================\n";
	        for (UUID c : conversations) {
                String tempOutput = "";
	            if (unreadConversations.contains(c)){
                    tempOutput += "[Unread] ";
                }
                ConvoType type = conversationManager.getType(c);
	            int index = conversationManager.findConversation(c);
                tempOutput += index + " - ";
                if (type == ConvoType.EVENT){tempOutput += conversationManager.getEventName(c)+ " - ";}
		        List<UUID> participants = conversationManager.getConversationParticipants(index);
                tempOutput += showConversationParticipants(userManager, participants) + "\n";

                switch (type){
                    case DIRECT:
                        directOutput += tempOutput;
                        break;
                    case EVENT:
                        eventOutput += tempOutput;
                        break;
                    case ORGANIZER:
                        organizerOutput += tempOutput;
                        break;
                }
	        }
            if (organizerOutput != "\nOrganizer Notifications\n===========================\n"){ output += organizerOutput; }
            if (eventOutput != "\nEvent Notifications\n===========================\n"){ output += eventOutput; }
            if (directOutput == "\nConversations\n===========================\n"){ directOutput += "You have no direct conversations\n"; }
            output += directOutput + "\n";
        }
        return output;
    }

    /**
     * A method that returns a string representation of all the messages for a conversation.
     * Example:
     *
     * Messages between: Nathan, John
     * ===========================
     * Nathan (9:45): Hey guys, this is an example of the messages being displayed.
     * John (10:03): Your code is garbage.
     * Nathan (10:08): Damn bro, why you gotta hurt my feelings like that :(
     *
     * @param userManager The UserManager to use.
     * @param conversationManager The ConversationManager to use.
     * @param index Index of the conversation in conversationManager's allConversations list.
     * @return A string representation of all messages in the specified conversation.
     */
    public String viewMessages(UserManager userManager, ConversationManager conversationManager, int index) {
        HashMap<UUID, Integer> deletedMessagesIndexList = userManager.getDeletedConversationsList();
        HashMap<UUID, Integer> archivedMessagesIndexList = userManager.getArchivedConversationsList();
        UUID conversationID = conversationManager.getIdFromIndex(index);
        if(!conversationManager.isValidConversationIndex(index)) {
            return "Invalid Conversation Index\n\n";
        }
        if(!userManager.hasConversation(conversationID)){
        	return "You are not a member of this conversation\n\n";
        }
        userManager.markConversationAsRead(conversationID);
        String output = "\nConversation with: ";
        List<UUID> participants = conversationManager.getConversationParticipants(index);
        output += showConversationParticipants(userManager, participants) + "\n===========================\n";



        List<MessageInfo> messagesInfo = conversationManager.getMessagesInfo(index);
         if (deletedMessagesIndexList.get(conversationID)== messagesInfo.size()){
             output +="All messages in this conversation have been deleted\n\n";
             return output;
         }
        if (archivedMessagesIndexList.get(conversationID)== messagesInfo.size()){
            output +="All messages in this conversation have been archived\n\n";
            return output;
        }
        int firstMessageIndex;
        if (deletedMessagesIndexList.get(conversationID)>=archivedMessagesIndexList.get(conversationID)){
            firstMessageIndex = deletedMessagesIndexList.get(conversationID);
        }else{
            firstMessageIndex=archivedMessagesIndexList.get(conversationID);
        }


        ListIterator<MessageInfo> messageIterator = messagesInfo.listIterator(firstMessageIndex);

        MessageInfo m;
        while(messageIterator.hasNext()) {
            m = messageIterator.next();
            String sender = userManager.getUserName(userManager.findUserIndexById(m.getSenderId()));
            output += sender + " (" + m.getTime().getHour() + ":" + m.getTime().getMinute() + "): " + m.getContent() + "\n";
        }
        return output + "\n";
    }

    /**
     * A method that returns a string displaying all the active user's friends in the form:
     * "i - friend's email" Where 'i' is the index of the friend in UserManager's allUsers.
     * @param userManager The UserManager to use.
     * @return A string representation of the logged in user's conversations.
     */
    public String viewAllFriends(UserManager userManager){
        List<UUID> friends = userManager.getFriends();
        String output = "\nList of Friends\n===========================\n";
        output += displayUsersFromIdList(userManager, friends);
        return output;
    }

    /**
     * Add a friend whose email is the provided one to the active user's friend list iff the friend is not there already
     * @param userManager The UserManager to use.
     * @param email a string that represents an email address
     * @return A String representing the status of adding the friend.
     */
    public String addFriend(UserManager userManager, String email) {
        int index = userManager.findUserByEmail(email);
        // if no such email holder exists
        if (index == -1) {
            return "No such user exists!\n";
        }
        else {
            if(userManager.isFriend(index)){
                return "This user is already your friend.\n";
            }
            else { //not a friend, so we can add properly
                UUID friend = userManager.getUserIdByIndex(index);
                userManager.addFriendById(friend);
                return "Successfully Added.\n";
            }
        }
    }

    /**
     * Removes a friend from active user's friend list based on given user email
     * @param userManager The UserManager to use.
     * @param friendEmail a string that represents an email address
     * @return A String representing the status of removing the friend.
     */
    public String removeFriend(UserManager userManager, String friendEmail){
        int ind = userManager.findUserByEmail(friendEmail);
        if (ind == -1){
            return "No such user exists!\n";
        }

        if (userManager.isFriend(ind)){
            UUID f = userManager.getUserIdByIndex(ind);
            userManager.removeFriendById(f);
            return "Successfully Removed.\n";
        }
        else {
            return "This user is not your friend.\n";
        }
    }

    /**
     * A method that returns the UserType of the currently logged in user.
     * @param userManager The UserManager to use.
     * @return The active user's type.
     */
    private UserType getActiveUserType(UserManager userManager){
        return userManager.getActiveUserType();
    }

    /**
     * Returns whether or not a user is currently logged in.
     * @param userManager The UserManager to use.
     * @return true if a user is logged in and false if not.
     */
    private boolean getIsUserLoggedIn(UserManager userManager) {
        return userManager.getIsUserLoggedIn();
    }

    /**
     * A private helper method to prevent duplicate code. Used in viewConversations() and viewMessages(). Returns a
     * String representation of participants in a conversation.
     * @param userManager The UserManager to use.
     * @param participants participants list to be formatted into a String
     * @return A string displaying participant names
     */
    private String showConversationParticipants(UserManager userManager, List<UUID> participants) {
        String output = "";
        ArrayList<UUID> copy = new ArrayList<UUID>();
        copy.addAll(participants);
        copy.remove(userManager.getActiveUserId());
        for(int i = 0; i < copy.size(); i++){
        	if(i != 0){
        		output += ", ";
	        }
            UUID p = copy.get(i);
            output += userManager.getUserName(userManager.findUserIndexById(p));
        }
        return output;
    }

	/**
	 * A private helper method for displaying users from a list of user UUIDs.
     * @param userManager The UserManager to use.
	 * @param ids The list of user UUIDs
	 * @return The displayed users in String form
	 */
    public String displayUsersFromIdList(UserManager userManager, List<UUID> ids) {
    	String output = "";
	    for(UUID id : ids){
		    String email = userManager.getEmail(id);
		    output += userManager.findUserIndexById(id) + " - " + email + "\n";
	    }
	    return output;
    }

    /**
     * @param userRequestManager The UserRequestManager to use.
     * @param userManager The UserManager to use.
     * @param eventsManager The EventsManager to use.
     * @return Returns all User request organized by status
     */
    public String viewUserRequests(UserRequestManager userRequestManager, UserManager userManager, EventsManager eventsManager){
        String output = "";
        List<UserRequest> userRequests = userRequestManager.getUserRequestByStatus(false);
        if (!userRequests.isEmpty()){
            output += "\nPending User Requests:\n";
            for (UserRequest r: userRequests){
                output += userRequestManager.findRequestIndexById(r.getId()) + " : " + userManager.getUserByIndex(userManager.findUserIndexById(r.getSenderId())).getEmail() +" - " + r.getContent() + " | Associated Event: " + eventsManager.getEventName(r.getAssociatedEventId()) + "\n";
            }
        }
        userRequests = userRequestManager.getUserRequestByStatus(true);
        if (!userRequests.isEmpty()) {
            output += "\nAddressed User Requests:\n";
            for (UserRequest r : userRequests) {
                output += userRequestManager.findRequestIndexById(r.getId()) + " : " + userManager.getUserByIndex(userManager.findUserIndexById(r.getSenderId())).getEmail() + " - " + r.getContent() + " | Associated Event: " + eventsManager.getEventName(r.getAssociatedEventId()) + "\n";
            }
        }
        if (output == ""){output += "There are currently no user requests";}
        return output;

    }

    /**
     * Method that adds a userRequest
     * @param userRequestManager The UserRequestManager to use.
     * @param userManager The UserManager to use.
     * @param content user request input
     * @param eventID event ID for associated event
     */
    public void addUserRequest(UserRequestManager userRequestManager, UserManager userManager, String content, UUID eventID){
        userRequestManager.addUserRequest(userManager.getActiveUserId(), content, eventID);
    }

    /**
     * Mark a UserRequest as addressed
     * @param userRequestManager The UserRequestManager to use.
     * @param index index of the userRequest
     */
    public void markUserRequestAsAddressed(UserRequestManager userRequestManager, int index){
        userRequestManager.markUserRequestAsAddressed(index);
    }

    /**
     * Returns Username of user
     * @param userManager The UserManager to use.
     * @param id UUID of user
     * @return user name
     */
    public String getUserName(UserManager userManager, UUID id){
        return userManager.getUserName(userManager.findUserIndexById(id));
    }

    public HashMap<String, Integer> statsGetTopEnrolledEvents(EventsManager eventsManager) {
        HashMap<String, Integer> topEvents = new HashMap<>();
        for (Event event : eventsManager.getAllEvents().values()) {
            topEvents.put(event.getEventName(), event.getAttendees().size());
        }
        return topEvents;
    }

    public HashMap<String, Integer> statsGetTopEnrolledUsers(UserManager userManager) {
        HashMap<String, Integer> topUsers = new HashMap<>();
        for (User user : userManager.getAllUsers()) {
            topUsers.put(user.getName(), user.getEventsList().size());
        }
        return topUsers;
    }

}
