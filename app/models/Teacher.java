package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.apache.commons.collections.map.HashedMap;

import net.sf.cglib.core.ClassesKey;
import play.data.binding.ParamNode;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.db.jpa.NoTransaction;

@Entity
public class Teacher extends User{
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="teacher_class", joinColumns={@JoinColumn(name="teacher_id")}, inverseJoinColumns={@JoinColumn(name="class_id")})
	public List<KidClass> classes;
	
	@OneToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="teacher_id")
	public List<Food> food;
	
	@OneToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="teacher_id")
	public List<Lesson> lessons;
	
	public void addClass(KidClass clz){
		if(this.classes == null){
			this.classes = new ArrayList<KidClass>();
		}
		this.classes.add(clz);
		this.save();
	}
}
