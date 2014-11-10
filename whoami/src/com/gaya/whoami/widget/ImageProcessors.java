package com.gaya.whoami.widget;

import android.content.Context;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;


import static com.gaya.whoami.ioc.ServiceLocator.getService;


/**
 * @author suriel
 *         Date: 7/9/14
 *         Time: 4:33 PM
 */
public class ImageProcessors {
    public final static BitmapProcessor ROUNDED_BITMAP_PROCESSOR = new RoundedBitmapProcessor(getService(Context.class).getResources(), 10, 10);
    public final static BitmapProcessor CIRCLE_STROKED_BITMAP_PROCESSOR = new CircledBitmapProcessor(getService(Context.class).getResources()).setStroke(2, Color.WHITE);
    public final static BitmapProcessor CIRCLE_BITMAP_PROCESSOR = new CircledBitmapProcessor(getService(Context.class).getResources());

    public interface BitmapProcessor {
        Drawable process(Bitmap bitmap);
    }



    static class MaskedBitmapDrawable extends BitmapDrawable {
        final Bitmap bitmap;
        final Bitmap mask;
        final Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public MaskedBitmapDrawable(Resources resources, Bitmap bitmap, Bitmap mask) {
            super(resources, bitmap);
            this.bitmap = bitmap;
            this.mask = mask;
            getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.drawBitmap(mask, 0, 0, maskPaint);
            super.draw(canvas);
            canvas.restore();
        }
    }

    public static class MaskedBitmapProcessor implements BitmapProcessor {
        private final Bitmap mask;
        private final Resources resources;

        public MaskedBitmapProcessor(Bitmap mask, Resources resources) {
            this.mask = mask;
            this.resources = resources;
        }

        @Override
        public Drawable process(Bitmap bitmap) {
            return new MaskedBitmapDrawable(resources, bitmap, mask);
        }
    }

    static class CustomShaderBitmapDrawable extends BitmapDrawable {
        final Bitmap bitmap;
        final Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public CustomShaderBitmapDrawable(Resources resources, Bitmap bitmap, Shader mask) {
            super(resources, bitmap);
            this.bitmap = bitmap;

            getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            maskPaint.setShader(mask);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.drawPaint(maskPaint);
            super.draw(canvas);
            canvas.restore();
        }
    }

    public static class Gradient {
        private float startX = 0f;
        private float endX = 1f;
        private float startY = 0f;
        private float endY = 1f;

        private int[] colors;
        private float[] positions;
        private Shader.TileMode tile = Shader.TileMode.CLAMP;

        public float getStartX() {
            return startX;
        }

        public Gradient setStartX(float startX) {
            this.startX = startX;
            return this;
        }

        public float getEndX() {
            return endX;
        }

        public Gradient setEndX(float endX) {
            this.endX = endX;
            return this;
        }

        public float getStartY() {
            return startY;
        }

        public Gradient setStartY(float startY) {
            this.startY = startY;
            return this;
        }

        public float getEndY() {
            return endY;
        }

        public Gradient setEndY(float endY) {
            this.endY = endY;
            return this;
        }

        public int[] getColors() {
            return colors;
        }

        public Gradient setColors(int... colors) {
            this.colors = colors;
            return this;
        }

        public float[] getPositions() {
            return positions;
        }

        public Gradient setPositions(float... positions) {
            this.positions = positions;
            return this;
        }

        public Shader.TileMode getTile() {
            return tile;
        }

        public Gradient setTile(Shader.TileMode tile) {
            this.tile = tile;
            return this;
        }

        private Shader getShader(RectF rectF) {
            LinearGradient linearGradient = new LinearGradient(rectF.left + (rectF.width() * startX), rectF.top + (rectF.height() * startY),
                    rectF.left + (rectF.width() * endX), rectF.top + (rectF.height() * endY),
                    colors, positions, tile);
            return linearGradient;
        }
    }

    static class GradientBitmapDrawable extends BitmapDrawable {
        final Bitmap bitmap;
        final Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Gradient gradient;

        public GradientBitmapDrawable(Resources resources, Bitmap bitmap, Gradient gradient) {
            super(resources, bitmap);
            this.bitmap = bitmap;

            getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            this.gradient = gradient;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            maskPaint.setShader(gradient.getShader(new RectF(canvas.getClipBounds())));
            canvas.drawPaint(maskPaint);
            super.draw(canvas);
            canvas.restore();
        }
    }


    static class CircleBitmapDrawable extends ShaderBitmapDrawable {
        final Bitmap bitmap;
        final Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        final Paint stroke;

        @Override
        public int getIntrinsicHeight() {
            return Math.min(super.getIntrinsicHeight(), super.getIntrinsicWidth());
        }

        @Override
        public int getIntrinsicWidth() {
            return Math.min(super.getIntrinsicHeight(), super.getIntrinsicWidth());
        }

        public CircleBitmapDrawable(Resources resources, Bitmap bitmap, Paint stroke) {
            super(resources, bitmap);
            this.bitmap = bitmap;
            this.stroke = stroke;

        }

        @Override
        protected void draw(Canvas canvas, Paint paint, Rect rc) {
            canvas.drawCircle(rc.centerX(), rc.centerY(), Math.min(rc.height(), rc.width()) / 2, paint);
            if (stroke != null) {
                canvas.drawCircle(rc.centerX(), rc.centerY(), (Math.min(rc.height(), rc.width()) / 2) - 1f, stroke);
            }
        }

    }


    public static class GradientBitmapProcessor implements BitmapProcessor {
        private final Gradient gradient;
        private final Resources resources;

        public GradientBitmapProcessor(Gradient gradient, Resources resources) {
            this.gradient = gradient;
            this.resources = resources;
        }

