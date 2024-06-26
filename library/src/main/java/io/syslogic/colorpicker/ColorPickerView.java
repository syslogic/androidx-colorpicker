package io.syslogic.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.Objects;

/**
 * The Color-Picker {@link View} (refactored version).
 * It displays a color picker to the user and allow them to select a color.
 * the slider for the alpha-channel can be enabled setAlphaSliderVisible(true).
 * @author Martin Zeitler
 */
public class ColorPickerView extends View {

    private final static int PANEL_SAT   = 0;
    private final static int PANEL_HUE   = 1;
    private final static int PANEL_ALPHA = 2;

    /** To remember which panel that has the "focus" when processing hardware button data. */
    private int mLastTouchedPanel = PANEL_SAT;

    private int mSliderTrackerColor = 0xFF1C1C1C;

    private int mBorderColor = 0xFF6E6E6E;

    private float mDensity = 1f;

    /** The width in pixels of the border surrounding all color panels. */
    private float BORDER_WIDTH_PX;

    /** The width in dp of the hue panel. */
    private float HUE_PANEL_WIDTH;

    /** The height in dp of the alpha panel */
    private float ALPHA_PANEL_HEIGHT;

    /** The distance in dp between the different color panels. */
    private float PANEL_SPACING;

    /** The radius in dp of the color palette tracker circle. */
    private float PALETTE_CIRCLE_TRACKER_RADIUS;

    /** The dp which the tracker of the hue or alpha panel will extend outside of its bounds. */
    private float RECTANGLE_TRACKER_OFFSET;

    private OnColorChangedListener mListener;

    private Paint mSaturationPaint;
    private Paint mSaturationTrackerPaint;

    private Paint mHuePaint;
    private Paint mHueTrackerPaint;

    private Paint mAlphaPaint;
    private Paint mAlphaTextPaint;

    private Paint mBorderPaint;

    private Shader mContrastShader;
    private Shader mSaturationShader;
    private Shader mHueShader;
    private Shader mAlphaShader;

    private int mAlpha = 0xff;
    private float mHue = 360f;
    private float mSaturation = 0f;
    private float mContrast = 0f;

    private String mAlphaSliderText = "";
    private boolean mShowAlphaPanel = false;

    /**
     * Offset from the edge we must have or else the finger tracker
     * will get clipped when it is drawn outside of the view.
     */
    private float mDrawingOffset;

    /** Distance from the edges of the view of where we are allowed to draw. */
    private RectF mDrawingRect;

    private RectF mSatRect;
    private RectF mHueRect;
    private RectF mAlphaRect;

    private AlphaPatternDrawable mAlphaPattern;

    private Point mStartTouchPoint = null;

    /**
     * Constructor
     * @param context the context.
     */
    public ColorPickerView(@NonNull Context context) {
        this(context, null);
    }

    /**
     * Constructor
     * @param context the context.
     * @param attrs view attributes.
     */
    public ColorPickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor
     * @param context the context.
     * @param attrs view attributes.
     * @param defStyle The default style to apply to this view.
     *                 If 0, no style will be applied (beyond what is included in the theme).
     *                 This may either be an attribute resource, whose value will be retrieved
     *                 from the current theme, or an explicit style resource.
     */
    public ColorPickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(@NonNull Context context) {

        this.mDensity = getDisplayDensity(context);

        BORDER_WIDTH_PX               = getDimension(context, R.dimen.border_width_px);
        PALETTE_CIRCLE_TRACKER_RADIUS = mDensity * getDimension(context, R.dimen.palette_circle_tracker_radius);
        RECTANGLE_TRACKER_OFFSET      = mDensity * getDimension(context, R.dimen.rectangle_tracker_offset);
        HUE_PANEL_WIDTH               = mDensity * getDimension(context, R.dimen.hue_panel_width);
        ALPHA_PANEL_HEIGHT            = mDensity * getDimension(context, R.dimen.alpha_panel_height);
        PANEL_SPACING                 = mDensity * getDimension(context, R.dimen.panel_spacing);

        this.mDrawingOffset = this.calculateRequiredOffset();
        this.setFocusableInTouchMode(true);
        this.setFocusable(true);
        this.initPaintTools();
    }

