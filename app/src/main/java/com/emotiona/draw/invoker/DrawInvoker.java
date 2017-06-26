package com.emotiona.draw.invoker;

import android.graphics.Canvas;

import com.emotiona.draw.brush.impl.DrawPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sxshi on 2016/10/11.
 * @email:emotiona_xiaoshi@126.com
 * @describe:绘制请求封装类
 */

public class DrawInvoker {
    /***
     * 绘制列表
     */
    private List<DrawPath> drawList = Collections.synchronizedList(new ArrayList<DrawPath>());
    //重做列表
    private List<DrawPath> redoList = Collections.synchronizedList(new ArrayList<DrawPath>());

    /***
     * 增加一条绘画命令
     * @param command
     */
    public void add(DrawPath command) {
        redoList.clear();
        if (!drawList.contains(command))
            drawList.add(command);
    }

    /***
     * 撤销上一步命令
     */
    public void undo() {
        if (drawList.size() > 0) {
            DrawPath undo = drawList.get(drawList.size() - 1);
            drawList.remove(drawList.size() - 1);
            undo.undo();
            redoList.add(undo);
        }
    }

    /***
     * 重做上一条命令
     */
    public void redo() {
        if (redoList.size() > 0) {
            DrawPath redoCommand = redoList.get(redoList.size() - 1);
            redoList.remove(redoList.size() - 1);
            drawList.add(redoCommand);
        }
    }

    /***
     * 执行命令
     * @param canvas
     */
    public void execute(Canvas canvas) {
        if (drawList != null) {
            for (DrawPath drawPath : drawList) {
                drawPath.draw(canvas);
            }
        }
    }

    /***
     * 是否可以重做
     * @return
     */
    public boolean canRedo() {
        return redoList.size() > 0;
    }

    /***
     * 是否可以撤销
     * @return
     */
    public boolean canUndo() {
        return drawList.size() > 0;
    }
}
