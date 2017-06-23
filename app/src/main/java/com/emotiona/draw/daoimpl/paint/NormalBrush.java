package com.emotiona.draw.daoimpl.paint;

import android.graphics.Path;

import com.emotiona.draw.dao.IBrush;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:普通画笔
 */

public class NormalBrush implements IBrush{
    @Override
    public void down(Path path, float x, float y) {
        path.moveTo(x,y);
    }

    @Override
    public void move(Path path, float x, float y) {
        path.lineTo(x,y);
    }

    @Override
    public void up(Path path, float x, float y) {

    }
}
