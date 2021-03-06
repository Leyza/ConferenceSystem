package controllers;

import presenters.AppPresenter;
import enums.ConvoType;
import enums.RoomFeatures;
import enums.UserOption;
import enums.UserType;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.format.*;
import gateways.ReadWriteSerializable;
import use_cases.*;
import main.TestData;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Main Controller that handles user input and passes output to the presenter.
 */
public class MainController {
    private AppPresenter presenter = new AppPresenter();
    private UserController userController = new UserController();
    private VipController vipController = new VipController();
    private SpeakerController speakerController = new SpeakerController();
    private OrganizerController organizerController = new OrganizerController();
    private Scanner scanner = new Scanner(System.in);
    private UserManager userManager;
    private EventsManager eventsManager;
    private ConversationManager conversationManager;
    private RoomManager roomManager;
    private UserRequestManager userRequestManager;

    private ReadWriteSerializable readWriteSerializable;


    public MainController() {
        //Instantiation of the readWriteSerializable
        this.readWriteSerializable = new ReadWriteSerializable();

        if (readWriteSerializable.hasSaveData()) {
            this.userManager = new UserManager(this.readWriteSerializable.usersReadFromSerializable());
            this.conversationManager = new ConversationManager(this.readWriteSerializable.conversationsReadFromSerializable());
            this.roomManager = new RoomManager(this.readWriteSerializable.roomsReadFromSerializable());
            this.eventsManager = new EventsManager(this.readWriteSerializable.eventsReadFromSerializable());
            this.userRequestManager = new UserRequestManager(this.readWriteSerializable.userRequestsReadFromSerializable());
        } else { // App's first launch, let's generate test data.
            this.userManager = new UserManager(new ArrayList<>());
            this.conversationManager = new ConversationManager(new ArrayList<>());
            this.roomManager = new RoomManager(new HashMap<>());
            this.eventsManager = new EventsManager(new HashMap<>());
            this.userRequestManager = new UserRequestManager(new ArrayList<>());
            TestData.generateUsersDemoData(userManager);
            TestData.generateConversationsDemoData(userManager, conversationManager);
            TestData.generateRoomsDemoData(roomManager);
            TestData.generateEventsDemoData(roomManager, eventsManager, userManager);
        }

    }

    /**
     * Gets credentials from a user and logs in with them.
     * If the credentials are invalid, it asks for them until they are.
     */
    private void login() {
        boolean loggedIn = false;
        while (!loggedIn) {
            presenter.println("What are your credentials?");
            String email = getUserInput("Email: ");
            String password = getUserInput("Password: ");
            loggedIn = userController.login(userManager, email, password);
            presenter.println(loggedIn ? "Logged in successfully." : "Invalid credentials.");
        }
    }

    /**
     * Logs out the current user.
     */
    private void logout() {
        presenter.println("Logging out... good bye!");
        userController.logout(userManager);
    }

    /**
     * Login a user as a guest.
     */
    private void exploreAsAGuest() {
        presenter.println(userController.loginAsAGuest(userManager));
    }


    /**
     * Displays all options the user can perform and asks the user which one they want to select.
     * If the selection is valid, it calls the option's method.
     * If the selection is invalid, it informs the user and asks for input again.
     */
    public void displayMenu() {
        while (true) {
            presenter.println("What would you like to do?");
            UserOption selection = getSelectionFromList(userController.getUserOptions(userManager));
            routeToOption(selection);
            if (selection == UserOption.EXIT) break;
            presenter.println(""); // Print a new line to add some distinction in the console between actions.
        }
    }

    /**
     * Gets user input through the console.
     * @param caption Text that will be displayed beside where the user will enter input.
     * @return A string of the user's input.
     */
    private String getUserInput(String caption) {
        presenter.print(caption);
        String input = scanner.nextLine();
        return input;
    }

    /**
     * Takes in a list and shows its contents to a user. It lets them select an element by index or its
     * string representation. After a valid selection, it returns the element they selected.
     * @param list The list you want the user to pick an element from.
     * @return The element the user selected.
     */
    private <T> T getSelectionFromList(List<T> list) {
        if (list.size() == 0) throw new IllegalArgumentException("List must have at least one element in it.");
        T selection = null;

        while (selection == null) {
            for (int i = 0; i < list.size(); i++) {
                presenter.println((i + 1) + ". " + list.get(i));
            }

            presenter.println(String.format("You can either refer to an option using the index or by writing the" +
                    " name of the option (i.e. '%d', or '%s').\n", 1, list.get(0).toString()));
            String inputSelection = getUserInput("Selection: ");

            // Check for option name (i.e. 'Add Friend')
            for (int i = 0; i < list.size(); i++) {
                T option = list.get(i);
                if (option.toString().equalsIgnoreCase(inputSelection)) {
                    selection = option;
                }
            }

            // Check for index (i.e. '1')
            try {
                int inputSelectionInt = Integer.parseInt(inputSelection);
                selection = list.get(inputSelectionInt - 1);
            } catch (Exception e) {}

            if (selection == null) presenter.println("Invalid selection, please enter a valid option.");
        }

        return selection;
    }

