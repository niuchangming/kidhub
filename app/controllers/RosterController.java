package controllers;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import models.Child;
import models.KidClass;
import models.MarkType;
import models.Parent;
import play.mvc.Controller;
import play.mvc.With;
import utils.CommonUtils;

@With(Interceptor.class)
public class RosterController extends Controller{
	
	public static void classRoster(){
		long clzId = renderArgs.get("clzId", Long.class);
		List<Child> children = Child.find("clz_id = ?", clzId).fetch();
		renderTemplate("/classlifecontroller/classroster.html", children);
	}
	
	public static void createChild(String firstName, String lastName, Date birth){
		long clzId = renderArgs.get("clzId", Long.class);
		Child child = new Child(firstName, lastName, birth);
		child.createChildByClzId(clzId);
		renderJSON(CommonUtils.getJsonString(child, Child.class));
	}
	
	public static void editChild(long childId, String firstName, String lastName, Date birth){
		Child child = Child.findById(childId);
		child.firstName = firstName;
		child.lastName = lastName;
		child.birth = birth;
		child.save();
		renderJSON(CommonUtils.getJsonString(child, Child.class));
	}
	
}