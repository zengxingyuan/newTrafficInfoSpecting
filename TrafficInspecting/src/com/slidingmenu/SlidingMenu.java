package com.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {
	
	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;
	private int mMenuWidth;
	private int menuRightPadding = 100;
	private boolean isopen = false;

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//��ȡ�ֻ���Ļ��С
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
	    mScreenWidth = outMetrics.widthPixels;
		//��dpת��Ϊpx ���COMPLEX_UNIT_SP�Ļ����ǰ�spת��Ϊpx ���Է�װ
	    //���ò˵��ұ߾�
	    menuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,context.getResources().getDisplayMetrics());
		 
	}

	/*�Զ���ViewGroup:
	 * 1.onMeasure:�����ڲ�(��view)�Ŀ�͸ߣ��Լ��Լ��Ŀ�͸�
	 *2.onLayout:������view�ķ��õ�λ��
	 *3.onTouchEvent:�ж���ָ�Ļ���״̬
	 */
	
	 @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWapper = (LinearLayout) getChildAt(0);
		mMenu = (ViewGroup) mWapper.getChildAt(0);
		mContent = (ViewGroup) mWapper.getChildAt(1);
		mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - menuRightPadding;
		mContent.getLayoutParams().width = mScreenWidth;
		mWapper.getLayoutParams().width = mScreenWidth;
	}
	 
	 @Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			// ͨ������ƫ������menu����
			
			super.onLayout(changed, l, t, r, b);
			if(changed){
				this.scrollTo(mMenuWidth,0);
			}
		}
	 
	 public boolean onTouchEvent(MotionEvent ev) {
			int action = ev.getAction();
			switch(action){
			case MotionEvent.ACTION_UP:
				int scrollX = getScrollX();//��������������ز���
				if(scrollX>=mMenuWidth/2){
					this.smoothScrollTo(mMenuWidth, 0);//smoothScrollTo�и�����Ч��
					isopen = false;
				}
				else{
					this.smoothScrollTo(0,0);
					isopen=true;
				}
				return true;
			}
			return super.onTouchEvent(ev);
			
		}
	//�򿪲˵�
		public void openMenu(){
			if(isopen)
				return;
			else{
				this.smoothScrollTo(0, 0);
				isopen = true;
			}
		}
		//�رղ˵�
		public void closeMenu(){
			if(isopen){
				this.smoothScrollTo(mMenuWidth, 0);
				isopen=false;
			}
			else return;
		}
		//�л��˵�
		public void toggle(){
			if(isopen)
				closeMenu();
			else openMenu();
		}
	}

