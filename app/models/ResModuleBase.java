package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.gson.Gson;

import play.db.jpa.Model;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class ResModuleBase extends Model{
	
	@OneToMany
	@JoinColumn(name="res_module_id")
	public List<Resource> resources;
	
}
