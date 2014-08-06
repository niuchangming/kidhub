import java.text.ParseException;
import java.util.Date;
import java.util.List;

import enums.AttendanceStatus;
import enums.MoodType;
import models.Attendance;
import models.Child;
import models.Mood;
import play.jobs.Job;
import play.jobs.On;
import utils.CommonUtils;

@On("0 00 00 * * ?")
public class EveningJob extends Job{

	@Override
	public void doJob() throws Exception {
		List<Child> children = Child.findAll();
		if(children != null){
			defaulTodayAttend(children);
			defaulTodayMood(children);
		}
	}
	
	private void defaulTodayAttend(List<Child> children){
		for(Child child : children){
			if(isAttendanceExistBy(new Date(), child.id)) continue;
			
			Attendance attendance = new Attendance(new Date(), AttendanceStatus.PRESENT);
			attendance.save();	
			child.attendances.add(attendance);
			child.save();
		}
	}
	
	private void defaulTodayMood(List<Child> children){
		for(Child child : children){
			if(isMoodExistBy(new Date(), child.id)) continue;
			
			Mood mood = new Mood(MoodType.SMILE, new Date());
			mood.save();
			child.moods.add(mood);
			child.save();
		}
	}
	
	private boolean isAttendanceExistBy(Date date, long childId){
		Attendance attendance = null;
		try {
			attendance = Attendance.find("child_id = ? and date = ?", childId, CommonUtils.getDateByFormat(date, null)).first();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return attendance != null;
	}
	
	private boolean isMoodExistBy(Date date, long childId){
		Mood mood = null;
		try {
			mood = Mood.find("child_id = ? and date = ?", childId, CommonUtils.getDateByFormat(date, null)).first();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mood != null;
	}
	
}
