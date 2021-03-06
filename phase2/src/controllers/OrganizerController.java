package controllers;

import enums.ConvoType;

import java.time.LocalDateTime;
import java.util.*;

import enums.RoomFeatures;
import enums.UserType;
import use_cases.*;

/**
 * Controller for Organizers. Contains functionality exclusive to organizers.
 */
public class OrganizerController extends UserController{

    /**
     * A method for sending a message to all users of a certain type (either Attendee or Speaker). Should only be
     * accessible by organizers. Returns a string representation of whether the message was successfully sent.
     * @param userManager The UserManager to use.
     * @param conversationManager The ConversationManager to use.
     * @param content The content of the message as a String.
     * @param group The type of user that will be receiving the message.
     * @return Whether the message was successfully sent.
     */
    public String sendMessageToAll(UserManager userManager, ConversationManager conversationManager, String content, UserType group){
        List<UUID> recipientIds;
        if(group == UserType.ATTENDEE) {
            recipientIds = userManager.getAllUsersOfType(UserType.ATTENDEE);
        }else {
            recipientIds = userManager.getAllUsersOfType(UserType.SPEAKER);
        }
        ArrayList<Integer> recipients = new ArrayList<Integer>();
        for(UUID r : recipientIds) {
            recipients.add(userManager.findUserIndexById(r));
        }
        return sendMessage(userManager, conversationManager, content, recipients, ConvoType.ORGANIZER);
    }

    /**
     * Method that lets organizers create new events.
     * @param roomManager The RoomManager to use.
     * @param eventsManager The EventsManager to use.
     * @param userManager The UserManager to use.
     * @param conversationManager The ConversationManager to use.
     * @param name The name of the new event
     * @param time Time of the new event
     * @param roomId UUID of the room of the new event
     * @param speakerId An HashSet of UUID where each UUID represents a unique speaker of the new event
     * @param capacity an Integer object that represents the maximum number of attendees allowed in the event
     * @param duration an Integer object represents the number of hours that the event will last. All events
     *                 can only last increment in hours.
     * @param features a list of RoomFeatures objects where each represent a feature that is required for the event
     * @return A String representing the status of the creation of the event.
     */
    public String createNewEvent(RoomManager roomManager, EventsManager eventsManager, UserManager userManager, ConversationManager conversationManager, String name, LocalDateTime time, UUID roomId, HashSet<UUID> speakerId, int capacity, int duration, List<RoomFeatures> features){
        if (roomManager.checkRoomAvailability(roomId, time, duration)  &&
                roomManager.getRoomCapacity(roomId) >= capacity) {
            try {
                UUID newEventId = eventsManager.addEvent(name, time, roomId, capacity, duration, features);
                roomManager.bookRoom(roomId, time, newEventId, duration);
                for (UUID ID : speakerId) {
                    if (!speakerConflict(eventsManager, ID, time)) {
                        eventsManager.addSpeaker(newEventId, speakerId);
                    }
                }
                newsForVips(userManager, conversationManager, eventsManager, newEventId);
                return "Event Successfully Added. Some chosen speakers might not be added if the speaker has a schedule conflict" +
                        " with this event.";
            } catch (Exception e) {
                return "Event not created, duration must be greater than 0 and less than 24 and capacity must be greater than 0.";
            }

        }
        else{
            return "Unable to create this Event. The room may not be available at this time, or " +
                    " the room's capacity is less than the maximum allowed attendees for the event.";
        }
    }

