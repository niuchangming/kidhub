package controllers;

import models.Teacher;
import models.User;
import static models.User.Constant.*;
import play.Logger;
import play.cache.Cache;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Router;
import utils.CommonUtils;

public class Interceptor extends Controller{
	
	@Before(priority = 1, unless={"AlertController.getActicleForApi", "Application.getArticlesByRange"})
	static void checkAuthenticated() {
		String sessionId = session.get(SESSION);
		User user = Cache.get(sessionId, User.class);
		if (user == null || CommonUtils.isBlank(sessionId) || !session.contains(SESSION)) {
			if(!request.action.equals("Application.index")){
				Application.index();
			}
		}
		renderArgs.put("user", user);
	}
	
	@Before(priority = 0, only={"Dashboard.classGrid"})
	static void updateUserBefore(){
		updateUser();
	}
	
	@After(priority = 0, only={"AlertController.deleteById", "Dashboard.createClass"})
	static void updateUserAfter(){
		updateUser();
	}
	
	static void updateUser(){
		String sessionId = session.get(SESSION);
		User user = Cache.get(sessionId, User.class);
		if(user == null || CommonUtils.isBlank(sessionId) || !session.contains(SESSION)){
			
		}else{
			user = User.find("byId", user.id).first();
			Cache.set(sessionId, user);
		}
		
	}
	
	@Catch(IllegalStateException.class)
    public static void logIllegalState(Throwable throwable) {
        Logger.error("Illegal state %s…", throwable);
    }
	
}
