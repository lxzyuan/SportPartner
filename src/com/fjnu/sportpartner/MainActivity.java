package com.fjnu.sportpartner;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fjnu.domain.Player;
import com.fjnu.func.ShowHeart;
import com.fjnu.func.ShowSport;

public class MainActivity extends Activity {

	private ListView lv;
	private List<Player> playerdata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lv = (ListView) findViewById(R.id.lv);
		initListData();
		//
	}

	private void initListData() {
		new Thread() {
			public void run() {
				try {
					String path = "http://192.168.2.150:8000/USER";
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream in = conn.getInputStream();
						playerdata = DataServer.parsePlayerJson(in);
						runOnUiThread(new Runnable() {
							public void run() {
								lv.setAdapter(new MyAdapter());
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return playerdata.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.item, null);
			} else {
				view = convertView;
			}
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			Button bt_sport = (Button) view.findViewById(R.id.bt_sport);
			Button bt_heart = (Button) view.findViewById(R.id.bt_heart);

			tv_title.setText(playerdata.get(position).getId());
			
			final String id = playerdata.get(position).getId();
					
			bt_heart.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					System.out.println("心率，，button跳转");
					System.out.println(id);
		
					Intent intent = new Intent();
					intent.putExtra("id", id);
					intent.setClass(MainActivity.this, ShowHeart.class);
					MainActivity.this.startActivity(intent);
				}
			});
//
			bt_sport.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					System.out.println("加速度，，button跳转");
					System.out.println(id);
					
					Intent intent = new Intent();
					intent.putExtra("id", id);
					intent.setClass(MainActivity.this, ShowSport.class);
					MainActivity.this.startActivity(intent);
				}
			});
			return view;
		}
	}

}
