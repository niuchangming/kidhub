package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import play.db.jpa.Model;

/**
 * @author cmniu
 *
 */

@Entity
public class Album extends Model{
	@Transient
	public final static int LOAD_COUNT_PER_PAGE = 20;
	
	public String title;
	
	@Temporal(TemporalType.DATE)
	public Date date;
	
	@Lob
	public String description;
	
	@ManyToOne(fetch=FetchType.LAZY)
	public KidClass clz;
	
	@OneToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="album_id")
	@LazyCollection(LazyCollectionOption.EXTRA)
	public List<Photo> photos;

	public Album(String title, String description) {
		super();
		this.title = title;
		this.description = description;
		this.date = new Date();
		this.photos = new ArrayList<Photo>();
	}

}


































