package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import enums.LessonType;
import play.db.jpa.Model;

@Entity
public class Lesson extends Model{
	
	@Column(nullable=false)
	public String title;
	
	@OneToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	public Teacher teacher; 
	
	@Lob
	public String description;
	
	public void createLessonByClzId(long clzId){
		this.save();
		KidClass clz = KidClass.findById(clzId);
		clz.lessons.add(this);
		clz.save();
	}

}
