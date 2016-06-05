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
				//ͼ����ߵ�Y�������
				YAxis mYAxis=mLineChart.getAxisLeft();
				//Y����������
				mYAxis.setAxisMaxValue(500.0f);
				mYAxis.setAxisMinValue(0.0f);
				//��һ��Ҫ��0��ʼ
				mYAxis.setStartAtZero(false);
				setChartStyle(mLineChart,mLineData);
				//ȡ��һ��֮�ڵĵڼ�����
				break;
			case REQUESTNOTFOUND: 
				Toast.makeText(getApplicationContext(), "������Դ������", 0).show();
				break;
			case REQUESTEXCEPTION: 
				Toast.makeText(getApplicationContext(), "������æ ���Ժ�....", 1)
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
		System.out.println("���ٶ�id=====" + id);
		
		
		showidsp.setText(id);
		initData();
	}//

	private void initData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				try {//
					String path =" http://192.168.2.150:8000/GET/accleration?id="+id;
					System.out.println("��ȡ���ٶ�����---"+path);
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
		// �Ƿ�����������ӱ߿�
		mLineChart.setDrawBorders(false);
		// ��������
		mLineChart.setDescription("�˶����ٶ�ʾ��ͼ");
		// �������û�����ݣ���ͻῴ��ʲô����
		mLineChart.setNoDataTextDescription("������ʱû������");
		// ��������ͼ�ı���
		mLineChart.setDrawGridBackground(false);
		// ���ô���
		mLineChart.setTouchEnabled(true);
		// ������ק
		mLineChart.setDragEnabled(true);
		// ��������
		mLineChart.setScaleEnabled(true);
		mLineChart.setPinchZoom(false);
		// xy��ı���
		mLineChart.setBackgroundColor(Color.BLUE);
		// xy������
		mLineChart.setData(mLineData);
		// ����ͼ�ꡣ��y��value ,legendΪͼ�ı���
		Legend mLegend = mLineChart.getLegend();
		mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
		// ��ʽ
		mLegend.setForm(LegendForm.LINE);
		// ����
		mLegend.setFormSize(20.0f);
		//����X��֮��Ŀ�϶ ------------------------------------------------------------
        mLegend.setXEntrySpace(1);
       //----------------------------------------------------------------------
		// ����������ɫ
		mLegend.setTextColor(Color.RED);
		// ����x��Ķ���
		mLineChart.animateX(15000);
		// ˢ��ͼ��  ------------------------------------------------
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
		        String sd = sdf.format(new Date(a*1000));   // ʱ���ת����ʱ��
		        Datadatetime.add(Integer.parseInt(sd.substring(13)));
			   Datadata.add(sensor.getAcceleration());
				/*long a=sensor.getTime();
				 
		       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmmss");
		       String sd = sdf.format(new Date(a*1000));   // ʱ���ת����ʱ��

		       Datadatetime.add(Integer.parseInt(sd.substring(13,15)));
		       Datadatetimemiao.add(Integer.parseInt(sd.substring(13,15)));
			   Datadata.add(sensor.getAcceleration());*/
			}
		} catch (Exception e) {
			System.out.println("yichang....");
			e.printStackTrace();
		}
		/*
		 * ������Ǵӷ�������ȡ���ݲ��Ҵ���
		 * 
		 * 
		 * 
		 */
		ArrayList<Integer> Datadatetime1=new ArrayList<Integer>();
		ArrayList<String> xArrayList = new ArrayList<String>();
		// x�������
		for (int j = 0; j <Datadatetime.size(); j++) {
			Datadatetime1.add(Datadatetime.get(j)-Datadatetime.get(0)+1);
			xArrayList.add("��"+Datadatetime1.get(j)+"��");
		}
		// y�������
		System.out.println("dssgdsgdsdydgwygfydgfydgfyds");
		ArrayList<Entry> yArrayList = new ArrayList<Entry>();
		for (int j = 0; j <Datadata.size(); j++) {

			Entry entry=new Entry(Datadata.get(j), j);
			   yArrayList.add(entry);
		}
		System.out.println("y�������������");
		/*ArrayList<Entry> yArrayList1 = new ArrayList<Entry>();
		for (int j = 0; j < 30; j++) {

			Entry entry1 = new Entry(2f + j * 3f + 5 * 2f, j * 2);
			yArrayList1.add(entry1);
		}*/
		// y������ݼ�
		LineDataSet set = new LineDataSet(yArrayList, "���ٶȺϳ�ͼ");
	//	LineDataSet set1 = new LineDataSet(yArrayList1, "��ɫ����x");
		// ��y��ļ��������ò���
		// �߿�
		set.setLineWidth(2.0f);
		//set1.setLineWidth(2.0f);
		// ��ʾԲ�δ�С
		set.setCircleSize(0.05f);
	//	set1.setCircleSize(0.05f);
		// ���ߵ���ɫ
		set.setColor(Color.RED);
		//set1.setColor(Color.BLUE);
		// Բ�����ɫ
		set.setCircleColor(Color.RED);
		// ����ʮ�ֽ�����ݺ���
		// set.setDrawHighlightIndicators(true);
		// ʮ�ֽ����ߵ���ɫ
		set.setHighLightColor(Color.BLUE);
		//set1.setHighLightColor(Color.BLUE);
		// ������ʾ���ݵ������С
		set.setValueTextSize(10.0f);
	//	set1.setValueTextSize(10.0f);
		ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
		mLineDataSets.add(set);
	//	mLineDataSets.add(set1);
		LineData mLineData = new LineData(xArrayList, mLineDataSets);
		return mLineData;
	}//
}
