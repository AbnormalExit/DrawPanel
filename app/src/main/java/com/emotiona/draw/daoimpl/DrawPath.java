package com.emotiona.draw.daoimpl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.emotiona.draw.dao.IDraw;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:绘制路径
 */

public class DrawPath implements IDraw{
    private Path path;//需要绘制的路径
    private Paint paint;//绘制的画笔

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

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path,paint);
    }

    @Override
    public void undo() {

    }
}
