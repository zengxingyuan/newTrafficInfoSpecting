package com.testspeed;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class ReadFile {

	public static byte[] getFileFromUrl(String url, NetWorkSpeedInfo netWorkSpeedInfo) {
		int currentByte = 0;
		int fileLength = 0;
		long startTime = 0;
		long intervalTime = 0;  //设置间隔时间

		byte[] b = null;     //定义字节类型数组，最大是127,最小是-128

		int bytecount = 0;
		URL urlx = null;
		URLConnection con = null; //抽象类 URLConnection 是所有表示应用程序与 URL 之间通信链路的类的超类。
		InputStream stream = null;
		try {
			Log.d("URL:", url);
			urlx = new URL(url);
			con = urlx.openConnection();
			con.setConnectTimeout(20000); //设置连接主机超时
			con.setReadTimeout(20000);//设置从主机读取数据超时
			fileLength = con.getContentLength();
			stream = con.getInputStream(); //从连接端口获得输入流
			netWorkSpeedInfo.totalBytes = fileLength;
			b = new byte[fileLength];
			startTime = System.currentTimeMillis();//获得当前系统的时间
			/*	long currentTime = System.currentTimeMillis();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");//把时间格式转换后输出
				Date date = new Date(currentTime);
				System.out.println(formatter.format(date));
			 *
			 */
			while ((currentByte = stream.read()) != -1) {
				netWorkSpeedInfo.hadFinishedBytes++;
				intervalTime = System.currentTimeMillis() - startTime;
				if (intervalTime == 0) {
					netWorkSpeedInfo.speed = 1000;
				} else {
					netWorkSpeedInfo.speed = (netWorkSpeedInfo.hadFinishedBytes / intervalTime) * 1000;
				}
				if (bytecount < fileLength) {
					b[bytecount++] = (byte) currentByte;
				}
			}
		} catch (Exception e) {
			Log.e("exception : ", e.getMessage() + "");
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (Exception e) {
				Log.e("exception : ", e.getMessage());
			}

		}
		return b;
	}

}
