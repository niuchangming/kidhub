package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity(name="alert")
public class Alert extends Model{
	
	public String title;
	
	@Lob
	public String content;
	
	@Column(name="created_date")
	public Date createdDate;
	
	public boolean active = true;
	
	@ManyToOne
	@JoinColumn(name="author_id")
	public User author;
	
	@ManyToMany
	@JoinTable(name="alert_receiver", joinColumns={@JoinColumn(name="alert_id")}, inverseJoinColumns={@JoinColumn(name="user_id")})
	public List<User> receivers;

	public AlertType type;
	
	public Alert() {
		receivers = new ArrayList<User>();
	}

	public static Alert createAlert(AlertType type, User author, List<User> receivers){
		Alert alert = new Alert();
		switch(type){
		case INVITE:
			alert.title = "Class-invite request";
			alert.content = author.username + "invite you as a class member.";
			break;
		case JOIN:
			alert.title = "Class-joined request";
			alert.content = author.username + "want to join your class.";
			break;
		}
		
		alert.createdDate = new Date();
		alert.author = author;
		alert.type = type;
		alert.receivers.addAll(receivers);
		return alert;
	}
	
}