    private void initPaintTools() {

        mBorderPaint = new Paint();

        mHuePaint = new Paint();
        mHueTrackerPaint = new Paint();
        mHueTrackerPaint.setColor(mSliderTrackerColor);
        mHueTrackerPaint.setStyle(Style.STROKE);
        mHueTrackerPaint.setStrokeWidth(2f * mDensity);
        mHueTrackerPaint.setAntiAlias(true);

        mSaturationPaint = new Paint();
        mSaturationTrackerPaint = new Paint();
        mSaturationTrackerPaint.setStyle(Style.STROKE);
        mSaturationTrackerPaint.setStrokeWidth(2f * mDensity);
        mSaturationTrackerPaint.setAntiAlias(true);

        mAlphaPaint = new Paint();
        mAlphaTextPaint = new Paint();
        mAlphaTextPaint.setColor(0xff1c1c1c);
        mAlphaTextPaint.setTextSize(14f * mDensity);
        mAlphaTextPaint.setAntiAlias(true);
        mAlphaTextPaint.setTextAlign(Align.CENTER);
        mAlphaTextPaint.setFakeBoldText(true);
    }

    private static float getDimension(@NonNull Context context, @DimenRes int resId) {
        return context.getResources().getDimension(resId);
    }

    private static float getDisplayDensity(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    private float calculateRequiredOffset() {
        float offset = Math.max(PALETTE_CIRCLE_TRACKER_RADIUS, RECTANGLE_TRACKER_OFFSET);
        offset = Math.max(offset, BORDER_WIDTH_PX);
        return offset * 1.5f;
    }

    @NonNull
    private int[] getHueColors() {
        int[] list = new int[361];
        int count = 0;
        for (int i = list.length - 1; i >= 0; i--, count++) {
            list[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        return list;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (mDrawingRect.width() <= 0 || mDrawingRect.height() <= 0) {
            return;
        }
        drawSatPanel(canvas);
        drawHuePanel(canvas);
        drawAlphaPanel(canvas);
    }

    private void drawSatPanel(@NonNull Canvas canvas) {

        final RectF rect = mSatRect;
        mBorderPaint.setColor(mBorderColor);
        canvas.drawRect(mDrawingRect.left, mDrawingRect.top, rect.right + BORDER_WIDTH_PX, rect.bottom + BORDER_WIDTH_PX, mBorderPaint);

        if (mContrastShader == null) {
            mContrastShader = new LinearGradient(rect.left, rect.top, rect.left, rect.bottom,
                    0xffffffff, 0xff000000, TileMode.CLAMP);
        }

        int rgb = Color.HSVToColor(new float[]{mHue, 1f, 1f});

        mSaturationShader = new LinearGradient(rect.left, rect.top, rect.right, rect.top, 0xffffffff, rgb, TileMode.CLAMP);
        ComposeShader mShader = new ComposeShader(mContrastShader, mSaturationShader, PorterDuff.Mode.MULTIPLY);
        mSaturationPaint.setShader(mShader);

        canvas.drawRect(rect, mSaturationPaint);
        Point p = satToPoint(mSaturation, mContrast);

        mSaturationTrackerPaint.setColor(0xff000000);
        canvas.drawCircle(p.x, p.y, PALETTE_CIRCLE_TRACKER_RADIUS - mDensity, mSaturationTrackerPaint);

        mSaturationTrackerPaint.setColor(0xffdddddd);
        canvas.drawCircle(p.x, p.y, PALETTE_CIRCLE_TRACKER_RADIUS, mSaturationTrackerPaint);
    }

    private void drawHuePanel(@NonNull Canvas canvas) {

        final RectF rect = mHueRect;
        mBorderPaint.setColor(mBorderColor);
        canvas.drawRect(rect.left - BORDER_WIDTH_PX, rect.top - BORDER_WIDTH_PX, rect.right + BORDER_WIDTH_PX, rect.bottom + BORDER_WIDTH_PX, mBorderPaint);

        if (mHueShader == null) {
            mHueShader = new LinearGradient(rect.left, rect.top, rect.left, rect.bottom, getHueColors(), null, TileMode.CLAMP);
            mHuePaint.setShader(mHueShader);
        }

        canvas.drawRect(rect, mHuePaint);
        float rectHeight = 4 * mDensity / 2;
        Point p = hueToPoint(mHue);
        RectF r = new RectF();
        r.left = rect.left - RECTANGLE_TRACKER_OFFSET;
        r.right = rect.right + RECTANGLE_TRACKER_OFFSET;
        r.top = p.y - rectHeight;
        r.bottom = p.y + rectHeight;

        canvas.drawRoundRect(r, 2, 2, mHueTrackerPaint);
    }

    private void drawAlphaPanel(Canvas canvas) {

        if (!mShowAlphaPanel || mAlphaRect == null || mAlphaPattern == null) return;
        final RectF rect = mAlphaRect;
        mBorderPaint.setColor(mBorderColor);
        canvas.drawRect(rect.left - BORDER_WIDTH_PX, rect.top - BORDER_WIDTH_PX, rect.right + BORDER_WIDTH_PX, rect.bottom + BORDER_WIDTH_PX, mBorderPaint);
        mAlphaPattern.draw(canvas);

        float[] hsv = new float[]{mHue, mSaturation, mContrast};
        int color = Color.HSVToColor(hsv);
        int aColor = Color.HSVToColor(0, hsv);

        mAlphaShader = new LinearGradient(rect.left, rect.top, rect.right, rect.top, color, aColor, TileMode.CLAMP);
        mAlphaPaint.setShader(mAlphaShader);
        canvas.drawRect(rect, mAlphaPaint);

        if (mAlphaSliderText != null && !Objects.equals(mAlphaSliderText, "")) {
            canvas.drawText(mAlphaSliderText, rect.centerX(), rect.centerY() + 4 * mDensity, mAlphaTextPaint);
        }

        float rectWidth = 4 * mDensity / 2;
        Point p = alphaToPoint(mAlpha);

        RectF r = new RectF();
        r.left = p.x - rectWidth;
        r.right = p.x + rectWidth;
        r.top = rect.top - RECTANGLE_TRACKER_OFFSET;
        r.bottom = rect.bottom + RECTANGLE_TRACKER_OFFSET;

        canvas.drawRoundRect(r, 2, 2, mHueTrackerPaint);
    }


    @NonNull
    private Point hueToPoint(float hue) {
        final RectF rect = mHueRect;
        final float height = rect.height();
        Point p = new Point();
        p.y = (int) (height - (hue * height / 360f) + rect.top);
        p.x = (int) rect.left;
        return p;
    }

    @NonNull
    private Point satToPoint(float sat, float val) {
        final RectF rect = mSatRect;
        final float height = rect.height();
        final float width = rect.width();
        Point p = new Point();
        p.x = (int) (sat * width + rect.left);
        p.y = (int) ((1f - val) * height + rect.top);
        return p;
    }

    @NonNull
    private Point alphaToPoint(int alpha) {
        final RectF rect = mAlphaRect;
        final float width = rect.width();
        Point p = new Point();
        p.x = (int) (width - (alpha * width / 0xff) + rect.left);
        p.y = (int) rect.top;
        return p;
    }

    @NonNull
    private float[] pointToSat(float x, float y) {

        final RectF rect = mSatRect;
        float[] result = new float[2];
        float width = rect.width();
        float height = rect.height();

        if (x < rect.left) {x = 0f;}
        else if (x > rect.right) {x = width;}
        else {x = x - rect.left;}

        if (y < rect.top) { y = 0f;}
        else if (y > rect.bottom) {y = height;}
        else {y = y - rect.top;}

        result[0] = 1.f / width * x;
        result[1] = 1.f - (1.f / height * y);
        return result;
    }

    private float pointToHue(float y) {
        final RectF rect = mHueRect;
        float height = rect.height();
        if (y < rect.top) {y = 0f;}
        else if (y > rect.bottom) {y = height;}
        else {y = y - rect.top;}
        return 360f - (y * 360f / height);
    }

    private int pointToAlpha(int x) {
        final RectF rect = mAlphaRect;
        final int width = (int) rect.width();
        if (x < rect.left) {
            x = 0;
        } else if (x > rect.right) {
            x = width;
        } else {
            x = x - (int) rect.left;
        }
        return 0xff - (x * 0xff / width);
    }

    @Override
    public boolean onTrackballEvent(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        boolean update = false;
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            switch (mLastTouchedPanel) {
                case PANEL_SAT -> {
                    float sat, val;
                    sat = mSaturation + x / 50f;
                    val = mContrast - y / 50f;
                    if (sat < 0f) {
                        sat = 0f;
                    } else if (sat > 1f) {
                        sat = 1f;
                    }
                    if (val < 0f) {
                        val = 0f;
                    } else if (val > 1f) {
                        val = 1f;
                    }
                    mSaturation = sat;
                    mContrast = val;
                    update = true;
                }
                case PANEL_HUE -> {
                    float hue = mHue - y * 10f;
                    if (hue < 0f) {
                        hue = 0f;
                    } else if (hue > 360f) {
                        hue = 360f;
                    }
                    mHue = hue;
                    update = true;
                }
                case PANEL_ALPHA -> {
                    if (mShowAlphaPanel || mAlphaRect != null) {
                        int alpha = (int) (mAlpha - x * 10);
                        if (alpha < 0) {
                            alpha = 0;
                        } else if (alpha > 0xff) {
                            alpha = 0xff;
                        }
                        mAlpha = alpha;
                        update = true;
                    }
                }
            }
        }
        if (update) {
            if (mListener != null) {
                int intValue = Color.HSVToColor(mAlpha, new float[]{mHue, mSaturation, mContrast});
                mListener.onColorChanged(intValue);
            }
            invalidate();
            return true;
        }
        return super.onTrackballEvent(event);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean update = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN -> {
                mStartTouchPoint = new Point((int) event.getX(), (int) event.getY());
                update = moveTrackersIfNeeded(event);
            }
            case MotionEvent.ACTION_MOVE -> update = moveTrackersIfNeeded(event);
            case MotionEvent.ACTION_UP -> {
                mStartTouchPoint = null;
                update = moveTrackersIfNeeded(event);
            }
        }

        if (update) {
            if (mListener != null) {
                mListener.onColorChanged(Color.HSVToColor(mAlpha, new float[]{mHue, mSaturation, mContrast}));
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean moveTrackersIfNeeded(MotionEvent event) {
        if (mStartTouchPoint == null) return false;
        int startX = mStartTouchPoint.x;
        int startY = mStartTouchPoint.y;
        boolean update = false;
        if (mHueRect.contains(startX, startY)) {
            mLastTouchedPanel = PANEL_HUE;
            mHue = pointToHue(event.getY());
            update = true;
        } else if (mSatRect.contains(startX, startY)) {
            mLastTouchedPanel = PANEL_SAT;
            float[] result = pointToSat(event.getX(), event.getY());
            mSaturation = result[0];
            mContrast = result[1];
            update = true;
        } else if (mAlphaRect != null && mAlphaRect.contains(startX, startY)) {
            mLastTouchedPanel = PANEL_ALPHA;
            mAlpha = pointToAlpha((int) event.getX());
            update = true;
        }
        return update;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width;
        int height;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthAllowed = MeasureSpec.getSize(widthMeasureSpec);
        int heightAllowed = MeasureSpec.getSize(heightMeasureSpec);

        widthAllowed = chooseWidth(widthMode, widthAllowed);
        heightAllowed = chooseHeight(heightMode, heightAllowed);

        if (! mShowAlphaPanel) {

            height = (int) (widthAllowed - PANEL_SPACING - HUE_PANEL_WIDTH);

            // if calculated height (based on the width) is more than the allowed height.
            if (height > heightAllowed || getTag().equals("landscape")) {
                height = heightAllowed;
                width = (int) (height + PANEL_SPACING + HUE_PANEL_WIDTH);
            } else {
                width = widthAllowed;
            }
        } else {
            width = (int) (heightAllowed - ALPHA_PANEL_HEIGHT + HUE_PANEL_WIDTH);
            if (width > widthAllowed) {
                width = widthAllowed;
                height = (int) (widthAllowed - HUE_PANEL_WIDTH + ALPHA_PANEL_HEIGHT);
            } else {
                height = heightAllowed;
            }
        }
        setMeasuredDimension(width, height);
    }

    private int chooseWidth(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else {
            return getPreferredWidth();
        }
    }

    private int chooseHeight(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else {
            return getPreferredHeight();
        }
    }

    private int getPreferredWidth() {
        int width = getPreferredHeight();
        if (mShowAlphaPanel) {
            width -= (int) (PANEL_SPACING + ALPHA_PANEL_HEIGHT);
        }
        return (int) (width + HUE_PANEL_WIDTH + PANEL_SPACING);
    }

    private int getPreferredHeight() {
        int height = (int) (200 * mDensity);
        if (mShowAlphaPanel) {
            height += (int) (PANEL_SPACING + ALPHA_PANEL_HEIGHT);
        }
        return height;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mDrawingRect = new RectF();
        mDrawingRect.left = mDrawingOffset + getPaddingLeft();
        mDrawingRect.right = w - mDrawingOffset - getPaddingRight();
        mDrawingRect.top = mDrawingOffset + getPaddingTop();
        mDrawingRect.bottom = h - mDrawingOffset - getPaddingBottom();
        setUpSatRect();
        setUpHueRect();
        setUpAlphaRect();
    }

    private void setUpSatRect() {
        final RectF dRect = mDrawingRect;
        float panelSide = dRect.height() - BORDER_WIDTH_PX * 2;
        if (mShowAlphaPanel) {panelSide -= PANEL_SPACING + ALPHA_PANEL_HEIGHT;}
        float left = dRect.left + BORDER_WIDTH_PX;
        float top = dRect.top + BORDER_WIDTH_PX;
        float bottom = top + panelSide;
        float right = left + panelSide;
        mSatRect = new RectF(left, top, right, bottom);
    }

    private void setUpHueRect() {
        final RectF dRect = mDrawingRect;
        float left = dRect.right - HUE_PANEL_WIDTH + BORDER_WIDTH_PX;
        float top = dRect.top + BORDER_WIDTH_PX;
        float bottom = dRect.bottom - BORDER_WIDTH_PX - (mShowAlphaPanel ? (PANEL_SPACING + ALPHA_PANEL_HEIGHT) : 0);
        float right = dRect.right - BORDER_WIDTH_PX;
        mHueRect = new RectF(left, top, right, bottom);
    }

    private void setUpAlphaRect() {
        if (!mShowAlphaPanel) return;
        final RectF dRect = mDrawingRect;
        float left = dRect.left + BORDER_WIDTH_PX;
        float top = dRect.bottom - ALPHA_PANEL_HEIGHT + BORDER_WIDTH_PX;
        float bottom = dRect.bottom - BORDER_WIDTH_PX;
        float right = dRect.right - BORDER_WIDTH_PX;
        mAlphaRect = new RectF(left, top, right, bottom);
        mAlphaPattern = new AlphaPatternDrawable((int) (5 * mDensity));
        mAlphaPattern.setBounds(Math.round(mAlphaRect.left), Math.round(mAlphaRect.top), Math.round(mAlphaRect.right), Math.round(mAlphaRect.bottom));
    }

    /**
     * Set a OnColorChangedListener to get notified when the color selected by the user has changed.
     *
     * @param listener to be used for callbacks.
     */
    public void setOnColorChangedListener(@NonNull OnColorChangedListener listener) {
        mListener = listener;
    }

    /**
     * Set the color of the border surrounding all panels.
     *
     * @param color the color value to set.
     */
    @SuppressWarnings("unused")
    public void setBorderColor(int color) {
        mBorderColor = color;
        invalidate();
    }

    /**
     * Get the color of the border surrounding all panels.
     * @return the current border color.
     */
    @SuppressWarnings("unused")
    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * Get the current color this view is showing.
     * @return the current color.
     */
    public int getColor() {
        return Color.HSVToColor(mAlpha, new float[]{mHue, mSaturation, mContrast});
    }

    /**
     * Set the color the view should show.
     * @param color The color that should be selected.
     */
    public void setColor(int color) {
        setColor(color, false);
    }

    /**
     * Set the color this view should show.
     * @param color    The color that should be selected.
     * @param callback If you want to get a callback to your OnColorChangedListener.
     */
    public void setColor(int color, boolean callback) {
        int alpha = Color.alpha(color);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        mAlpha      = alpha;
        mHue        = hsv[0];
        mSaturation = hsv[1];
        mContrast   = hsv[2];
        if (callback && mListener != null) {
            mListener.onColorChanged(Color.HSVToColor(mAlpha, new float[]{mHue, mSaturation, mContrast}));
        }
        invalidate();
    }

    /**
     * Get the drawing offset of the color picker view.
     * The drawing offset is the distance from the side of a panel to the side of the view minus the padding.
     * Useful if you want to have your own panel below showing the currently selected color and want to align it perfectly.
     * @return The offset in pixels.
     */
    public float getDrawingOffset() {
        return mDrawingOffset;
    }

    /**
     * Set if the user is allowed to adjust the alpha panel. Default is false.
     * When it is set to false, no alpha will be set.
     *
     * @param visible whether to show the alpha-slider panel.
     */
    @SuppressWarnings("unused")
    public void setAlphaSliderVisible(boolean visible) {
        if (mShowAlphaPanel != visible) {
            mShowAlphaPanel = visible;

            /* reset all shader to force a recreation - otherwise they will not look right after the size of the view has changed. */
            mContrastShader = null;
            mSaturationShader = null;
            mHueShader = null;
            mAlphaShader = null;
            requestLayout();
        }
    }

    /** @return if the alpha-panel shall be shown. */
    @SuppressWarnings("unused")
    public boolean getAlphaSliderVisible() {
        return mShowAlphaPanel;
    }

    /** @param color the color of the tracker painter. */
    @SuppressWarnings("unused")
    public void setSliderTrackerColor(int color) {
        mSliderTrackerColor = color;
        mHueTrackerPaint.setColor(mSliderTrackerColor);
        invalidate();
    }

    /** @return the color of the tracker painter. */
    @SuppressWarnings("unused")
    public int getSliderTrackerColor() {
        return mSliderTrackerColor;
    }

    /**
     * Set the text that should be shown in the alpha slider. Set to null to disable text.
     * @param resId string resource id.
     */
    @SuppressWarnings("unused")
    public void setAlphaSliderText(@StringRes int resId) {
        String text = getContext().getString(resId);
        setAlphaSliderText(text);
    }

    /**
     * Set the text that should be shown in the alpha slider. Set to null to disable text.
     * @param text Text that should be shown.
     */
    public void setAlphaSliderText(@NonNull String text) {
        this.mAlphaSliderText = text;
        invalidate();
    }

    /**
     * Get the current value of the text that will be shown in the alpha slider.
     * @return Text that is being shown.
     */
    @SuppressWarnings("unused")
    @NonNull
    public String getAlphaSliderText() {
        return this.mAlphaSliderText;
    }
}
