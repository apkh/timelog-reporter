package com.vranec.jira.gateway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class WorkloadMap {
	// DayOfMonth -> (User -> (TaskKey -> hours) )
	private Map<Integer, Map<String, Map<String, Float>>> wlMap;
	// TaskKey -> summary
	private Map<String, String> taskSummaryMap;

	public WorkloadMap() {
		wlMap = new HashMap<Integer, Map<String, Map<String, Float>>>();
		taskSummaryMap = new HashMap<String, String>();
	}

	public void addWl(Date taskDate, String taskKey, String summary, String userName, int minutes) {
		int date = new DateTime(taskDate).getDayOfMonth();
		Map<String, Map<String, Float>> dateWl = wlMap.get(date);
		if (dateWl == null) {
			dateWl = new HashMap<String, Map<String, Float>>();
			wlMap.put(date, dateWl);
		}
		Map<String, Float> userWl = dateWl.get(userName);
		if (userWl == null) {
			userWl = new HashMap<String, Float>();
			dateWl.put(userName, userWl);
		}
		userWl.put(taskKey, (float)minutes / 60);
		taskSummaryMap.put(taskKey, summary);
	}

	public List<Integer> getDates() {
		Set<Integer> dateSet = wlMap.keySet();
		ArrayList<Integer> dateArray = new ArrayList<Integer>(dateSet);
		Collections.sort(dateArray, new Comparator() {

			@Override
			public int compare(Object arg0, Object arg1) {
				if (arg0 == null || arg1 == null ||
						arg0.getClass() != Integer.class ||
						arg1.getClass() != Integer.class) {
					return 0;
				}
				Integer d0 = (Integer)arg0;
				Integer d1 = (Integer)arg1;
				
				return d0.compareTo(d1);
			}
			
		});
		return dateArray;
	}

	public  Map<String, Float> getTasks(Integer date, String user) {
		Map<String, Map<String, Float>> dateMap = wlMap.get(date);
		if (dateMap == null) {
			return null;
		}
		Map<String, Float> userMap = dateMap.get(user);
		if (userMap == null) {
			return Collections.EMPTY_MAP;
		}
		return userMap;
	}
	
	
}
