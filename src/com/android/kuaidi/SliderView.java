package com.android.kuaidi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

//显示在右侧的字母索引View
public class SliderView extends View{
	//触摸事件
	OnTouchingLettersChangedListener onTouchingLettersChangedListener;
	//26个字母
	public static String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I",  
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
        "W", "X", "Y", "Z", "#"};
	private int chooseIndex = -1; //选中索引
	private Paint mPaint = new Paint();
	private TextView mTextDialog;
	
	public interface OnTouchingLettersChangedListener {
		public void onTouchingLettersChanged(String letters);
	}
	public void setOnTouchingLettersChangedListener(
			OnTouchingLettersChangedListener onTouchingLettersChangedListener) {
		this.onTouchingLettersChangedListener = onTouchingLettersChangedListener;
	}
	
	public SliderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public SliderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	public void setTextView(TextView textDialog) {
		this.mTextDialog = textDialog;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height/LETTERS.length; //每一个字母的高度
		
		for (int i = 0; i < LETTERS.length; i++) {
			mPaint.setColor(Color.rgb(0, 0, 0));
			mPaint.setTypeface(Typeface.DEFAULT_BOLD);
			mPaint.setAntiAlias(true);
			mPaint.setTextSize(22);
			
			if(i == chooseIndex) { //选中时
				mPaint.setColor(Color.RED);
				mPaint.setFakeBoldText(true);
			}
			float posX = width/2 - mPaint.measureText(LETTERS[i])/2;
			float posY = singleHeight*i + singleHeight;
			
			canvas.drawText(LETTERS[i], posX, posY, mPaint);
			mPaint.reset();
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		final int action = event.getAction();
		final int oldChooseIndex = chooseIndex;
		final float posY = event.getY(); //按下时Y点坐标
		final OnTouchingLettersChangedListener listener = onTouchingLettersChangedListener;
		final int curIndex = (int)(posY/getHeight()*LETTERS.length); //当前的索引
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			chooseIndex = -1;
			invalidate();
			if(mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			if(oldChooseIndex != curIndex 
				&& curIndex >=0 
				&& curIndex < LETTERS.length) {
				if(listener != null) {
					listener.onTouchingLettersChanged(LETTERS[curIndex]);
				}
				if(mTextDialog != null) {
					mTextDialog.setText(LETTERS[curIndex]);
					mTextDialog.setVisibility(View.VISIBLE);					
				}
				chooseIndex = curIndex;
				invalidate();
			}
			break;
		}
		
		
		return true;
	}
}
