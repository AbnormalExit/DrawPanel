package com.emotiona.draw.brush.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.emotiona.draw.brush.IDraw;
import com.emotiona.draw.invoker.PaintMode;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:绘制路径
 */

public class DrawPath implements IDraw {
    private Path path;//需要绘制的路径
    private Paint paint;//绘制的画笔
    private PaintMode mMode = PaintMode.DRAW;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setPaintMode(PaintMode mode) {
        this.mMode = mode;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mMode == PaintMode.DRAW.DRAW) {
            paint.setXfermode(null);
        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public void undo() {

    }
}
