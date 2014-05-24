package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Resource extends Model{
	
	@Transient
	public static final String BASE_PATH = "/classlifecontroller/showresourcebinary";
	
	public String type;
	
	@Column(name="module_type")
	public String moduleType;
	
	@Column(name="file_name")
	public String fileName;
	
	public Blob file;
	
}
