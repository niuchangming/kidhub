package controllers;
import models.Report;
import play.mvc.Controller;
import play.mvc.With;

@With(Interceptor.class)
public class ReportShowController extends Controller{
	
	public static void showReport(long reportId){
		Report report = Report.findById(reportId);
		renderTemplate("/ClassLifeController/showreport.html", report);
	}
}
