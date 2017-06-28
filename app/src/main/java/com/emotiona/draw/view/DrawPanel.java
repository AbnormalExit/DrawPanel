package com.emotiona.draw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.emotiona.draw.R;
import com.emotiona.draw.brush.impl.DrawPath;
import com.emotiona.draw.invoker.DrawInvoker;
import com.emotiona.draw.invoker.PaintMode;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:实现自定义画布
 */

public class DrawPanel extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = this.getClass().getSimpleName();
    public boolean isDrawing, isRunning;//表示是否可以绘制，绘制线程是否可运行

    private Bitmap mBitmap;//原始位图

    private DrawInvoker drawInvoker;//绘制命令请求对象
    private DrawThread drawThread;//绘制线程

    private boolean isEraser = false;

    public DrawPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
        Log.d(TAG, "surfaceCreated: start");
        isRunning = true;
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged: start");
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: start");
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
                        //设置背景色只能用drawRGB方式才能生效
                        c.drawRGB(218, 210, 213);// TODO: 2017/6/28 设置背景橡皮擦功能不能用
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

    /**
     * @return save bitmap
     */
    public Bitmap buildBitmap() {
        Bitmap result = Bitmap.createBitmap(mBitmap);
        destroyDrawingCache();
        return result;
    }
}
