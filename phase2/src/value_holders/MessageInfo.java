package value_holders;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A class for storing information about messages, used to give printable information to UserController during
 * viewMessages().
 */
public class MessageInfo {
	private UUID senderId;
	private LocalDateTime time;
	private String content;

	/**
	 * The constructor for a MessageInfo object
	 * @param senderId The message's senderId
	 * @param time The message's time
	 * @param content The message's content
	 */
	public MessageInfo(UUID senderId, LocalDateTime time, String content) {
		this.senderId = senderId;
		this.time = time;
		this.content = content;
	}

	/**
	 * Getter for senderId
	 * @return Returns senderId
	 */
	public UUID getSenderId() {
		return senderId;
	}

	/**
	 * Getter for time
	 * @return Returns time
	 */
	public LocalDateTime getTime() {
		return time;
	}

	/**
	 * Getter for content
	 * @return Returns content
	 */
	public String getContent() {
		return content;
	}
}