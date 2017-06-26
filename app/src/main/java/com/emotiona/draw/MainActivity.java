package com.emotiona.draw;

import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.emotiona.draw.brush.IBrush;
import com.emotiona.draw.brush.impl.DrawPath;
import com.emotiona.draw.brush.impl.CircleBrush;
import com.emotiona.draw.brush.impl.NormalBrush;
import com.emotiona.draw.view.DrawPanel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawPanel mPanel;
    private Button btn_red;
    private Button btn_green;
    private Button btn_blue;
    private Button btn_normal;
    private Button btn_circle;
    private Button btn_undo;
    private Button btn_redo;

    private Paint mPaint;//画笔
    private DrawPath mPath;//绘制路径命令
    private IBrush mBrush;//笔触对象

    private int mPaintWidth = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPaint = new Paint();
        mBrush = new NormalBrush();
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xFF0000FF);
        initView();
    }

    private void initView() {
        mPanel = (DrawPanel) findViewById(R.id.mPanel);
        btn_red = (Button) findViewById(R.id.btn_red);
        btn_green = (Button) findViewById(R.id.btn_green);
        btn_blue = (Button) findViewById(R.id.btn_blue);
        btn_normal = (Button) findViewById(R.id.btn_normal);
        btn_circle = (Button) findViewById(R.id.btn_circle);
        btn_undo = (Button) findViewById(R.id.btn_undo);
        btn_redo = (Button) findViewById(R.id.btn_redo);

        btn_red.setOnClickListener(this);
        btn_green.setOnClickListener(this);
        btn_blue.setOnClickListener(this);
        btn_normal.setOnClickListener(this);
        btn_circle.setOnClickListener(this);
        btn_undo.setOnClickListener(this);
        btn_redo.setOnClickListener(this);
        mPanel.setOnTouchListener(new DrawTouchListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_red:
                mPaint=new Paint();
                mPaint.setStrokeWidth(mPaintWidth);
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(0xFFFF0000);
                break;
            case R.id.btn_green:
                mPaint=new Paint();
                mPaint.setStrokeWidth(mPaintWidth);
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(0xFF00FF00);
                break;
            case R.id.btn_blue:
                mPaint=new Paint();
                mPaint.setStrokeWidth(mPaintWidth);
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(0xFF0000FF);
                break;
            case R.id.btn_normal:
                mBrush = new NormalBrush();
                break;
            case R.id.btn_circle:
                mBrush = new CircleBrush();
                break;
            case R.id.btn_undo:
                mPanel.undo();
                if (!mPanel.canUndo()) {
                    btn_undo.setEnabled(false);
                }
                btn_redo.setEnabled(true);
                break;
            case R.id.btn_redo:
                mPanel.redo();
                if (!mPanel.canRedo()) {
                    btn_redo.setEnabled(false);
                }
                btn_undo.setEnabled(true);
                break;
        }
    }

    private class DrawTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPath = new DrawPath();
                mPath.setPaint(mPaint);
                mPath.setPath(new Path());
                mBrush.down(mPath.getPath(), event.getX(), event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mBrush.move(mPath.getPath(), event.getX(), event.getY());
                addPath();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mBrush.up(mPath.getPath(), event.getX(), event.getY());
            }
            return true;
        }
    }

    /**
     * add to draw path
     */
    private void addPath() {
        mPanel.add(mPath);
        mPanel.isDrawing = true;
        btn_undo.setEnabled(true);
        btn_redo.setEnabled(false);
    }
}
