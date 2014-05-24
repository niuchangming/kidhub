package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Role extends Model{
	@Column(unique=true)
	public String role;
	
	@Column(unique=true)
	public int weight;
	
	public boolean active = true;
	
	public Role(String role, int weight, boolean active) {
		this.role = role;
		this.weight = weight;
		this.active = active;
	}
	
}
