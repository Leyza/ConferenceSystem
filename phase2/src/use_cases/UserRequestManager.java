package use_cases;

import entities.UserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A Use Case class for managing UserRequests.
 * @author Laxan Premachandran
 */
public class UserRequestManager {
    private ArrayList<UserRequest> allUserRequests;

    /**
     * The constructor for UserRequestsManager
     * @param userRequests User requests to load into the manager.
     */
    public UserRequestManager(List<UserRequest> userRequests){
        this.allUserRequests = new ArrayList<>(userRequests);
    }

    /**
     * Adds a User Request to allUserRequests
     * @param senderId UUID of user that made the request
     * @param content String representation of request
     * @param eventID UUID of associated event
     */
    public void addUserRequest( UUID senderId, String content, UUID eventID){
        allUserRequests.add(new UserRequest( senderId, content, eventID));
    }

    /**
     * Returns a list of UUID with the given isAddressed
     * @param isAddressed boolean
     * @return List of UserRequests with the given status
     */
    public List<UserRequest> getUserRequestByStatus(boolean isAddressed){
        ArrayList<UserRequest> output = new ArrayList<>();
        for (UserRequest r: allUserRequests){
            if (r.getIsAddressed()== isAddressed){output.add(r);}
        }
        return output;
    }

    /**
     * Finds a UserRequest's index given the UUID
     * @param id UUID of UserRequest
     * @return index of UserRequest in allUserRequests
     */
    public int findRequestIndexById(UUID id) {
        for (UserRequest r: allUserRequests){
            if (r.getId().equals(id)){
                return allUserRequests.indexOf(r);
            }
        }
        return -1;
    }

    /**
     * Marks a UserRequest as addressed
     * @param index index of userRequest in allUserRequests
     */
    public void markUserRequestAsAddressed(int index){
        allUserRequests.get(index).setIsAddressed(true);
    }

    /**
     * Gets the full list of all user requests
     * @return a List of UserRequest
     */
    public List<UserRequest> getAllUserRequests() {
        return allUserRequests;
    }
}
