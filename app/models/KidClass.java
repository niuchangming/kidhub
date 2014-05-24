package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

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
	@JoinColumn(name="clz_id")
	public List<Menu> menus;
	
	public boolean active = true;
	
	public KidClass(String className, String classLevel, Date startDate,
			Date endDate, String logoURL, String classDesc) {
		this.className = className;
		this.classLevel = classLevel;
		this.startDate = startDate;
		this.endDate = endDate;
		this.logoURL = logoURL;
		this.classDesc = classDesc;
	}

	public KidClass createClass(String className, String classLevel, String startDate, 
			String endDate, String logoURL, String classDesc){
		Date start = CommonUtils.getDateByFormat(startDate, null);
		Date end = CommonUtils.getDateByFormat(endDate, null);
		return new KidClass(className, classLevel, start, end, logoURL, classDesc).save();
	}
	
	public void addReport(Report report){
		if(this.reports == null){
			this.reports = new ArrayList<Report>();
		}
		this.reports.add(report);
		this.save();
	}
}
