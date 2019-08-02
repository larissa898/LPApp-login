/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SignatureCanvasView extends View {

    private Paint signPaint;
    private Path path;

    private boolean clearCanvas = false;

    public SignatureCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        initSigningPaint();
    }

    private void initSigningPaint() {
        signPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        signPaint.setColor(Color.BLACK);
        signPaint.setStrokeJoin(Paint.Join.ROUND);
        signPaint.setStyle(Paint.Style.STROKE);
        signPaint.setStrokeCap(Paint.Cap.ROUND);
        signPaint.setStrokeWidth(10f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (clearCanvas) {
            path = new Path();
            canvas.drawColor(Color.WHITE);
            clearCanvas = false;
        } else {
            canvas.drawPath(path, signPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void clear() {
        clearCanvas = true;
        invalidate();
    }

}
