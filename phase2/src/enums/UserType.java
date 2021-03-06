package enums;

import java.util.List;
import java.util.ArrayList;

/**
 * Enum for the different types of users.
 * @author Andrew
 */
public enum UserType {
    ATTENDEE,
    ORGANIZER,
    SPEAKER,
    GUEST,
    VIP;

    /**
     * Returns a list of UserOption that a type of user can perform.
     * @param type The type of user that you would like user options for.
     * @return A list of UserOption containing all options that the user type can perform.
     */
    public static List<UserOption> optionsFor(UserType type) {
        switch (type) {
            case ATTENDEE:
                ArrayList<UserOption> attendeeOptions = new ArrayList();
                attendeeOptions.addAll(UserOption.ATTENDEE_OPTIONS);
                return attendeeOptions;
            case ORGANIZER:
                ArrayList<UserOption> organizerOptions = new ArrayList();
                organizerOptions.addAll(UserOption.ATTENDEE_OPTIONS);
                organizerOptions.addAll(UserOption.ORGANIZER_OPTIONS);
                return organizerOptions;
            case SPEAKER:
                ArrayList<UserOption> speakerOptions = new ArrayList();
                speakerOptions.addAll(UserOption.ATTENDEE_OPTIONS);
                speakerOptions.addAll(UserOption.SPEAKER_OPTIONS);
                return speakerOptions;
            case GUEST:
                ArrayList<UserOption> guestOptions = new ArrayList();
                guestOptions.addAll(UserOption.GUEST_OPTIONS);
                return guestOptions;
            case VIP:
                ArrayList<UserOption> vipOptions = new ArrayList();
                vipOptions.addAll(UserOption.ATTENDEE_OPTIONS);
                vipOptions.addAll(UserOption.VIP_OPTIONS);
                return vipOptions;
            default:
                return new ArrayList<UserOption>();
        }
    }
}
