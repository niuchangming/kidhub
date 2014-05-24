package models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.markdown4j.Markdown4jProcessor;

import enums.DataType;
import play.Play;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import play.libs.Images;
import utils.CommonUtils;
import vo.ReportVO;
import vo.ReportVO.ReportData;

@Entity
public class Report extends ResModuleBase{
	@Transient
	public final String THUMNAIL_BASE = "data/thumnails/";
	
	@Lob
	@Column(name="html_content")
	public String htmlContent;
	
	@Column(name="title")
	public String title;
	
	@Column(name="short_desc")
	public String shortDesc;
	
	@Column(name="thumnail")
	public String thumnail;
	
	@Column(name="teacher_id")
	public long teacherId;
	
	@Column(name="create_date")
	public Date createdDate;
	
	public void saveReportByJson(long teacherId, String reportByJson){
		this.teacherId = teacherId;
		this.createdDate = new Date();
		try {
			setHTMLContentByJson(reportByJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.save();
	}
	
	public void setHTMLContentByJson(String json) throws IOException{
		ReportVO reportVO = new ReportVO();
		reportVO.initByJson(json);
		
		if(reportVO.data != null && reportVO.data.size() > 0){
			StringBuffer html = new StringBuffer();
			for(ReportData report : reportVO.data){
				DataType dataType = DataType.valueOf(report.type.toUpperCase());
				switch(dataType){
				case TEXT:
					String paragraph = new Markdown4jProcessor().process(report.data.get("text"));
					html.append(paragraph);
					if(shortDesc == null){
						this.shortDesc = getShortDescOrTitle(paragraph);
					}
					break;
				case LIST:
					html.append(new Markdown4jProcessor().process(report.data.get("text")));
					break;
				case HEADING:
					String heading = new Markdown4jProcessor().process(report.data.get("text"));
					if(title == null){
						this.title = getShortDescOrTitle(heading);
					}
					html.append("<h3>" + heading + "</h3>");
					break;
				case IMAGE:
					long imageId = Long.parseLong(report.data.get("id"));
					html.append("<img src=\"" + Resource.BASE_PATH + "?resourceId=" + imageId + "\"/>");
					System.out.println("<img src=\"" + Resource.BASE_PATH + "?resourceId=" + imageId + "\"/>");
					addReportResources(imageId);
					break;
				case VIDEO:
					String remoteId = report.data.get("remote_id");
					String iframe = "<iframe src=\"http://www.youtube.com/embed/" + remoteId + "\" frameborder=\"0\" allowfullscreen=\"\"></iframe>";
					html.append(iframe);
					break;
				}
			}
			this.htmlContent = html.toString();
		}
	}
	
	public void addReportResources(long resId){
		Resource resource = Resource.findById(resId);
		if(resource != null){
			if(this.resources == null){
				this.resources = new ArrayList<Resource>();
			}
			this.resources.add(resource);
		}
		if(this.thumnail == null){
			addReportThumnail(resource);	
		}
	}
	
	public void addReportThumnail(Resource resource){
		File file = resource.file.getFile();
		File dir = new File(THUMNAIL_BASE);
		if(!dir.exists()){
			dir.mkdir();
		}
		File newFile = new File(dir, resource.file.getUUID()); // create random unique filename here
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Images.resize(file, newFile, 240, -1);
		thumnail = THUMNAIL_BASE + resource.file.getUUID();
	}
	
	public String getShortDescOrTitle(String content){
		int startIndex = content.indexOf("<p>");
		int endIndex = content.indexOf("</p>");
		String text = "";
		if(startIndex != -1){
			text = content.substring(startIndex + 3, endIndex);
			if(text.length() > 255){
				text = text.substring(0, 254);
			}
		}else{
			throw new RuntimeException("Could not found the paragraph.");
		}
		return text;
	}
	
	public static List<Report> getReportsByRange(long clzId, int offset, int max){
		List<Report> reports = Report.find("clz_id = ? order by create_date desc", clzId).from(offset).fetch(max);
		return reports;
	}
	
}
