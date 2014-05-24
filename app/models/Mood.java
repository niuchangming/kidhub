package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import enums.MoodType;
import play.db.jpa.Model;

@Entity
public class Mood extends Model{
	@Transient
	public final static String defaultIconUrl = "../../public/images/moods/mood_smile.png";
	
	@Transient
	public final static MoodType DEFAULT_MOOD = MoodType.SMILE;
	
	public MoodType mood;
	
	@Index(name="idx_mood_date")
	@Temporal(TemporalType.DATE)
	public Date date;
	
	@Column(name="icon_url")
	public String iconURL = defaultIconUrl;

	public Mood(MoodType mood, Date date) {
		super();
		this.mood = mood == null ? DEFAULT_MOOD : mood;
		this.date = date;
		initIcon();
	}
	
	void initIcon(){
		switch(this.mood){
		case SMILE:
			this.iconURL = "../../public/images/moods/mood_smile.png";
			break;
		case LAUGH:
			this.iconURL = "../../public/images/moods/mood_laugh.png";
			break;
		case COOL:
			this.iconURL = "../../public/images/moods/mood_cool.png";
			break;
		case CHEEKY:
			this.iconURL = "../../public/images/moods/mood_cheeky.png";
			break;
		case DEVIL:
			this.iconURL = "../../public/images/moods/mood_devil.png";
			break;
		case SAD:
			this.iconURL = "../../public/images/moods/mood_sad.png";
			break;
		case ANGRY:
			this.iconURL = "../../public/images/moods/mood_angry.png";
			break;
		case CRY:
			this.iconURL = "../../public/images/moods/mood_cry.png";
			break;
		}
	}
}