    /**
     * Routes a user's option to its associated method and calls it.
     * @param option The UserOption that needs to be routed.
     */
    private void routeToOption(UserOption option) {
        switch (option) {
            case VIEW_AVAILABLE_EVENTS:
                viewAvailableEvents();
                break;
            case VIEW_ALL_EVENTS:
                viewAllEvents();
                break;
            case VIEW_SIGN_UP_EVENTS:
                viewSignUpEvents();
                break;
            case VIEW_EVENTS_BY_SPEAKER:
                viewEventsBySpeaker();
                break;
            case VIEW_EVENTS_BY_DAY:
                viewEventsByDay();
                break;
            case CREATE_EVENT:
                createEvent();
                break;
            case CREATE_USER:
                createUser();
                break;
            case CHANGE_USER_TYPE:
                changeUserType();
                break;
            case ACCESS_MESSAGING:
                accessMessaging();
                break;
            case SIGN_UP_FOR_EVENT:
                signUpForEvent();
                break;
            case CANCEL_SPOT_FOR_EVENT:
                cancelSpotForEvent();
                break;
            case SEND_MESSAGE_TO_EVENT_ATTENDEES:
                sendMessageToEventAttendees();
                break;
            case SEND_MESSAGE_TO_ALL:
                sendMessageToAll();
                break;
            case VIEW_SCHEDULED_TALK:
                viewScheduledTalk();
                break;
            case VIEW_TALK_ATTENDEES:
                viewTalkAttendees();
                break;
            case ADD_ROOMS:
                addRoom();
                break;
            case SCHEDULE_SPEAKER_TALK:
                scheduleSpeakerTalk();
                break;
            case VIEW_CONFERENCE_STATS:
                viewConferenceStats();
                break;
            case REMOVE_SPEAKER:
                removeSpeaker();
                break;
            case CANCEL_EVENT:
                cancelEvent();
                break;
            case CHANGE_CAPACITY:
                changeEventCapacity();
                break;
            case ADDRESS_USER_REQUEST:
                addressUserRequest();
                break;
            case VIEW_USER_REQUESTS:
                viewUserRequest();
                break;
            case LOG_IN:
                login();
                break;
            case LOG_OUT:
                logout();
                break;
            case EXPLORE_AS_A_GUEST:
                exploreAsAGuest();
                break;
            case EXIT:
                exit();
                break;
            case CREATE_PARTY:
                createParty();
                break;
            case MANAGE_INTERESTED_EVENTS:
                manageInterestedEvents();
                break;
            case MANAGE_FAVOURITE_SPEAKERS:
                manageFavouriteSpeakers();
                break;
            default:
                presenter.println("Error! This functionality is not supported right now.");
        }
    }

    /**
     * Shows interesting statistics of all events for organizers.
     */
    private void viewConferenceStats() {
        //Bar graph in console of Events with the highest number of attendees.
        presenter.println("Below is every event available, with a bar graph below each event that shows the number of enrolled attendees for each event.");
        HashMap<String, Integer> topEvents = userController.statsGetTopEnrolledEvents(eventsManager);
        ArrayList<String> eventNames = new ArrayList<>(topEvents.keySet());
        for (int i = 0; i < topEvents.size(); i++) {
            presenter.println(eventNames.get(i));
            String barLine = String.join("", Collections.nCopies(topEvents.get(eventNames.get(i)), "[+]"));
            presenter.println(barLine);
        }

        //Bar graph in console of Users with the highest number of enrolled events.
        presenter.println("Below is every user available, with a bar graph below each user's name that shows the number of enrolled events for each user.");
        HashMap<String, Integer> topUsers = userController.statsGetTopEnrolledUsers(userManager);
        ArrayList<String> userNames = new ArrayList<>(topUsers.keySet());
        for (int i = 0; i < topUsers.size(); i++) {
            presenter.println(userNames.get(i));
            String barLine = String.join("", Collections.nCopies(topUsers.get(userNames.get(i)), "[+]"));
            presenter.println(barLine);
        }
    }

    /**
     * Allows organizer to create a new user account for any user type except for guest user
     */
    private void createUser(){
        // Let organizer choose user type
        String checkUserType = "";
        String type = "";
        while (checkUserType.equals("")) {
            presenter.println("Which user account would you like to create?");

            // print user type options except for GUEST
            for (UserType u : UserType.values()) {
                if (! u.name().equals("GUEST")){
                    presenter.println("- " + u.name());
                }
            }
            // Convert type to upper case so it is ready to use in the following steps
            type = getUserInput("Please specify the user type (case does not matter): ").toUpperCase();

            checkUserType = checkUserType(type);
        }

        // Collect other user information
        String name = getUserInput("Please enter name of the user: ");
        String email = getUserInput("Please enter an email as user name: ");
        String pwd = getUserInput("Please enter a password: ");
        String result = organizerController.createUser(userManager, email, pwd, name, type);
        presenter.println(result);
    }

    /**
     * Checks whether the input is a valid user type
     * @param type a string received from user
     * @return the correct user type or empty string
     */
    private String checkUserType(String type) {
        for (UserType u : UserType.values()) {
            if (u.name().equals(type.toUpperCase())) {
                presenter.println("Valid user type: "+type);
                return type;
            }
        }
        presenter.println("Invalid user type. Please try again.");
        return "";
    }

    /**
     * Allows organizer to change a user's type provided correct credential
     */
    private void changeUserType() {
        // Collect credential
        String email  = getUserInput("Please enter the user email: ");
        String pwd = getUserInput("Please enter the user password: ");

        // Let organizer choose user type
        String checkUserType = "";
        String type = "";
        while (checkUserType.equals("")) {
            presenter.println("Which user type are you going to set the user as?");

            // print user type options except for GUEST
            for (UserType u : UserType.values()) {
                if (! u.name().equals("GUEST")){
                    presenter.println("- " + u.name());
                }
            }
            // Convert type to upper case so it is ready to use in the following steps
            type = getUserInput("Please specify the user type (case does not matter): ").toUpperCase();

            checkUserType = checkUserType(type);
        }

        String result = organizerController.changeUserType(userManager, email, pwd, type);
        presenter.println(result);
    }

