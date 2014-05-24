import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import models.MarkType;
import models.Role;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.mvc.Controller;

@OnApplicationStart
public class Bootstrap extends Job{
	
	@Override
	public void doJob() throws Exception {
		if(Role.count() == 0){
			new Role("parent", 1, true).save();
			new Role("teacher", 2, true).save();
			new Role("manager", 3, true).save();
			new Role("admin", 4, true).save();
		}
		
		if(MarkType.count() == 0){
			new MarkType("Helpping others", 1, "/public/images/heart.png").save();
			new MarkType("On task", 1, "/public/images/heart.png").save();
			new MarkType("Participating", 1, "/public/images/heart.png").save();
			new MarkType("Persistence", 1, "/public/images/heart.png").save();
			new MarkType("Team work", 1, "/public/images/heart.png").save();
			new MarkType("Work hard", 1, "/public/images/heart.png").save();
			new MarkType("Concentration", 1, "/public/images/heart.png").save();
			new MarkType("Intelligence", 1, "/public/images/heart.png").save();
			
			new MarkType("Bullying", -1, "/public/images/heart.png").save();
			new MarkType("Disrespect", -1, "/public/images/heart.png").save();
			new MarkType("No homework", -1, "/public/images/heart.png").save();
			new MarkType("Off task", -1, "/public/images/heart.png").save();
			new MarkType("Talking", -1, "/public/images/heart.png").save();
			new MarkType("Unprepared", -1, "/public/images/heart.png").save();
			new MarkType("Absent-minded", -1, "/public/images/heart.png").save();
			new MarkType("Naughty", -1, "/public/images/heart.png").save();
		}
	}

}
