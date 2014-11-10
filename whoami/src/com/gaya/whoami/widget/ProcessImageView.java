package com.gaya.whoami.widget;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.android.volley.toolbox.NetworkImageView;

/**
 * @author suriel
 *         Date: 12/25/13
 *         Time: 10:21 AM
 */
public class ProcessImageView extends NetworkImageView {


    private boolean border = false;

    private void setFramePaint(Paint p, int side, float iw, float ih) {
        // paint, side of rect, image width, image height

        p.setShader(null);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        float borderSize = 0.1f; //relative size of border
        //use the smaller image size to calculate the actual border size
        float bSize = (iw > ih) ? ih * borderSize : ih * borderSize;
        float g1x = 0;
        float g1y = 0;
        float g2x = 0;
        float g2y = 0;
        int c1 = 0, c2 = 0;

        if (side == 1) {
            //left
            g1x = 0;
            g1y = ih / 2;
            g2x = bSize;
            g2y = ih / 2;
            c1 = Color.TRANSPARENT;
            c2 = Color.BLACK;

        } else if (side == 2) {
            //top
            g1x = iw / 2;
            g1y = 0;
            g2x = iw / 2;
            g2y = bSize;
            c1 = Color.TRANSPARENT;
            c2 = Color.BLACK;


        } else if (side == 3) {
            //right
            g1x = iw;
            g1y = ih / 2;
            g2x = iw - bSize;
            g2y = ih / 2;
            c1 = Color.TRANSPARENT;
            c2 = Color.BLACK;


        } else if (side == 4) {
            //bottom
            g1x = iw / 2;
            g1y = ih;
            g2x = iw / 2;
            g2y = ih - bSize;
            c1 = Color.TRANSPARENT;
            c2 = Color.BLACK;
        }

        p.setShader(new LinearGradient(g1x, g1y, g2x, g2y, c1, c2, Shader.TileMode.CLAMP));

    }

    private ImageProcessors.BitmapProcessor bitmapProcessor;// = new ImageHelpers.CircledBitmapProcessor(getResources());

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        if (border) {

            float circleX = getWidth() / 2, circleY = getHeight() / 2,
                    radius = Math.min(getWidth(), getHeight()) / 2;
            RadialGradient gradient = new RadialGradient(
                    circleX, circleY, radius, 0xFF000000, 0x00000000,
                    Shader.TileMode.CLAMP);

            // Draw transparent circle into tempBitmap
            Paint p = new Paint();
            p.setShader(gradient);
            p.setColor(0xFF000000);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawCircle(circleX, circleY, radius, p);
            super.draw(canvas);
        } else
            super.draw(canvas);
    }


    public ImageProcessors.BitmapProcessor getBitmapProcessor() {
        return bitmapProcessor;
    }

    public ProcessImageView setBitmapProcessor(ImageProcessors.BitmapProcessor bitmapProcessor) {
        this.bitmapProcessor = bitmapProcessor;
        return this;
    }

    protected Drawable process(Bitmap bitmap) {
        if (bitmapProcessor != null)
            return bitmapProcessor.process(bitmap);
        if (bitmap == null)
            return null;
        return new BitmapDrawable(getResources(), bitmap);
    }

    public ProcessImageView(Context context) {
        super(context);
    }

    public ProcessImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProcessImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageDrawable(process(bm));
        //super.setImageBitmap(bm);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (getDrawable() instanceof BitmapDrawable) {
            setImageBitmap(((BitmapDrawable) getDrawable()).getBitmap());
        }
    }

    public boolean isBorder() {
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }
}