    /**
     * Display all events that this user can sign up to.
     */
    private void viewAvailableEvents() {
        if (displayEvents(getAvailableEventIds(getSignedUpEventIds())).isEmpty()) {
            presenter.println("There are currently no events that are available for you to sign up to. Either those events " +
                    "are full or you have a schedule conflict with those events.");
        }
        else {
            for (String s : displayEvents(getAvailableEventIds(getSignedUpEventIds()))) {
                presenter.println(s);
            }
        }
    }

    /**
     * Display all events.
     */
    private void viewAllEvents() {
        if (displayEvents(userController.getAllEventIds(eventsManager)).isEmpty()) {
            presenter.println("There are currently no events scheduled.");
        }
        else {
            for (String s : displayEvents(userController.getAllEventIds(eventsManager))) {
                presenter.println(s);
            }
        }
    }

    /**
     * Display all events that user has signed up to.
     */
    private void viewSignUpEvents() {
        if (displayEvents(getSignedUpEventIds()).isEmpty()) {
            presenter.println("You are not signed up to any events currently.");
        }
        else {
            for (String s : displayEvents(getSignedUpEventIds())) {
                presenter.println(s);
            }
        }
    }


    /**
     * Helps organizer to create an event with detailed specification
     */
    private void createEvent(){
        createEventHelper("event");
    }

    /**
     * Method that lets organizers create new events.
     */
    private void createEventHelper(String type) {
        String eventName = getUserInput("Name of the " + type + ": ");
        UUID roomId;
        UUID speakerId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String selectTime = getUserInput("The time the " + type + " start in the format: dd/MM/yyyy HH:mm ");
        while (!isValidDateTime(selectTime, formatter)) {
            selectTime = getUserInput("Invalid time. Select another time for the " + type + " in the format: dd/MM/yyyy HH:mm ");
        }
        LocalDateTime eventTime = LocalDateTime.parse(selectTime, formatter);

        String strCapacity = getUserInput("Enter the maximum amount of attendee allowed in this " + type + ". (e.g. '10'): ");
        while (!canParse(strCapacity)){
            strCapacity = getUserInput("Invalid capacity. Enter the maximum amount of attendee allowed in this " + type + ". (e.g. '10'): ");
        }
        int capacity = Integer.parseInt(strCapacity);

        String strDuration = getUserInput("Enter the duration of the " + type + " in hours, must be between 1 to 23. (e.g. '5'): ");
        while (!canParse(strDuration)){
            strDuration = getUserInput("Invalid duration. Enter the duration of the " + type + " in hours, must be between 1 to 23. (e.g. '5'): ");
        }
        int duration = Integer.parseInt(strDuration);

        presenter.println("Select the features this " + type + " needs. Select the same feature twice to unselect it. Select 'Done' to save and show suggested room list.");
        List<String> roomFeaturesDisplay = displayAllRoomFeatures();
        List<String> selectedFeatures = new ArrayList<>();
        roomFeaturesDisplay.add("Done");
        String selectedFeature = getSelectionFromList(roomFeaturesDisplay);
        while (!selectedFeature.equals("Done")){
            if (selectedFeatures.contains(selectedFeature)){
                selectedFeatures.remove(selectedFeature);
                presenter.println("Feature removed.");
            }
            else{
                selectedFeatures.add(selectedFeature);
                presenter.println("Feature added.");
            }
            presenter.println("Current features:" + selectedFeatures);
            selectedFeature = getSelectionFromList(roomFeaturesDisplay);
        }
        List<RoomFeatures> requiredFeatures = convertNamesToFeatures(selectedFeatures);

        presenter.println("Here are suggested rooms based on your " + type + " requirements. Select the Room for the " + type + ":");

        List<UUID> roomIds = getSuggestedRoomIds(requiredFeatures, capacity, eventTime, duration);
        List<String> displayRooms = displayRooms(roomIds);
        if (roomIds.size() == 0){
            presenter.println("No rooms fit the requirements that your " + type + " has.");
        }
        displayRooms.add(0, "Show all rooms list.");
        String selectRoom = getSelectionFromList(displayRooms);
        if (selectRoom.equalsIgnoreCase("Show all rooms list.")){
            roomIds = getAllRoomIds();
            displayRooms = displayRooms(roomIds);
            selectRoom = getSelectionFromList(displayRooms);
            roomId = roomIds.get(displayRooms.indexOf(selectRoom));
        }
        else {
            roomId = roomIds.get(displayRooms.indexOf(selectRoom) - 1);
        }

        HashSet<UUID> speakers = new HashSet<>();
        if (type.equalsIgnoreCase("event")) {
            String selection = getUserInput("Type y if the event will have speaker, n if the event will have no speaker: ");
            while (!selection.equals("n") && !selection.equals("y")) {
                selection = getUserInput("Invalid input. Type y if the event will have speaker, n if the event will have no speaker: ");
            }
            if (!selection.equals("n")) {
                String selection2 = "y";
                while (selection2.equals("y")) {
                    presenter.println("Select the speaker for the event (Note: No duplicate speakers will added as speakers): ");
                    List<UUID> allSpeakerIds = getAllSpeakerIds();
                    List<String> displaySpeakers = displayUsers(allSpeakerIds);
                    String selectSpeaker = getSelectionFromList(displaySpeakers);

                    speakerId = allSpeakerIds.get(displaySpeakers.indexOf(selectSpeaker));
                    speakers.add(speakerId);
                    selection2 = getUserInput("Type y if you want to add more speakers, type n if all speakers have been selected: ");
                }
            }
        }

        String confirmation = getUserInput("Confirm creation of " + eventName + "? Type yes/no: ");
        if (confirmation.equals("yes")) {
            presenter.println(organizerController.createNewEvent(roomManager, eventsManager, userManager, conversationManager, eventName, eventTime, roomId, speakers, capacity, duration, requiredFeatures));
        } else {
            presenter.println("Aborting " + type + " creation...");
        }
    }

