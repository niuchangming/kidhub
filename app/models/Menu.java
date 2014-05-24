package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import enums.FoodType;
import play.db.jpa.Model;
import utils.CommonUtils;

@Entity
@Table(name="menu")
public class Menu extends Model{
	
	@OneToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	public List<Food> food;
	
	public Date date;
	
	@Column(name="is_template")
	public boolean isTemplate;
	
	public static List<Menu> getMenusByWeek(Date curDate){
		List<Menu> menus = Menu.find(getWeekQuery(curDate)).fetch();
		return menus;
	}
	
	private static String getWeekQuery(Date curDate){
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
