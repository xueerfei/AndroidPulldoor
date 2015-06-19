package com.manymore13.scrollerdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class PullDoorView extends RelativeLayout {

	private Context mContext;
	private MainActivity mActivity;
	private Scroller mScroller;

	private int mScreenWidth = 0;

	private int mScreenHeigh = 0;

	private int mLastDownY = 0;

	private int mCurryY;

	private int mDelY;

	public boolean mCloseFlag = false;

	private ImageView mImgView;
	private int bg_index;
	//��ӭҳ�ı�����ͨ��bg_index������ѡ��
	int []mBackgroundIndex= {R.drawable.bg1,R.drawable.liuyifei1,R.drawable.liuyifei2};
	
	public PullDoorView(Context context) {
		super(context);
		mContext = context;
		mActivity=(MainActivity)context;
		setupView();
	}

	public PullDoorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mActivity=(MainActivity)context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolBar);  
		bg_index=typedArray.getInt(R.styleable.ToolBar_bgIndex, 0);
		setupView();
	}

	@SuppressLint("NewApi")
	private void setupView() {
		// ���Interpolator��������ñ�� ������ѡ������е���Ч����Interpolator
		Interpolator polator = new BounceInterpolator();
		mScroller = new Scroller(mContext, polator);
		// ��ȡ��Ļ�ֱ���
		WindowManager wm = (WindowManager) (mContext.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mScreenHeigh = dm.heightPixels;
		mScreenWidth = dm.widthPixels;
		// ������һ��Ҫ���ó�͸������,��Ȼ��Ӱ���㿴���ײ㲼��
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		mImgView = new ImageView(mContext);
		mImgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		mImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);// ���������Ļ
		mImgView.setImageResource(mBackgroundIndex[bg_index]); // ѡ�񱳾�
		addView(mImgView);
	}

	// �����ƶ��ű���
	public void setBgImage(int id) {
		mImgView.setImageResource(id);
	}

	// �����ƶ��ű���
	public void setBgImage(Drawable drawable) {
		mImgView.setImageDrawable(drawable);
	}

	// �ƶ��ŵĶ���
	public void startBounceAnim(int startY, int dy, int duration) {
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastDownY = (int) event.getY();
			return true;
		case MotionEvent.ACTION_MOVE:
			mCurryY = (int) event.getY();
			mDelY = mCurryY - mLastDownY;
			if (mDelY < 0) {//�ϻ���Ч,�ѵ�ǰView���ϻ���
				scrollTo(0, -mDelY); 
			}else{//������»��ľ��볬����1/3,����һ��ҳ��Ū����
				if(this.getScrollY()==0 && mDelY>mScreenHeigh / 3){
					mActivity.showView(false);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			mCurryY = (int) event.getY();
			mDelY = mCurryY - mLastDownY;
			if (mDelY < 0) {
				if (Math.abs(mDelY) > mScreenHeigh / 2) {
					// ���ϻ������������Ļ�ߵ�ʱ�� ����������ʧ����
					startBounceAnim(this.getScrollY(), mScreenHeigh, 450);
					mCloseFlag = true;//��ʾ��Ҫ�رմ�ҳ��
				} else {
					// ���ϻ���δ���������Ļ�ߵ�ʱ�� �������µ�������
					startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);
				}
			}else{//������ԭ����û����䣬����ʱ���֣���������������ʱ�򣬻����޷�����ȫ��Ļ����������������ǶԵײ�����Marging����Ҫ����һ��
				startBounceAnim(this.getScrollY(), -this.getScrollY(), 450);
			}

			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void computeScroll() {

		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// ��Ҫ���Ǹ��½���
			postInvalidate();
		} else {
			if (mCloseFlag) {//�رմ�ҳ�棬
				this.setVisibility(View.GONE);
				mActivity.showView(true);//��ʱ�Ѿ����뵽�µ�view
			}
		}
	}

}
