package com.flybean.study.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.flybean.study.R;


public class CircleProgress extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;
	
	/**
	 * 圆环的颜色
	 */
	private int roundColor;
	
	/**
	 * 圆环进度的颜色
	 */
	private int roundProgressColor;
	
	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;
	
	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float textSize;
	
	/**
	 * 圆环的宽度
	 */
	private float roundWidth;
	
	/**
	 * 最大进度
	 */
	private int max;
	
	/**
	 * 当前进度
	 */
	private int progress;
	/**
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable;
	
	/**
	 * 进度的风格，实心或者空心
	 */
	private int style;
	
	public static final int STROKE = 0;
	public static final int FILL = 1;
	
	public CircleProgress(Context context) {
		this(context, null);
	}

	public CircleProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CircleProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		paint = new Paint();

		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CircleProgress);
		
		//获取自定义属性和默认值
		roundColor = mTypedArray.getColor(R.styleable.CircleProgress_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(R.styleable.CircleProgress_roundProgressColor, Color.GREEN);
		textColor = mTypedArray.getColor(R.styleable.CircleProgress_textColor, Color.BLACK);
		textSize = mTypedArray.getDimension(R.styleable.CircleProgress_textSize, 15);
		roundWidth = mTypedArray.getDimension(R.styleable.CircleProgress_roundWidth, 5);
		progress = mTypedArray.getInteger(R.styleable.CircleProgress_progress, 50);
		max = mTypedArray.getInteger(R.styleable.CircleProgress_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(R.styleable.CircleProgress_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.CircleProgress_style, 0);
		
		mTypedArray.recycle();
	}
	

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/**
		 * 画最外层的大圆环
		 */
		int centre = getWidth() / 2; // 获取圆心的x坐标
		int radius = (int) (centre - roundWidth); // 圆环的半径
		paint.setColor(roundColor); // 设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); // 设置空心
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setAntiAlias(true); // 消除锯齿
		canvas.drawCircle(centre, centre, radius, paint); // 画出圆环

		/**
		 * 画进度百分比
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
		int percent = (int) (((float) progress / (float) max) * 100); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
		float textWidth = paint.measureText("" + percent); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		if (textIsDisplayable && style == STROKE) {
			canvas.drawText("" + percent, centre - textWidth / 2, centre + textSize / 2 + radius / 6, paint); // 画出进度百分比
		}
		
		Bitmap bp = null;
		if (progress==0) {
			bp = BitmapFactory.decodeResource(getResources(), R.drawable.sound_mute);
		} else {
			bp = BitmapFactory.decodeResource(getResources(), R.drawable.sound_icon);
		}
		
		Rect src = new Rect(0, 0, bp.getWidth(), bp.getHeight());
		Rect dst = new Rect((int)(centre - radius / 3), 
	            			(int)(centre - radius / 2 - radius / 3), 
	            			(int)(centre + radius / 3), 
	            			(int)(centre - radius / 2 + radius / 3));
		canvas.drawBitmap(bp, src, dst, paint);

		/**
		 * 画圆弧 ，画圆环的进度
		 */

		// 设置进度是实心还是空心
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setColor(roundProgressColor); // 设置进度的颜色
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

		switch (style) {
		case STROKE: {
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 270.f - 360.0f * progress / max, 360.0f
					* progress / max, false, paint); // 根据进度画圆弧
			break;
		}
		case FILL: {
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0)
				canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // 根据进度画圆弧
			break;
		}
		}

	}
	
	
	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 获取进度.需要同步
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}
		
	}
	
	
	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}



}
