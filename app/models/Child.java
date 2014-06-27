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
	@OrderBy(value="attendanceDate desc")
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
		if(this.attendances == null)
			this.attendances = new ArrayList<Attendance>();
		this.attendances.add(new Attendance(new Date(), null));
		if(this.moods == null)
				this.moods = new ArrayList<Mood>();
		this.moods.add(new Mood(null, new Date()));
	}

	public void createChildByClzId(long clzId){
		KidClass clz = KidClass.findById(clzId);
		this.clz = clz;
		this.avatarURL = Child.defaultIconUrl;
		this.save();
	}
	
	public void markChildbyType(long typeId, String reason, String othBehavior, int othWeight){
		MarkType type = MarkType.findById(typeId);
		this.markChild(type, reason, othBehavior, othWeight);
	}
	
	public void markChildByOther(String reason, String othBehavior, int othWeight){
		markChild(null, reason, othBehavior, othWeight);
	}
	
	public void markChild(MarkType type, String reason, String othBehavior, int othWeight){
		if(marks == null){
			marks = new ArrayList<Mark>();
		}
		Mark mark = null;
		if(type != null){
			mark = new Mark(reason, type);
			this.marks.add(mark);
		}else{
			mark = new Mark();
			mark.otherType = othBehavior;
			mark.weight = othWeight;
			mark.reason = reason;
			this.marks.add(mark);
		}
		this.score = this.score + (mark.type == null ? mark.weight : mark.type.weight);
		this.save();
	}
	
	public void createAvatarByChildId(String avatarUrl){
		this.avatarURL = avatarUrl;
		this.save();
	}

	public Attendance markAttendance(int status) {
		Attendance attendance = Attendance.find("child_id = ? AND attendance_date = ?", 
				this.id, CommonUtils.getDateString(new Date(), null)).first();
		
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
	
	private AttendanceStatus getStatusValue(int status){
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
	
}












