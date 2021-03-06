package enums;

public enum RoomFeatures {
    WHITEBOARD,
    PROJECTOR,
    AUDITORIUM_SETUP,
    CONFERENCE_SETUP,
    EMPTY_SETUP,
    COMPUTER,
    STAGE,
    MICROPHONE;

    /**
     * Get string representation of the enum values
     * @return a string
     */
    @Override
    public String toString(){
        switch (this){
            case WHITEBOARD:
                return "Whiteboard: Room has a whiteboard.";
            case PROJECTOR:
                return "Projector: Room has a projector and screen.";
            case AUDITORIUM_SETUP:
                return "Auditorium setup: Room has rows of seats surrounding a speaker platform.";
            case CONFERENCE_SETUP:
                return "Conference setup: Room has seats around tables.";
            case EMPTY_SETUP:
                return "Empty setup: Room is empty.";
            case COMPUTER:
                return "Computer: Room has a computer accessible to speakers, organizers, and admins only.";
            case STAGE:
                return "Stage: Room has an elevated stage platform.";
            case MICROPHONE:
                return "Microphone: Room has a microphone and speakers.";
            default:
                return "Not an available feature";
        }
    }

    /**
     * Get the name of the enum values
     * @return a string
     */
    public String featureName() {
        switch (this){
            case WHITEBOARD:
                return "Whiteboard";
            case PROJECTOR:
                return "Projector";
            case AUDITORIUM_SETUP:
                return "Auditorium setup";
            case CONFERENCE_SETUP:
                return "Conference setup";
            case EMPTY_SETUP:
                return "Empty setup";
            case COMPUTER:
                return "Computer";
            case STAGE:
                return "Stage";
            case MICROPHONE:
                return "Microphone";
            default:
                return "Unknown feature";
        }
    }
}
