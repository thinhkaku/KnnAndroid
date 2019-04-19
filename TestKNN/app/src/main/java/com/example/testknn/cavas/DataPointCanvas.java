package com.example.testknn.cavas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.testknn.model.Category;
import com.example.testknn.model.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class DataPointCanvas extends View {
    private Paint paint;
    private List<DataPoint> listDataPoints = new ArrayList<>();

    public DataPointCanvas(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    private void setupPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
    }

    public void setListDataPoints(List<DataPoint> listDataPoints) {
        this.listDataPoints = listDataPoints;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (DataPoint dataPoint : listDataPoints) {
            if (dataPoint.getCategory() == Category.RED) {
                paint.setColor(Color.RED);
                Log.d("data: ", "1");
            } else if (dataPoint.getCategory() == Category.BLUE) {
                paint.setColor(Color.BLUE);
                Log.d("data: ", "2");
            } else if (dataPoint.getCategory() == Category.TEST) {
                paint.setColor(Color.BLACK);
                Log.d("data: ", "3");
            }
            canvas.drawCircle((float) dataPoint.getX() + 100, (float) dataPoint.getY() + 100, 5, paint);

        }
    }

}
