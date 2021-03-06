package gateways;

//entities
import entities.*;

//IO
import java.io.*;

//util
import java.util.*;

//logging
import java.util.logging.*;

/**
 * This class will allow (de)serialization of files.
 * Modified to serialize and deserialize data for all 4 data types: Conversations, Events, Rooms, and Events.
 *
 * @author Sebin
 */
public class ReadWriteSerializable {

    //logging
    private static final Logger logger = Logger.getLogger(ReadWriteSerializable.class.getPackage().getName());

    //filepaths for each data being serialized
    public static final String CONVERSATIONS_FILEPATH = "conversations.ser";
    public static final String EVENTS_FILEPATH = "events.ser";
    public static final String ROOMS_FILEPATH = "rooms.ser";
    public static final String USERS_FILEPATH = "users.ser";
    public static final String USER_REQUESTS_FILEPATH = "user_requests.ser";

    /**
     * A function method that reads the file that contains Conversations and deserializes the data from the file.
     * @return A List of Conversations
     */
    public List<Conversation> conversationsReadFromSerializable() {
        try {
            InputStream file = new FileInputStream(CONVERSATIONS_FILEPATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            ArrayList<Conversation> recoveredConversations = (ArrayList<Conversation>) input.readObject();
            input.close();
            return recoveredConversations;
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform deserialization. Returning new blank Arraylist.", eIO);
            return new ArrayList<>();
        } catch (ClassNotFoundException eCNF) {
            logger.log(Level.SEVERE, "Cannot find class. Returning new blank Arraylist.", eCNF);
            return new ArrayList<>();
        }
    }

    /**
     * A function method that will serialize arraylist of Conversations into the filepath.
     * @param conversations Arraylist of all available Conversations.
     */
    public void conversationsWriteToSerializable(List<Conversation> conversations) {
        try {
            OutputStream file = new FileOutputStream(CONVERSATIONS_FILEPATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(conversations);
            output.close();
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization.", eIO);
        }
    }

    /**
     * A function method that reads the file that contains Events and deserializes the data from the file.
     * @return HashMap of Events along with UUID as keys
     */
    public HashMap<UUID, Event> eventsReadFromSerializable() {
        try {
            InputStream file = new FileInputStream(EVENTS_FILEPATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            HashMap<UUID, Event> recoveredEvents = (HashMap<UUID, Event>) input.readObject();
            input.close();
            return recoveredEvents;
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform deserialization. Returning new blank HashMap.", eIO);
            return new HashMap<>();
        } catch (ClassNotFoundException eCNF) {
            logger.log(Level.SEVERE, "Cannot find class. Returning new blank HashMap.", eCNF);
            return new HashMap<>();
        }
    }

    /**
     * A function method that will serialize HashMap of Events into the filepath.
     * @param hashMapEvents HashMap of all available Events.
     */
    public void eventsWriteToSerializable(HashMap<UUID, Event> hashMapEvents) {
        try {
            OutputStream file = new FileOutputStream(EVENTS_FILEPATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(hashMapEvents);
            output.close();
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization.", eIO);
        }
    }

    /**
     * A function method that reads the file that contains Rooms and deserializes the data from the file.
     * @return HashMap of Rooms along with UUID as keys
     */
    public HashMap<UUID, Room> roomsReadFromSerializable() {
        try {
            InputStream file = new FileInputStream(ROOMS_FILEPATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            HashMap<UUID, Room> recoveredRooms = (HashMap<UUID, Room>) input.readObject();
            input.close();
            return recoveredRooms;
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform deserialization. Returning new blank HashMap.", eIO);
            return new HashMap<>();
        } catch (ClassNotFoundException eCNF) {
            logger.log(Level.SEVERE, "Cannot find class. Returning new blank HashMap.", eCNF);
            return new HashMap<>();
        }
    }

    /**
     * A function method that will serialize HashMap of Rooms into the filepath.
     * @param hashMapRooms HashMap of all available Rooms.
     */
    public void roomsWriteToSerializable(HashMap<UUID, Room> hashMapRooms) {
        try {
            OutputStream file = new FileOutputStream(ROOMS_FILEPATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(hashMapRooms);
            output.close();
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization.", eIO);
        }
    }

    /**
     * A function method that reads the file that contains Users and deserializes the data from the file.
     * @return A List of Users
     */
    public List<User> usersReadFromSerializable() {
        try {
            InputStream file = new FileInputStream(USERS_FILEPATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            ArrayList<User> recoveredUsers = (ArrayList<User>) input.readObject();
            input.close();
            return recoveredUsers;
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform deserialization. Returning new blank Arraylist.", eIO);
            return new ArrayList<>();
        } catch (ClassNotFoundException eCNF) {
            logger.log(Level.SEVERE, "Cannot find class. Returning new blank Arraylist.", eCNF);
            return new ArrayList<>();
        }
    }

    /**
     * A function method that will serialize arraylist of Users into the filepath.
     * @param users List of all available Users.
     */
    public void usersWriteToSerializable(List<User> users) {
        try {
            OutputStream file = new FileOutputStream(USERS_FILEPATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(users);
            output.close();
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization.", eIO);
        }
    }

    /**
     * A function method that reads the file that contains UserRequests and deserializes the data from the file.
     * @return List of UserRequest
     */
    public List<UserRequest> userRequestsReadFromSerializable() {
        try {
            InputStream file = new FileInputStream(USER_REQUESTS_FILEPATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            ArrayList<UserRequest> recoveredUserRequests = (ArrayList<UserRequest>) input.readObject();
            input.close();
            return recoveredUserRequests;
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform deserialization. Returning new blank Arraylist.", eIO);
            return new ArrayList<>();
        } catch (ClassNotFoundException eCNF) {
            logger.log(Level.SEVERE, "Cannot find class. Returning new blank Arraylist.", eCNF);
            return new ArrayList<>();
        }
    }

    /**
     * A function method that will serialize arraylist of UserRequest into the filepath.
     * @param userRequests List of all UserRequest.
     */
    public void userRequestsWriteToSerializable(List<UserRequest> userRequests) {
        try {
            OutputStream file = new FileOutputStream(USER_REQUESTS_FILEPATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(userRequests);
            output.close();
        } catch (IOException eIO) {
            logger.log(Level.SEVERE, "Cannot perform serialization.", eIO);
        }
    }

    /**
     * Checks if a file exists at the provided filepath. Returns true if it does and false if it does not.
     * @param filepath The filepath to check.
     * @return A boolean whether or not the file exists at the provided path.
     */
    private boolean fileAtPathDoesExist(String filepath) {
        return new File(filepath).exists();
    }

    /**
     * Checks if the user has save files for all supported data types. Returns true if and only if all data types are saved.
     * @return A boolean whether or not the user has save files.
     */
    public boolean hasSaveData() {
        List<String> paths = Arrays.asList(CONVERSATIONS_FILEPATH, EVENTS_FILEPATH, ROOMS_FILEPATH, USERS_FILEPATH, USER_REQUESTS_FILEPATH);
        for (String path : paths) {
            if (!fileAtPathDoesExist(path)) return false;
        }

        return true;
    }
}