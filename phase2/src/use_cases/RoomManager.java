package use_cases;

import java.util.*;
import java.time.*;
import entities.Room;
import enums.RoomFeatures;

/**
 * This class manages rooms.
 *
 * @author Nina
 */
public class RoomManager {
    private HashMap<UUID, Room> rooms;

    /**
     * Constructor for RoomManager.
     * @param allRooms a hashmap of room ids to rooms
     */
    public RoomManager(HashMap<UUID, Room> allRooms){
        rooms = allRooms;
    }

    /**
     * Add a room to rooms and return the id of the new room.
     * @param name name of the room
     * @param capacity integer object that represents maximum number of attendees allowed in the room.
     * @param earliestStartTime integer object that represents the hour in a 24 hour clock cycle,
     *                          when the room will be open for events. So 7 would mean, room would open at 7:00.
     * @param closingTime integer object that represents the hour in a 24 hour clock cycle,
     *                    when the room will be closed and no events can be scheduled from then till the end of the day.
     *                    So 21 would mean, room would be closed at 21:00.
     * @return an UUID object that represents the new room.
     */
    public UUID addRoom(String name, int capacity, int earliestStartTime, int closingTime){
        Room newRoom = new Room(name, capacity, earliestStartTime, closingTime);
        rooms.put(newRoom.getRoomID(), newRoom);
        return newRoom.getRoomID();
    }

    /**
     * Get the room capacity.
     * @param roomId which room
     * @return the capacity in integers of the room
     */
    public Integer getRoomCapacity(UUID roomId){
        return getRoom(roomId).getCapacity();
    }

    /**
     * Get the specified room by room id.
     * @param roomId room id
     * @return the room
     */
    public Room getRoom(UUID roomId){
        return rooms.get(roomId);
    }

    /**
     * Get and return all existing rooms
     * @return hashmap of room ids to rooms
     */
    public HashMap<UUID, Room> getAllRooms() {
        return rooms;
    }

    /**
     * Method that gets the name of the room from room id.
     * @param roomId UUID object that represents a room
     * @return The name of the room
     */
    public String getRoomName(UUID roomId){
        return rooms.get(roomId).getRoomName();
    }

    /**
     * Book the room out at a specific time for a given duration.
     * @param roomId the room id that was booked
     * @param time the time that was booked
     * @param eventId the event id that the room is booked for
     * @param duration an integer object that represents how long the room will be booked for for the event
     */
    public void bookRoom(UUID roomId, LocalDateTime time, UUID eventId, int duration){
        getRoom(roomId).bookRoom(time, eventId, duration);
    }

    /**
     * Check whether the room is available at a specific time for a given duration.
     * @param roomId the room id to check
     * @param time the time to check
     * @param duration the duration from the given time, represented in hour, that to be checked if the room is available
     * @return true if the room is available from the given time throughout the given duration, false otherwise.
     */
    public boolean checkRoomAvailability(UUID roomId, LocalDateTime time, int duration){
        return getRoom(roomId).checkRoomAvailability(time, duration);
    }

    /**
     * Return a list of all room ids.
     * @return A List of UUIDS of room ids.
     */
    public List<UUID> getAllRoomIds(){
        return new ArrayList<>(rooms.keySet());
    }

    /**
     * Return a list of room information: Room Name | Room capacity
     * @param selectRooms A List of room ids
     * @return A List of room information
     */
    public List<String> displayRooms(List<UUID> selectRooms){
        ArrayList<String> roomDisplay = new ArrayList<>();
        for (UUID i : selectRooms){
            roomDisplay.add(getRoom(i).toString());
        }
        return roomDisplay;
    }

    /**
     * Remove the booking of a event at the given booking time.
     * @param time an LocalDateTime object that represents the starting time of the booking event that would be removed.
     */
    public void removeBooking(LocalDateTime time) {
        rooms.remove(time);
    }


    /**
     * Get a string representation of all features in the room
     * @param roomId the room to show the feature of
     * @return A string of feature names
     */
    public String getFeaturesDisplay(UUID roomId){
        return getRoom(roomId).displayFeatures();
    }

    /**
     * Check whether the room has given feature.
     * @param roomId the id of the given room
     * @param roomFeature the RoomFeatures to check
     * @return true if room has the feature, false otherwise.
     */
    public boolean hasFeature(UUID roomId, RoomFeatures roomFeature){
        return getRoom(roomId).hasFeature(roomFeature);
    }

    /**
     * Check whether the room has all the given features.
     * @param roomId the id of the given room
     * @param roomFeatures a list of RoomFeatures
     * @return true if room has all given room features, false otherwise.
     */
    public boolean hasFeatures(UUID roomId, List<RoomFeatures> roomFeatures){
        return getRoom(roomId).hasFeatures(roomFeatures);
    }

    /**
     * Add a feature to a room
     * @param roomId the id of the given room
     * @param feature the RoomFeatures to add
     */
    public void addFeature(UUID roomId, RoomFeatures feature){
        getRoom(roomId).addFeature(feature);
    }

    /**
     * Remove a feature to a room
     * @param roomId the id of the given room
     * @param feature the RoomFeatures to remove
     */
    public void removeFeature(UUID roomId, RoomFeatures feature){
        getRoom(roomId).removeFeature(feature);
    }

}
