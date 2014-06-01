package utils;

import java.io.File;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import models.Child;
import models.User;

import com.google.gson.GsonBuilder;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

import flexjson.JSONSerializer;

public class CommonUtils {
	
	public static String md5(final String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			byte[] output = md.digest();
			return bytesToHex(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return input;
	}

	public static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		
		return buf.toString();
	}
	
	public static Date getDateByFormat(String dateStr, String format){
		DateFormat formatter = getDateFormat(format);
		Date date = null;
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getDateString(Date date, String format){
		DateFormat formatter = getDateFormat(format);
		return formatter.format(date);
	}
	
	public static DateFormat getDateFormat(String format){
		if(format == null)
			return new SimpleDateFormat("yyyy-MM-dd");
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public static Date getDateByFormat(Date date, String format) throws ParseException{
		DateFormat formatter = getDateFormat(format);
		String dateStr = formatter.format(date);
		return formatter.parse(dateStr);
	}
	
	public static boolean isBlank(final String str) {
		if (null == str)
			return true;
		if (str.isEmpty())
			return true;

		return str.trim().isEmpty();
	}
	
	public static String uuid() {
		return UUID.randomUUID().toString();
	}
	
	public static String getJsonString(Object obj, Class clz){
		String json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(obj, clz);
		return json;
	}
	
	public static String getRelativePath(String absolutePath, String basePath) {
        return absolutePath.replace(basePath, "");
    }
	
	public static void clearDirectory(File dir) {
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory()) clearDirectory(file);
	        file.delete();
	    }
	}
	
	public static String getObjectAsJsonStr(Object obj){
		JSONSerializer serializer = new JSONSerializer();
		return serializer.deepSerialize(obj);
	}
	
}