    /**
     * Get A list of room ids that don't conflict with given parameters
     * @param requiredFeatures List of required features
     * @param capacity Required capacity
     * @param startTime required start time of event
     * @param duration duration of event
     * @return List of room ids
     */
    private List<UUID> getSuggestedRoomIds(List<RoomFeatures> requiredFeatures, int capacity, LocalDateTime startTime, int duration){
        List<UUID> allRooms = getAllRoomIds();
        ArrayList<UUID> suggestedRooms = new ArrayList<>();
        for (UUID room: allRooms){
            if (roomManager.hasFeatures(room, requiredFeatures) && roomManager.getRoomCapacity(room) >= capacity && roomManager.checkRoomAvailability(room, startTime, duration)){
                suggestedRooms.add(room);
            }
        }
        return suggestedRooms;
    }

    /**
     * Get a list of all room ids.
     * @return A List of room ids.
     */
    private List<UUID> getAllRoomIds(){
        return roomManager.getAllRoomIds();
    }

    /**
     * Method that initiates messaging framework. Takes user inputs and performs messaging functions.
     */
    private void accessMessaging() {
        presenter.print("\nWelcome to Messenger! All future command prompts will be\nfor the messenger subprogram " +
                "(indicated by '[Messenger]').\nTo exit the subprogram, select the exit option ('8')." +
                "\n=============================================");
        presenter.println("\nYou may either:\n1. View Conversations\n2. View Messages\n3. Send Message\n4. Reply\n5. View Friends\n6. Add Friend\n7. Remove Friend\n8. Exit\n");
        String selection = getUserInput("[Messenger] Enter selection (e.g. '1'): ");
        while(!selection.equals("8")){
            switch(selection) {
                case "1": // View Conversations
                    presenter.print(userController.viewConversations(userManager, conversationManager));
                    break;
                case "2": // View Messages
                    String input1 = getUserInput("Enter conversation index: ");
                    if(canParse(input1)) {
                        int index1 = Integer.parseInt(input1);
                        String output = userController.viewMessages(userManager, conversationManager, index1);
                        presenter.print(output);

                        if (!output.contains("deleted") && !output.contains("archived")){
                            String input4 = getUserInput("For this conversation, you may either: \n1. Archive this message\n2. Delete this message\n3. Return to Messaging Menu\n4. Mark as Unread and Return to Messaging Menu\n\n[Conversation] Selection: ");

                            switch(input4){
                                case "1":
                                    userController.archiveConversation(conversationManager, userManager, index1, -1);
                                    break;
                                case "2":
                                    userController.deleteConversation(conversationManager, userManager, index1, -1);
                                    break;
                                case "3":
                                    break;
                                case "4":
                                    userController.markConversationAsUnread(conversationManager, userManager, index1);
                                    break;
                                default:
                                    presenter.println("\nInvalid option selected. Returning to Messaging Menu...\n\n");
                                    break;
                            }
                        }
                    }else {
                        presenter.println("Invalid format\n");
                    }
                    break;
                case "3": // Send Message
                    String input2 = getUserInput("Enter emails or recipient indices from friends list (e.g. 'joe@jahoo.com' or '4,9,11'): ");
                    String[] segments = input2.split(",");
                    ArrayList<Integer> recipients = new ArrayList<Integer>();
                    boolean canSend = true;
                    for(int i = 0; i < segments.length; i++) {
                        int potentialEmailIndex = getUserIndexFromEmail(segments[i]);
                        if(canParse(segments[i])) {
                            recipients.add(Integer.parseInt(segments[i]));
                        } else if(potentialEmailIndex != -1) {
                            recipients.add(potentialEmailIndex);
                        } else {
                            presenter.println("Invalid Format or email\n");
                            canSend = false;
                            break;
                        }
                    }
                    if(canSend) {
                        String messageContent = getUserInput("Enter message: ");
                        presenter.println(userController.sendMessage(userManager, conversationManager, messageContent, recipients, ConvoType.DIRECT));
                    }
                    break;
                case "4": // Reply
                    String input3 = getUserInput("Enter conversation index: ");
                    if(canParse(input3)) {
                        int index2 = Integer.parseInt(input3);
                        String replyContent = getUserInput("Enter reply: ");
                        presenter.println(userController.replyToConversation(conversationManager, userManager, replyContent, index2));
                    }else {
                        presenter.println("Invalid Format\n");
                    }
                    break;
                case "5": // View Friends
                    presenter.println(userController.viewAllFriends(userManager));
                    break;
                case "6": // Add Friend
                    String email1 = getUserInput("Enter the email of the user you want to add as a friend: ");
                    presenter.println(userController.addFriend(userManager, email1));
                    break;
                case "7": // Remove Friend
                    String email2 = getUserInput("Enter the email of the user you want to remove as a friend: ");
                    presenter.println(userController.removeFriend(userManager, email2));
                    break;
                default:
                    presenter.println("Invalid Selection\n");
                    break;
            }
            selection = getUserInput("[Messenger] Enter selection (e.g. '1'): ");
        }
        presenter.println("Exiting...");
    }

