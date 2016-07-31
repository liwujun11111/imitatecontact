package com.leo.imitatecontact.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.leo.imitatecontact.R;

/**
 * Created by LEO on 2016/7/30.
 */
public class NavigationLetterListView extends View {

    private Paint mPaint; //初始化画笔
    private int mHeight;  //控件高度
    private int mWidth;   //控件宽度
    private float mDefaultTextSize = 13f; //默认字母到大小
    private int mChoosePosition = -1;  //所选字母的位置
    private int mSingleLetterHeight;//单个字母需要的高度
    private boolean mDrawBackground = false; //绘制整个导航条的背景
    private int textNormalColor;
    private int textPressColor;
    private int background;
    private float textSize;
    private ScrollCallBackLetterListener mScrollCallBackLetterListener;
    private String mLetters[] = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"
    };
    public NavigationLetterListView(Context context) {
        super(context);
        init(context,null);
    }

    public NavigationLetterListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public NavigationLetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationLetterListView);
        textNormalColor = ta.getColor(R.styleable.NavigationLetterListView_text_color_normal,0);
        textPressColor = ta.getColor(R.styleable.NavigationLetterListView_text_color_press,0);
        background = ta.getColor(R.styleable.NavigationLetterListView_letter_background,0);
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,ta.getFloat(R.styleable.NavigationLetterListView_text_size,mDefaultTextSize),getContext().getResources().getDisplayMetrics());
        ta.recycle();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        if (mDrawBackground) {
            canvas.drawColor(background);
        }
        mSingleLetterHeight =mHeight/ mLetters.length;
        for (int i = 0; i < mLetters.length; i++) {
            mPaint.setColor(textNormalColor);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setTextSize(textSize);
            if (i == mChoosePosition){
                mPaint.setColor(textPressColor);
                mPaint.setFakeBoldText(true);
            }
            //设置抗锯齿样式
            mPaint.setAntiAlias(true);
            int singleLetterWidth = (int) mPaint.measureText(mLetters[i]);
            int baseLineX = mWidth / 2 - singleLetterWidth / 2;
            canvas.drawText(mLetters[i], baseLineX, mSingleLetterHeight*i+mSingleLetterHeight, mPaint);
            //还原画笔
            mPaint.reset();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentY = (int)event.getY();
        final int lastPosition = mChoosePosition;
        mChoosePosition = currentY / mSingleLetterHeight;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDrawBackground = true;
                if (mChoosePosition >= 0 && mChoosePosition <mLetters.length){
                    if (mChoosePosition != lastPosition && mScrollCallBackLetterListener!=null) {
                        mScrollCallBackLetterListener.showSelectLetter(mLetters[mChoosePosition]);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mChoosePosition >= 0 && mChoosePosition <mLetters.length){
                    if (mChoosePosition != lastPosition && mScrollCallBackLetterListener!=null){
                        mScrollCallBackLetterListener.showSelectLetter(mLetters[mChoosePosition]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mDrawBackground = false;
                mChoosePosition = -1;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    public void setmScrollCallBackLetterListener(ScrollCallBackLetterListener mScrollCallBackLetterListener) {
        this.mScrollCallBackLetterListener = mScrollCallBackLetterListener;
    }

    /**
     * 选择字母回掉监听器
     */
    public interface ScrollCallBackLetterListener{
         void showSelectLetter(String selectLetter);
    }
}

