package entities;

import enums.RoomFeatures;

import java.io.Serializable;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * An entity class for events.
 */
public class Event implements Serializable {
    private final UUID id;
    private String name;
    private HashSet<UUID> speaker = new HashSet<>();
    private LocalDateTime dateAndTime;
    private UUID room;
    private int capacity;
    private int duration;
    private Set<UUID> attendees = new HashSet<>();
    private List<RoomFeatures> features;
    private static final long serialVersionUID = 1626213873034458493L; // See: https://stackoverflow.com/a/8336144

    /**
     * Initializes an object of Event with a randomly generated ID, empty attendees list, given
     * room ID, given speaker ID, given date and time and given duration of event. All events must have a duration
     * between 1 to 23 hours.
     * @param name a String object that represents the name of the event
     * @param dateAndTime a LocalDateTime object that represents the start time and date of event
     * @param room a UUID object that represents the room where event will be held
     * @param capacity an Integer object that represents the maximum number of attendees allowed in the event. Must be greater than 0.
     * @param duration an Integer object represents the number of hours that the event will last. All events
     *                 can only last increment in hours.
     * @param features a list of RoomFeatures objects where each represent a feature that is required for the event
     * @throws IllegalArgumentException if duration is less than or equal to 0 or more than 23.
     */

    public Event (String name, LocalDateTime dateAndTime, UUID room, int capacity, int duration, List<RoomFeatures> features) {
        if (duration > 0 && duration < 24 && capacity > 0) {
            this.name = name;
            this.id = UUID.randomUUID();
            this.dateAndTime = dateAndTime;
            this.room = room;
            this.capacity = capacity;
            this.duration = duration;
            this.features = features;
        }
        else {
            throw new IllegalArgumentException("Event not created, duration must be greater than 0 and less than 24. and" +
                    "capacity must be greater than 0");
        }
    }

    /**
     * Get event's unique ID.
     * @return the UUID that represents this event.
     */
    public UUID getEventId () {
        return this.id;
    }

    /**
     * Get name of this event.
     * @return string representation of the name of this event.
     */
    public String getEventName () {
        return this.name;
    }


    /**
     * Get the speaker of the event.
     * @return the speaker's unique ID for the event.
     */
    public HashSet<UUID> getSpeakers() {
        return this.speaker;
    }

    /**
     * Add a speaker, represented by its unique ID, to the speaker list of event.
     * @param speakerID a UUID that represents the unique ID of the speaker that would be added to the speaker list
     *                  of the event.
     */
    public void addSpeaker(UUID speakerID) {
        this.speaker.add(speakerID);
    }

    /**
     * Add set of speakers, represented by its unique ID, to the speaker list of the event.
     * @param speakersID a set of UUID that represents unique ID of speakers that would be added to the speaker list
     *                  of the event.
     */
    public void addSpeaker(HashSet<UUID> speakersID) {
        this.speaker.addAll(speakersID);
    }

    /**
     * Remove the given speaker from the speaker list of the event.
     * @param speakerID a UUID object that represents the speaker.
     */
    public void removeSpeaker(UUID speakerID) {
        this.speaker.remove(speakerID);
    }

    /**
     * Get the UUID that represents the unique ID of the room that is hosting the event.
     * @return the UUID that represents the room that is hosting the event.
     */
    public UUID getEventRoom() {
        return this.room;
    }

    /**
     * Get the list of attendees who signed up for the event.
     * @return the list of attendees who signed up for the event, represented by each of their own unique ID.
     */
    public Set<UUID> getAttendees() {
        return this.attendees;
    }

    /**
     * Add an attendee, represented by its unique ID, to the attendee list of the event.
     * @param attendeeId a UUID that represents the unique ID of the attendee that would be added to the attendee list.
     */
    public void addAttendee(UUID attendeeId) {
        this.attendees.add(attendeeId);
    }

    /**
     * Remove an attendee, represented by its unique ID, from the attendee list of the event.
     * @param attendeeID a UUID that represents the unique ID of the attendee that would be removed
     * from the attendee list.
     */
    public void removeAttendee(UUID attendeeID) {
        this.attendees.remove(attendeeID);
    }


    /**
     * Get a string representation of the date and time of when the event will start.
     * @return a string representation of the date and time of when the event will start.
     */
    public String getEventSchedule() {
        DateTimeFormatter formattedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.dateAndTime.format(formattedDate);
    }

    /**
     * Get a string representation of the event.
     * @return a string representation of the event which consists of the event name and the date, starting time
     * and type of the event.
     */
    public String getEventString() {
        String output = "";
        output = output + this.name + " : " + getEventSchedule() + " | " + "Event Type: " + getEventType();
        return output;
    }

    /**
     * Get the date and starting time of the event.
     * @return a LocalDateTime object that represents the date and starting time for the event.
     */
    public LocalDateTime getEventDateAndTime() {
        return this.dateAndTime;
    }


    /**
     * Return true iff given user has signed up for the event. Return false otherwise.
     * @param user UUID object that represents the user that is checked if the user is currently signed up to the event.
     * @return true iff given user has signed up for the event. Return false otherwise.
     */
    public boolean isUserSignedUp(UUID user) {
        return this.attendees.contains(user);
    }

    /**
     * Return the maximum capacity of this event.
     * @return an integer representing the maximum number of attendees that are allowed to attend the event.
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Set the maximum number of attendees that are allowed to attend the event.
     * @param capacity an integer representing the new maximum number of attendees that are allowed to attend the event.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Get the event type based on the number of speakers in the event.
     * @return a String object that represents the type of this event.
     */
    public String getEventType() {
        if (this.speaker.size() == 0) {
            return "Party";
        }
        else if (this.speaker.size() == 1) {
            return "Talk";
        }
        else {
            return "Panel";
        }
    }

    /**
     * Convert the features of this room into a string representation.
     * @return a string of features in this room.
     */
    public String displayFeatures(){
        StringBuilder display = new StringBuilder();
        for (RoomFeatures feature: features){
            display.append(" ").append(feature.featureName()).append(",");
        }
        if (display.length() > 1){
            display.deleteCharAt(display.length() - 1);
        }
        return display.toString();
    }

    /**
     * Check if this event has the feature
     * @param feature the RoomFeature to check
     * @return true if the event has the feature, false otherwise
     */
    public boolean hasFeature(RoomFeatures feature){
        return features.contains(feature);
    }

    /**
     * Add a feature to this event
     * @param feature The RoomFeature to add
     */
    public void addFeature(RoomFeatures feature){
        features.add(feature);
    }

    /**
     * Remove a feature from this event
     * @param feature the RoomFeature to remove
     */
    public void removeFeature(RoomFeatures feature){
        features.remove(feature);
    }
}
