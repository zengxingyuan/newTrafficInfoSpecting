package com.setting;

import com.trafficinspecting.MainActivity;
import com.trafficinspecting.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setting extends ActionBarActivity implements OnClickListener{
	private EditText Total_et;
	private EditText Used_et;
	private Button bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		Total_et = (EditText) findViewById(R.id.setting_total);
		Used_et = (EditText) findViewById(R.id.setting_used);
		bt = (Button) findViewById(R.id.setting_bt);
		
		bt.setOnClickListener(this);
		
		if(!MainActivity.total.equals("0")){
			Total_et.setText(MainActivity.total);
			Log.i("hhh","hello");
			Used_et.setText(MainActivity.used);
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		MainActivity.total = Total_et.getText().toString();
		MainActivity.used = Used_et.getText().toString();
		Total_et.setText(MainActivity.total);
		Used_et.setText(MainActivity.used);
		Toast.makeText(this,"…Ë÷√≥…π¶!",Toast.LENGTH_LONG).show();
		Intent intent =  new Intent(Setting.this,com.trafficinspecting.MainActivity.class);
		startActivity(intent);
		
	}

}
