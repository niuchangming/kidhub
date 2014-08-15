package models;

import java.io.File;
import java.io.IOException;
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
import play.libs.Images;

/**
 * @author cmniu
 *
 */
@Entity
public class Photo extends Model{
	@Transient
	public final String THUMNAIL_BASE = "data/thumnails/";
	
	@Transient
	public final static int LOAD_COUNT_PER_PAGE = 20;
	
	@Temporal(TemporalType.DATE)
	public Date date;
	
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	public User author;
	
	public Blob image;
	
	public String thumbnail;

	public Photo(User author, Blob image) {
		super();
		this.date = new Date();
		this.author = author;
		this.image = image;
		setThumbnail(this.image);
	}
	
	public void setThumbnail(Blob blob){
		File dir = new File(THUMNAIL_BASE);
		if(!dir.exists()){
			dir.mkdir();
		}
		File newFile = new File(dir, blob.getUUID()); // create random unique filename here
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Images.resize(blob.getFile(), newFile, 240, 180);
		thumbnail = THUMNAIL_BASE + blob.getUUID();
	}
	
}
