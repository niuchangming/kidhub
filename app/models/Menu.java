package models;

import java.util.ArrayList;
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
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

import enums.FoodType;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.CommonUtils;

@Entity
@Table(name="menu")
public class Menu extends Model{
	@Expose
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="menu_food", joinColumns={@JoinColumn(name="menu_id")}, inverseJoinColumns={@JoinColumn(name="food_id")})
	public List<Food> food;
	
	@Temporal(TemporalType.DATE)
	public Date date;
	
	@Column(name="is_template")
	public boolean isTemplate;

	public Menu() {
		this.food = new ArrayList<Food>();
		isTemplate = false;
		date = new Date();
	}
	
	public void createMenuByFoodIds(long[] foodIds, boolean isTemplate){
		List<Food> foodArr = Food.find(getQueryByFoodIds(foodIds)).fetch();
		this.food.addAll(foodArr);
		this.isTemplate = isTemplate;
		this.save();
	}
	
	public void updateMenuByFoodIds(long[] foodIds){
		this.food.clear();
		List<Food> foodArr = Food.find(getQueryByFoodIds(foodIds)).fetch();
		this.food.addAll(foodArr);
		this.save();
	}
	
	private String getQueryByFoodIds(long[] foodIds){
		StringBuilder query = new StringBuilder();
		for(long foodId : foodIds){
			query.append("id = " + foodId + " or ");
		}
		return query.substring(0, query.length() - 4);
	}
	 
}