    /**
     * Getter for a user's index given their email. Used in access messaging to allow sender to specify an email instead
     * of an index.
     * @param email The user's email
     * @return The user's index
     */
    private Integer getUserIndexFromEmail(String email) {
        return userManager.findUserByEmail(email);
    }

    /**
     * Lets Users to sign up to events.
     */
    private void signUpForEvent(){
        List<UUID> allEventIds = userController.getAllEventIds(eventsManager);
        List<String> eventsDisplay = displayEvents(allEventIds);
        eventsDisplay.add("exit event sign up.");
        presenter.println("All events:");
        String selection = getSelectionFromList(eventsDisplay);
        if (selection.equals("exit event sign up.")){
            presenter.println("Exiting...");
        }
        else{
            presenter.println(userController.signUpToEvent(userManager, eventsManager, roomManager, allEventIds.get(eventsDisplay.indexOf(selection))));
            String selection1 = getUserInput("\nYou may either: \n1. Submit an additional request\n2. Exit\nSelection: ");

            switch(selection1){
                case "1":
                    String requestInput = getUserInput("Enter Request Details (ie.Require gluten free meal):");
                    userController.addUserRequest(userRequestManager, userManager, requestInput, allEventIds.get(eventsDisplay.indexOf(selection)));
                    break;
                case "2":
                    presenter.println("Exiting...");
                    break;
                default:
                    presenter.println("Invalid option selected. Exiting...");
                    break;
            }
        }
    }

    /**
     * Lets Users cancel an event that he/she has signed up for.
     */
    private void cancelSpotForEvent() {
        List<UUID> signedUpEventIds = getSignedUpEventIds();
        List<String> eventsDisplay = displayEvents(signedUpEventIds);
        eventsDisplay.add("exit event cancellation.");
        presenter.println("Current signed up events:");
        String selection = getSelectionFromList(eventsDisplay);
        if (selection.equals("exit event cancellation.")){
            presenter.println("Exiting...");
        }
        else{
            presenter.print(userController.disEnrollToEvent(userManager, eventsManager, signedUpEventIds.get(eventsDisplay.indexOf(selection))));
        }
    }

    /**
     * Option for Speakers to send messages to their event attendees.
     */
    private void sendMessageToEventAttendees() {
        presenter.println("Welcome to the Attendee Messenger.\nYou may either:\n1. Message all of your attendees\n2. Message attendees for a specific event\n");
        String selection = getUserInput("Enter selection ('1' or '2'): ");
        List<UUID> scheduledTalkIds = speakerController.getScheduledTalkIds(userManager, eventsManager, null);
        if(selection.equals("1")) {
            String content = getUserInput("Enter Message: ");
            for(UUID id : scheduledTalkIds) {
                speakerController.sendMessageToEventAttendees(eventsManager, userManager, conversationManager, content, id);
            }
            presenter.println("Sent message to all attendees");
        }else if(selection.equals("2")) {
            List<String> eventsDisplay = displayEvents(scheduledTalkIds);
            presenter.println("Select the event whose attendees you wish to message:");
            String eventSelection = getSelectionFromList(eventsDisplay);
            String content = getUserInput("Enter Message: ");
            UUID id = scheduledTalkIds.get(eventsDisplay.indexOf(eventSelection));
            presenter.println(speakerController.sendMessageToEventAttendees(eventsManager, userManager, conversationManager, content, id));
        }else{
            presenter.println("Invalid selection");
        }
    }

    /**
     * Option for Organizers to send messages to either all attendees or all speakers.
     */
    private void sendMessageToAll() {
        presenter.println("Welcome to Message All.\nYou may either:\n1. Message all attendees\n2. Message all speakers\n");
        String selection = getUserInput("Enter selection ('1' or '2'): ");
        if(selection.equals("1")) {
            String content = getUserInput("Enter Message: ");
            presenter.println(organizerController.sendMessageToAll(userManager, conversationManager, content, UserType.ATTENDEE));
        }else if(selection.equals("2")) {
            String content = getUserInput("Enter Message: ");
            presenter.println(organizerController.sendMessageToAll(userManager, conversationManager, content, UserType.SPEAKER));
        }else {
            presenter.println("Invalid Selection");
        }
    }

    /**
     * Display all events this speaker is scheduled to speak in. A speaker exclusive method.
     */
    private void viewScheduledTalk() {
        presenter.println(speakerController.viewScheduledTalks(userManager, eventsManager, roomManager, null));
    }

    /**
     * Displays all attendees for a user specified event. A speaker exclusive method.
     */
    private void viewTalkAttendees() {
        presenter.println("Select the event whose attendees you would like to view:");
        List<UUID> scheduledTalksIds = speakerController.getScheduledTalkIds(userManager, eventsManager, null);
        List<String> eventsDisplay = displayEvents(scheduledTalksIds);
        String selection = getSelectionFromList(eventsDisplay);
        presenter.println(speakerController.viewEventAttendees(eventsManager, userManager, scheduledTalksIds.get(eventsDisplay.indexOf(selection))));
    }

