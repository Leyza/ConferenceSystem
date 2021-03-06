package main;

import entities.Room;
import enums.ConvoType;
import enums.RoomFeatures;
import enums.UserType;
import use_cases.ConversationManager;
import use_cases.EventsManager;
import use_cases.RoomManager;
import use_cases.UserManager;

import java.time.LocalDateTime;
import java.util.*;

/**
 * A class with helper methods that generate demo data for testing and showing off the program.
 */
public class TestData {
    /**
     * Adds demo user data to the provided UserManager.
     * @param userManager the UserManager that will be filled with data
     */
    public static void generateUsersDemoData(UserManager userManager) {

        //creating users
        userManager.createUser("Organizer1", "organizer1@conference.com", "organizer1", UserType.ORGANIZER);
        userManager.createUser("Attendee1", "attendee1@conference.com", "attendee1", UserType.ATTENDEE);
        userManager.createUser("Attendee2", "attendee2@conference.com", "attendee2", UserType.ATTENDEE);
        userManager.createUser("Speaker1", "speaker1@conference.com", "speaker1", UserType.SPEAKER);
        userManager.createUser("Vip1", "vip1@conference.com", "vip1", UserType.VIP);

        //adding friends
        //attendee1 and attendee2 are friends because they have added each other.
        userManager.login("attendee1@conference.com", "attendee1");
        userManager.addFriendById(userManager.getUserByIndex(userManager.findUserByEmail("attendee2@conference.com")).getId());
        userManager.login("attendee2@conference.com", "attendee2");
        userManager.addFriendById(userManager.getUserByIndex(userManager.findUserByEmail("attendee1@conference.com")).getId());
        userManager.logout();

    }

    /**
     * Adds demo user data to the provided RoomManager.
     * @param roomManager the RoomManager that will be filled with data
     */
    public static void generateRoomsDemoData(RoomManager roomManager) {

        roomManager.addRoom("Main Conference Room", 10, 9, 21);

    }

