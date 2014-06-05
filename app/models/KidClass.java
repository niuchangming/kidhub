package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.MapKeyTemporal;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.CommonUtils;

@Entity
@Table(name="class")
public class KidClass extends Model{
	@Transient
	public final static String defaultLogoUrl = "/public/images/Brook-icon.png";
	
	@Expose
	@Column(name="class_name", nullable=false)
	public String className;
	
	@Expose
	@Column(name="class_level", nullable=false)
	public String classLevel;
	
	@Expose
	@Column(name="start_date")
	public Date startDate;
	
	@Expose
	@Column(name="end_date")
	public Date endDate;
	
	@Expose
	@Column(name="logo_url")
	public String logoURL = defaultLogoUrl;
	
	@Expose
	@Column(name="class_desc")
	public String classDesc;
	
	@ManyToMany(mappedBy="classes", cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	public List<Teacher> teachers;
	
	@OneToMany(targetEntity = Child.class, mappedBy="kidClass", cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	public Set<Child> children;
	
	@OneToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="clz_id")
	public List<Report> reports;
	
	@OneToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinTable(name = "date_menu", joinColumns = {@JoinColumn(name="clz_id")}, inverseJoinColumns={@JoinColumn(name="menu_id")})
	@MapKeyColumn(name="date_key", nullable=false, unique=true)
	@MapKeyTemporal(TemporalType.DATE)
	public Map<Date, Menu> menus;
	
	public boolean active = true;
	
	public KidClass(String className, String classLevel, Date startDate,
			Date endDate, String logoURL, String classDesc) {
		this.className = className;
		this.classLevel = classLevel;
		this.startDate = startDate;
		this.endDate = endDate;
		this.logoURL = logoURL;
		this.classDesc = classDesc;
		this.reports = new ArrayList<Report>();
		this.menus = new HashMap<Date, Menu>();
	}

	public KidClass createClass(String className, String classLevel, String startDate, 
			String endDate, String logoURL, String classDesc){
		Date start = CommonUtils.getDateByFormat(startDate, null);
		Date end = CommonUtils.getDateByFormat(endDate, null);
		return new KidClass(className, classLevel, start, end, logoURL, classDesc).save();
	}
	
	public void addReport(Report report){
		this.reports.add(report);
		this.save();
	}
	
	public void addMenu(Menu menu){
		this.menus.put(new Date(), menu);
		this.save();
	}
	
	public List<Food> getFood(){
		List<Teacher> teachers = this.teachers;
		List<Food> food = new ArrayList<Food>();
		for(Teacher teacher : teachers){
			food.addAll(teacher.food);
		}
		return food;
	}
	
	private String getWeekQuery(Date curDate){
		Calendar c = Calendar.getInstance();
		c.setTime(curDate == null ? new Date() : curDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DAY_OF_MONTH, - dayOfWeek);
		Date weekStart = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, 6);
		Date weekEnd = c.getTime();
		return "date between " + CommonUtils.getDateString(weekStart, null) + " and "  + CommonUtils.getDateString(weekEnd, null);
	}
}
