package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import enums.LessonType;
import play.db.jpa.Model;

@Entity
public class Lesson extends Model{
	
	@Column(nullable=false)
	public String title;
	
	@Lob
	public String description;
	
	public void createLessonByTeacher(long teacherId){
		this.save();
		Teacher teacher = Teacher.findById(teacherId);
		teacher.lessons.add(this);
		teacher.save();
	}

}