    /**
     * Create a new room as organizer.
     */
    private void addRoom() {
        String selection = getUserInput("Please enter the name of the new room you would like to add: ");
        String strCapacity = getUserInput("Enter the maximum amount of attendee allowed in the room. (e.g. Enter '10'): ");
        String strEarliestStartTime = getUserInput("Enter the earliest time the room can host an event in 24 hour clock cycle (e.g. 13 means room opens at 13:00): ");
        String strClosingTime = getUserInput("Enter the closing time of the room in 24 hour clock cycle (e.g. 22 means room closes at 22:00): ");
        if (canParse(strEarliestStartTime) && canParse(strCapacity) && canParse(strClosingTime)) {
            int capacity = Integer.parseInt(strCapacity);
            int earliestStartTime = Integer.parseInt(strEarliestStartTime);
            int closingTime = Integer.parseInt(strClosingTime);
            try {
                UUID newRoomId = organizerController.addNewRooms(roomManager, selection, capacity, earliestStartTime, closingTime);
                presenter.println("Select the features this room contains. Select the same feature twice to unselect it. Select 'Done' to save and exit.");
                List<String> roomFeaturesDisplay = displayAllRoomFeatures();
                roomFeaturesDisplay.add("Done");
                String selectedFeature = getSelectionFromList(roomFeaturesDisplay);
                while (!selectedFeature.equals("Done")){
                    presenter.println(organizerController.editRoomFeature(roomManager, newRoomId, selectedFeature));
                    presenter.println("Current features: " + displayRoomFeatures(newRoomId));
                    selectedFeature = getSelectionFromList(roomFeaturesDisplay);
                }
                presenter.println("Room has been added.");
            } catch (Exception e) {
                presenter.println("Room not created. earliestStartTime and/or closingTime argument and/or capacity argument is not acceptable");
            }

        }
        else {
            presenter.println("Invalid input. Aborting room creation...");
        }
    }

    /**
     * Get a string representation of features in a room
     * @param roomId the room to show features of
     * @return a string of feature names
     */
    private String displayRoomFeatures(UUID roomId){
        return roomManager.getFeaturesDisplay(roomId);
    }

    /**
     * Organizer to select speaker and schedule him/her to speak in an event if there is no schedule conflict.
     */
    private void scheduleSpeakerTalk() {
        presenter.println("Select the speaker you would like to schedule for a talk:");
        List<UUID> allSpeakerIds = getAllSpeakerIds();
        List<String> displaySpeakers = displayUsers(allSpeakerIds);
        String selectSpeaker = getSelectionFromList(displaySpeakers);
        UUID speakerId = allSpeakerIds.get(displaySpeakers.indexOf(selectSpeaker));

        List<UUID> allEventIds = userController.getAllEventIds(eventsManager);
        List<String> eventsDisplay = displayEvents(allEventIds);
        presenter.println("Here are all events, select event that you would like speaker to speak in:");
        String selection = getSelectionFromList(eventsDisplay);
        presenter.println(organizerController.addSpeaker(eventsManager, speakerId, allEventIds.get(eventsDisplay.indexOf(selection))));
    }

    /**
     * Organizer to select an event and remove a chosen speaker from the speaker list.
     */
    private void removeSpeaker() {
        List<UUID> allEventIds = userController.getAllEventIds(eventsManager);
        List<String> eventsDisplay = displayEvents(allEventIds);
        presenter.println("Here are all events, select the event that you would like to remove a speaker from: ");
        String selection = getSelectionFromList(eventsDisplay);
        UUID chosenEvent = allEventIds.get(eventsDisplay.indexOf(selection));

        List<UUID> speakersID = userController.getSpeakers(eventsManager, chosenEvent);
        List<String> speakerDisplay = displayUsers(speakersID);
        if (speakerDisplay.isEmpty()) {
            presenter.println("No speakers are scheduled to this event yet. Exiting...");
        }
        else {
            String selection2 = getSelectionFromList(speakerDisplay);
            UUID speakerID = speakersID.get(speakerDisplay.indexOf(selection2));
            presenter.println(organizerController.removeSpeaker(eventsManager, chosenEvent, speakerID));
            presenter.println("Chosen speaker has been removed from the speaker list of the event.");
        }
    }

    /**
     * Organizer to select an event to be cancelled.
     */
    private void cancelEvent() {
        List<UUID> allEventIds = userController.getAllEventIds(eventsManager);
        List<String> eventsDisplay = displayEvents(allEventIds);
        presenter.println("Here are all events, select the event that you would like to cancel: ");
        String selection = getSelectionFromList(eventsDisplay);
        UUID chosenEvent = allEventIds.get(eventsDisplay.indexOf(selection));
        organizerController.cancelEvent(eventsManager, userManager, roomManager, chosenEvent);
        presenter.println("Selected event has been cancelled.");
    }

    /**
     * Organizer to select an event and change its capacity.
     */
    private void changeEventCapacity() {
        List<UUID> allEventIds = userController.getAllEventIds(eventsManager);
        List<String> eventsDisplay = displayEvents(allEventIds);
        presenter.println("Here are all events, select the event that you would like to change its capacity: ");
        String selection = getSelectionFromList(eventsDisplay);
        UUID chosenEvent = allEventIds.get(eventsDisplay.indexOf(selection));

        String strCapacity = getUserInput("Enter the new capacity for this event. (e.g. Enter '10'): ");
        while (!canParse(strCapacity)) {
            strCapacity = getUserInput("Invalid input. Enter a valid capacity for this event. (e.g. Enter '10'): ");
        }
        int capacity = Integer.parseInt(strCapacity);
        presenter.println(organizerController.changeEventCapacity(eventsManager, chosenEvent, capacity));
    }

    /**
     * Called when the user chooses the exit option in the program.
     */
    private void exit() {serializeAllWhenClose();
    }