    /**
     * Adds demo user data to the provided EventsManager. Depends on a roomManager and userManager.
     * @param roomManager a RoomManager that has data that will be used to create events.
     * @param eventsManager the EventsManager that will be filled with data
     * @param userManager a UserManager that has data that will be used to create events.
     */
    public static void generateEventsDemoData(RoomManager roomManager, EventsManager eventsManager, UserManager userManager) {

        List<Room> roomList = new ArrayList<>(roomManager.getAllRooms().values()); //This should only be "Main Conference Room"
        List<UUID> speakerList = userManager.getAllUsersOfType(UserType.SPEAKER); //This should only be "speaker1"
        UUID tempEventID = eventsManager.addEvent("Possible Vaccines For Coronavirus",
                LocalDateTime.of(2020, 12, 31, 9, 0), roomList.get(0).getRoomID(),
                2, 1, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Serum Of Immortality",
                LocalDateTime.of(2020, 12, 31, 10, 0), roomList.get(0).getRoomID(),
                 3, 1, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Self-Sustainable House-Cleaning Robot",
                LocalDateTime.of(2020, 12, 31, 11, 0), roomList.get(0).getRoomID(),
                 4, 1, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Why Normal People Can't Purchase RTX 3090",
                LocalDateTime.of(2020, 12, 31, 12, 0), roomList.get(0).getRoomID(),
                5, 1, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Concerns Over Legalization Of Recreational Drug Use In Oregon",
                LocalDateTime.of(2020, 12, 31, 13, 0), roomList.get(0).getRoomID(),
                 6, 1, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Reasons Why Two-Party System Is Harmful",
                LocalDateTime.of(2020, 12, 31, 14, 0), roomList.get(0).getRoomID(),
                7, 1, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Discovery Of Ancient 'Driving' Under Influence",
                LocalDateTime.of(2020, 12, 31, 15, 0), roomList.get(0).getRoomID(),
                8, 1, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Why Turtles Are The True Ancestors Of Humans In The Theory Of Evolution",
                LocalDateTime.of(2020, 12, 31, 16, 0), roomList.get(0).getRoomID(),
                9, 1, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Insignificant Salaries Of Volleyball Players",
                LocalDateTime.of(2020, 12, 31, 17, 0), roomList.get(0).getRoomID(),
                10, 2, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        tempEventID = eventsManager.addEvent("Reasons Behind Canada's Excellence In Curling, Instead Of Soccer",
                LocalDateTime.of(2020, 12, 31, 19, 0), roomList.get(0).getRoomID(),
                11, 2, new ArrayList<>(Arrays.asList(RoomFeatures.CONFERENCE_SETUP)));
        eventsManager.addSpeaker(tempEventID, speakerList.get(0));
        //Note that the last two events are duration of 2 hours, so the entire conference will end at closing time.

    }

    /**
     * Adds demo messaging data to the provided ConversationManager. Depends on a UserManager.
     * @param conversationManager the ConversationManager that will be filled with data
     * @param userManager a UserManager that has data that will be used to create conversations and messages.
     */
    public static void generateConversationsDemoData(UserManager userManager, ConversationManager conversationManager) {

        //creating private conversation of attendee1 and attendee2
        List<UUID> usersIndexDM = new ArrayList<>();
        usersIndexDM.add(userManager.getUserIdByIndex(userManager.findUserByEmail("attendee1@conference.com")));
        usersIndexDM.add(userManager.getUserIdByIndex(userManager.findUserByEmail("attendee2@conference.com")));
        UUID privateConversation1 = conversationManager.createConversation(usersIndexDM, ConvoType.DIRECT);
        //private conversation of attendee1 and attendee2
        conversationManager.sendMessage("Hello, attendee2. I am attendee1.", userManager.getUserByIndex(userManager.findUserByEmail("attendee1@conference.com")).getId(), privateConversation1);
        conversationManager.sendMessage("Hello, attendee1. I am attendee2.", userManager.getUserByIndex(userManager.findUserByEmail("attendee2@conference.com")).getId(), privateConversation1);
        conversationManager.sendMessage("Are you going to [redacted] event?", userManager.getUserByIndex(userManager.findUserByEmail("attendee1@conference.com")).getId(), privateConversation1);
        conversationManager.sendMessage("Yes, I am going to [redacted] event.", userManager.getUserByIndex(userManager.findUserByEmail("attendee2@conference.com")).getId(), privateConversation1);

        //creating group conversation of attendee1, attendee2 and vip1
        List<UUID> usersIndexGC = new ArrayList<>();
        usersIndexGC.add(userManager.getUserIdByIndex(userManager.findUserByEmail("attendee1@conference.com")));
        usersIndexGC.add(userManager.getUserIdByIndex(userManager.findUserByEmail("attendee2@conference.com")));
        usersIndexGC.add(userManager.getUserIdByIndex(userManager.findUserByEmail("vip1@conference.com")));
        UUID groupConversation1 = conversationManager.createConversation(usersIndexGC, ConvoType.DIRECT);
        //creating group conversation of attendee1, attendee2 and vip1
        conversationManager.sendMessage("Hello group. I am attendee1.", userManager.getUserByIndex(userManager.findUserByEmail("attendee1@conference.com")).getId(), groupConversation1);
        conversationManager.sendMessage("Hello group. I am attendee2.", userManager.getUserByIndex(userManager.findUserByEmail("attendee2@conference.com")).getId(), groupConversation1);
        conversationManager.sendMessage("Hello group. I am vip1.", userManager.getUserByIndex(userManager.findUserByEmail("vip1@conference.com")).getId(), groupConversation1);

        //add each conversation to each users involved, and mark only the private conversations as unread.
        //adding private messages
        userManager.addConversation(privateConversation1, userManager.findUserByEmail("attendee1@conference.com"));
        userManager.addConversation(privateConversation1, userManager.findUserByEmail("attendee2@conference.com"));
        //marking private messages as unread for both parties
        userManager.markConversationAsUnread(privateConversation1, userManager.findUserByEmail("attendee1@conference.com"));
        userManager.markConversationAsUnread(privateConversation1, userManager.findUserByEmail("attendee2@conference.com"));
        //adding group messages
        userManager.addConversation(groupConversation1, userManager.findUserByEmail("attendee1@conference.com"));
        userManager.addConversation(groupConversation1, userManager.findUserByEmail("attendee2@conference.com"));
        userManager.addConversation(groupConversation1, userManager.findUserByEmail("vip1@conference.com"));

    }
}
