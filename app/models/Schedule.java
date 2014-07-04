package models;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;

import play.db.jpa.Model;
import utils.CommonUtils;
import vo.ScheduleVO;

@Entity
public class Schedule extends Model{
	@Temporal(TemporalType.DATE)
	@Column(nullable = false, unique=true)
	public Date date;
	
	@ManyToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinTable(name = "time_lesson", joinColumns = {@JoinColumn(name="schedule_id")}, inverseJoinColumns={@JoinColumn(name="lesson_id")})
	@MapKeyColumn(name="time_key", nullable=false, unique=true)
	@Temporal(TemporalType.TIME)
	public Map<Date, Lesson> lessons;

	public static List<Schedule> showScheduleByWeek(long clzId, Date date) {
		Date[] dates = CommonUtils.getWeekStartAndEnd(date);
		List<Schedule> schedules = Schedule.find("clz_id = ? and date between ? and ? order by date desc", clzId, dates[0], dates[1]).fetch();
		return schedules;
	}
	
	public void addSchedule(Date date, String lessonJson){
		this.date = date;
		this.lessons = ScheduleVO.getLessonsByJson(lessonJson);
		this.save();
	}
	
	public void updateSchedule(String lessonJson){
		this.lessons = ScheduleVO.getLessonsByJson(lessonJson);
		this.save();
	}
	
}
