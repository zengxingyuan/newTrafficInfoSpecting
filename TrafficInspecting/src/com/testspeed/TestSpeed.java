package com.testspeed;

import java.util.ArrayList;
import java.util.List;

import com.trafficinspecting.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TestSpeed extends ActionBarActivity {
private String url = "http://gdown.baidu.com/data/wisegame/baidusearch_Android_10189_1399k.apk"; //�������ٵ����ص�ַ
	

	byte[] imageData = null;
	Button b;
	NetWorkSpeedInfo netWorkSpeedInfo = null;
	private final int UPDATE_SPEED = 1;// ������
	private final int UPDATE_DNOE = 0;// �������
	private ImageView imageView;
	private long begin = 0;
	private Button startButton;
	private TextView connectionType, nowSpeed, avageSpeed;
	long tem = 0;
	long falg = 0;
	long numberTotal = 0;
	List<Long> list = new ArrayList<Long>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testspeed);
		imageView = (ImageView) findViewById(R.id.iv_needle);
		startButton = (Button) findViewById(R.id.start_button);
		connectionType = (TextView) findViewById(R.id.connection_type);
		nowSpeed = (TextView) findViewById(R.id.now_speed);
		avageSpeed = (TextView) findViewById(R.id.average_speed);
		// timer.schedule(task, 1000, 1000);
		netWorkSpeedInfo = new NetWorkSpeedInfo();
		
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				list.clear();
				tem = 0;
				falg = 0;
				numberTotal = 0;
				
				new Thread() {
					@Override
					public void run() {
						Log.i("��ʼ", "**********��ʼ  ReadFile*******");
						imageData = ReadFile.getFileFromUrl(url, netWorkSpeedInfo);
					}
				}.start();

				new Thread() {
					@Override
					public void run() {
						Log.i("��ʼ", "**********��ʼ  netWorkSpeedInfo1*******");
						while (netWorkSpeedInfo.hadFinishedBytes < netWorkSpeedInfo.totalBytes) {
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(UPDATE_SPEED);
						}
						if (netWorkSpeedInfo.hadFinishedBytes == netWorkSpeedInfo.totalBytes) {
							handler.sendEmptyMessage(UPDATE_SPEED);
							netWorkSpeedInfo.hadFinishedBytes = 0;
						}

					}
				}.start();
			}
		});
	}

	protected void startAnimation(double d) {
		AnimationSet animationSet = new AnimationSet(true);
		/**
		 * ǰ��������������ת����ʼ�ͽ����Ķ�������������������Բ�ĵ�λ��
		 */
		// Random random = new Random();
		int end = getDuShu(d);

		Log.i("", "********************begin:" + begin + "***end:" + end);
		RotateAnimation rotateAnimation = new RotateAnimation(begin, end, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
		rotateAnimation.setDuration(1000);
		animationSet.addAnimation(rotateAnimation);
		imageView.startAnimation(animationSet);
		begin = end;
	}

	public int getDuShu(double number) {
		double a = 0;
		if (number >= 0 && number <= 512) {
			a = number / 128 * 15;
		} else if (number > 521 && number <= 1024) {
			a = number / 256 * 15 + 30;
		} else if (number > 1024 && number <= 10 * 1024) {
			a = number / 512 * 5 + 80;
		} else {
			a = 180;
		}
		return (int) a;
	}

	private Handler handler = new Handler() {
		long tem = 0;
		long falg = 0;
		long numberTotal = 0;
		List<Long> list = new ArrayList<Long>();

		@Override
		public void handleMessage(Message msg) {
			int value = msg.what;
			switch (value) {
			case UPDATE_SPEED:
				tem = netWorkSpeedInfo.speed / 1024;
				list.add(tem);
				Log.i("a", "tem****" + tem);
				for (Long numberLong : list) {
					numberTotal += numberLong;
				}
				falg = numberTotal / list.size();
				numberTotal = 0;
				nowSpeed.setText(tem + "kb/s");
				avageSpeed.setText(falg + "kb/s");
				startAnimation(Double.parseDouble(tem+""));
				break;
			default:
				break;
			}
		}
	};

}
