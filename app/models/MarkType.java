package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Model;

@Entity
@Table(name="mark_type")
public class MarkType extends Model{
	@Column(nullable=false)
	public String type;
	
	@Column(nullable=false)
	public int weight;
	
	@Column(name="icon_url")
	public String iconURL;
	
	public boolean active = true;
	
	public MarkType(String type, int weight, String iconURL) {
		this.type = type;
		this.weight = weight;
		this.iconURL = iconURL;
	}
	
}
