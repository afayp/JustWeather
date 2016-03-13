package com.pfh.app.justweather.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.pfh.app.justweather.R;
import com.pfh.app.justweather.utils.DensityUtils;
import com.pfh.app.justweather.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;


public class WeatherChart extends View {

    private Context mContext;
    //整张图表宽度，高度
    private int mWidth;
    private int mHeight;
    //画笔,画两条线的点,线
    private Paint mPaintColdPoint;
    private Paint mPaintWarmPoint;
    private Paint mPaintColdLine;
    private Paint mPaintWarmLine;
    private Paint mPaintTemptext;
    private Paint mPaintDateText;

    private List<Integer> mDataX;
    //每一个最终的坐标点
    private List<Point> mColdPoints;
    private List<Point> mWarmPoints;


    //临时的y坐标，用于绘制
    private List<Integer> mProgressColdY;
    private List<Integer> mProgressWarmY;

    private List<Integer> mRealColdY;
    private List<Integer> mRealWarmY;


    //底部的日期
    private List<String> mTimeList;

    //margin边上留白的部分
    private int marginTop = 50;
    private int marginBottom = 150;
    private int marginLeft = 100;
    private int marginRight = 100;
    private DisplayMetrics metrics;

    private int times = 0;
    private boolean isFirst;



    public WeatherChart(Context context) {
        this(context, null);
    }

    public WeatherChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public void setData(List<Integer> coldData, List<Integer> warmData) {
//        this.mRealColdY.clear();
//        this.mRealWarmY.clear();
//        this.mRealColdY.addAll(coldData);
//        this.mRealWarmY.addAll(warmData);
//        times = 0;
//        invalidate();
        Log.e("111", "setData执行了！");
        this.mRealColdY = coldData;
        this.mRealWarmY = warmData;
        setPoints();
        Log.e("111", "setData之后的数据：coldY：" + mRealColdY.toString());
        times = 0;
//        isFirst = false;
        Log.e("111", "setData: times " + times);
        //恢复点的起始位置
//        for (int i = 0; i < 5; i++) {
//            mProgressColdY.set(i, mHeight);
//            mProgressWarmY.set(i, mHeight);
//        }
//        postInvalidate();
//        invalidate();
        Log.e("111", "setData: 调用了invalidate！");

    }

    private void initView() {
        Log.e("111", "initView开始！");
        isFirst = true;
        metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
//        marginLeft = dip2px(50);
//        marginRight = dip2px(50);
//        marginTop = dip2px(50);
//        marginBottom = dip2px(50);
        //初始化集合
        mProgressColdY = new ArrayList<>();
        mProgressWarmY = new ArrayList<>();
        mColdPoints = new ArrayList<>();
        mWarmPoints = new ArrayList<>();
        mDataX = new ArrayList<>();
        mRealColdY = new ArrayList<>();
        mRealWarmY = new ArrayList<>();

        //初始化各种画笔
        mPaintColdPoint = new Paint();
        mPaintColdPoint.setColor(getResources().getColor(R.color.coldColor));
        mPaintColdPoint.setAntiAlias(true);


        mPaintWarmPoint = new Paint();
        mPaintWarmPoint.setColor(getResources().getColor(R.color.warmColor));
        mPaintWarmPoint.setAntiAlias(true);

        mPaintColdLine = new Paint();
        mPaintColdLine.setColor(getResources().getColor(R.color.coldColor));
        mPaintColdLine.setStrokeWidth(6);

        mPaintWarmLine = new Paint();
        mPaintWarmLine.setColor(getResources().getColor(R.color.warmColor));
        mPaintWarmLine.setStrokeWidth(6);

        mPaintTemptext = new Paint();
        mPaintTemptext.setColor(getResources().getColor(R.color.tempTextColor));
        mPaintTemptext.setStrokeWidth(3);
        mPaintTemptext.setTextSize(40);

        mPaintDateText = new Paint();
        mPaintDateText.setColor(getResources().getColor(R.color.weatherTextColor));
        mPaintDateText.setStrokeWidth(5);
        mPaintDateText.setTextSize(40);

        //自动拿到底部显示的日期
        mTimeList = TimeUtils.getTimeFromNowToSixDay();
        Log.e("111", "initView结束！");

    }

    /**
     * 计算出x轴的坐标
     */
    private void initDataX() {
        int temp = (mWidth - marginLeft - marginRight) / 4;
        for (int i = 0; i < 5; i++) {
            mDataX.add(marginLeft + temp * i);
        }
    }


