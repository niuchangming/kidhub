package models;

import java.util.*;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
 
import play.db.jpa.*;

import play.db.jpa.Model;

@Entity
public class Parent extends User{
	
	@OneToMany(targetEntity = Child.class, mappedBy="parent", cascade=CascadeType.MERGE)
	public List<Child> children;
	
}
