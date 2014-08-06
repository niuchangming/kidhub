package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.Model;

/**
 * @author cmniu
 *
 */
@Entity
public class Photo extends Model{
	@Transient
	public final static int LOAD_COUNT_PER_PAGE = 20;
	
	public String title;
	
	@Lob
	public String description;
	
	@Temporal(TemporalType.DATE)
	public Date date;
	
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	public User author;
	
	public Blob image;
	
	public Blob thumbnail;
}
