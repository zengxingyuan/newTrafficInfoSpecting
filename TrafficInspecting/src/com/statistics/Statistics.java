package com.statistics;



import java.util.List;
import com.trafficinspecting.R;
import com.trafficinspecting.TrafficInfo;
import com.trafficinspecting.TrafficInfoProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Statistics extends ActionBarActivity {
	private ListView lv; //չ�������б�
	private TrafficInfoProvider provider; //��ȡ���о���INTERNEȨ�޵�Ӧ�õ�������Ϣ
	private List<TrafficInfo> trafficInfos;//��װ��������INTERNETȨ�޵�Ӧ�õ�������Ϣ
    private TrafficAdapter tadapter =new TrafficAdapter();
	/*Handler��ʶ��
	 * ��Ӧ�ó�������ʱ��Android���ȻῪ��һ�����̣߳�UI�̣߳������߳�Ϊ��������е�UI�ؼ��������¼��ַ�
	 * ����:��Ҫ�������̷߳��͵����ݣ����ô�����������̸߳���UI���������߳�ͨ��Message���󴫵����ݣ����߳���sendMessage()�������ݣ���
	 * */
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			lv.setAdapter(tadapter);
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.statistics);
		
		 lv=(ListView) findViewById(R.id.listview);
		 provider = new TrafficInfoProvider(this);
	       new Thread(){
	    	   public void run(){
	    		   trafficInfos = provider.getTrafficInfos();     //trafficInfos�а��������з��صľ���INTETNET����Ȩ�޵�Ӧ��
	    		   //�����̷߳���һ������Ϣ������֪ͨ���̸߳�������
	    		   handler.sendEmptyMessage(0);
	    	   };
	       }.start();
	       
	    }
	/*�����Զ���adapter
     * �̳���SimpleAdapter,���帴д����������4�֣�
     * getCount():����ListView��Ҫ��ʾ����view����
     * getItem():����һ����view��Ŀ��Ҳ�����Զ��巵������Ҫ����Ϣ
     * getItemId():����ListView�е�λ�÷���id
     * getView():���������Ŀ��������Ϣ����һ�������Ĳ����ļ�
     * 
     */

	
	
	public class TrafficAdapter extends BaseAdapter {

		@Override
		//�õ���view����Ŀ��
		public int getCount() {
			return trafficInfos != null? trafficInfos.size():0;
		}
		//����ListViewλ�÷���View
		@Override
		public Object getItem(int arg0) {
		 return trafficInfos.get(arg0);

		}
		//����listviewλ�õõ�list�е�ID
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		//����λ�õõ�View����ListView�ж��ٸ�Item�͵��ö��ٴ�
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder = new ViewHolder();
			TrafficInfo info = trafficInfos.get(position);
			//���û����view
			if(convertView == null){
				/*inflate������
				 *  inflate()�����ǽ�xml�����һ�������ҳ����γ�һ��view���󣬵������صģ�����Ҫʱ��setContentView(View)��ʾ������ 
				 * */
				view = View.inflate(getApplicationContext(),R.layout.item, null);
				holder.iv_icon = (ImageView) view.findViewById(R.id.icon);
				holder.tv_name = (TextView) view.findViewById(R.id.appname);
				holder.tv_rx = (TextView) view.findViewById(R.id.downtext);
				holder.tv_tx = (TextView) view.findViewById(R.id.uptext);
				holder.tv_total = (TextView) view.findViewById(R.id.total);
				view.setTag(holder);
				/*View�е�setTag(Object)��ʾ��View���һ����������ݣ��Ժ�Ҫ�ÿ�����getTag()���������ȥȡ����
				 */
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.iv_icon.setImageDrawable(info.getIcon());
			holder.tv_name.setText(info.getAppName());
			long rx = info.getRx();
			long tx = info.getTx();
			//��ǿ����Ľ�׳��
			if(rx<0) rx = 0;
			if(tx<0) tx = 0;
			//�����ݽϴ�ʱ��λת����M,G
			holder.tv_rx.setText(Formatter.formatFileSize(getApplicationContext(),rx));
			holder.tv_tx.setText(Formatter.formatFileSize(getApplicationContext(), tx));
			long total = tx + rx;
			holder.tv_total.setText(Formatter.formatFileSize(getApplicationContext(), total));
		
			return view;
		}

	}
	static class ViewHolder{
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_tx;
		TextView tv_rx;
		TextView tv_total;
	}

	}
	

