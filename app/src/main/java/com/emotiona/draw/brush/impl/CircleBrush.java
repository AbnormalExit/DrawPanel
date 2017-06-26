package com.emotiona.draw.brush.impl;

import android.graphics.Path;

import com.emotiona.draw.brush.IBrush;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:圆形画笔
 */

public class CircleBrush implements IBrush{
    @Override
    public void down(Path path, float x, float y) {
        path.moveTo(x,y);
    }

    @Override
    public void move(Path path, float x, float y) {
        path.addCircle(x,y,5, Path.Direction.CW);
    }

    @Override
    public void up(Path path, float x, float y) {

    }
}
