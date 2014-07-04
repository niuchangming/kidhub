package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="lesson_time")
public class LessonTiming extends Model{
	
	@Column(name="date_time", nullable=false)
	public Date dateTime;
	
	@ManyToOne
	public Lesson lesson;
}
