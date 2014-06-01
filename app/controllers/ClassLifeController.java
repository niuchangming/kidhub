package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.api.message.Attachment;

import flexjson.JSONSerializer;

import models.Child;
import models.Food;
import models.KidClass;
import models.MarkType;
import models.Menu;
import models.Mood;
import models.Report;
import models.ResModuleBase;
import models.Resource;
import models.Teacher;
import models.User;
import play.data.Upload;
import play.mvc.Controller;
import play.mvc.With;
import play.utils.HTML;
import utils.CommonUtils;
import vo.ReportVO;

@With(Interceptor.class)
public class ClassLifeController extends Controller{
	
	public static void classReport(long clzId){
		KidClass clz = KidClass.findById(clzId);
		List<Report> reports = Report.getReportsByRange(clzId, 0, ReportVO.DEFAULT_CCOUNT_PER_PAGE);
		render(clz, reports);
	}
	
	public static void reportsByRange(long clzId, int offset, int max){
		List<Report> reports = Report.getReportsByRange(clzId, offset, max);
		renderJSON(reports);
	}
	
	public static void markGrid(long clzId){
		KidClass clz = KidClass.findById(clzId);
		List<MarkType> posTypes = MarkType.find("byWeightGreaterThan", 0).fetch();
		List<MarkType> negTypes = MarkType.find("byWeightLessThan", 0).fetch();
		render(clz, posTypes, negTypes);
	}
	
	public static void markChildbyId(long childId, long typeId, String reason, String othBehavior, int othWeight){
		Child child = Child.findById(childId);
		if(typeId != 0){
			child.markChildbyType(typeId, reason, othBehavior, othWeight);
		}else{
			child.markChildByOther(reason, othBehavior, othWeight);
		}
		renderJSON(CommonUtils.getJsonString(child, Child.class));
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
	
	public static void createReport(String reportByJson, long clzId){
		KidClass clz = KidClass.findById(clzId);
		Report report = new Report();
		Teacher teacher = renderArgs.get("user", Teacher.class);
		report.saveReportByJson(teacher.id, reportByJson);
		clz.addReport(report);
		renderJSON(report);
	}
	
	public static void moodGrid(long clzId){
		KidClass clz = KidClass.findById(clzId);
		render(clz);
	}
	
	public static void moodChildById(long childId, int moodValue){
		Child child = Child.findById(childId);
		Mood mood = child.moodChildByMoodValue(moodValue);
		renderJSON(mood);
	}
	
	public static void showMenuByWeek(long classId, Date date){
		List<Menu> menus = Menu.getMenusByWeek(date);
		KidClass clz = KidClass.findById(classId);
		renderTemplate("/ClassLifeController/showmenu.html", clz, menus);
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
	
	public static void getClassFood(long clzId){
		KidClass clz = KidClass.findById(clzId);
		List<Food> food = clz.getFood();
		renderJSON(CommonUtils.getObjectAsJsonStr(food));
	}
	
	public static void createMenuByClzId(long clzId, long[] foodIds){
		Menu menu = new Menu();
		menu.createMenuByFoodIds(foodIds);
			
		KidClass clz = KidClass.findById(clzId);
		clz.addMenu(menu);
		
		renderJSON(CommonUtils.getObjectAsJsonStr(menu));
	}
}








