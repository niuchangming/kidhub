package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;
import utils.CommonUtils;

@Entity
public class Schedule extends Model{
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	public Date date;
	
	@ManyToOne
	public Lesson lesson;

	public static List<Schedule> showScheduleByWeek(long clzId, Date date) {
		Date[] dates = CommonUtils.getWeekStartAndEnd(date);
		List<Schedule> schedules = Schedule.find("clz_id = ? and date between ? and ?", clzId, dates[0], dates[1]).fetch();
		return schedules;
	}
	
	
}
