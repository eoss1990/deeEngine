package com.seeyon.v3x.dee.common.hotdeploy;

import com.seeyon.v3x.dee.DirectoryWatcher;
import com.seeyon.v3x.dee.TransformFactory;
import com.seeyon.v3x.dee.schedule.QuartzManager;

import java.io.File;
import java.util.Timer;
import java.util.regex.Pattern;

public class ScheduleCheck extends DirectoryWatcher {
	private static final String CONF = "conf";
	public ScheduleCheck(File directoryToWatch, Pattern pattern) {
		super(directoryToWatch, pattern);
	}
	public ScheduleCheck(String directoryToWatch, Pattern pattern) {
		super(directoryToWatch, pattern);
	}
	
	@Override
	protected File processFile(File file) throws Exception {
		QuartzManager m = QuartzManager.getInstance();
		if(file!=null&&m.getJobList().size()<=0){
			m.flowSchedule();
		}
		return file;
	}
	public static void main(String args[]){
		ScheduleCheck sc = new ScheduleCheck(TransformFactory.getInstance().getHomeDirectory()+ File.separator+CONF, Pattern.compile("(dee.xml)"));
		Timer timer = new Timer();
		timer.schedule(sc, 1000, 10000);
		
	}

}
