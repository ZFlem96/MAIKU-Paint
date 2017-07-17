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

    public Bitmap canvasBitmap;
    private Canvas canvasArea;
    private ArrayList<Line> lines;
    private Path drawingPath;
    private Paint drawingPaint;
    private float posX, posY;
    private static final float MOVEMENT_TOLERANCE = 5;


    public CanvasView(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);

        lines = new ArrayList<>();

        drawingPaint = new Paint();
        drawingPaint.setAntiAlias(true);
        drawingPaint.setColor(Color.BLACK);
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setStrokeJoin(Paint.Join.ROUND);
        drawingPaint.setStrokeWidth(4f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasArea = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Line line : lines){
            canvas.drawPath(line.getPath(), line.getPaint());
        }
    }

	public void undoDraw(){
		lines.remove(lines.size() - 1);
		invalidate();
	}

	public void clearCanvas(){
		lines.clear();
		invalidate();
	}

    private void drawStart(float x, float y){
        drawingPath = new Path();
        drawingPath.moveTo(x, y);
        posX = x;
        posY = y;
    }

    private void drawMove(float x, float y){
        float deltaX = Math.abs(x - posX);
        float deltaY = Math.abs(y - posY);
        if(deltaX >= MOVEMENT_TOLERANCE || deltaY >= MOVEMENT_TOLERANCE){
	        drawingPath.quadTo(posX, posY, (x + posX)/2, (y + posY)/2);
            posX = x;
            posY = y;
        }

        drawingPath.lineTo(posX, posY);
    }

    private void drawEnd(){
        drawingPath.lineTo(posX, posY);

        lines.add(new Line(drawingPath, drawingPaint));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                drawMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                drawEnd();
                invalidate();
                break;
        }
        return true;
    }
}
