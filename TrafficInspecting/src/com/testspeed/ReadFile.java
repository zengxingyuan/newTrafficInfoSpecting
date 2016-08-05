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
		long intervalTime = 0;  //���ü��ʱ��

		byte[] b = null;     //�����ֽ��������飬�����127,��С��-128

		int bytecount = 0;
		URL urlx = null;
		URLConnection con = null; //������ URLConnection �����б�ʾӦ�ó����� URL ֮��ͨ����·����ĳ��ࡣ
		InputStream stream = null;
		try {
			Log.d("URL:", url);
			urlx = new URL(url);
			con = urlx.openConnection();
			con.setConnectTimeout(20000); //��������������ʱ
			con.setReadTimeout(20000);//���ô�������ȡ���ݳ�ʱ
			fileLength = con.getContentLength();
			stream = con.getInputStream(); //�����Ӷ˿ڻ��������
			netWorkSpeedInfo.totalBytes = fileLength;
			b = new byte[fileLength];
			startTime = System.currentTimeMillis();//��õ�ǰϵͳ��ʱ��
			/*	long currentTime = System.currentTimeMillis();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy��-MM��dd��-HHʱmm��ss��");//��ʱ���ʽת�������
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
