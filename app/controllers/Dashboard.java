package controllers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.core.IsInstanceOf;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import models.Child;
import models.KidClass;
import models.Mark;
import models.MarkType;
import models.Parent;
import models.Teacher;
import models.User;
import static models.User.Constant.*;
import play.cache.Cache;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
import utils.CommonUtils;

@With(Interceptor.class)
public class Dashboard extends Controller{
	
	public static void index(){
		User user = renderArgs.get("user", User.class);
		if(user instanceof Teacher){
			classGrid();
		}else if(user instanceof Parent){
			render();
		}
	}
	
	public static void classGrid(){
		render();
	}
	
	public static void teacherBoard(long clzId){
		KidClass clz = KidClass.findById(clzId);
		render(clz);
	}
	
	
	public static void createClass(KidClass clz){
		clz.save();
		
		User user = renderArgs.get("user", User.class);
		Teacher teacher = Teacher.findById(user.id);
		teacher.addClass(clz);
		
		renderJSON(CommonUtils.getJsonString(clz, KidClass.class));
	}
	
}






















