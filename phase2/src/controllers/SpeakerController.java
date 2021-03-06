package controllers;

import enums.ConvoType;
import use_cases.EventsManager;
import use_cases.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import use_cases.*;

/**
 * Controller for Speakers. Contains functionality exclusive to speakers.
 */
public class SpeakerController extends UserController{

    /**
     * A method for sending a message to all attendees for an event. Should only be accessible by speakers. Returns
     * a string representation of whether the message was successfully sent.
     * @param eventsManager The EventsManager to use.
     * @param userManager The UserManager to use.
     * @param conversationManager The ConversationManager to use.
     * @param content The content of the message as a String.
     * @param eventId The UUID of the event.
     * @return Whether the message was successfully sent.
     */
    public String sendMessageToEventAttendees(EventsManager eventsManager, UserManager userManager, ConversationManager conversationManager, String content, UUID eventId){
        Set<UUID> attendees = eventsManager.getAttendeeList(eventId);
        ArrayList<Integer> recipients = new ArrayList<Integer>();
        List<UUID> participantUUID = new ArrayList<>();
        if(attendees.size() > 0) {
            for(UUID a : attendees){
                recipients.add(userManager.findUserIndexById(a));
                participantUUID.add(a);
            }
            String output = sendMessage(userManager, conversationManager, content, recipients, ConvoType.EVENT);
            participantUUID.add(userManager.getActiveUserId());
            if (output=="Message Sent\n"){conversationManager.setEventNameForConvo(participantUUID, eventsManager.getEventName(eventId));}
            return output;
        }
        return "There are no attendees in this event to message";
    }

    /**
     * Method for returning all event id's that a given speaker is scheduled to speak in. Finds schedule for active user if speaker is null.
     * @param userManager The UserManager to use.
     * @param eventsManager The EventsManager to use.
     * @param speaker an UUID object that represents the unique ID of the given speaker
     * @return A list of UUIDs for events this speaker is speaking in
     */
    public List<UUID> getScheduledTalkIds(UserManager userManager, EventsManager eventsManager, UUID speaker) {
        List<UUID> allEventIds = getAllEventIds(eventsManager);
        UUID speakerID;
        if (speaker == null){
            speakerID = userManager.getActiveUserId();
        }else{
            speakerID = speaker;
        }

        ArrayList<UUID> scheduledTalkIds = new ArrayList<UUID>();
        for(UUID id : allEventIds) {
            if(eventsManager.getSpeakers(id).contains(speakerID)) {
                scheduledTalkIds.add(id);
            }
        }
        return scheduledTalkIds;
    }

    /**
     * Display events that a  given speaker is scheduled to speak in.
     * @param userManager The UserManager to use.
     * @param eventsManager The EventsManager to use.
     * @param roomManager The RoomManager to use.
     * @param id an UUID object that represents the unique ID of the given speaker
     * @return String representation of all events that the speaker is scheduled to speak in.
     */
    public String viewScheduledTalks(UserManager userManager, EventsManager eventsManager, RoomManager roomManager, UUID id) {
        String output = "";
        UUID speakerId;
        if (id == null){
            speakerId = userManager.getActiveUserId();
        }else{
            speakerId= id;
        }
        List<UUID> scheduledTalkIds = getScheduledTalkIds(userManager, eventsManager, speakerId);
        for (UUID i : scheduledTalkIds) {
            output = output + eventsManager.getEventString(i) + " " +
                    roomManager.getRoomName(eventsManager.getEventRoomId(i)) + "\n";
        }
        if (output.equals("")) {
            return "You have no scheduled talks.";
        }
        return output;
    }

    /**
     * A method for viewing the emails of all attendees of a specific event.
     * @param eventsManager The EventsManager to use.
     * @param userManager The UserManager to use.
     * @param event The UUID of the event
     * @return A string display of the event's attendees' emails
     */
    public String viewEventAttendees(EventsManager eventsManager, UserManager userManager, UUID event) {
        ArrayList<UUID> attendees = new ArrayList<UUID>();
        attendees.addAll(eventsManager.getAttendeeList(event));
        String output = "\nList of Attendees\n===========================\n";
        String display = displayUsersFromIdList(userManager, attendees);
        if(display.equals("")){
            return "There are no attendees scheduled for this event";
        }
        output += display;
        return output;
    }
}
