package enums;

import java.util.Arrays;
import java.util.List;

/**
 * Enum for all options that are available to users.
 * Permissions for what each user can do are handled in the controllers.
 * @author Andrew
 */
public enum UserOption {
    ACCESS_MESSAGING,
    VIEW_AVAILABLE_EVENTS,
    VIEW_ALL_EVENTS,
    VIEW_SIGN_UP_EVENTS,
    VIEW_EVENTS_BY_DAY,
    VIEW_EVENTS_BY_SPEAKER,
    SIGN_UP_FOR_EVENT,
    CANCEL_SPOT_FOR_EVENT,
    CREATE_EVENT,
    CREATE_USER,
    CHANGE_USER_TYPE,
    SEND_MESSAGE_TO_EVENT_ATTENDEES,
    SEND_MESSAGE_TO_ALL,
    VIEW_SCHEDULED_TALK,
    VIEW_TALK_ATTENDEES,
    SCHEDULE_SPEAKER_TALK,
    ADD_ROOMS,
    VIEW_CONFERENCE_STATS,
    REMOVE_SPEAKER,
    CANCEL_EVENT,
    CHANGE_CAPACITY,
    VIEW_USER_REQUESTS,
    ADDRESS_USER_REQUEST,
    LOG_IN,
    LOG_OUT,
    EXIT,
    EXPLORE_AS_A_GUEST,
    VIEW_FAVOURITE_SPEAKERS,
    CREATE_PARTY,
    VIEW_INTERESTED_EVENTS,
    MANAGE_INTERESTED_EVENTS,
    MANAGE_FAVOURITE_SPEAKERS;

    public final static List<UserOption> ATTENDEE_OPTIONS = Arrays.asList(ACCESS_MESSAGING,
            VIEW_AVAILABLE_EVENTS, VIEW_ALL_EVENTS, VIEW_SIGN_UP_EVENTS, SIGN_UP_FOR_EVENT, CANCEL_SPOT_FOR_EVENT, VIEW_EVENTS_BY_DAY,
            VIEW_EVENTS_BY_SPEAKER);
    public final static List<UserOption> ORGANIZER_OPTIONS = Arrays.asList(CREATE_EVENT, CREATE_USER, CHANGE_USER_TYPE, SCHEDULE_SPEAKER_TALK, ADD_ROOMS, SEND_MESSAGE_TO_ALL, VIEW_CONFERENCE_STATS, REMOVE_SPEAKER, CANCEL_EVENT, CHANGE_CAPACITY, VIEW_USER_REQUESTS, ADDRESS_USER_REQUEST);
    public final static List<UserOption> SPEAKER_OPTIONS = Arrays.asList(VIEW_SCHEDULED_TALK, VIEW_TALK_ATTENDEES, SEND_MESSAGE_TO_EVENT_ATTENDEES);
    public final static List<UserOption> GUEST_OPTIONS = Arrays.asList(VIEW_ALL_EVENTS, VIEW_EVENTS_BY_DAY, VIEW_EVENTS_BY_SPEAKER, LOG_IN);
    public final static List<UserOption> VIP_OPTIONS = Arrays.asList(CREATE_PARTY, MANAGE_INTERESTED_EVENTS, MANAGE_FAVOURITE_SPEAKERS);


    /**
     * Returns an english representation of the user option.
     * @return The english representation of the user option.
     */
    @Override
    public String toString() {
        switch(this) {
            case ACCESS_MESSAGING:
                return "Enter Messenger";
            case VIEW_AVAILABLE_EVENTS:
                return "View Events Available for You to Sign-Up";
            case VIEW_ALL_EVENTS:
                return "View All Scheduled Events";
            case VIEW_SIGN_UP_EVENTS:
                return "View Events You Have Signed up To";
            case VIEW_EVENTS_BY_DAY:
                return "View Events by Day";
            case VIEW_EVENTS_BY_SPEAKER:
                return "View Events by Speaker";
            case SIGN_UP_FOR_EVENT:
                return "Sign Up for Event";
            case CANCEL_SPOT_FOR_EVENT:
                return "Cancel Spot in Event";
            case CREATE_EVENT:
                return "Create Event";
            case CREATE_USER:
                return "Create User";
            case CHANGE_USER_TYPE:
                return "Change User Type";
            case SEND_MESSAGE_TO_EVENT_ATTENDEES:
                return "Send Message to Event Attendees";
	        case SEND_MESSAGE_TO_ALL:
	        	return "Send Message to All";
            case VIEW_SCHEDULED_TALK:
                return "View All Events That You Are Scheduled to Speak In";
	        case VIEW_TALK_ATTENDEES:
	        	return "View Attendees for an Event you are Speaking In";
            case ADD_ROOMS:
                return "Create New Rooms";
            case SCHEDULE_SPEAKER_TALK:
                return "Schedule Speakers to Speak in an Event";
            case VIEW_CONFERENCE_STATS:
                return "View Interesting Statistics of a All Conferences/Events.";
            case REMOVE_SPEAKER:
                return "Remove a speaker from a event's speaker list";
            case CANCEL_EVENT:
                return "Cancel an event";
            case CHANGE_CAPACITY:
                return "Change an event's capacity";
            case VIEW_USER_REQUESTS:
                return "View all User Requests";
            case ADDRESS_USER_REQUEST:
                return "Mark a User Request as Addressed";
            case LOG_IN:
                return "Login";
            case LOG_OUT:
                return "Log Out";
            case EXIT:
                return "Exit and Save the Program";
            case EXPLORE_AS_A_GUEST:
                return "Explore as a Guest";
            case CREATE_PARTY:
                return "Host a Party";
            case MANAGE_INTERESTED_EVENTS:
                return "Manage Interested Events";
            case MANAGE_FAVOURITE_SPEAKERS:
                return "Manage Favourite Speakers";
            default:
                return "Unsupported Option";
        }
    }
}