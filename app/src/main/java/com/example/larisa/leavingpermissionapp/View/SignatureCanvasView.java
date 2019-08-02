/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.larisa.leavingpermissionapp.Activity.SignatureActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignatureCanvasView extends View {

    private Paint signPaint;
    private Bitmap bitmap;
    private Path path;

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
        canvas.drawPath(path, signPaint);
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
        path.reset();
        invalidate();
    }

    public void save(SignatureActivity signatureActivity) {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(), Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        this.draw(canvas);

        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File file = new File(dirPath, "signature.png");
        try {
            FileOutputStream mFileOutStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
            mFileOutStream.flush();
            mFileOutStream.close();

            signatureActivity.onSuccesfulImageSave();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
