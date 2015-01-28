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
	 * ���ʶ��������
	 */
	private Paint paint;
	
	/**
	 * Բ������ɫ
	 */
	private int roundColor;
	
	/**
	 * Բ�����ȵ���ɫ
	 */
	private int roundProgressColor;
	
	/**
	 * �м���Ȱٷֱȵ��ַ�������ɫ
	 */
	private int textColor;
	
	/**
	 * �м���Ȱٷֱȵ��ַ���������
	 */
	private float textSize;
	
	/**
	 * Բ���Ŀ��
	 */
	private float roundWidth;
	
	/**
	 * ������
	 */
	private int max;
	
	/**
	 * ��ǰ����
	 */
	private int progress;
	/**
	 * �Ƿ���ʾ�м�Ľ���
	 */
	private boolean textIsDisplayable;
	
	/**
	 * ���ȵķ��ʵ�Ļ��߿���
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
		
		//��ȡ�Զ������Ժ�Ĭ��ֵ
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
		 * �������Ĵ�Բ��
		 */
		int centre = getWidth() / 2; // ��ȡԲ�ĵ�x����
		int radius = (int) (centre - roundWidth); // Բ���İ뾶
		paint.setColor(roundColor); // ����Բ������ɫ
		paint.setStyle(Paint.Style.STROKE); // ���ÿ���
		paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
		paint.setAntiAlias(true); // �������
		canvas.drawCircle(centre, centre, radius, paint); // ����Բ��

		/**
		 * �����Ȱٷֱ�
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); // ��������
		int percent = (int) (((float) progress / (float) max) * 100); // �м�Ľ��Ȱٷֱȣ���ת����float�ڽ��г������㣬��Ȼ��Ϊ0
		float textWidth = paint.measureText("" + percent); // ���������ȣ�������Ҫ��������Ŀ��������Բ���м�

		if (textIsDisplayable && style == STROKE) {
			canvas.drawText("" + percent, centre - textWidth / 2, centre + textSize / 2 + radius / 6, paint); // �������Ȱٷֱ�
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
		 * ��Բ�� ����Բ���Ľ���
		 */

		// ���ý�����ʵ�Ļ��ǿ���
		paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
		paint.setColor(roundProgressColor); // ���ý��ȵ���ɫ
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���

		switch (style) {
		case STROKE: {
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 270.f - 360.0f * progress / max, 360.0f
					* progress / max, false, paint); // ���ݽ��Ȼ�Բ��
			break;
		}
		case FILL: {
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0)
				canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // ���ݽ��Ȼ�Բ��
			break;
		}
		}

	}
	
	
	public synchronized int getMax() {
		return max;
	}

	/**
	 * ���ý��ȵ����ֵ
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * ��ȡ����.��Ҫͬ��
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ��
	 * ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
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
