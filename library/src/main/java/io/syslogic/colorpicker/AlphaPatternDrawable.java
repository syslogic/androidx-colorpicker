package io.syslogic.colorpicker;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This drawable that draws a simple white and gray chessboard pattern.
 * @author Martin Zeitler
 */
public class AlphaPatternDrawable extends Drawable {

    private final int mRectangleSize;
    private final Paint mPaint = new Paint();
    private final Paint mPaintWhite = new Paint();
    private final Paint mPaintGray = new Paint();

    private int numRectanglesHorizontal;
    private int numRectanglesVertical;

    private Bitmap mBitmap;

    AlphaPatternDrawable(int rectangleSize) {
        this.mRectangleSize = rectangleSize;
        this.mPaintWhite.setColor(0xFFFFFFFF);
        this.mPaintGray.setColor(0xFFCBCBCB);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawBitmap(mBitmap, null, getBounds(), mPaint);
    }

    /** Must return either: PixelFormat.UNKNOWN, PixelFormat.TRANSLUCENT, PixelFormat.TRANSPARENT, PixelFormat.OPAQUE */
    @Override
    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    @Override
    public void setAlpha(int alpha) {
        throw new UnsupportedOperationException("Alpha is not supported by this drawable.");
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter cf) {
        throw new UnsupportedOperationException("ColorFilter is not supported by this drawable.");
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        numRectanglesHorizontal = bounds.width() / mRectangleSize;
        numRectanglesVertical = bounds.height() / mRectangleSize;
        generatePatternBitmap();
    }

    /**
     * This will generate a bitmap with the pattern as big as the rectangle we were allow to draw on.
     * We do this to cache the bitmap so we don't need to recreate it each time draw() is called since it takes a few milliseconds.
     */
    private void generatePatternBitmap() {
        if (getBounds().width() <= 0 || getBounds().height() <= 0) {return;}
        mBitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        Rect r = new Rect();
        boolean verticalStartWhite = true;
        for (int i = 0; i <= numRectanglesVertical; i++) {
            boolean isWhite = verticalStartWhite;
            for (int j = 0; j <= numRectanglesHorizontal; j++) {
                r.top = i * mRectangleSize;
                r.left = j * mRectangleSize;
                r.bottom = r.top + mRectangleSize;
                r.right = r.left + mRectangleSize;
                canvas.drawRect(r, isWhite ? mPaintWhite : mPaintGray);
                isWhite = !isWhite;
            }
            verticalStartWhite = !verticalStartWhite;
        }
    }
}
