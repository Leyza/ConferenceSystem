package entities;

import enums.RoomFeatures;

import java.io.Serializable;
import java.util.*;
import java.time.LocalDateTime;

/**
 * An entity class for room
 */
public class Room implements Serializable {
    private final UUID id;
    private final int capacity;
    private final String name;
    private int earliestStartTime;
    private int closingTime;
    private HashMap<LocalDateTime, ArrayList<Object>> schedule = new HashMap<>();
    private List<RoomFeatures> features = new ArrayList<>();
    private static final long serialVersionUID = -6846653117568982208L; // See: https://stackoverflow.com/a/8336144

    /**
     * Initializes an object of Room with a a randomly generated ID, set capacity,
     * set earliest start time and set closing start time for the room, empty schedule and given name. All events
     * using the room must end at or before 23:00 of the same day. There will be at least 1 hour down time everyday
     * from 23:00 to 00:00 where no events can be scheduled for.
     * @param name a string object that represents the name of the room.
     * @param capacity an integer object that represents the maximum number of attendees allowed to be in the room.
     *                 Must be greater than 0.
     * @param earliestStartTime an integer object that represents the earliest start time the room will be open for an
     *                          event in a 24 hour clock cycle. So 0 means the room will open at 00:00. earliestStartTime
     *                          must be between 0 to 22.
     * @param closingTime an integer object that represents the time that the room will close and events must end before
     *                    this time of the day in a 24 hour clock cycle. So 22 means the room will close at 22:00 and
     *                    all events must end before that time. closingTime must be between 1 to 23.
     * @throws IllegalArgumentException if earliestStartTime is less than 0 or closingTime is more than 23 or closingTime
     * is less than or equal to earliestStartTime or capacity is less than or equal to 0.
     */
    public Room(String name, int capacity, int earliestStartTime, int closingTime) {
        if (earliestStartTime >= 0 && closingTime <= 23 && closingTime > earliestStartTime && capacity > 0) {
            this.id = UUID.randomUUID();
            this.capacity = capacity;
            this.name = name;
            this.earliestStartTime = earliestStartTime;
            this.closingTime = closingTime;
        }
        else {
            throw new IllegalArgumentException("earliestStartTime and/or closingTime and/or capacity argument is not acceptable.");
        }
    }

    /**
     * Get room's unique ID.
     * @return the UUID that represents the room
     */
    public UUID getRoomID() {
        return this.id;
    }

    /**
     * Get the name of the room.
     * @return a string that represents the name of the room.
     */
    public String getRoomName() {
        return this.name;
    }

    /**
     * Get the capacity of the room.
     * @return an integer that represents maximum number of attendees allowed in the room for an event.
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Check if room is available to be booked for an event at the given starting time slot for given duration
     * @param dateAndTime a LocalDateTime object that represents the start time that will be checked if
     *                    room is available to booked.
     * @param duration an integer object that represents the number of hours from the starting time that needs to be
     *                 checked.
     * @return true iff there is an open slot at the given starting time and is within the opening time of the room.
     * Return false otherwise.
     */
    public boolean checkRoomAvailability(LocalDateTime dateAndTime, int duration) {
        for (Map.Entry<LocalDateTime, ArrayList<Object>> entry: schedule.entrySet()){
            if (!dateAndTime.plusHours(duration).isBefore(entry.getKey()) && !dateAndTime.isAfter(entry.getKey().plusHours(entry.getValue().indexOf(1)))){
                return false;
            }
            else if (dateAndTime.plusHours(duration).getHour() == closingTime && dateAndTime.plusHours(duration).getMinute() > 0 ) {
                return false;
            }
            else if (dateAndTime.plusHours(duration).getDayOfMonth() != dateAndTime.getDayOfMonth()) {
                return false;
            }
        }
        return !schedule.containsKey(dateAndTime) &&
                closingTime >= dateAndTime.getHour() + duration &&
                dateAndTime.getHour() >= earliestStartTime;
    }

    /**
     * Book a time slot of the room for an event if the timeslot is available.
     * @param dateAndTime a LocalDateTime object that represents the start time of the event.
     * @param eventID     a UUID that represents the unique ID of the event that will be hosted at the given time.
     * @param duration an integer object that represents the number of hours that will be booked.
     */
    public void bookRoom(LocalDateTime dateAndTime, UUID eventID, int duration) {
        ArrayList<Object> a = new ArrayList<>();
        a.add(eventID);
        a.add(duration);
        schedule.put(dateAndTime, a);
    }

    /**
     * Return a list of features this room has.
     * @return a list of RoomFeatures.
     */
    public List<RoomFeatures> getFeatures(){
        return features;
    }

    /**
     * Check if room has this feature
     * @param feature RoomFeature to check
     * @return true if room has given room feature, false otherwise
     */
    public boolean hasFeature(RoomFeatures feature){
        return features.contains(feature);
    }

    /**
     * Check if this room has the given features.
     * @param roomFeatures The given list of RoomFeatures
     * @return true if the room has the given features, false otherwise.
     */
    public boolean hasFeatures(List<RoomFeatures> roomFeatures){
        for (RoomFeatures feature : roomFeatures){
            if (!features.contains(feature)){
                return false;
            }
        }
        return true;
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
     * Add a Room Feature to this room
     * @param feature a RoomFeature
     */
    public void addFeature(RoomFeatures feature){
        features.add(feature);
    }

    /**
     * Remove a room feature from this room
     * @param feature the RoomFeature to remove
     */
    public void removeFeature(RoomFeatures feature){
        features.remove(feature);
    }

    /**
     * Get a string representation of room information.
     * @return A string of room information.
     */
    @Override
    public String toString() {
        return name + " | Capacity: " + capacity + " | Open at: " + earliestStartTime + ":00" + " | Closed at: " +
                closingTime + ":00" + " | Features:" + displayFeatures();
    }

}




