package vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vo.ReportVO.ReportData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import flexjson.JSONDeserializer;
import models.Lesson;
import models.LessonTiming;
import models.Schedule;

public class ScheduleVO {
	private long lessonId;
	private Date time;
	
	public static List<LessonTiming> getLessonsByJson(String lessonJson) {
		List<ScheduleVO> vos = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create()
		.fromJson(lessonJson, new TypeToken<List<ScheduleVO>>(){}.getType());
		
		List<LessonTiming> lessonTimings = new ArrayList<LessonTiming>();
		for(ScheduleVO vo : vos){
			Lesson lesson = Lesson.findById(vo.lessonId);
			if(lesson != null){
				LessonTiming lt= new LessonTiming();
				lt.dateTime = vo.time;
				lt.lesson = lesson;
				lt.save();
				lessonTimings.add(lt);
			}
		}
		return lessonTimings;
	}
}
