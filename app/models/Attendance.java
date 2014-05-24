package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import enums.AttendanceStatus;
import play.db.jpa.Model;

@Entity
public class Attendance extends Model{
	@Transient
	public final static AttendanceStatus DEFAULT_STATUS = AttendanceStatus.PRESENT;
	
	@Index(name="idx_attendance_date")
	@Column(name="attendance_date")
	@Temporal(TemporalType.DATE)
	public Date attendanceDate;
	
	public AttendanceStatus status;
	
	public Attendance(Date attendanceDate, AttendanceStatus status) {
		super();
		this.attendanceDate = attendanceDate;
		this.status = status == null ? DEFAULT_STATUS : status;
	}
	
}
