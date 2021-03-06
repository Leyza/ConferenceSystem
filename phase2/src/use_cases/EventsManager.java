package use_cases;

import entities.Event;
import enums.RoomFeatures;

import java.util.*;
import java.time.*;

/**
 * This class manages events.
 *
 * @author Nina
 */
public class EventsManager{
    private HashMap<UUID, Event> events;

    /**
     * Constructor for EventsManager.
     * @param loadedEvents a hashmap of event ids to events
     */
    public EventsManager(HashMap<UUID, Event> loadedEvents){
        events = loadedEvents;
    }

    /**
     * Add event to the HashMap events.
     * @param name a String object that represents the name of the event
     * @param time a LocalDateTime object that represents the start time of event
     * @param roomId UUID object that represents the room
     * @param capacity an integer object represents the maximum number of attendees allowed in th event
     * @param duration an integer object represents the duration of the event, in hours
     * @param features a list of RoomFeatures objects where each represent a feature that is required for the event
     * @return the UUID of the newly created event
     */
    public UUID addEvent(String name, LocalDateTime time, UUID roomId, int capacity, int duration, List<RoomFeatures> features){
        Event newEvent = new Event(name, time, roomId, capacity, duration, features);
        events.put(newEvent.getEventId(), newEvent);
        return newEvent.getEventId();
    }



    /**
     * Get and return an existing event by its ID.
     * @param eventId UUID object that represents the event
     * @return an event
     */
    private Event getEvent(UUID eventId){
        return events.get(eventId);
    }

    /**
     * Get and return all existing events.
     * @return hashmap of event ids to events
     */
    public HashMap<UUID, Event> getAllEvents(){
        return events;
    }

    /**
     * Adds an attendee to an event attendee list.
     * @param eventId UUID object that represents the event
     * @param userId UUID object that represents the user.
     */
    public void addAttendee(UUID eventId, UUID userId){
        getEvent(eventId).addAttendee(userId);
    }

    /**
     * Removes an attendee from an event attendee list.
     * @param eventId UUID object that represents the event
     * @param userId UUID object that represents the user.
     */
    public void removeAttendee(UUID eventId, UUID userId){
        getEvent(eventId).removeAttendee(userId);
    }

    /**
     * Get the name of the given event
     * @param eventID UUID object that represents the event
     * @return the name of the event
     */
    public String getEventName(UUID eventID) { return getEvent(eventID).getEventName();}

    /**
     * Get the date and starting time of the given event.
     * @param eventID UUID object that represents the event
     * @return a LocalDateTime object that represents the date and starting time for the given event.
     */
    public LocalDateTime getEventDateAndTime(UUID eventID) {
        return getEvent(eventID).getEventDateAndTime();
    }

    /**
     * Get speaker of the given event.
     * @param eventId UUID object that represents the event
     * @return a Set of UUIDs object where each UUID represents a speaker of the event.
     */
    public List<UUID> getSpeakers(UUID eventId) {
        HashSet<UUID> speakerSet = getEvent(eventId).getSpeakers();
        return new ArrayList<>(speakerSet);
    }

    /**
     * Add speaker to event.
     * @param eventId UUID object that represents the event
     * @param speakerId UUID object that represents the new speaker of the event
     */
    public void addSpeaker(UUID eventId, UUID speakerId){
        getEvent(eventId).addSpeaker(speakerId);
    }

    /**
     * Add a set of speakers to event
     * @param eventId UUID object that represents the event
     * @param speakersID Set of UUID object where each UUID object represents an unique speaker
     */
    public void addSpeaker(UUID eventId, HashSet<UUID> speakersID) {
        getEvent(eventId).addSpeaker(speakersID);
    }

    /**
     * Remove the given speaker from the speaker list of a given event.
     * @param eventId UUID object that represents the event
     * @param speakerId UUID object that represents the speaker to be removed from the speaker list of the event
     */
    public void removeSpeaker(UUID eventId, UUID speakerId){
        getEvent(eventId).removeSpeaker(speakerId);
    }

    /**
     * Get and returns a list of attendees for the event.
     * @param eventId UUID object that represents the event
     * @return A Set of users
     */
    public Set<UUID> getAttendeeList(UUID eventId){
        return getEvent(eventId).getAttendees();
    }

    /**
     * Get the number of attendees for an event
     * @param eventId UUID object that represents the event
     * @return the number of attendees
     */
    public Integer getNumAttendees(UUID eventId){
        return getAttendeeList(eventId).size();
    }

    /**
     * Get the room id for an event.
     * @param eventId UUID that represents the event
     * @return UUID of the room for the event
     */
    public UUID getEventRoomId(UUID eventId){
        return getEvent(eventId).getEventRoom();
    }