    /**
     * 设置数据点
     */
    public void setPoints() {
        Log.e("111", "setPoints开始！");
        int minY = getMinY(mRealColdY);
        int maxY = getMaxY(mRealWarmY);
        Log.e("111 setData", minY + "minandmax" + maxY);
        Log.e("111", "chuangjianPoints！");
        Log.e("111", "," + mHeight);
        for (int i = 0; i < 5; i++) {
            //TODO 处理最高温-最低温=0的情况。。
            Point p = new Point(mDataX.get(i), mHeight - marginBottom - (mHeight - marginBottom - marginTop) / (maxY - minY) * Math.abs(mRealColdY.get(i) - minY));
//            Point p = new Point(mDataX.get(i),mHeight-marginBottom-marginTop-100);
            mColdPoints.add(p);
        }

        for (int i = 0; i < 5; i++) {
            Point p = new Point(mDataX.get(i), mHeight - marginBottom - (mHeight - marginBottom - marginTop) / (maxY - minY) * Math.abs(mRealWarmY.get(i) - minY));
//            Point p = new Point(mDataX.get(i),mHeight-marginBottom-marginTop-50);
            mWarmPoints.add(p);
        }
        Log.e("111 setData points:", mWarmPoints.get(0).toString());
        Log.e("111", "setPoints结束！");

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("111", "onMeasure");
        mWidth = 0;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            mWidth = widthSpecSize;
        } else {
            mWidth = DensityUtils.dp2px(mContext, 100);
            if (widthSpecMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSpecSize);
            }
        }
        mHeight = 0;
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = heightSpecSize;
        } else {
            mHeight = mWidth / 2;
            if (heightSpecMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, heightSpecSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.e("111", "onsizechanged开始！");
        super.onSizeChanged(w, h, oldw, oldh);

//        List<Integer> cold = new ArrayList<>();
//        List<Integer> warm = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            cold.add(i - 3);
//            warm.add(i + 4);
//
//        }
//        setPoints(cold, warm);

        //点的起始位置
        for (int i = 0; i < 5; i++) {
            mProgressColdY.add(i, mHeight);
            mProgressWarmY.add(i, mHeight);
        }

        Log.e("111", "onsizechanged结束！");


    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initDataX();
//        if (isFirst) {
//            setDefaultData();
//        }

        setPoints();
        drawDate(canvas);

        //绘制点
        for (int i = 0; i < 5; i++) {
            canvas.drawCircle(mDataX.get(i), mProgressWarmY.get(i), 15, mPaintWarmPoint);
            canvas.drawCircle(mDataX.get(i), mProgressColdY.get(i), 15, mPaintColdPoint);

        }

        //mHeight - mHeight/(maxY-minY)*coldData.get(i)
        if (times < 30) {
            times++;
            for (int i = 0; i < 5; i++) {
                int tempWarm = (mHeight - mWarmPoints.get(i).y) / 30;
                int tempCold = (mHeight - mColdPoints.get(i).y) / 30;
                mProgressWarmY.set(i, mHeight - tempWarm * times);
                mProgressColdY.set(i, mHeight - tempCold * times);
            }
            postInvalidate();
//            postInvalidateDelayed(200);
        }

        if (times >= 30) {
            for (int i = 0; i < 4; i++) {
                canvas.drawLine(mDataX.get(i), mProgressWarmY.get(i), mDataX.get(i + 1), mProgressWarmY.get(i + 1), mPaintWarmLine);
                canvas.drawLine(mDataX.get(i), mProgressColdY.get(i), mDataX.get(i + 1), mProgressColdY.get(i + 1), mPaintColdLine);
            }
            for (int i = 0; i < 5; i++) {
                String coldTemp = String.valueOf(mRealColdY.get(i)) + "°";
                String warmTemp = String.valueOf(mRealWarmY.get(i)) + "°";
                Rect mTextBounds = new Rect();
                mPaintTemptext.getTextBounds(coldTemp, 0, coldTemp.length(), mTextBounds);
                canvas.drawText(warmTemp, mDataX.get(i) - mTextBounds.width() / 2, mProgressWarmY.get(i) - 20, mPaintTemptext);
                canvas.drawText(coldTemp, mDataX.get(i) - mTextBounds.width() / 2, mProgressColdY.get(i) + 50, mPaintTemptext);
            }

        }


    }


    /**
     * 绘制底部的日期
     *
     * @param canvas
     */
    private void drawDate(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            Rect rect = new Rect();
            mPaintDateText.getTextBounds(mTimeList.get(i), 0, mTimeList.get(i).length(), rect);
            canvas.drawText(mTimeList.get(i), mDataX.get(i) - rect.width() / 2, mHeight, mPaintDateText);
        }

    }

    private int getMaxY(List<Integer> tempList) {
        int tempMax = tempList.get(0);
        for (int i = 0; i < 5; i++) {
            if (tempList.get(i) > tempMax) {
                tempMax = tempList.get(i);
            }
        }
        return tempMax;
    }

    private int getMinY(List<Integer> tempList) {
        int tempMin = tempList.get(0);
        for (int i = 0; i < 5; i++) {
            if (tempList.get(i) < tempMin) {
                tempMin = tempList.get(i);
            }
        }
        return tempMin;
    }


    public void setDefaultData() {
        List<Integer> cold = new ArrayList<>();
        List<Integer> warm = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cold.add(0);
            warm.add(1);

        }
        this.mRealColdY = cold;
        this.mRealWarmY = warm;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        return (int) (dpValue * metrics.density + 0.5f);
    }


}