    /**
     * Helper method for checking if a string is parsable into an integer.
     * @param input String to test
     * @return True if parsable. Else, false.
     */
    private static boolean canParse(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Helper method for checking if a string is parsable into a DateTime given a DateTime formatter.
     * @param input String to test
     * @param formatter DateTime formatter
     * @return True if parsable, false otherwise.
     */
    private static boolean isValidDateTime(String input, DateTimeFormatter formatter){
        try {
            LocalDateTime.parse(input, formatter);
            return true;
        } catch (DateTimeParseException pe){
            return false;
        }
    }

    /**
     * Helper method for checking if a string is parsable into a Date given a Date formatter.
     * @param input String to test
     * @param formatter DateTime formatter
     * @return True if parsable, false otherwise.
     */
    private static boolean isValidDate(String input, DateTimeFormatter formatter){
        try {
            LocalDate.parse(input, formatter);
            return true;
        } catch (DateTimeParseException pe){
            return false;
        }
    }

    /**
     * Called when organizer chooses the view user request option to display all user requests organized by status
     */
    public void viewUserRequest(){
        presenter.println(userController.viewUserRequests(userRequestManager, userManager, eventsManager));
    }

    /**
     * Mark a user request as addressed
     */
    public void addressUserRequest(){
        String input1 = getUserInput("\n\nEnter user request index: ");
        if(canParse(input1)) {
            int index1 = Integer.parseInt(input1);
            userController.markUserRequestAsAddressed(userRequestManager, index1);
            presenter.print("User request marked as addressed. Returning to menu...\n\n");
        }else{
            presenter.println("Invalid format. Returning to menu...\n\n");
        }

    }

    /**
     * Display all events on a given day
     */
    private void viewEventsByDay() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String selectTime = getUserInput("Time of the event in the format: dd/MM/yyyy");
        while (!isValidDate(selectTime, formatter)) {
            selectTime = getUserInput("Invalid time. Select another time for the event in the format: dd/MM/yyyy");
        }
        LocalDate eventDate = LocalDate.parse(selectTime, formatter);
        presenter.println("\nEvents on "+eventDate +":");
        presenter.println(userController.viewEventsByDay(eventsManager, roomManager, eventDate));

    }


    /**
     * Display all events for a given speaker
     */
    private void viewEventsBySpeaker() {
        List<UUID> allSpeakerIds = getAllSpeakerIds();
        if (allSpeakerIds.isEmpty()) {
            presenter.println("No speaker's are currently registered");
        }
        else {
            presenter.println("Select the speaker for whom you would like to view a a schedule:");
            List<String> displaySpeakers = displayUsers(allSpeakerIds);
            String selectSpeaker = getSelectionFromList(displaySpeakers);
            UUID speakerId = allSpeakerIds.get(displaySpeakers.indexOf(selectSpeaker));
            presenter.println("\nEvents for the speaker " + userController.getUserName(userManager, speakerId) +":");
            presenter.println(speakerController.viewScheduledTalks(userManager, eventsManager, roomManager, speakerId));
        }
    }

    /**
     * Method that lets VIP users to create a party.
     */
    private void createParty() {
        createEventHelper("party");
    }


    /**
     * Present different options in managing interested events. Guide user to each option.
     */
    private void manageInterestedEvents() {
        String selection1 = getUserInput("\nWhat would you like to do? \n1. View my interested events" +
                " \n2. Add a new event that you're interested in \n3. Remove an uninterested event \n4. Exit" +
                " \nSelect an option number: ");

        switch(selection1){
            case "1":
                viewInterestedEvents();
                break;
            case "2":
                interestedInEvent();
                break;
            case "3":
                uninterestedInEvent();
            case "4":
                presenter.println("Exiting...");
                break;
            default:
                presenter.println("Invalid option selected. Exiting...");
                break;
        }
    }

    /**
     * Lets Vip to add an event to their interested events.
     */
    private void interestedInEvent() {
        List<UUID> allEventIds = userController.getAllEventIds(eventsManager);
        List<String> eventsDisplay = displayEvents(allEventIds);
        eventsDisplay.add("exit managing interested events.");
        presenter.println("All events:");
        String selection = getSelectionFromList(eventsDisplay);
        if (selection.equals("exit managing interested events.")) {
            presenter.println("Exiting...");
        } else {
            presenter.println(vipController.interestedInEvent(userManager, allEventIds.get(eventsDisplay.indexOf(selection))));
        }
    }




    /**
     * Lets the Vip user remove an event from the interested events.
     */
    private void uninterestedInEvent() {
        List<UUID> interestedEventIds = vipController.getInterestedEventIds(userManager);
        List<String> eventsDisplay = displayEvents(interestedEventIds);
        eventsDisplay.add("exit managing interested events.");
        presenter.println("Current interested events:");
        String selection = getSelectionFromList(eventsDisplay);
        if (selection.equals("exit managing interested events")){
            presenter.println("Exiting...");
        }
        else{
            presenter.println(vipController.uninterestedInEvent(userManager, interestedEventIds.get(eventsDisplay.indexOf(selection))));
        }
    }

    /**
     * Display all events that Vip showed interests.
     */
    private void viewInterestedEvents() {
        if (displayEvents(vipController.getInterestedEventIds(userManager)).isEmpty()) {
            presenter.println("No interested events!");
        }
        else {
            for (String s : displayEvents(vipController.getInterestedEventIds(userManager))) {
                presenter.println(s);
            }
        }
    }

    /**
     * Display all favourite speakers of user(vip user).
     */
    private void viewFavouriteSpeakers() {
        presenter.println(vipController.viewFavouriteSpeakers(userManager));
    }

