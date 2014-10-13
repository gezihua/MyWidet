package com.example.seekbar_my;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ExtendableSeekBar extends View implements OnTouchListener {
    private final int DEFAULT_BAR_MAX = 100;
    private final int DEFAULT_THUMB_COLOR = 0xff95cb03;
    private final int DEFAULT_LINE_GRAY = 0xff9c9c9c;
    private ShapeDrawable mThumb;
    private ExtendableSeekBarChangedListener mExtendableSeekBarChangedListener;
    private boolean isTouchable = true;
    private int mPaddingLeft = 5;
    private int mCurBarColor = DEFAULT_LINE_GRAY;
    private int mPaddingRight = 5;
    private int mMax = DEFAULT_BAR_MAX;
    private int mProgress = 0;
    private int mDrawStart = 10;
    private int mDrawEnd;
    private int mDrawCurX = 50;
    private int mDrawCurY;
    private int mWidth = 350;
    private int mHeight = 100;
    private int mThumbSize = 30;
    private boolean isOVerBar = false;

    public ExtendableSeekBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public ExtendableSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public ExtendableSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        mThumb = new ShapeDrawable(new OvalShape());
        this.setOnTouchListener(this);
    }

    private void drawThumb(Canvas canvas) {
        int size = mThumbSize / 2;
        if (mDrawCurX < mPaddingLeft + mThumbSize / 2) {
            // Draw the thumb off
            mThumb.getPaint().setColor(mCurBarColor);
            mThumb.setBounds(mPaddingLeft, mHeight / 2 - size, mPaddingLeft + mThumbSize, mHeight
                    / 2 + size);
        } else if (mDrawCurX > mWidth + mThumbSize / 2 - mPaddingRight) {
            mThumb.getPaint().setColor(mCurBarColor);
            mThumb.setBounds(mWidth - mPaddingRight / 2 - mThumbSize / 2, mHeight / 2 - size,
                    mWidth
                            + mThumbSize / 2 - mPaddingRight / 2, mHeight
                            / 2 + size);
        } else {
            // Draw the thumb on
            mThumb.getPaint().setColor(DEFAULT_THUMB_COLOR);
            mThumb.setBounds(mDrawCurX - size, mHeight / 2 - size, mDrawCurX + size, mHeight / 2
                    + size);
        }
        mThumb.draw(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        mDrawCurY = (int) event.getY();
        mDrawCurX = (int) event.getX();
        int drag = 0;
        if(mExtendableSeekBarChangedListener != null && !isTouchable){
            return false;
        }
        if (mDrawCurX > mWidth + mThumbSize / 2 - mPaddingRight) {
            invalidate();
            mCurBarColor = DEFAULT_THUMB_COLOR;
            isOVerBar = true;
            drag = mMax;
        } else if (mDrawCurX < mPaddingLeft + mThumbSize / 2) {
            invalidate();
            mCurBarColor = DEFAULT_LINE_GRAY;
            isOVerBar = true;
            drag = 0;
        } else {
            isOVerBar = false;
            drag = (int) (((float) mDrawCurX / mWidth) * mMax);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (mExtendableSeekBarChangedListener != null) {
                    mExtendableSeekBarChangedListener.onStartTrackingTouch(this);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (mExtendableSeekBarChangedListener != null) {
                    mExtendableSeekBarChangedListener.onStopTrackingTouch(this);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mExtendableSeekBarChangedListener != null) {
                    mExtendableSeekBarChangedListener.onProcessChanged(drag, true);
                }
                break;
            }
        }
        if (isOVerBar) {
            return false;
        }
        isOVerBar = false;
        invalidate();
        return true;
    }

    // Draw the bg and draw the thumb
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        if (isOVerBar) {
            paint.setColor(mCurBarColor);
            canvas.drawLine(mPaddingLeft + mDrawStart, mHeight / 2, mWidth - mPaddingRight,
                    mHeight / 2, paint);
        } else {
            paint.setColor(DEFAULT_THUMB_COLOR);
            canvas.drawLine(mPaddingLeft + mDrawStart, mHeight / 2, mDrawCurX, mHeight / 2, paint);
            paint.setColor(DEFAULT_LINE_GRAY);
            canvas.drawLine(mDrawCurX, mHeight / 2, mWidth - mPaddingRight, mHeight / 2, paint);
        }
        drawThumb(canvas);
    }

    private void setMax(int max) {
        this.mMax = max;
    }

    public int getmProgress() {
        return mProgress;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        this.mDrawCurX = (int) (((float) mProgress / mMax) * mWidth);
    }

    public interface ExtendableSeekBarChangedListener {
        void onProcessChanged(int drag, boolean fromUser);

        void onStartTrackingTouch(ExtendableSeekBar seekBar);

        void onStopTrackingTouch(ExtendableSeekBar seekBar);

    }

    public void setmExtendableSeekBarChangedListener(
            ExtendableSeekBarChangedListener mExtendableSeekBarChangedListener) {
        this.mExtendableSeekBarChangedListener = mExtendableSeekBarChangedListener;
    }

    public void setTouchable(boolean flag){
        this.isTouchable = flag;
    }
}
