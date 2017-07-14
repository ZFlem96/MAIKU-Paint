package maiku.maikupaintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Randy McClure on 7/13/2017.
 */

public class CanvasView extends View {

//    public int colorIndex;
//    public Bitmap bitmap;
//    public Canvas canvas;
//    public Path path;
//    public Paint paint;
//    public float posX, posY;
//    public final int TOLERANCE = 5;
//    public ArrayList<PaintPath> storedPaths = new ArrayList<>();
//
//
//    public CanvasView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//
//    class PaintPath{
//
//    }

    public int width, height;
    public Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    Context context;
    private Paint paint;
    private float posX, posY;
    private static final float TOLERANCE = 5;


    public CanvasView(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);
        context = c;

        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
    }

    private void startTouch(float x, float y){
        path.moveTo(x, y);
        posX = x;
        posY = y;
    }

    private void moveTouch(float x, float y){
        float deltaX = Math.abs(x - posX);
        float deltaY = Math.abs(y - posY);
        if(deltaX >= TOLERANCE || deltaY >= TOLERANCE){
            path.quadTo(posX, posY, (x + posX)/2, (y + posY)/2);
            posX = x;
            posY = y;
        }
    }

    public void clearCanvas(){
        path.reset();
        invalidate();
    }

    private void stopTouch(){
        path.lineTo(posX, posY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                stopTouch();
                invalidate();
                break;
        }
        return true;
    }
}
