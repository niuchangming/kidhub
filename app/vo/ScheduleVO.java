package vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vo.ReportVO.ReportData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import flexjson.JSONDeserializer;
import models.Lesson;
import models.Schedule;

public class ScheduleVO {
	private long lessonId;
	private Date time;
	
	public static Map<Date, Lesson> getLessonsByJson(String lessonJson) {
		List<ScheduleVO> vos = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create()
		.fromJson(lessonJson, new TypeToken<List<ScheduleVO>>(){}.getType());
		
		Map<Date, Lesson> lessons = new HashMap<Date, Lesson>();
		for(ScheduleVO vo : vos){
			Lesson lesson = Lesson.findById(vo.lessonId);
			if(lesson != null)
				lessons.put(vo.time, lesson);
		}
		return lessons;
	}
}
