package com.emotiona.draw.dao;

import android.graphics.Canvas;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:具体绘画接口
 */

public interface IDraw {
    /***
     * 绘制命令
     * @param canvas
     */
    void draw(Canvas canvas);

    /**
     * 撤销
     */
    void undo();
}
