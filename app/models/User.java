package models;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.Play;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import utils.CommonUtils;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class User extends Model{
	public static final String AVATAR_BASE = "data/avatars/";
	
	@Column(unique=true, nullable=false)
	public String username;
	
	@Column(nullable=false)
	public String password;
	
	@Column(name="first_name")
	public String firstName;
	
	@Column(name="last_name")
	public String lastName;
	
	public String phone;
	
	@Column(name="avatar_url")
	public String avatarURL;
	
	public String email;
	
	public boolean active = true; // indicate the user if deleted
	
	public boolean enable; // indicate if the user can access the full features by his role
	
	@OneToOne
	@JoinColumn(name="role_id")
	public Role role;
	
	@OneToMany(mappedBy="author", targetEntity=Alert.class, cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	public Set<Alert> myAlerts;
	
	@ManyToMany(mappedBy="receivers", fetch=FetchType.EAGER)
	public Set<Alert> alerts;
	
	public User(String username, String password, String email, Role role) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public User() {}
	
	public static User connect(String userName, String password) {
        return find("byUsernameAndPassword", userName, password).first();
    }
	
	public void createUser(String username, String password, String email, Role role){
		this.username = username;
		this.password = CommonUtils.md5(password);
		this.email = email;
		this.role = role;
		this.save();
	}
	
	public static interface Constant {
		public final String SESSION = "_kid_hub_session_";
	}
	
	public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality){
		int type = (img.getTransparency() == Transparency.OPAQUE) ?
		BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage)img;
		int w, h;
		if (higherQuality && img.getWidth() > targetWidth && img.getHeight() > targetHeight) {
			w = img.getWidth();
			h = img.getHeight();
		} else {
			w = targetWidth;
			h = targetHeight;
		}
		
		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}
		
		if (higherQuality && h > targetHeight) {
			h /= 2;
			if (h < targetHeight) {
				h = targetHeight;
			}
		}
		
		BufferedImage tmp = new BufferedImage(w, h, type);
		Graphics2D g2 = tmp.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
		g2.drawImage(ret, 0, 0, w, h, null);
		g2.dispose();
		
		ret = tmp;
		} while (w != targetWidth || h != targetHeight);
		
		return ret;
	}
} 
