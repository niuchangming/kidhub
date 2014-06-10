package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import utils.CommonException;
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
		KidClass clz = KidClass.findById(classId);
		Map<String, Menu> menus = clz.getMenusByWeek(new Date());
		/**
		 *	select menus0_.clz_id, menus0_.menu_id, menus0_.date_key, 
		 *	menu1_.id, menu1_.date, menu1_.is_template from date_menu menus0_ 
		 *	inner join menu menu1_ on menus0_.menu_id=menu1_.id where menus0_.clz_id=?
		 */
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
	
	public static void createMenuByClzId(long clzId, long[] foodIds, String dateStr, boolean isTemplate){
		Menu menu = null;
		KidClass clz = KidClass.findById(clzId);
		if(!clz.menus.containsKey(dateStr)){
			menu = new Menu();
			menu.createMenuByFoodIds(foodIds, isTemplate);
			clz.addMenu(menu, dateStr);
		}
		renderJSON(CommonUtils.getObjectAsJsonStr(menu));
	}
}








