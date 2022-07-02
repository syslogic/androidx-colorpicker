package io.syslogic.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;

/**
 * The Color-Picker Panel {@link View}
 *
 * This class draws a panel which which will be filled with a color which can be set.
 * It can be used to show the currently selected color which you will get from the {@link ColorPickerView}.
 * @author Martin Zeitler
 */
public class ColorPickerPanelView extends View {

    private int mBorderColor = 0xFF6E6E6E;
    private int mColor = 0xFF000000;

    /** the width in pixels of the border surrounding the color panel. */
    private float BORDER_WIDTH_PX;

    private float mDensity = 1f;

    private Paint mBorderPaint;
    private Paint mColorPaint;

    private RectF mDrawingRect;
    private RectF mColorRect;

    private AlphaPatternDrawable mAlphaPattern;

    public ColorPickerPanelView(@NonNull Context context, @NonNull AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerPanelView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(@NonNull Context context) {
        this.mDensity = getDisplayDensity(context);
        BORDER_WIDTH_PX = getDimension(context, R.dimen.border_width_px);
        this.initPaintTools();
    }

    private void initPaintTools() {
        mBorderPaint = new Paint();
        mColorPaint = new Paint();
    }

    private static float getDimension(@NonNull Context context, @DimenRes int resId) {
        return context.getResources().getDimension(resId);
    }

    private static float getDisplayDensity(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        final RectF rect = mColorRect;
        mBorderPaint.setColor(mBorderColor);
        canvas.drawRect(mDrawingRect, mBorderPaint);
        if (mAlphaPattern != null) {mAlphaPattern.draw(canvas);}
        mColorPaint.setColor(mColor);
        canvas.drawRect(rect, mColorPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mDrawingRect = new RectF();
        mDrawingRect.left = getPaddingLeft();
        mDrawingRect.right = w - getPaddingRight();
        mDrawingRect.top = getPaddingTop();
        mDrawingRect.bottom = h - getPaddingBottom();
        setUpColorRect();
    }

    private void setUpColorRect() {

        final RectF dRect = mDrawingRect;
        float left = dRect.left + BORDER_WIDTH_PX;
        float top = dRect.top + BORDER_WIDTH_PX;
        float bottom = dRect.bottom - BORDER_WIDTH_PX;
        float right = dRect.right - BORDER_WIDTH_PX;
        mColorRect = new RectF(left, top, right, bottom);

        mAlphaPattern = new AlphaPatternDrawable((int) (5 * mDensity));
        mAlphaPattern.setBounds(
            Math.round(mColorRect.left),
            Math.round(mColorRect.top),
            Math.round(mColorRect.right),
            Math.round(mColorRect.bottom)
        );
    }

    /**
     * Set the color that should be shown by this view.
     *
     * @param color the color value to set.
     */
    public void setColor(int color) {
        mColor = color;
        invalidate();
    }

    /**
     * Get the color currently shown by this view.
     *
     * @return the current color value.
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Set the color of the border surrounding the panel.
     *
     * @param color the color value to set.
     */
    @SuppressWarnings("unused")
    public void setBorderColor(int color) {
        mBorderColor = color;
        invalidate();
    }

    /**
     * Get the color of the border surrounding the panel.
     *
     * @return the current border color value.
     */
    @SuppressWarnings("unused")
    public int getBorderColor() {
        return mBorderColor;
    }
}
