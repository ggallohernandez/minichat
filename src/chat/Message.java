package chat;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private static final long serialVersionUID = -7409422654096666054L;
	private String from;
	private String date;
	private String text;
	
	public Message(String from, String text) {
		this.from = from;
		this.text = text;
		this.date = new Date().toString();
	}
	
	public String toString() {
		return date + " " + from + ": " + text;
	}
}
