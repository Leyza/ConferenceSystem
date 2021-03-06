package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import use_cases.*;

/**
 * Controller for Vips. Contains functionality exclusive to vips.
 */
public class VipController extends UserController {

    /**
     * Get a list of event ids that the vip has shown interests.
     * @param userManager The UserManager to use.
     * @return A List of interested event ids
     */
    public List<UUID> getInterestedEventIds(UserManager userManager) {
        return new ArrayList<>(userManager.getInterestedEventsIds());
    }

    /***
     * As the user has no interests in the event, remove it from the favourite events of the user
     * @param userManager The UserManager to use.
     * @param eventToRemove id of the event to be removed
     * @return a string indicating whether the event was removed or it wasn't there at all
     */
    public String uninterestedInEvent(UserManager userManager, UUID eventToRemove) {
        if (userManager.uninterestedInEvent(eventToRemove)) {
            return "Successfully completed.";
        } else {
            return "Uh-oh, you were not interested anyway!";
        }
    }

    /***
     * As the user is interested in the event, add it to the favourite events of the user
     * @param userManager The UserManager to use.
     * @param eventToAdd id of the event to be added
     * @return a string indicating whether the event was added or it was the user showing interests twice
     */
    public String interestedInEvent(UserManager userManager, UUID eventToAdd) {
        if (userManager.interestedInEvent(eventToAdd)) {
            return "Successfully completed.";
        } else {
            return "You were interested already!";
        }
    }

    /**
     * A method that returns a string displaying all the active vip user's favourite speakers.
     * @param userManager The UserManager to use.
     * @return A string representation of the logged in user's conversations.
     */
    public String viewFavouriteSpeakers(UserManager userManager){
        List<UUID> favouriteSpeakers = userManager.getFavouriteSpeakersId();
        String output = "\nList of Favourite Speakers\n===========================\n";
        output += displayUsersFromIdList(userManager, favouriteSpeakers);
        return output;
    }

    /***
     * Lets the vip user to add a speaker to their favourites
     * @param userManager The UserManager to use.
     * @param speakerId id of the speaker to be added
     * @return a string that indicates whether the task was done successfully
     */
    public String addFavouriteSpeaker(UserManager userManager, UUID speakerId) {
        if (!userManager.getFavouriteSpeakersId().contains(speakerId)) {
            userManager.likeSpeakerById(speakerId);
            return "Successfully added to our favourites.";
        } else {
            return "Please try again with the valid speaker!";
        }
    }

    /***
     * Lets the vip user to remove a speaker from their favourites
     * @param userManager The UserManager to use.
     * @param speakerId id of the speaker to be removed
     * @return a string that indicates whether the task was done successfully
     */
    public String removeFavouriteSpeaker(UserManager userManager, UUID speakerId) {
        if (userManager.getFavouriteSpeakersId().contains(speakerId)) {
            userManager.dislikeSpeakerById(speakerId);
            return "Well, the speaker was not your favourite this time!";
        } else {
            return "Please try again with the valid speaker!";
        }
    }

    /***
     * Get the set of all favourite speakers of the (vip) user, in order to display and let the user
     * to select from it to perform further actions
     * @param userManager The UserManager to use.
     * @return List of the favourite speakers' ids
     */
    public List<UUID> getFavouriteSpeakersIds(UserManager userManager) {
        List<UUID> favouriteSpeakersId = userManager.getFavouriteSpeakersId();
        return favouriteSpeakersId;
    }
}
