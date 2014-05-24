package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import enums.ArticleType;

import play.db.jpa.Model;

@Entity
public class Article extends Model{
	@Transient
	public final static int DEFAULT_CCOUNT_PER_PAGE = 20;
	
	@Column(nullable=false)
	public String title;
	
	@Lob
	@Column(nullable=false)
	public String content;
	
	@Lob
	@Column(name="short_desc", length=1024)
	public String shortDesc;
	
	@Lob
	@Column(name="image_json")
	public String imageJSON;
	
	@Column(nullable=false)
	public String author;
	
	@Column(name="created_date")
	public Date createdDate;
	
	public ArticleType type; 
	
	public boolean active = true;
}