    /***
     * Notifies the VIPs that there are new events with their favourite speakers created.
     * @param userManager The UserManager to use.
     * @param conversationManager The ConversationManager to use.
     * @param eventsManager The EventsManager to use.
     * @param eventId an id of the newly event created
     */
    private void newsForVips(UserManager userManager, ConversationManager conversationManager, EventsManager eventsManager, UUID eventId){
        // when a new event is created, look at the speaker.
        List<UUID> speakerIds = getSpeakers(eventsManager, eventId);
        for (UUID speakerId : speakerIds) {
            List<UUID> allVipIds = userManager.getAllUsersOfType(UserType.VIP);
            // for all vips, if the speaker is their favourites, message them(helper will handle this).
            for (UUID vipId : allVipIds) {
                if (userManager.isFavouriteSpeaker(vipId, speakerId)) {
                    // for each vip:
                    Integer index = userManager.findUserIndexById(vipId);
                    ArrayList<Integer> oneVipList = new ArrayList<Integer>(Arrays.asList(index));
                    String eventString = eventsManager.getEventString(eventId);
                    String content = "There is a new event created with one of your favourite speakers, " +
                            userManager.getUserName(userManager.findUserIndexById(speakerId)) + ". \n" +
                            "The event details are as follows:\n" + eventString;
                    sendMessage(userManager, conversationManager, content, oneVipList, ConvoType.DIRECT);
                }
            }
        }
    }

    /**
     * To determine if the given speaker has a schedule conflict with the given date and time. Organizer exclusive method.
     * @param eventsManager The EventsManager to use.
     * @param speakerID a UUID object that represents the speaker.
     * @param dateAndTime a local date time object that represents the date and time of the event.
     * @return true iff speaker has a schedule conflict with the given date and time. Return false otherwise.
     */
    private boolean speakerConflict(EventsManager eventsManager, UUID speakerID, LocalDateTime dateAndTime) {
        List<UUID> events = eventsManager.getAllEventIds();
        boolean conflict = false;
        for (UUID i : events) {
            LocalDateTime lowerRange = eventsManager.getEventDateAndTime(i).minusMinutes(60);
            LocalDateTime upperRange = eventsManager.getEventDateAndTime(i).plusMinutes(60);
            if (eventsManager.getSpeakers(i).contains(speakerID) && dateAndTime.isAfter(lowerRange) && dateAndTime.isBefore(upperRange)) {
                conflict = true;
                break;
            }
        }
        return conflict;
    }


    /**
     * Method that lets organizers create new use accounts except for guest user. If the email is already registered, it will pop an error message.
     * @param userManager The UserManager to use.
     * @param email email address of the new speaker
     * @param password password of the new speaker
     * @param name name of the new speaker
     * @param type type of the user to be created
     * @return a message indicating whether successfully creating the user account
     */
    public String createUser(UserManager userManager, String email, String password, String name, String type){
        if (userManager.findUserByEmail(email) == -1){
            userManager.createUser(name, email, password, UserType.valueOf(type));
            return type+" user account was created.";
        }

        return "Email is already registered.";
    }

    /**
     * Changes the current user's user type provided correct credential
     * @param userManager The UserManager to use.
     * @param email user email
     * @param password user password
     * @param type the new user type
     * @return a prompt indicating whether we successfully change the user type
     */
    public String changeUserType(UserManager userManager, String email, String password, String type){
        if (userManager.findUserByEmail(email) == -1){
            return "User does not exist.";
        }
        else {
            if (userManager.checkCredential(email, password)) {
                userManager.setUserType(userManager.findUserByEmail(email), UserType.valueOf(type));
                return "User type was set to " + type + ".";
            }
            else{
                return "Email and password do not match. Failed to change the user type.";
            }
        }
    }

    /**
     * Edit the RoomFeatures of a room
     * @param roomManager The RoomManager to use.
     * @param roomId the id of the given room
     * @param name the name of the feature to add/remove
     * @return String representation of whether feature was added/removed.
     */
    public String editRoomFeature(RoomManager roomManager, UUID roomId, String name){
        for (RoomFeatures feature: EnumSet.allOf(RoomFeatures.class)) {
            if (feature.featureName().equalsIgnoreCase(name)) {
                if (roomManager.hasFeature(roomId, feature)){
                    roomManager.removeFeature(roomId, (feature));
                    return "Feature removed from room.";
                }
                else{
                    roomManager.addFeature(roomId, feature);
                    return "Feature added to room.";
                }
            }
        }
        return "Could not find the corresponding feature";
    }

