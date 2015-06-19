package com.manymore13.scrollerdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button btnBelow, btnAbove,btnAbove1;
	private TextView tvHint;
	private PullDoorView myImage, myImage1;
	private int mCurrentIndex = 0;
	private final int mViewSize = 2;
	private PullDoorView[] mWelcomView = new PullDoorView[mViewSize];//两个欢迎页面
	/**************************************************************/
	public void showView(boolean flag) {
		if (flag) {// 打开下一个页面
			if (mCurrentIndex == 0) {
				++mCurrentIndex;
			}
			if (mCurrentIndex == mViewSize - 1) {// 已经是最后一个或者第一个，则只需要开启动画即可
				return;
			} else {// 先关闭当前动画，然后再开启新页面动画
				++mCurrentIndex;
			}
		} else {// 打开上一个页面
			if (mCurrentIndex == 0) {// 已经是第一个，不能再回退了
				return;
			}//如果上一页还有view，则显示上一页的内容，把上一页的view“砸”下来
			mWelcomView[--mCurrentIndex].setVisibility(View.VISIBLE);
			mWelcomView[mCurrentIndex].mCloseFlag = false;// 不需要关闭
			mWelcomView[mCurrentIndex].startBounceAnim(mWelcomView[mCurrentIndex].getScrollY(),-mWelcomView[mCurrentIndex].getScrollY(), 1000);
		}
	}

	private void initWelcomViewe() {
		myImage = (PullDoorView) findViewById(R.id.myImage);//第二层View
		myImage1 = (PullDoorView) findViewById(R.id.myImage1);//最上面一层View
		mWelcomView[1] = myImage;
		mWelcomView[0] = myImage1;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initWelcomViewe();

		btnBelow = (Button) this.findViewById(R.id.btn_Below);
		btnAbove = (Button) this.findViewById(R.id.btn_above);
		btnAbove1 = (Button) this.findViewById(R.id.btn_above1);
		tvHint = (TextView) this.findViewById(R.id.tv_hint);
		btnBelow.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "这是下面一层按钮", 1000).show();
				myImage.setVisibility(View.VISIBLE);
				myImage.mCloseFlag = false;
				myImage.startBounceAnim(myImage.getScrollY(),-myImage.getScrollY(), 1000);
			}
		});

		btnAbove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "这是上面一层按钮", 1000).show();
				myImage1.setVisibility(View.VISIBLE);
				myImage1.mCloseFlag = false;
				myImage1.startBounceAnim(myImage1.getScrollY(),-myImage1.getScrollY(), 1000);
			}
		});

		btnAbove1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "这是首页按钮", 1000).show();
				
			}
		});
		Animation ani = new AlphaAnimation(0f, 1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		tvHint.startAnimation(ani);
	}

	int lastDownY = 0;

}
