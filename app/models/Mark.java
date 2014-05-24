package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class Mark extends Model{
	public String reason;
	
	@OneToOne
	@JoinColumn(name="type_id")
	public MarkType type;

	@Column(name="other_type")
	public String otherType;
	public int weight;
	
	public Mark() {}

	public Mark(String reason, MarkType type) {
		super();
		this.reason = reason;
		this.type = type;
	}
	
}
