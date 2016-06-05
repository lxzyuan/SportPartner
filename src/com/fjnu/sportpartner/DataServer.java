package com.fjnu.sportpartner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fjnu.domain.Heart;
import com.fjnu.domain.Player;

public class DataServer {
	
	/**
	 * ½âÎöJSONÊý¾Ý
	 * @param inStream
	 * @return
	 */
	public static List<Heart> parseJSON(InputStream in) throws Exception{
		List<Heart> datas = new ArrayList<Heart>();
		byte[] data = StreamTool.read(in);
		String json = new String(data);
		JSONArray array = new JSONArray(json);
		for(int i = 0 ; i < array.length() ; i++){
			JSONObject jsonObject = array.getJSONObject(i);
			Heart data1 = new Heart(jsonObject.getInt("datetime"),jsonObject.getInt("data"));
			datas.add(data1);
		}
		return datas;
	}
	
	public static List<Player> parsePlayerJson(InputStream in) throws Exception{
		List<Player> playerdata = new ArrayList<Player>();
		byte[] data = StreamTool.read(in);
		String json = new String(data);
		JSONArray array = new JSONArray(json);
		for(int i = 0 ; i < array.length() ; i++){
			JSONObject jsonObject = array.getJSONObject(i);
			Player data1 = new Player(jsonObject.getString("id"));
			//System.out.println("id = " jsonObject.getString("id"));
			playerdata.add(data1);
		}
		return playerdata;
	}



}
