package com.emotiona.draw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.emotiona.draw.brush.impl.DrawPath;
import com.emotiona.draw.invoker.DrawInvoker;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:实现自定义画布
 */

public class DrawPanel extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = this.getClass().getSimpleName();
    public boolean isDrawing, isRunning;//表示是否可以绘制，绘制线程是否可运行

    private Bitmap mBitmap;//绘制到的位图像
    private DrawInvoker drawInvoker;//绘制命令请求对象
    private DrawThread drawThread;//绘制线程

    public DrawPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawInvoker = new DrawInvoker();
        drawThread = new DrawThread();
        this.setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().addCallback(this);
    }

    public void add(DrawPath drawPath) {
        drawInvoker.add(drawPath);
    }

    public void redo() {
        isDrawing = true;
        drawInvoker.redo();

    }

    public void undo() {
        isDrawing = true;
        drawInvoker.undo();
    }

    public boolean canUndo() {
        return drawInvoker.canUndo();
    }

    public boolean canRedo() {
        return drawInvoker.canRedo();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        isRunning = false;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DrawThread extends Thread {
        @Override
        public void run() {
            Canvas canvas = null;
            while (isRunning) {
                if (isDrawing) {
                    try {
                        canvas = getHolder().lockCanvas(null);
                        if (mBitmap == null) {
                            mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                        }
                        Canvas c = new Canvas(mBitmap);
                        c.drawColor(Color.parseColor("#D6D7D7"), PorterDuff.Mode.CLEAR);
                        drawInvoker.execute(c);
                        canvas.drawBitmap(mBitmap, 0, 0, null);
                    } finally {
                        getHolder().unlockCanvasAndPost(canvas);
                    }
                    isDrawing = false;
                }
            }
        }
    }
}
