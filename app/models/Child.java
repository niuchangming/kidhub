package models;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.persistence.*;

import org.codehaus.groovy.runtime.metaclass.NewMetaMethod;
import org.hibernate.annotations.Type;

import com.google.gson.annotations.Expose;

import enums.AttendanceStatus;
import enums.MoodType;
import play.data.Upload;
import play.db.jpa.*;
import utils.CommonUtils;

@Entity
public class Child extends GenericModel{
	@Id
    @GeneratedValue
    @Expose
    public Long id;
	
	@Expose
	@Column(name="first_name", nullable=false)
	public String firstName;
	
	@Expose
	@Column(name="last_name", nullable=false)
	public String lastName;
	
	public String nickName;
	
	public String gender;
	
	@Expose
	@Type(type = "date")
	public Date birth;
	
	@Expose
	@Column(name="avatar_url")
	public String avatarURL;
	
	@Expose
	@ManyToOne
	public Parent parent;
	
	@ManyToOne(fetch=FetchType.LAZY)
	public KidClass clz;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="child_id")
	@OrderBy(value="date desc")
	public List<Attendance> attendances;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="child_id")
	public List<Mark> marks;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="child_id")
	@OrderBy(value="date desc")
	public List<Mood> moods;
	
	@Expose
	public int score = 50;
	
	public boolean active = true;
	
	@Transient
	public final static String defaultIconUrl = "../../public/images/monster/cute1.png";

	public Child(String firstName, String lastName, Date birth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birth = birth;
		if(marks == null){
			marks = new ArrayList<Mark>();
		}
		if(this.attendances == null){
			this.attendances = new ArrayList<Attendance>();
		}
		this.attendances.add(new Attendance(new Date(), null));
		if(this.moods == null){
			this.moods = new ArrayList<Mood>();
		}
		this.moods.add(new Mood(null, new Date()));
	}

	public void createChildByClzId(long clzId){
		KidClass clz = KidClass.findById(clzId);
		this.clz = clz;
		this.avatarURL = Child.defaultIconUrl;
		this.save();
	}
	
	public static Child markChildbyType(long childId, long typeId, String reason, String othBehavior, int othWeight){
		MarkType type = MarkType.findById(typeId);
		Child child = markChild(childId, type, reason, othBehavior, othWeight);
		return child;
	}
	
	public static List<Child> markChildrenbyType(long typeId, String reason, String othBehavior, int othWeight, long...childIds){
		List<Child> children = new ArrayList<Child>();
		MarkType type = MarkType.findById(typeId);
		for(long childId : childIds){
			children.add(markChild(childId, type, reason, othBehavior, othWeight));
		}
		return children;
	}
	
	public static Child markChildByOther(long childId, String reason, String othBehavior, int othWeight){
		Child child = markChild(childId, null, reason, othBehavior, othWeight);
		return child;
	}
	
	public static List<Child> markChildrenByOther(String reason, String othBehavior, int othWeight, long...childIds){
		List<Child> children = new ArrayList<Child>();
		for(long childId : childIds){
			children.add(markChild(childId, null, reason, othBehavior, othWeight));
		}
		return children;
	}
	
	public static Child markChild(long childId, MarkType type, String reason, String othBehavior, int othWeight){
		Child child = Child.findById(childId);
		Mark mark = null;
		if(type != null){
			mark = new Mark(reason, type);
			child.marks.add(mark);
		}else{
			mark = new Mark(reason, othBehavior, othWeight);
			child.marks.add(mark);
		}
		child.score = child.score + (mark.type == null ? mark.weight : mark.type.weight);
		child.save();
		return child;
	}
	
	public void createAvatarByChildId(String avatarUrl){
		this.avatarURL = avatarUrl;
		this.save();
	}

	public Attendance getAttendanceByDate(){
		Attendance attendance = Attendance.find("child_id = ? and date = ?", this.id, new Date()).first();
		if(attendance == null){
			attendance = new Attendance(new Date(), AttendanceStatus.PRESENT).save();
			this.attendances.add(attendance);
			this.save();
		}
		return attendance;
	}
	
	public Attendance markAttendance(int status) {
		Attendance attendance = Attendance.find("child_id = ? AND date = ?", this.id, new Date()).first();
		
		if(attendance != null){
			attendance.status = getStatusValue(status);
			attendance.save();
		}else{
			attendance = new Attendance(new Date(), getStatusValue(status));
			attendance.save();
			this.attendances.add(attendance);
			this.save();
		}
		return attendance;
	}
	
	private static AttendanceStatus getStatusValue(int status){
		AttendanceStatus attStatus = AttendanceStatus.PRESENT;
		switch(status){
		case 0:
			attStatus = AttendanceStatus.PRESENT;
			break;
		case 1:
			attStatus = AttendanceStatus.LATE;
			break;
		case 2:
			attStatus = AttendanceStatus.ABSENT;
			break;
		case 3:
			attStatus = AttendanceStatus.MC;
			break;
		}
		return attStatus;
	}
	
	public Mood getMoodByDate(){
		Mood mood = Mood.find("child_id = ? and date = ?", this.id, new Date()).first();
		if(mood == null){
			mood = new Mood(MoodType.SMILE, new Date()).save();
			this.moods.add(mood);
			this.save();
		}
		return mood;
	}
	
	public Mood moodChildByMoodValue(int moodValue){
		Mood mood = Mood.find("child_id = ? AND date = ?", this.id, new Date()).first();
		if(mood != null){
			mood.mood = MoodType.of(moodValue);
			mood.initIcon();
			mood.save();
		}else{
			mood = new Mood(MoodType.of(moodValue), new Date());
			mood.save();
			this.moods.add(mood);
			this.save();
		}
		return mood;
	}
	
	public static int markAttendChildrenByIds(int status, long...ids){
		if(ids.length < 1){
			return 0;
		}
		String sql = "update attendance set status = " + status + " where ";
		for(long id : ids){
			sql += "child_id = " + id + " or "; 
		}
		sql = sql.substring(0, sql.length() - 4) + " and date order by date desc limit " + ids.length;
		int count = JPA.em().createNativeQuery(sql).executeUpdate();
		return count;
	}
	
	public static int moodChildrenByIds(int moodValue, long... ids){
		if(ids == null || ids.length < 1){
			return 0;
		}
		String sql = "update mood set mood = " + moodValue + ", icon_url = '" + getIconByMood(moodValue) + "' where ";
		for(long id : ids){
			sql += "child_id = " + id + " or "; 
		}
		sql = sql.substring(0, sql.length() - 4) + " and date order by date desc limit " + ids.length;
		System.out.println(sql);
		int count = JPA.em().createNativeQuery(sql).executeUpdate();
		return count;
	}
	
	public static String getIconByMood(int moodValue){
		String iconURL = "../../public/images/moods/mood_smile.png";
		switch(MoodType.of(moodValue)){
		case SMILE:
			iconURL = "../../public/images/moods/mood_smile.png";
			break;
		case LAUGH:
			iconURL = "../../public/images/moods/mood_laugh.png";
			break;
		case COOL:
			iconURL = "../../public/images/moods/mood_cool.png";
			break;
		case CHEEKY:
			iconURL = "../../public/images/moods/mood_cheeky.png";
			break;
		case DEVIL:
			iconURL = "../../public/images/moods/mood_devil.png";
			break;
		case SAD:
			iconURL = "../../public/images/moods/mood_sad.png";
			break;
		case ANGRY:
			iconURL = "../../public/images/moods/mood_angry.png";
			break;
		case CRY:
			iconURL = "../../public/images/moods/mood_cry.png";
			break;
		}
		return iconURL;
	}
	
}