    /**
     * Present different options in managing favourite speakers. Guide user to each option.
     */
    private void manageFavouriteSpeakers() {
        String selection1 = getUserInput("\nWhat would you like to do? \n1. View my favourite speakers \n2. Add a speaker to your favourites" +
                " \n3. Remove a speaker from my favourites \n4. Exit\nSelect an option number: ");

        switch(selection1){
            case "1":
                viewFavouriteSpeakers();
                break;
            case "2":
                addFavouriteSpeaker();
                break;
            case "3":
                removeFavouriteSpeaker();
                break;
            case "4":
                presenter.println("Exiting...");
                break;
            default:
                presenter.println("Invalid option selected. Exiting...");
                break;
        }
    }

    /**
     * Present the process of adding a favourite speaker.
     */
    private void addFavouriteSpeaker() {
        List<UUID> allSpeakerIds = getAllSpeakerIds();
        if (allSpeakerIds.isEmpty()) {
            presenter.println("No speaker's are currently registered");
        } else {
            presenter.println("Select the speaker you want to add as your favourite speakers:");
            List<String> displaySpeakers = displayUsers(allSpeakerIds);
            String selectSpeaker = getSelectionFromList(displaySpeakers);
            UUID speakerId = allSpeakerIds.get(displaySpeakers.indexOf(selectSpeaker));
            presenter.println(vipController.addFavouriteSpeaker(userManager, speakerId));
        }
    }

    /**
     * Present the process of removing a favourite speaker.
     */
    private void removeFavouriteSpeaker() {
        List<UUID> allFavouriteSpeakerIds = vipController.getFavouriteSpeakersIds(userManager);
        if (allFavouriteSpeakerIds.isEmpty()) {
            presenter.println("There's no favourite speakers yet!");
        } else {
            presenter.println("Select the speaker you want to remove from your favourite speakers:");
            List<String> displaySpeakers = displayUsers(vipController.getFavouriteSpeakersIds(userManager));
            String selectSpeaker = getSelectionFromList(displaySpeakers);
            UUID speakerId = allFavouriteSpeakerIds.get(displaySpeakers.indexOf(selectSpeaker));
            presenter.println(vipController.removeFavouriteSpeaker(userManager, speakerId));
        }
    }

    /**
     * Get the RoomFeatures given their names
     * @param featureNames A list of feature names
     * @return A list of RoomFeatures
     */
    private List<RoomFeatures> convertNamesToFeatures(List<String> featureNames){
        ArrayList<RoomFeatures> features = new ArrayList<>();
        for (String name: featureNames) {
            for (RoomFeatures feature : EnumSet.allOf(RoomFeatures.class)) {
                if (feature.featureName().equalsIgnoreCase(name)) {
                    features.add(feature);
                }
            }
        }
        return features;
    }

    /**
     * Get a list of the names of all the RoomFeatures
     * @return a list of Strings
     */
    private List<String> displayAllRoomFeatures(){
        List<String> allRoomFeatures = new ArrayList<>();
        for (RoomFeatures feature: EnumSet.allOf(RoomFeatures.class)){
            allRoomFeatures.add(feature.featureName());
        }
        return allRoomFeatures;
    }

    /**
     * Get a list of event ids that do not conflict with user current signed up events
     * @param userEvents A List of UUID where each UUID represents an unique event.
     * @return An List of event ids where each event in the list has no schedule conflict with the user.
     */
    private List<UUID> getAvailableEventIds(List<UUID> userEvents){
        return eventsManager.getNoConflictEventIds(userEvents);
    }

    /**
     * Get a list of event ids that user signed up for.
     * @return A List of signed up event ids
     */
    private List<UUID> getSignedUpEventIds(){
        return userManager.getEventList();
    }

    /**
     * Display a list of event information: Event name | Date and Time of Event | Event Room | Event Type.
     * @param eventIds A list of IDs of events to be displayed
     * @return List of Strings of event information.
     */
    private List<String> displayEvents(List<UUID> eventIds){
        ArrayList<String> eventsDisplay = new ArrayList<>();
        for (UUID i : eventIds){
            eventsDisplay.add(eventsManager.getEventName(i) + " | " + eventsManager.getEventSchedule(i) + " | " +
                    roomManager.getRoomName(eventsManager.getEventRoomId(i)) + " | " + eventsManager.getEventType(i));
        }
        return eventsDisplay;
    }

    /**
     * Display a list of rooms with room information: Room name | Room capacity
     * @param selectRooms A list of room UUIDs
     * @return A List of room information
     */
    private List<String> displayRooms(List<UUID> selectRooms){
        return roomManager.displayRooms(selectRooms);
    }

    /**
     * Get a list of all speaker ids.
     * @return Arraylist of speaker ids.
     */
    private List<UUID> getAllSpeakerIds(){
        return userManager.getAllUsersOfType(UserType.SPEAKER);
    }

    /**
     * Display a list of User information: Name | email
     * @param selectUsers A list of IDs of users to be displayed
     * @return List of Strings of User information
     */
    private List<String> displayUsers(List<UUID> selectUsers){
        return userManager.displayUsers(selectUsers);
    }

    /**
     * A public method that serializes all of the information handled while the program was running.
     * Only handles Events and Rooms for phase 1, with easy additions for phase 2 and onwards.
     */
    public void serializeAllWhenClose() {
        this.readWriteSerializable.
                conversationsWriteToSerializable(this.conversationManager.getAllConversations());
        this.readWriteSerializable.eventsWriteToSerializable(this.eventsManager.getAllEvents());
        this.readWriteSerializable.roomsWriteToSerializable(this.roomManager.getAllRooms());
        this.readWriteSerializable.usersWriteToSerializable(this.userManager.getAllUsers());
        this.readWriteSerializable.userRequestsWriteToSerializable(this.userRequestManager.getAllUserRequests());
    }

}
