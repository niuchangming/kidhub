package controllers;

import static models.User.Constant.SESSION;
import models.Parent;
import models.Role;
import models.Teacher;
import models.User;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.OAuth2;
import play.mvc.Controller;
import play.mvc.With;
import utils.CommonUtils;

public class SignController extends Controller{
	
	public static void signup(String username, String password, String email, String role){
    	Role r = Role.find("byRole", role).first();
    	User user = null;
    	if(role.equals("parent")){
    		user = new Parent();
    	}else if(role.equals("teacher")){
    		user = new Teacher();
    	}
    	user.createUser(username, password, email, r);
    	
    	String sessionId = CommonUtils.uuid();
		session.put(SESSION, sessionId);
		Cache.set(sessionId, user);
		
    	renderText("success");
    }
    
    public static void signin(@Required String username, @Required String password){
    	User user = null;
    	if(validation.hasErrors()){
    		System.out.println(validation.toString());
    	}else{
    		user = User.find("byUsernameAndPassword", username, CommonUtils.md5(password)).first();
    	}
    	if(user != null){
    		String sessionId = CommonUtils.uuid();
    		session.put(SESSION, sessionId);
    		Cache.set(sessionId, user);
    	}else{
    		renderText("User does not exist !");
    	}
    	renderText("success");
    }
	
    public static String signout(){
    	session.clear();
    	return "success";
    }
    
	public static boolean isUsernameAvailable(String username){
    	User user = User.find("byUsername", username).first();
    	if(user == null){
    		return true;
    	}
    	return false;
    }
	
}
