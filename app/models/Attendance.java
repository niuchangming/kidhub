package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import enums.AttendanceStatus;
import play.db.jpa.Model;

@Entity
@org.hibernate.annotations.Table(appliesTo = "attendance",
indexes = {
        @Index(name = "idx_chid_date",
                columnNames = {"child_id" , "date"}
        	  )
		}
)
@Table(name="attendance")
public class Attendance extends Model{
	@Transient
	public final static AttendanceStatus DEFAULT_STATUS = AttendanceStatus.PRESENT;
	
	@Index(name="idx_attendance_date")
	@Temporal(TemporalType.DATE)
	public Date date;
	
	public AttendanceStatus status;
	
	public Attendance(Date date, AttendanceStatus status) {
		super();
		this.date = date;
		this.status = status == null ? DEFAULT_STATUS : status;
	}
	
}
