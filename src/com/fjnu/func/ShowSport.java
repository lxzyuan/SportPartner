package com.fjnu.func;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.fjnu.domain.Accele;
import com.fjnu.sportpartner.R;
import com.fjnu.sportpartner.StreamTool;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class ShowSport extends Activity {
	protected final int REQUESTSUCESS = 0; 
	protected final int REQUESTNOTFOUND = 1;
	protected final int REQUESTEXCEPTION = 2;
	String id;
	List<Accele> dataAccele;
	
	TextView showidsp;
	private Handler handler = new Handler() {
		@SuppressLint("ShowToast")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REQUESTSUCESS: 

				String content = (String) msg.obj;
				try {
					dataAccele = parseAcceleJSON(content);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LineChart mLineChart=(LineChart) findViewById(R.id.acceration_linechart);
				LineData mLineData=LineData(60);
				XAxis mXAxis=mLineChart.getXAxis();
				mXAxis.setPosition(XAxisPosition.BOTTOM);
				//图标左边的Y轴的坐标
				YAxis mYAxis=mLineChart.getAxisLeft();
				//Y轴最大的坐标
				mYAxis.setAxisMaxValue(500.0f);
				mYAxis.setAxisMinValue(0.0f);
				//不一定要从0开始
				mYAxis.setStartAtZero(false);
				setChartStyle(mLineChart,mLineData);
				//取得一天之内的第几分钟
				break;
			case REQUESTNOTFOUND: 
				Toast.makeText(getApplicationContext(), "请求资源不存在", 0).show();
				break;
			case REQUESTEXCEPTION: 
				Toast.makeText(getApplicationContext(), "服务器忙 请稍后....", 1)
						.show();
				break;
			default:
				break;
			}
		};
	};
	
	public static  List<Accele> parseAcceleJSON(String json) throws Exception{
		List<Accele> datas = new ArrayList<Accele>();
		
		JSONArray array = new JSONArray(json);
		for(int i = 0 ; i < array.length() ; i++){
			JSONObject jsonObject = array.getJSONObject(i);
			Accele dataAccele = new Accele(jsonObject.getInt("acceleration"),jsonObject.getInt("id"),jsonObject.getInt("time"));
			datas.add(dataAccele);
		}
		return datas;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acceration);
		showidsp = (TextView) findViewById(R.id.showidsp);
		
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		System.out.println("加速度id=====" + id);
		
		
		showidsp.setText(id);
		initData();
	}//

	private void initData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				try {//
					String path =" http://192.168.2.150:8000/GET/accleration?id="+id;
					System.out.println("获取加速度数据---"+path);
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream in = conn.getInputStream();
						
						byte[] data = StreamTool.read(in);
						String json = new String(data);
						/*dataHeart = DataServer.parseJSON(in);
						System.out.println("OKOKOKOKOK");*/
						Message msg1 = new Message();
						msg1.what = REQUESTSUCESS;
						msg1.obj = json;
						handler.sendMessage(msg1);

					} else {
						Message msg3 = new Message();
						msg3.what = REQUESTNOTFOUND;
						handler.sendMessage(msg3);
					}

				} catch (Exception e) {
					e.printStackTrace();
					Message msg4 = new Message();
					msg4.what = REQUESTEXCEPTION;
					handler.sendMessage(msg4); 
				}
			};
		}.start();
	}


	private void setChartStyle(LineChart mLineChart,
			com.github.mikephil.charting.data.LineData mLineData) {
		// TODO Auto-generated method stub
		// 是否在折线上添加边框
		mLineChart.setDrawBorders(false);
		// 数据描述
		mLineChart.setDescription("运动加速度示意图");
		// 设置如果没有数据，你就会看到什么数据
		mLineChart.setNoDataTextDescription("现在暂时没有数据");
		// 设置折线图的背景
		mLineChart.setDrawGridBackground(false);
		// 设置触摸
		mLineChart.setTouchEnabled(true);
		// 设置拖拽
		mLineChart.setDragEnabled(true);
		// 设置缩放
		mLineChart.setScaleEnabled(true);
		mLineChart.setPinchZoom(false);
		// xy轴的背景
		mLineChart.setBackgroundColor(Color.BLUE);
		// xy的数据
		mLineChart.setData(mLineData);
		// 比例图标。用y的value ,legend为图的比例
		Legend mLegend = mLineChart.getLegend();
		mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
		// 样式
		mLegend.setForm(LegendForm.LINE);
		// 字体
		mLegend.setFormSize(20.0f);
		//设置X的之间的空隙 ------------------------------------------------------------
        mLegend.setXEntrySpace(1);
       //----------------------------------------------------------------------
		// 下体字体颜色
		mLegend.setTextColor(Color.RED);
		// 设置x轴的动画
		mLineChart.animateX(15000);
		// 刷新图表  ------------------------------------------------
        mLineChart.invalidate();  
        //------------------------------------------------------
	}

	private LineData LineData(int i) {
		// TODO Auto-generated method stub
		ArrayList<Integer> Datadatetime=new ArrayList<Integer>();
		//ArrayList<Integer> Datadatetimemiao=new ArrayList<Integer>();
		ArrayList<Integer> Datadata=new ArrayList<Integer>();
		try {
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (Accele sensor : dataAccele) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				//item.put("id", sensor.getId());
				item.put("acceleration", sensor.getAcceleration());
				item.put("time", sensor.getTime());
				data.add(item);
				
				
				long a=sensor.getTime();
				 
		        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmmss");
		        String sd = sdf.format(new Date(a*1000));   // 时间戳转换成时间
		        Datadatetime.add(Integer.parseInt(sd.substring(13)));
			   Datadata.add(sensor.getAcceleration());
				/*long a=sensor.getTime();
				 
		       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmmss");
		       String sd = sdf.format(new Date(a*1000));   // 时间戳转换成时间

		       Datadatetime.add(Integer.parseInt(sd.substring(13,15)));
		       Datadatetimemiao.add(Integer.parseInt(sd.substring(13,15)));
			   Datadata.add(sensor.getAcceleration());*/
			}
		} catch (Exception e) {
			System.out.println("yichang....");
			e.printStackTrace();
		}
		/*
		 * 上面就是从服务器获取数据并且储藏
		 * 
		 * 
		 * 
		 */
		ArrayList<Integer> Datadatetime1=new ArrayList<Integer>();
		ArrayList<String> xArrayList = new ArrayList<String>();
		// x轴的数据
		for (int j = 0; j <Datadatetime.size(); j++) {
			Datadatetime1.add(Datadatetime.get(j)-Datadatetime.get(0)+1);
			xArrayList.add("第"+Datadatetime1.get(j)+"秒");
		}
		// y轴的数据
		System.out.println("dssgdsgdsdydgwygfydgfydgfyds");
		ArrayList<Entry> yArrayList = new ArrayList<Entry>();
		for (int j = 0; j <Datadata.size(); j++) {

			Entry entry=new Entry(Datadata.get(j), j);
			   yArrayList.add(entry);
		}
		System.out.println("y轴数据设置完毕");
		/*ArrayList<Entry> yArrayList1 = new ArrayList<Entry>();
		for (int j = 0; j < 30; j++) {

			Entry entry1 = new Entry(2f + j * 3f + 5 * 2f, j * 2);
			yArrayList1.add(entry1);
		}*/
		// y轴的数据集
		LineDataSet set = new LineDataSet(yArrayList, "加速度合成图");
	//	LineDataSet set1 = new LineDataSet(yArrayList1, "红色代表x");
		// 用y轴的集合来设置参数
		// 线宽
		set.setLineWidth(2.0f);
		//set1.setLineWidth(2.0f);
		// 显示圆形大小
		set.setCircleSize(0.05f);
	//	set1.setCircleSize(0.05f);
		// 折线的颜色
		set.setColor(Color.RED);
		//set1.setColor(Color.BLUE);
		// 圆球的颜色
		set.setCircleColor(Color.RED);
		// 设置十字交叉的纵横线
		// set.setDrawHighlightIndicators(true);
		// 十字交叉线的颜色
		set.setHighLightColor(Color.BLUE);
		//set1.setHighLightColor(Color.BLUE);
		// 设置显示数据点字体大小
		set.setValueTextSize(10.0f);
	//	set1.setValueTextSize(10.0f);
		ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
		mLineDataSets.add(set);
	//	mLineDataSets.add(set1);
		LineData mLineData = new LineData(xArrayList, mLineDataSets);
		return mLineData;
	}//
}
