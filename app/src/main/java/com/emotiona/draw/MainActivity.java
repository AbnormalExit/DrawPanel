package com.emotiona.draw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.emotiona.draw.brush.IBrush;
import com.emotiona.draw.brush.impl.DrawPath;
import com.emotiona.draw.brush.impl.CircleBrush;
import com.emotiona.draw.brush.impl.NormalBrush;
import com.emotiona.draw.invoker.PaintMode;
import com.emotiona.draw.utils.BitmapUtils;
import com.emotiona.draw.view.DrawPanel;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MainActivity";
    private DrawPanel mPanel;
    private Button btn_red;
    private Button btn_green;
    private Button btn_blue;
    private Button btn_normal;
    private Button btn_circle;
    private Button btn_undo;
    private Button btn_redo;
    private Button btn_eraser;

    private Paint mPaint;//画笔
    private DrawPath mPath;//绘制路径命令
    private IBrush mBrush;//笔触对象

    private int mPaintSize = 20;
    private int mEeaserSize = 40;
    private Handler mHandler;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;

    private boolean isEraser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNormalPaint();
        initView();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SAVE_FAILED:
                        Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        break;
                    case MSG_SAVE_SUCCESS:
                        Toast.makeText(MainActivity.this, "画板已保存", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    /**
     * normal paint
     */
    private void initNormalPaint() {
        mPaint = new Paint();
        mBrush = new NormalBrush();
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mPaintSize);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xFF0000FF);
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
        btn_eraser = (Button) findViewById(R.id.btn_eraser);

        btn_red.setOnClickListener(this);
        btn_green.setOnClickListener(this);
        btn_blue.setOnClickListener(this);
        btn_normal.setOnClickListener(this);
        btn_circle.setOnClickListener(this);
        btn_undo.setOnClickListener(this);
        btn_redo.setOnClickListener(this);
        btn_eraser.setOnClickListener(this);
        mPanel.setOnTouchListener(new DrawTouchListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bm = mPanel.buildBitmap();
                        String savedFile = BitmapUtils.saveImage(bm, 100);
                        if (savedFile != null) {
                            sendBroadcastPhoto(MainActivity.this, savedFile);
                            mHandler.obtainMessage(MSG_SAVE_SUCCESS).sendToTarget();
                        } else {
                            mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
                        }
                    }
                }).start();
                break;
        }
        return true;
    }

    /**
     * notice photo
     *
     * @param context  context
     * @param filePath file path
     */
    private static void sendBroadcastPhoto(Context context, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_red:
                isEraser = false;
                mPaint = new Paint();
                mPaint.setXfermode(null);
                mPaint.setStrokeWidth(mPaintSize);
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(0xFFFF0000);
                break;
            case R.id.btn_green:
                isEraser = false;
                mPaint = new Paint();
                mPaint.setXfermode(null);
                mPaint.setStrokeWidth(mPaintSize);
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(0xFF00FF00);
                break;
            case R.id.btn_blue:
                isEraser = false;
                mPaint = new Paint();
                mPaint.setXfermode(null);
                mPaint.setStrokeWidth(mPaintSize);
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(0xFF0000FF);
                break;
            case R.id.btn_eraser:
                isEraser = true;
                mPaint = new Paint();
                mPaint.setColor(Color.rgb(218, 210, 213));
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mEeaserSize);
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
                Log.d(TAG, "onTouch: down");
                mPath = new DrawPath();
                if (isEraser) mPath.setPaintMode(PaintMode.ERASER);
                mPath.setPaint(mPaint);
                mPath.setPath(new Path());
                mBrush.down(mPath.getPath(), event.getX(), event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                Log.d(TAG, "onTouch: move");
                mBrush.move(mPath.getPath(), event.getX(), event.getY());
                addPath();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mBrush.up(mPath.getPath(), event.getX(), event.getY());
                Log.d(TAG, "onTouch: up");
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
