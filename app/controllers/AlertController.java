package controllers;

import java.util.List;

import models.Alert;
import models.AlertType;
import models.Article;
import models.Parent;
import models.Teacher;
import models.User;
import static models.User.Constant.*;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.With;

@With(Interceptor.class)
public class AlertController extends Controller{
	
	public static boolean inviteAlert(long userId, String inviteusername, int weight){
		List<User> receivers = null;
		if(weight == 1){
			receivers = Parent.find("byUserName", inviteusername).fetch();
		}else if(weight == 2){
			receivers = Teacher.find("byUserName", inviteusername).fetch();
		}
		
		if(receivers.size() > 0){
			User author = User.findById(userId);
			Alert alert = Alert.createAlert(AlertType.INVITE, author, receivers);
			alert.save();
		}
		
		return receivers.size() > 0;
	}
	
	public static void deleteById(long id){
		Alert alert = Alert.find("byId", id).first();
		if(alert != null){
			alert.delete();
		}
	}
	
}
