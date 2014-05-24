package vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import enums.DataType;

public class ReportVO {
	public final static int DEFAULT_CCOUNT_PER_PAGE = 20;
	public List<ReportData> data;
	 
	public void initByJson(String jsonStr){
		JsonElement element = new JsonParser().parse(jsonStr);
		JsonObject obj = element.getAsJsonObject();
		JsonArray arr = obj.getAsJsonArray("data");
		
		Gson gson = new Gson();
		data = gson.fromJson(arr, new TypeToken<List<ReportData>>(){}.getType());
		for(int i = 0; i < this.data.size(); i++){
			switch(DataType.valueOf(data.get(i).type.toUpperCase())){
			case IMAGE:
				ReportData reportData = data.get(i);
				data.remove(reportData);
				data.add(Integer.parseInt(reportData.data.get("position")), reportData);
				break;
			}
		}
	}
	 
	public class ReportData{
		public String type;
		public Map<String, String> data;
	} 
}
