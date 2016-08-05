package com.trafficinspecting;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

public class TrafficInfoProvider {
	private PackageManager packagemanager;
	/*PackageManager��Ҫ�ǹ���Ӧ�ó�����������Ի�ȡӦ�ó������Ϣ
	 * ��getPackageManager()���Ի��һ��PackageManager�Ķ���
	 * ��Ҫ����:(�����õ���)
	 * 1.getApplicationIcon(String packageName)--���ظ���������ͼ�꣬���򷵻�null
	 * 2.getApplicationInfo(String packageName,int flags):flags:��Ӧ���Ǵ�flags��ǣ�ͨ��ֱ�Ӹ�0����  --���ظ�applicationinfo����
	 * 3.getInstalledApplications(int flags)flagsһ��ΪGET_UNINSTALLED_PACKAGES,��ʱ�᷵������ApplicationInfo,���ǿ��Զ�ApplicationInfo��flags���ˣ��õ�������Ҫ�ġ� --���ظ�������������ApplicationInfo
	 * 4.getInstalledPackages(int flags)--���ظ�������������PackageInfo
	 * */
	private Context context;  //����ΪʲôҪ����context
	public TrafficInfoProvider(Context context){
		this.context = context;
		packagemanager =  context.getPackageManager();
	}
	
	//�������е��л���������Ȩ�޵�Ӧ�ó����������Ϣ��
	public List<TrafficInfo> getTrafficInfos(){
		//��ȡ����Ȩ����Ϣ��Ӧ�ó���
		List<PackageInfo> packageinfos = packagemanager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		//��ž���INTERNETȨ����Ϣ��Ӧ��
		List<TrafficInfo> trafficinfos = new ArrayList<TrafficInfo>();
		for(PackageInfo packageinfo : packageinfos){
			//��ȡ��Ӧ�õ�����Ȩ����Ϣ
			String[] permissions = packageinfo.requestedPermissions;
			if(permissions != null&&permissions.length>0){
				for(String permission : permissions){
					//ɸѡ������INTERNETȨ�޵�Ӧ�ó���
					if("android.permission.INTERNET".equals(permission)){
						//��װ����InternetȨ�޵�Ӧ�ó�����Ϣ
						TrafficInfo trafficinfo = new TrafficInfo();
						trafficinfo.setPackName(packageinfo.packageName);
						trafficinfo.setAppName(packageinfo.applicationInfo.loadLabel(packagemanager).toString());
						trafficinfo.setIcon(packageinfo.applicationInfo.loadIcon(packagemanager));
						//��ȡ��Ӧ�õ�uid(user id)
						int uid = packageinfo.applicationInfo.uid;
						//TrafficStats����ͨ��Ӧ�õ�uid����ȡӦ�õ����ء��ϴ�������Ϣ  
                        trafficinfo.setRx(TrafficStats.getUidRxBytes(uid));  
                        trafficinfo.setTx(TrafficStats.getUidTxBytes(uid));  
                        trafficinfos.add(trafficinfo);  
                        trafficinfo = null;  
                        break;
					}
				}
			}
		}
		return trafficinfos;
	}
	
}
