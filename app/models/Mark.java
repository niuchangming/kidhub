package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.Model;

@Entity
public class Mark extends Model{
	public String reason;
	
	@OneToOne
	@JoinColumn(name="type_id")
	public MarkType type;
	
	@Temporal(TemporalType.DATE)
	public Date date;

	@Column(name="other_type")
	public String otherType;
	public int weight;
	
	public Mark() {}

	public Mark(String reason, MarkType type) {
		super();
		this.reason = reason;
		this.type = type;
		this.date = new Date();
	}
	
}