    /**
     * Determine if the given user has signed up for the given event.
     * @param userID UUID object that represents the user.
     * @param eventID UUID object that represents the event
     * @return true iff given user has signed up for the event. Return false otherwise.
     */
    public boolean isUserSignedUp(UUID userID, UUID eventID) {
        return getEvent(eventID).isUserSignedUp(userID);
    }

    /**
     * Get the date and time of when the the given event will start.
     * @param eventID UUID object that represents the event
     * @return a string representation of the date and time of when the given event will start.
     */
    public String getEventSchedule(UUID eventID) {
        return getEvent(eventID).getEventSchedule();
    }

    /**
     * Get a string representation of the given event.
     * @param eventID UUID object that represents the event
     * @return a string representation of the given event which consists of the event name and the
     * date and starting time of the event.
     */
    public String getEventString(UUID eventID) {return getEvent(eventID).getEventString(); }

    /**
     * Get a list of all events.
     * @return a list of UUID objects where each UUID represents an unique event. The list will consist of
     * UUID objects of all events.
     */
    public List<UUID> getAllEventIds(){
        return new ArrayList<>(events.keySet());
    }

    /**
     * Get a list of all events on a day
     * @param date a LocalDate object that represents the day that user would like all events to be return
     * @return a list of UUID objects where each UUID represents an unique event.
     */
    public List<UUID> getEventsByDay(LocalDate date){
        ArrayList<UUID> outputEvents = new ArrayList<>();
        Collection<Event> events = this.events.values();
        for (Event e: events){
            if (e.getEventDateAndTime().toLocalDate().equals(date)){
                outputEvents.add(e.getEventId());
            }
        }
        return outputEvents;
    }

    /**
     * Get a list of events that have no date and time conflict with all events in a given list.
     * @param conflictingEventIds A List of UUID objects where each UUID represents an unique event.
     * @return A List of UUID objects where each UUID represents an unique event. The list consist of events
     * that have no date and time conflict with any event in conflictingEventIds.
     */
    public List<UUID> getNoConflictEventIds(List<UUID> conflictingEventIds){
        ArrayList<LocalDateTime> conflictingTimes = new ArrayList<>();
        for (UUID i : conflictingEventIds){
            if (!conflictingTimes.contains(getEventDateAndTime(i))){
                conflictingTimes.add(getEventDateAndTime(i));
            }
        }
        ArrayList<UUID> noConflictEventIds = new ArrayList<>();
        for (UUID i : events.keySet()){
            if (!conflictingTimes.contains(getEventDateAndTime(i))){
                noConflictEventIds.add(i);
            }
        }
        return noConflictEventIds;
    }

    /**
     * Remove an event from the HashMap events by its ID.
     * @param eventId UUID object that represents the event
     */
    public void removeEvent(UUID eventId){
        events.remove(eventId);
    }

    /**
     * Return the maximum number of attendees allowed in the given event.
     * @param eventID UUID object that represents the event
     * @return an integer object that represents the maximum number of attendees allowed in the given event.
     */
    public int getCapacity(UUID eventID) {
        return this.getEvent(eventID).getCapacity();
    }
    /**
     * Set maximum number of attendees allowed in the given event.
     * @param eventID UUID object that represents the event
     * @param capacity Integer object that represents maximum number of attendees allowed in the given event.
     */
    public void setCapacity(UUID eventID, int capacity) {
        this.getEvent(eventID).setCapacity(capacity);
    }

    /**
     * Get the type of given event which is based on the number of speakers for the event.
     * @param eventID UUID object that represents the event
     * @return a String object that represents the event type.
     */
    public String getEventType(UUID eventID) {
        return this.getEvent(eventID).getEventType();
    }

    /**
     * Check if given event has given feature
     * @param eventID the id corresponding to the event to check
     * @param feature the RoomFeature
     * @return true if event has the feature, false otherwise
     */
    public boolean hasFeature(UUID eventID, RoomFeatures feature){
        return getEvent(eventID).hasFeature(feature);
    }

    /**
     * Add a feature to the event
     * @param eventID the id corresponding to the event to add features to
     * @param feature the RoomFeature to add
     */
    public void addFeature(UUID eventID, RoomFeatures feature){
        getEvent(eventID).addFeature(feature);
    }

    /**
     * Remove a feature from the event
     * @param eventID the id corresponding to the event to remove the feature
     * @param feature the RoomFeature to remove
     */
    public void removeFeature(UUID eventID, RoomFeatures feature){
        getEvent(eventID).removeFeature(feature);
    }
}
