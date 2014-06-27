package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.CommonUtils;

@Entity
@Table(name="menu_order")
public class MenuOrder extends Model{
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	public Date date;
	
	@ManyToOne
	public Menu menu;

	public MenuOrder(Date date, Menu menu) {
		super();
		this.date = date;
		this.menu = menu;
		this.save();
	}
	
	public static List<MenuOrder> showOrderByWeek(long clzId, Date date){
		Date[] dates = CommonUtils.getWeekStartAndEnd(date);
		List<MenuOrder> menus = MenuOrder.find("clz_id = ? and date between ? and ?", clzId, dates[0], dates[1]).fetch();
		return menus;
	}
	
}
