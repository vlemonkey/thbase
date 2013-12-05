package com.boco.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class JsonUtils {
	
//	private static Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

	public static void main(String[] args) {

	}

	public static String listmap2Json(List<Map<String, String>> list) {
		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}

	/**
	 * 将结果数据变成List<Map<String, String>>
	 * 
	 * @param meta
	 * @param list
	 * @return
	 */
	public static List<Map<String, String>> strlist2MapList(String[] meta,
			List<List<String>> list) {
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		for (int i = 0, n = list.size(); i < n; i++) {
			map  = new HashMap<String, String>();
			for (int j = 0, m = list.get(i).size(); j < m; j++) {
				map.put(meta[j], list.get(i).get(j));
			}
			retList.add(map);
		}

		return retList;
	}
}