    /**
     * Method that lets organizers add new rooms to the system and returns the added room id.
     * @param roomManager The RoomManager to use.
     * @param name name of the new room
     * @param capacity an Integer object that represents the maximum allowed attendees in the room
     * @param earliestStartTime integer object that represents in a 24 hour clock cycle when an event can
     *                          be scheduled at this room everyday.
     * @param closingTime integer object that represents in a 24 hour clock cycle when an event must end in this room
     *                    everyday.
     * @return return the new Room Id
     */
    public UUID addNewRooms(RoomManager roomManager, String name, int capacity, int earliestStartTime, int closingTime){
        UUID newRoomId = roomManager.addRoom(name, capacity, earliestStartTime, closingTime);
        return newRoomId;
    }

    /**
     * Add the speaker to a given event if possible. An organizer exclusive method.
     * @param eventsManager The EventsManager to use.
     * @param speakerID UUID object that represents the speaker.
     * @param eventID UUID object that represents the event.
     * @return A String representing the status of adding the speaker to the event.
     */
    public String addSpeaker(EventsManager eventsManager, UUID speakerID, UUID eventID) {
        if (eventsManager.getSpeakers(eventID).contains(speakerID)) { return "This speaker is already " +
                "scheduled to speak at this event.";}
        else {
            LocalDateTime time = eventsManager.getEventDateAndTime(eventID);
            boolean conflict = speakerConflict(eventsManager, speakerID, time);
            if (!conflict) {
                eventsManager.addSpeaker(eventID, speakerID);
                return "This speaker has now been scheduled to speak in this event.";
            } else {
                return "This speaker cannot be set to speak in this event due to schedule conflict.";
            }
        }
    }

    /**
     * Remove the given speaker from the speaker list of a given event.
     * @param eventsManager The EventsManager to use.
     * @param eventID UUID object that represents the event
     * @param speakerID UUID object that represents the speaker to be removed from the speaker list of the event
     * @return A String representing the status of removing the speaker.
     */
    public String removeSpeaker(EventsManager eventsManager, UUID eventID, UUID speakerID) {
        eventsManager.removeSpeaker(eventID, speakerID);
        return "This speaker has been removed from the speaker list of the event.";
    }

    /**
     * Cancel given event, remove booking of room for event and remove each attendee schedule for the given event.
     * An organizer exclusive method.
     * @param eventsManager The EventsManager to use.
     * @param userManager The UserManager to use.
     * @param roomManager The RoomManager to use.
     * @param event UUID object that represents the event object to be cancelled
     */
    public void cancelEvent(EventsManager eventsManager, UserManager userManager, RoomManager roomManager, UUID event) {
        Set<UUID> attendeeList = eventsManager.getAttendeeList(event);
        for (UUID i : attendeeList) {
            int index = userManager.findUserIndexById(i);
            userManager.removeEvent(userManager.getUserByIndex(index), event);
        }
        roomManager.removeBooking(eventsManager.getEventDateAndTime(event));
        eventsManager.removeEvent(event);
    }

    /**
     * Change event's capacity if the number of attendees in the event is less than or equal to the given new
     * capacity.
     * @param eventsManager The EventsManager to use.
     * @param eventID UUID object that represents the event that capacity will be changed
     * @param capacity Integer object that represents the new maximum number of attendees allowed. Must be greater than 0.
     * @return A String representing the status of changing the event capacity.
     */
    public String changeEventCapacity(EventsManager eventsManager, UUID eventID, int capacity) {
        if (eventsManager.getNumAttendees(eventID) > capacity) {
            return "Change could not be made as current number " +
                    "of attendees of event exceeds the given capacity.";
        }
        else if (capacity == 0) {
            return "Capacity must be greater than 0.";
        }
        else {
            eventsManager.setCapacity(eventID, capacity);
            return "The event's capacity has been set to " + capacity + ".";
        }
    }
}
