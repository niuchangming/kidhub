package models;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

import enums.FoodType;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import play.libs.Images;

@Entity
public class Food extends Model{
	@Transient
	public final static String defaultThumbnailUrl = "../../public/images/photo_default.png";
	
	@Transient
	public final String THUMNAIL_BASE = "data/food_thumnails/";
	
	@Column(unique=true)
	public String name;
	
	public Blob image;
	
	public String thumbnail = defaultThumbnailUrl;
	
	@Lob
	public String description;
	
	public FoodType type = FoodType.BREAKFAST;
	
	public void createFoodByTeacher(long teacherId){
		this.save();
		Teacher teacher = Teacher.findById(teacherId);
		teacher.food.add(this);
		teacher.save();
	}
	
	public void addFoodThumnail(){
		if(image == null) return;
		
		File file = image.getFile();
		File dir = new File(THUMNAIL_BASE);
		if(!dir.exists()){
			dir.mkdir();
		}
		File newFile = new File(dir, image.getUUID()); // create random unique filename here
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Images.resize(file, newFile, 240, -1);
		thumbnail = THUMNAIL_BASE + image.getUUID();
	}
	
}
