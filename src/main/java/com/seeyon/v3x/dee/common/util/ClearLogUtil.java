package com.seeyon.v3x.dee.common.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.client.service.DEEConfigService;
import com.seeyon.v3x.dee.context.AdapterKeyName;
import com.seeyon.v3x.dee.util.FileUtil;

public class ClearLogUtil {
	public static void delLogs(int day) throws TransformException {
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		Date exeDate = new Date();
    	calendar.setTime(exeDate);
    	calendar.add(Calendar.DATE, - day);
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
    	Date remainDate = calendar.getTime();
		DEEConfigService configService = DEEConfigService.getInstance();
		String date = format.format(remainDate);
		String str = configService.findLogByDate(date);
		if(!"".equals(str)){
			String[] strs = str.split(",");
			for(String s : strs){
				String[] ss = s.split("_");
				AdapterKeyName adapterKeyName = AdapterKeyName.getInstance();
				boolean isA8 = FileUtil.isA8Home();
	        	String path = "";
	        	String id = ss[1];
	        	String fid = ss[0];
				if (!"".equals(id)) {
					if (isA8) {
						path = adapterKeyName.getA8Home() + "/base/dee/flowLogs/" + 
								adapterKeyName.getFlowMap().get(fid)+ "_" + fid + "/";
					} else {
						path = adapterKeyName.getDeeHome() + "/flowLogs/" + 
								adapterKeyName.getFlowMap().get(fid) + "_" + fid + "/";
					}
					path = path + id + ".properties";
					File file = new File(path);
					if (file.exists()) {
						file.delete();
					}
				}
			}
			configService.delLogs(date);
		}
	}
}
