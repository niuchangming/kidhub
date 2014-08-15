package controllers;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.api.message.Attachment;

import flexjson.JSONSerializer;
import models.Album;
import models.Attendance;
import models.Child;
import models.Food;
import models.KidClass;
import models.Lesson;
import models.MarkType;
import models.Menu;
import models.Mood;
import models.MenuOrder;
import models.Photo;
import models.Report;
import models.ResModuleBase;
import models.Resource;
import models.Schedule;
import models.Teacher;
import models.User;
import play.data.Upload;
import play.data.binding.As;
import play.data.binding.types.FileArrayBinder;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Http.Request;
import play.utils.HTML;
import utils.CommonException;
import utils.CommonUtils;
import vo.ReportVO;

@With(Interceptor.class)
public class ClassLifeController extends Controller{
	
	public static void classReport(){
		long clzId = renderArgs.get("clzId", Long.class);
		List<Report> reports = Report.getReportsByRange(clzId, 0, ReportVO.DEFAULT_CCOUNT_PER_PAGE);
		render(reports);
	}
	
	public static void reportsByRange(int offset, int max){
		long clzId = renderArgs.get("clzId", Long.class);
		List<Report> reports = Report.getReportsByRange(clzId, offset, max);
		renderJSON(reports);
	}
	
	public static void classAttendance(){
		long clzId = renderArgs.get("clzId", Long.class);
		List<Child> children = Child.find("clz_id = ?", clzId).fetch();
		render(children);
	}
	
