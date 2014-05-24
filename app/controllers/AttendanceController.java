package controllers;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.imageio.ImageIO;
import enums.AttendanceStatus;
import models.Attendance;
import models.Child;
import models.KidClass;
import models.User;
import play.Play;
import play.data.Upload;
import play.libs.Images;
import play.mvc.Controller;
import play.mvc.With;
import utils.CommonUtils;

@With(Interceptor.class)
public class AttendanceController extends Controller{
	
	public static void classAttendance(long clzId){
		KidClass clz = KidClass.findById(clzId);
		render(clz);
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
	
}











