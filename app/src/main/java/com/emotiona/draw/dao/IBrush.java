package com.emotiona.draw.dao;

import android.graphics.Path;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:画笔接口
 */

public interface IBrush {
    /***
     * 按下时候
     * @param path
     * @param x
     * @param y
     */
     void down(Path path ,float x,float y);

    /***
     * 移动的时候
     * @param path
     * @param x
     * @param y
     */
    void move(Path path,float x,float y);

    /***
     * 离开屏幕
     * @param path
     * @param x
     * @param y
     */
    void up(Path path ,float x,float y);
}
