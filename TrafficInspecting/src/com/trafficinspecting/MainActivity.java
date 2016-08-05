package com.trafficinspecting;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slidingmenu.SlidingMenu;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements OnClickListener,OnItemClickListener{
	private ImageButton bt_switch; //切换至侧滑菜单的按钮 
	private SlidingMenu mMenu; //侧滑菜单
	private ListView interface_lv; //主页面的Listview控件
	private SimpleAdapter sadatper; //用simpleadapter适配器适配主页面的listview
	private List<Map<String,Object>>interface_dataList; //用来装数据源的datalist
	private ListView slidemenu_lv1;
	private ListView slidemenu_lv2;
	private ListView slidemenu_lv3;
	private TextView total_tv;
	private TextView used_tv;
	private TextView rest_tv;
	public static String total = "0";
	public static String  used = "0";
	public static String  rest = "0";
	public long total_long = 0;
	static long used_long;
	static long rest_long;
	static long ll = 0;
	private ArrayAdapter<String>arr_adapter1;
	private ArrayAdapter<String>arr_adapter2;
	private ArrayAdapter<String>arr_adapter3;
	String[] arr_data1 ={"硬件信息","网络测速"}; 
	String[] arr_data2 ={"设置提醒","流量消耗趋势"};
	String[] arr_data3 ={"关于助手","我要反馈"};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	//透明标题栏
    	
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        
 
        bt_switch=(ImageButton) findViewById(R.id.toSideMenu);
        mMenu = (SlidingMenu) findViewById(R.id.leftmenu);
        interface_lv = (ListView) findViewById(R.id.interface_listview);
        slidemenu_lv1 = (ListView) findViewById(R.id.slidingmenu_lv1);
        slidemenu_lv2 = (ListView) findViewById(R.id.slidingmenu_lv2);
        slidemenu_lv3 = (ListView) findViewById(R.id.slidingmenu_lv3);
        total_tv = (TextView) findViewById(R.id.total_amount);
        used_tv = (TextView) findViewById(R.id.used_amount);
        rest_tv = (TextView) findViewById(R.id.unused_amount);
        //考虑开关机getMobileTx(Rx)Bytes()会清空
      
        total_long = Integer.parseInt(total);
        ll = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
        used_long = ll - Integer.parseInt(used);
        rest_long = total_long - used_long;

      //当数据较大时单位转换成M,G
        total_tv.setText(Formatter.formatFileSize(getApplicationContext(), total_long));
        used_tv.setText(Formatter.formatFileSize(getApplicationContext(), used_long));
        rest_tv.setText(Formatter.formatFileSize(getApplicationContext(), rest_long));
        
        interface_dataList = new ArrayList<Map<String,Object>>();
        arr_adapter1 = new ArrayAdapter<String>(this,R.layout.slidingmenu_listview_item,R.id.slidingmenu_tv,arr_data1);
        arr_adapter2 = new ArrayAdapter<String>(this,R.layout.slidingmenu_listview_item,R.id.slidingmenu_tv,arr_data2);
        arr_adapter3 = new ArrayAdapter<String>(this,R.layout.slidingmenu_listview_item,R.id.slidingmenu_tv,arr_data3);
        
       bt_switch.setOnClickListener(this);

       slidemenu_lv1.setAdapter(arr_adapter1);
       slidemenu_lv2.setAdapter(arr_adapter2);
       slidemenu_lv3.setAdapter(arr_adapter3);
       
       //simpleadapter适配器的设置
       sadatper = new SimpleAdapter(this,getData(),R.layout.interface_item,new String[]{"imageview","textview"},new int[]{R.id.interface_item_img,R.id.interface_item_tv});
       interface_lv.setAdapter(sadatper); //给listview加载适配器
       interface_lv.setOnItemClickListener(this); //给listview的item设置监听事件
       slidemenu_lv1.setOnItemClickListener(this);
       slidemenu_lv2.setOnItemClickListener(this);
       slidemenu_lv3.setOnItemClickListener(this);
    }
    
    //放数据进interface_dataList
    private List<? extends Map<String,Object>> getData() {
		// TODO Auto-generated method stub
    	Map<String,Object>map1 = new HashMap<String, Object>();  //每放完一组数据都要重新声明HashMap
    	map1.put("imageview",R.drawable.jktj);
    	map1.put("textview","监控统计");
    	interface_dataList.add(map1);
    	
    	Map<String,Object>map2 = new HashMap<String, Object>();  //每放完一组数据都要重新声明HashMap
    	map2.put("imageview",R.drawable.sz);
    	map2.put("textview","设置");
    	interface_dataList.add(map2);
    	
    	Map<String,Object>map3 = new HashMap<String, Object>();  //每放完一组数据都要重新声明HashMap
    	map3.put("imageview",R.drawable.rl);
    	map3.put("textview","每日清单");
    	interface_dataList.add(map3);
    	
		return interface_dataList;
	}


	//按钮的监听事件
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		
		case R.id.toSideMenu:
			mMenu.toggle();
			break;
		}
		
	}
	
	
	public String Compute_rest(String total,String used){
		int total1 = Integer.parseInt(total);
		int used1 = Integer.parseInt(used);
		int rest1= total1 - used1;
		return String.valueOf(rest1); 
	}
	//记住int和String互转的方法，注意不能用Integer.toHexString();
	
	
   //主页面listview的item的监听事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(parent.getId()==R.id.interface_listview){
		if(position == 0){
			Intent intent = new Intent(MainActivity.this,com.statistics.Statistics.class);
			startActivity(intent);
		}else if(position == 1){
			Intent intent = new Intent(MainActivity.this,com.setting.Setting.class);
			startActivity(intent);
		}
		
		}else if(parent.getId() == R.id.slidingmenu_lv1){
			if(position == 0){
				Intent intent = new Intent(MainActivity.this,com.hardwareinformation.Hardware.class);
				startActivity(intent);
			}else if(position == 1){
				Intent intent = new Intent(MainActivity.this,com.testspeed.TestSpeed.class);
				startActivity(intent);
			}
			
		}else if(parent.getId() == R.id.slidingmenu_lv2){
			
		}else if(parent.getId() == R.id.slidingmenu_lv3){
		if(position == 0){
			Intent intent = new  Intent(MainActivity.this,com.aboutme.AboutMe.class);
			startActivity(intent);
		}else if(position == 1){
			Intent intent = new Intent(MainActivity.this,com.feedback.Feedback.class);
			startActivity(intent);
		}
		}else ;
	}
	
}