	public static void saveAvatarByChildId(long childId, int x, int y, int width, int height, float ratio, Upload image){
		Child child = Child.findById(childId); 
		String uuid = UUID.randomUUID().toString();
		File dir = new File(User.AVATAR_BASE);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File outFile = new File(User.AVATAR_BASE + uuid);
		BufferedImage img;
		try {
			img = ImageIO.read(image.asStream());
			BufferedImage cropped = img.getSubimage(Math.round(x / ratio), Math.round(y / ratio), Math.round(width / ratio), Math.round(height / ratio));
			BufferedImage resized = cropped;
			if (width != 100) {
				resized = User.getScaledInstance(cropped, 100, 100, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
			}
			ImageIO.write(resized, "png", outFile);
			child.createAvatarByChildId(User.AVATAR_BASE + uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderJSON(CommonUtils.getJsonString(child, Child.class));
	}
	
	public static void markAttendToChildByChildId(long childId, int status){
		Child child = Child.findById(childId);
		Attendance attendance = child.markAttendance(status);
		renderJSON(attendance);
	}
	
	public static void markAttendToChildrenByChildIds(int status, long... childIds){
		int count = Child.markAttendChildrenByIds(status, childIds);
		renderText(count);
	}
	
	public static void markGrid(){
		long clzId = renderArgs.get("clzId", Long.class);
		List<Child> children  = Child.find("clz_id = ?", clzId).fetch(); 
		List<MarkType> posTypes = MarkType.find("byWeightGreaterThan", 0).fetch();
		List<MarkType> negTypes = MarkType.find("byWeightLessThan", 0).fetch();
		render(children, posTypes, negTypes);
	}
	
	public static void markChildById(long childId, long typeId, String reason, String othBehavior, int othWeight){
		Child child = null;
		if(typeId != 0){
			child = Child.markChildbyType(childId, typeId, reason, othBehavior, othWeight);
		}else{
			child = Child.markChildByOther(childId, reason, othBehavior, othWeight);
		}
		renderJSON(CommonUtils.getJsonString(child, Child.class));
	}
	
	public static void markChildrenByIds(long typeId, String reason, String othBehavior, int othWeight, long...childIds){
		List<Child> children;
		if(typeId != 0){
			children = Child.markChildrenbyType(typeId, reason, othBehavior, othWeight, childIds);
		}else{
			children = Child.markChildrenByOther(reason, othBehavior, othWeight, childIds);
		}
		renderJSON(CommonUtils.getJsonString(children, List.class));
	}
	
	public static void uploadImage(Resource resource){
		resource.moduleType = "Report";
		resource.type = "image";
		resource.save();
		renderText(resource.id);
	}
	
	public static void showResourceBinary(long resourceId){
		Resource resource = Resource.findById(resourceId);
		renderBinary(resource.file.get());
	}
	
	public static void createReport(String reportByJson){
		long clzId = renderArgs.get("clzId", Long.class);
		KidClass clz = KidClass.findById(clzId);
		Report report = new Report();
		Teacher teacher = renderArgs.get("user", Teacher.class);
		report.saveReportByJson(teacher.id, reportByJson);
		clz.addReport(report);
		renderJSON(report);
	}
	
	public static void moodGrid(){
		long clzId = renderArgs.get("clzId", Long.class);
		List<Child> children  = Child.find("clz_id = ?", clzId).fetch(); 
		render(children);
	}
	
	public static void moodChildById(long childId, int moodValue){
		Child child = Child.findById(childId);
		Mood mood = child.moodChildByMoodValue(moodValue);
		renderJSON(mood);
	}
	
	public static void moodChildrenByIds(int moodValue, long... childIds){
		int count = Child.moodChildrenByIds(moodValue, childIds);
		renderText(count);
	}
	
	public static void showMenuOrder(){
		long clzId = renderArgs.get("clzId", Long.class);
		KidClass clz = KidClass.findById(clzId);
		List<MenuOrder> orders;
		Date today = new Date();
		if(clz.startDate.after(today)){
			orders = MenuOrder.showOrderByWeek(clzId, clz.startDate);
		}else if(clz.endDate.before(today)){
			orders = MenuOrder.showOrderByWeek(clzId, clz.endDate);
		}else{
			orders = MenuOrder.showOrderByWeek(clzId, today);
		}
		render(clz, orders);
	}
	
	public static void showMenuOrderByWeek(Date date){
		long clzId = renderArgs.get("clzId", Long.class);
		List<MenuOrder> orders = MenuOrder.showOrderByWeek(clzId, date);
		renderJSON(CommonUtils.getObjectAsJsonStr(orders));
	}
	
	public static void createFood(Food food){
		food.addFoodThumnail();
		User user = renderArgs.get("user", User.class);
		long teacherId = user.id;
		food.createFoodByTeacher(teacherId);
		renderJSON(food);
	}
	
	public static boolean isFoodAvailable(String name){
		Food food = Food.find("byName", name).first();
		return food == null;
	}
	
	public static void getClassFood(){
		long clzId = renderArgs.get("clzId", Long.class);
		KidClass clz = KidClass.findById(clzId);
		List<Food> food = clz.getFood();
		renderJSON(CommonUtils.getObjectAsJsonStr(food));
	}
	
	public static void createOrUpdateOrder(long[] foodIds, Date date, boolean isTemplate){
		long clzId = renderArgs.get("clzId", Long.class);
		KidClass clz = KidClass.findById(clzId);
		MenuOrder order = MenuOrder.find("byDate", date).first();
		if(order == null){
			Menu menu = new Menu();
			menu.createMenuByFoodIds(foodIds, isTemplate);
			order = new MenuOrder(date, menu);
			clz.addOrder(order);
		}else{
			order.menu.updateMenuByFoodIds(foodIds);
		}
		renderJSON(CommonUtils.getObjectAsJsonStr(order));
	}
	
	public static boolean isLessonAvailable(String title){
		Lesson lesson = Lesson.find("byTitle", title).first();
		return lesson == null;
	}
	
	public static void showSchedule(){
		long clzId = renderArgs.get("clzId", Long.class);
		KidClass clz = KidClass.findById(clzId);
		List<Schedule> schedules;
		Date today = new Date();
		if(clz.startDate.after(today)){
			schedules = Schedule.showScheduleByWeek(clzId, clz.startDate);
		}else if(clz.endDate.before(today)){
			schedules = Schedule.showScheduleByWeek(clzId, clz.endDate);
		}else{
			schedules = Schedule.showScheduleByWeek(clzId, today);
		}
		render(clz, schedules);
	}
	
	public static void showScheduleByWeek(Date date){
		long clzId = renderArgs.get("clzId", Long.class);
		List<Schedule> schedules = Schedule.showScheduleByWeek(clzId, date);
		renderJSON(CommonUtils.getObjectAsJsonStr(schedules));
	}
	
	public static void createLesson(Lesson lesson){
		long clzId = renderArgs.get("clzId", Long.class);
		lesson.createLessonByClzId(clzId);
		renderJSON(lesson);
	}
	
	public static void getClassLesson(){
		long clzId = renderArgs.get("clzId", Long.class);
		KidClass clz = KidClass.findById(clzId);
		renderJSON(CommonUtils.getObjectAsJsonStr(clz.lessons));
	}
	
	public static void createScheduleByJson(Date date, String lessonJson){
		long clzId = renderArgs.get("clzId", Long.class);
		KidClass clz = KidClass.findById(clzId);
		Schedule schedule = Schedule.find("date = ? and clz_id = ?", date, clzId).first();
		if(schedule == null){
			schedule = clz.addScheduleByLessonIds(date, lessonJson);
		}else{
			schedule.updateSchedule(lessonJson);
		}
		renderJSON(schedule);
	}
	
	public static void showClassAlbum(int offset){
		long clzId = renderArgs.get("clzId", Long.class);
		List<Album> albums = Album.find("clz_id = ?", clzId).from(offset).fetch(Album.LOAD_COUNT_PER_PAGE);
		render(albums);
	}
	
	public static void createAlbum(Album album){
		long clzId = renderArgs.get("clzId", Long.class);
		album.clz = KidClass.findById(clzId);
		album.date = new Date();
		album.save();
		renderJSON(CommonUtils.getObjectAsJsonStr(album, "clz"));
	}
	
	public static void uploadPhotosByAlbumId(long albumId, List<Blob> blobs){
	    if(blobs != null) {
	    	User user = renderArgs.get("user", User.class);
	    	Album album = Album.findById(albumId);
	        for(Blob blob : blobs) {
                if(blob.type().equals("image/jpeg") || blob.type().equals("image/png")) {
                    Photo photo = new Photo(user, blob).save();
                    album.photos.add(photo);
                }
	        }
	        album.save();
	    }
	}
	
	public static void showThumbnails(long albumId, int offset){
		List<Photo> photos = Photo.find("album_id = ?", albumId).from(offset).fetch(Photo.LOAD_COUNT_PER_PAGE);
		render(photos);
	}
	
	public static void showPhoto(long photoId){
		Photo photo = Photo.findById(photoId);
		renderBinary(photo.image.get());
	}
	
}