        @Override
        public Drawable process(Bitmap bitmap) {
            return new GradientBitmapDrawable(resources, bitmap, gradient);
        }
    }

    public static class CustomShaderBitmapProcessor implements BitmapProcessor {
        private final Shader shader;
        private final Resources resources;

        public CustomShaderBitmapProcessor(Shader shader, Resources resources) {
            this.shader = shader;
            this.resources = resources;
        }

        @Override
        public Drawable process(Bitmap bitmap) {
            return new CustomShaderBitmapDrawable(resources, bitmap, shader);
        }
    }

    public static class CircledBitmapProcessor implements BitmapProcessor {
        private final Resources resources;
        private Paint mStroke;

        public CircledBitmapProcessor setStrokeResource(int width, int colorId) {
            return setStroke(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, width, resources.getDisplayMetrics()), resources.getColor(colorId));
        }

        public CircledBitmapProcessor setStroke(float width, int color) {
            if (width == 0)
                mStroke = null;
            else {
                mStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
                mStroke.setColor(color);
                mStroke.setStrokeWidth(width);
                mStroke.setStyle(Paint.Style.STROKE);
            }
            return this;
        }

        public CircledBitmapProcessor(Resources resources) {
            this.resources = resources;
        }

        protected Drawable getEmptyDrawable() {
            return null;
        }

        @Override
        public Drawable process(Bitmap bitmap) {
            if (bitmap == null)
                return getEmptyDrawable();
            return new CircleBitmapDrawable(getResources(), bitmap, mStroke);

        }

        public Resources getResources() {
            return resources;
        }
    }

    static class ShaderBitmapDrawable extends BitmapDrawable {
        private final Rect mDstRect = new Rect();   // Gravity.apply() sets this
        private boolean builtShader;
        private final Paint mPaint;
        protected Paint mStroke;
        private final Resources resources;


        ShaderBitmapDrawable(Resources res, Bitmap bitmap) {
            super(res, bitmap);
            resources = res;

            mPaint = new Paint();
            mPaint.setDither(true);
            mPaint.setAntiAlias(true);

        }

        public ShaderBitmapDrawable setStrokeResource(int width, int colorId) {
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, resources.getDisplayMetrics());

            return setStroke(width, resources.getColor(colorId));
        }

        public ShaderBitmapDrawable setStroke(int width, int color) {
            if (width == 0)
                mStroke = null;
            else {
                mStroke = new Paint();
                mStroke.setColor(color);
                mStroke.setStrokeWidth(width);
                mStroke.setDither(true);
                mStroke.setAntiAlias(true);
                mStroke.setStyle(Paint.Style.STROKE);
            }
            return this;
        }

        void buildShader() {
            Bitmap bitmap = getBitmap();
            if (bitmap == null) return;

            BitmapShader shader = new BitmapShader(bitmap, getTileModeX() != null ? getTileModeX() : Shader.TileMode.CLAMP, getTileModeY() != null ? getTileModeY() : Shader.TileMode.CLAMP);
            builtShader = true;
            mPaint.setShader(shader);
        }

        @Override
        public void setTileModeXY(Shader.TileMode xmode, Shader.TileMode ymode) {
            super.setTileModeXY(xmode, ymode);
            buildShader();
        }

        protected void draw(Canvas canvas, Paint paint, Rect rect) {

            Rect area = rect;
            canvas.drawRect(area, paint);
            if (mStroke != null) {
                canvas.drawRect(area, mStroke);
            }
        }

        @Override
        public final void draw(Canvas canvas) {
            if (!builtShader)
                buildShader();
            copyBounds(mDstRect);
            draw(canvas, mPaint, mDstRect);

        }
    }


    static class RoundedBitmapDrawable extends ShaderBitmapDrawable {

        private final float xRadius;
        private final float yRadius;

        RoundedBitmapDrawable(Resources res, Bitmap bitmap, float xRadius, float yRadius) {
            super(res, bitmap);

            this.xRadius = xRadius;
            this.yRadius = yRadius;

        }


        @Override
        public void draw(Canvas canvas, Paint paint, Rect rect) {

            RectF area = new RectF(rect);
            canvas.drawRoundRect(area, xRadius, yRadius, paint);
            if (mStroke != null) {
                area.inset(1, 1);

                canvas.drawRoundRect(area, xRadius, yRadius, mStroke);
            }
        }
    }


    public static class RoundedBitmapProcessor implements BitmapProcessor {
        private final Resources resources;
        private final float xRadius;
        private final float yRadius;
        private int strokeWidth;
        private int strokeColor;


        public RoundedBitmapProcessor setStrokeResource(int width, int colorId) {
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
            return setStroke(width, resources.getColor(colorId));
        }

        public RoundedBitmapProcessor setStroke(int width, int color) {
            strokeColor = color;
            strokeWidth = width;
            return this;
        }


        public RoundedBitmapProcessor(Resources resources, int xRadius, int yRadius) {
            this.resources = resources;

            this.xRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, xRadius, resources.getDisplayMetrics());
            this.yRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, yRadius, resources.getDisplayMetrics());
        }


        protected Drawable getEmptyDrawable() {
            return null;
        }

        @Override
        public Drawable process(Bitmap bitmap) {
            if (bitmap == null)
                return getEmptyDrawable();

            RoundedBitmapDrawable drawable = new RoundedBitmapDrawable(resources, bitmap, xRadius, yRadius);
            if (strokeWidth > 0)
                drawable.setStroke(strokeWidth, strokeColor);
            return drawable;
        }


        public Resources getResources() {
            return resources;
        }
    }
}

