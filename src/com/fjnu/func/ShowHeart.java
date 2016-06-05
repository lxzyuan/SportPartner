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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fjnu.domain.Heart;
import com.fjnu.domain.Step;
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

public class ShowHeart extends Activity {
	protected final int REQUESTSUCESS = 0; 
	protected final int REQUESTNOTFOUND = 1;
	protected final int REQUESTEXCEPTION = 2;
	double playerwight =  60.000;
	String id;
	//
	TextView showstep;
	TextView showkall;
	TextView showid;
	List<Heart> dataHeart;
	static List<Step> dataStep;
	private Button heart_sendButton;
	private Button heart_stopButton;
	int stepcount = 1;
	private Handler handler = new Handler() {
		@SuppressLint("ShowToast")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REQUESTSUCESS: 

				String content = (String) msg.obj;
				try {
					dataHeart = parseJSON(content);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LineChart mLineChart=(LineChart) findViewById(R.id.heart_linechart);
				LineData mLineData=LineData(60);
				XAxis mXAxis=mLineChart.getXAxis();
				mXAxis.setPosition(XAxisPosition.BOTTOM);
				//ͼ����ߵ�Y�������..
				YAxis mYAxis=mLineChart.getAxisLeft();
				//Y����������
				mYAxis.setAxisMaxValue(150.0f);
				mYAxis.setAxisMinValue(30.0f);
				//��һ��Ҫ��0��ʼ//
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
	
	public static int parseStepjSON(String json) throws Exception {
		int stepcount1 = 0;
		List<Step> datastep = new ArrayList<Step>();

		JSONArray array = new JSONArray(json);
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			Step dataStep = new Step(jsonObject.getInt("step"));
			datastep.add(dataStep);
			System.out.println("111");
		}
			for (Step sensorstep: datastep) {
				stepcount1 = sensorstep.getStep();
			}
		return stepcount1;
	}
	
	public static  List<Heart> parseJSON(String json) throws Exception{
		List<Heart> datas = new ArrayList<Heart>();
		
		JSONArray array = new JSONArray(json);
		for(int i = 0 ; i < array.length() ; i++){
			JSONObject jsonObject = array.getJSONObject(i);
			Heart dataHeart = new Heart(jsonObject.getInt("datetime"),jsonObject.getInt("data"));
			datas.add(dataHeart);
		}
		return datas;
	}
	private void initShack(){
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try{
					//String id = "7001";
					String shack = "1";
					String path = "http://192.168.2.150:8000/STATUE?id=" + id + "&" + "shake=" + shack;
					System.out.println("��ʼ��URL ----"+path );
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					System.out.println("staartcodecodecode"+code);
					System.out.println("�򿪳ɹ�");
					System.out.println("path"+path);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}.start();
	}
	
	private void initStopshake(){
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try{
					//String id = "7001";
					String shack = "0";
					String path = "http://192.168.2.150:8000/STATUE?id=" + id + "&" + "shake=" + shack;
					System.out.println("ֹͣ��URL ----"+path );
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					System.out.println("codecodecode"+code);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}.start();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.heartrate);
		heart_sendButton=(Button) findViewById(R.id.heart_sendButton);
		heart_stopButton=(Button) findViewById(R.id.heart_stopButton);
		showstep = (TextView) findViewById(R.id.showstep);
		showkall = (TextView) findViewById(R.id.showkall);
		showid = (TextView) findViewById(R.id.showid);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		
		showid.setText(id);
		initStepdata();  
			
		
		//--------------------------------------------------
		
		initData();
		heart_sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initShack();
			}
		});
		heart_stopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initStopshake();
			}
		});
		/*LineData mLineData=LineData(60);
		XAxis mXAxis=mLineChart.getXAxis();
		mXAxis.setPosition(XAxisPosition.BOTTOM);
		//ͼ����ߵ�Y�������
		YAxis mYAxis=mLineChart.getAxisLeft();
		//Y����������
		mYAxis.setAxisMaxValue(200.0f);
		mYAxis.setAxisMinValue(-200.0f);
		//��һ��Ҫ��0��ʼ
		mYAxis.setStartAtZero(false);
		setChartStyle(mLineChart,mLineData);
		//ȡ��һ��֮�ڵĵڼ�����
*/

	}
	
	
	private  void initStepdata(){
		
		// TODO Auto-generated method stub
				new Thread() {
					public void run() {

						try {
							//String id = "7001";
							
							String path = "http://192.168.2.150:8000/GET/step?id="+id;
							System.out.println("��ȡ����URL ----"+path );
							URL url = new URL(path);
							HttpURLConnection conn = (HttpURLConnection) url
									.openConnection();
							conn.setRequestMethod("GET");
							conn.setConnectTimeout(5000);
							int code = conn.getResponseCode();
							if (code == 200) {
								InputStream in = conn.getInputStream();
								byte[] data1 = StreamTool.read(in);
								String json = new String(data1);
								stepcount = parseStepjSON(json);
								
								showstep.setText(stepcount+"");
								showkall.setText( playerwight*stepcount/1000.0*1.036 + "kcal");
								
								//Message msgstep = new Message();
								/*msgstep.what = REQUESTSUCESSSTEP;
								msgstep.obj = json;
								handler.sendMessage(msgstep);*/

							} /*else {
								Message msg3 = new Message();
								msg3.what = REQUESTNOTFOUND;
								handler.sendMessage(msg3);
							}*/
							

						} catch (Exception e) {
							e.printStackTrace();
							/*Message msg4 = new Message();
							msg4.what = REQUESTEXCEPTION;
							handler.sendMessage(msg4); */
						}
					};
				}.start();
		
	}
	private void initData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				try {
					//String id = "7001";
					String path = "http://192.168.2.150:8000/GET/Beating?id=" + id;
					System.out.println("��ȡ����URL ----"+path );
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

	

	private void setChartStyle(LineChart mLineChart, com.github.mikephil.charting.data.LineData mLineData) {
		// TODO Auto-generated method stub
		//�Ƿ�����������ӱ߿�
				mLineChart.setDrawBorders(false);
				//��������
				mLineChart.setDescription("����ʾ��ͼ");
				//�������û�����ݣ���ͻῴ��ʲô����
				mLineChart.setNoDataTextDescription("������ʱû������");
				//��������ͼ�ı���
				mLineChart.setDrawGridBackground(false);
				//���ô���
				mLineChart.setTouchEnabled(true);
				//������ק
				mLineChart.setDragEnabled(true);
				//��������
				mLineChart.setScaleEnabled(true);
				mLineChart.setPinchZoom(false);
				//xy��ı���
				mLineChart.setBackgroundColor(Color.YELLOW);
				//xy������
				mLineChart.setData(mLineData);
				//����ͼ�ꡣ��y��value  ,legendΪͼ�ı���
				Legend mLegend=mLineChart.getLegend();
				mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
				//��ʽ
				mLegend.setForm(LegendForm.LINE);
				//����
				mLegend.setFormSize(50.0f);
				
				//����������ɫ
				mLegend.setTextColor(Color.RED);
				//����x��Ķ���
				mLineChart.animateX(15000);
	}

	private LineData LineData(int i) {
		// TODO Auto-generated method stub
		ArrayList<Integer> Datadatetime=new ArrayList<Integer>();
		ArrayList<Integer> Datadata=new ArrayList<Integer>();
		try {
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (Heart sensor : dataHeart) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("datetime", sensor.getDatetime());
				item.put("data", sensor.getData());				
				data.add(item);
				 long a=sensor.getDatetime();
				 
			        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HHmmss");
			        String sd = sdf.format(new Date(a*1000));   // ʱ���ת����ʱ��
			        Datadatetime.add(Integer.parseInt(sd.substring(13, 15)));
				   Datadata.add(sensor.getData());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * ������Ǵӷ�������ȡ���ݲ��Ҵ�
		 * 
		 * 
		 *
		 */
		ArrayList<Integer> Datadatetime1=new ArrayList<Integer>();
		ArrayList<String> xArrayList=new ArrayList<String>();
		//x�������
		for(int j=0;j<Datadatetime.size();j++){
			Datadatetime1.add(Datadatetime.get(j)-Datadatetime.get(0)+1);
			xArrayList.add("��"+Datadatetime1.get(j)%60+"����");
		}
		
		//y�������
		ArrayList<Entry> yArrayList=new ArrayList<Entry>();
	   for(int j=0;j<Datadata.size();j++){
		
		   Entry entry=new Entry(Datadata.get(j), j);
		   yArrayList.add(entry);
	   }
	   /*ArrayList<Entry> yArrayList1=new ArrayList<Entry>();
	   for(int j=0;j<30;j++){
		
		   Entry entry1=new Entry(2f+j*3f+5*2f, j*2);
		   yArrayList1.add(entry1);
	   }*/
	  //y������ݼ�
	   LineDataSet set=new LineDataSet(yArrayList, "����ͼ");
	  // LineDataSet set1=new LineDataSet(yArrayList1, "��ɫ����x");
	   //��y��ļ��������ò���
	   //�߿�
	   set.setLineWidth(2.0f);
	 //  set1.setLineWidth(2.0f);
	   //��ʾԲ�δ�С
	   set.setCircleSize(0.05f);
	  // set1.setCircleSize(0.05f);
	   //���ߵ���ɫ
	   set.setColor(Color.BLUE );
	  // set1.setColor(Color.BLUE);
	   //Բ�����ɫ
	   set.setCircleColor(Color.RED);
	   //����ʮ�ֽ�����ݺ���
	   //set.setDrawHighlightIndicators(true);
	   //ʮ�ֽ����ߵ���ɫ
	   set.setHighLightColor(Color.BLUE);
	   //set1.setHighLightColor(Color.BLUE);
	   //������ʾ���ݵ������С
	   set.setValueTextSize(10.0f);
	  // set1.setValueTextSize(10.0f);
	   ArrayList<LineDataSet> mLineDataSets=new ArrayList<LineDataSet>();
	   mLineDataSets.add(set);
	  // mLineDataSets.add(set1);
	   LineData mLineData=new LineData(xArrayList,mLineDataSets);
	   return mLineData;
	}
	


	}

