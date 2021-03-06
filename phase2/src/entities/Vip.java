package entities;

import enums.UserType;

import java.util.HashSet;
import java.util.UUID;


/***
 *  Features of VIP users:
 *  1. They can select a event and put it in their Interested Events, which works as a wishlist
 *  2. They can "follow" their favourite speakers
 *      and get notified when there are new events schedules with their favourite speakers
 *  3. They can hold a party
 */

public class Vip extends User {
    private HashSet<UUID> interestedEventsIds  = new HashSet<>();
    private HashSet<UUID> favouriteSpeakersId = new HashSet<>();


    /**
     * Initializes an object of VIP User and the minimal amount of information required is email, password and type
     *
     * @param name     the full name of the user
     * @param email    a valid email which will also serves as the username when logging in
     * @param password a combination of characters defined by user
     */
    public Vip(String name, String email, String password) {
        super(name, email, password, UserType.VIP);
    }

    /***
     * Add an event with eventId to interestedEventsIds.
     * @param eventId id of the event to be added
     */
    public void interestedInEventId(UUID eventId) {
        interestedEventsIds.add(eventId);
    }

    /***
     * Remove an event with eventId from interestedEventsIds.
     * @param eventId id of the event to be removed
     */
    public void uninterestedInEventId(UUID eventId) {
        interestedEventsIds.remove(eventId);
    }

    /***
     * Getter for interestedEventsIds
     * @return interestedEventsIds
     */
    public HashSet<UUID> getInterestedEventsIds() {
        return interestedEventsIds;
    }

    /***
     * Add a speaker with speakerId to favouriteSpeakersId
     * @param speakerId of a speaker to be added
     */
    public void likeSpeakerById(UUID speakerId) {
        favouriteSpeakersId.add(speakerId);
    }

    /***
     * Remove a speaker with speakerId from favouriteSpeakersId
     * @param speakerId of a speaker to be removed
     */
    public void dislikeSpeakerById(UUID speakerId) {
        favouriteSpeakersId.remove(speakerId);
    }

    /***
     * Getter for favouriteSpeakersId
     * @return favouriteSpeakersId
     */
    public HashSet<UUID> getFavouriteSpeakersId() {
        return favouriteSpeakersId;
    }
}
