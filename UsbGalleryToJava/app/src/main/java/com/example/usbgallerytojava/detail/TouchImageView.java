package com.example.usbgallerytojava.detail;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class TouchImageView extends ImageView implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {

    private static final String TAG = "Goomba#TouchImageView";
    private static final float MAX_SCALE = 1.5f;
    private static final float MIN_SCALE = 1.0f;

    private Matrix mMatrix;
    private float[] mMatrixValue;
    private int mViewWidth, mViewHeight;
    private float mOriginWidth, mOriginHeight;
    private float mMaxScale = MAX_SCALE;
    private float mMinScale = MIN_SCALE;
    private float mSavedScale = 1.0f;

    private GestureDetector mGestureDetector;

    public TouchImageView(Context context) {
        super(context);
        initialize(context);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        fitImageToView();
    }

    private void initialize(Context context) {
        mGestureDetector = new GestureDetector(context, this);
        mMatrix = new Matrix();

        setImageMatrix(mMatrix);
        setScaleType(ScaleType.MATRIX);
    }

    private void fitImageToView() {
        Drawable drawable = getDrawable();
        if ( drawable == null ) {
            return;
        }

        if ( mMatrix == null ) {
            return;
        }

        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

        float scaleX = (float) mViewWidth / (float) imageWidth;
        float scaleY = (float) mViewHeight / (float) imageHeight;

        scaleX = scaleY = Math.min(scaleX, scaleY);

        float redundantXSpace = (float) mViewWidth - (scaleX * (float) imageWidth);
        float redundantYSpace = (float) mViewHeight - (scaleY * (float) imageHeight);

        mOriginWidth = mViewWidth * redundantXSpace;
        mOriginHeight = mViewHeight * redundantYSpace;

        mMatrix.setScale(scaleX, scaleY);
        mMatrix.postTranslate(redundantXSpace / 2.0f, redundantYSpace / 2.0f);
        mSavedScale = mMinScale;

        fixTrans();
        setImageMatrix(mMatrix);
    }

    private void fixTrans() {
        mMatrix.getValues(mMatrixValue);

        float transX = mMatrixValue[Matrix.MTRANS_X];
        float transY = mMatrixValue[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, mViewWidth, getImageWidth());
        float fixTransY = getFixTrans(transY, mViewHeight, getImageHeight());

        if ( fixTransX != 0 || fixTransY != 0 ) {
            mMatrix.postTranslate(fixTransX, fixTransY);
        }
    }

    private float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return minTrans - trans;
        if (trans > maxTrans)
            return maxTrans - trans;
        return 0;
    }

    private float getImageWidth() {
        return mOriginWidth * mSavedScale;
    }

    private float getImageHeight() {
        return mOriginHeight * mSavedScale;
    }
}
