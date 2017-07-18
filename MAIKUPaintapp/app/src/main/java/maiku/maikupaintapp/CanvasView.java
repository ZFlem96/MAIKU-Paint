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
import java.util.Random;

import static android.graphics.Color.BLACK;

/**
 * Created by Randy McClure on 7/13/2017.
 */

public class CanvasView extends View {
    public int colorIndex =0;
    public Bitmap canvasBitmap;
    private Canvas canvasArea;
    private ArrayList<Line> lines;
    private Path drawingPath;
    private Paint drawingPaint;
    private float posX, posY;
    private static final float MOVEMENT_TOLERANCE = 5;
    private boolean colorSwitchOn = false, sizeSwitchOn = false;



    public CanvasView(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);

        lines = new ArrayList<>();

        drawingPaint = new Paint();
        drawingPaint.setAntiAlias(true);
        drawingPaint.setColor(Color.RED);
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setStrokeJoin(Paint.Join.ROUND);
        drawingPaint.setStrokeWidth(4f);

    }
//    random color/size switcher
    public void Switch(Color color){
        if(colorSwitchOn){
            Random rand = new Random();
            int rnd = rand.nextInt(8);
            //black,red,white,argb(255, 247, 148, 29),argb(255, 255, 242, 0),argb(255, 57, 181, 74), argb(255, 0, 174, 239),argb(255, 133, 96, 168)
            switch (rnd){
                case 0:
                    drawingPaint.setColor(Color.WHITE);
                    break;
                case 1:
                    drawingPaint.setColor(Color.BLACK);
                    break;
                case 2:
                    drawingPaint.setColor(Color.RED);
                    break;
                case 3:
                    drawingPaint.setARGB(255, 247, 148, 29);
                    break;
                case 4:
                    drawingPaint.setARGB(255, 255, 242, 0);
                    break;
                case 5:
                    drawingPaint.setARGB(255, 57, 181, 74);
                    break;
                case 6:
                    drawingPaint.setARGB(255, 0, 174, 239);
                    break;
                case 7:
                    drawingPaint.setARGB(255, 133, 96, 168);
                    break;

            }
        }
        if(sizeSwitchOn){
            Random rand = new Random();
            int rnd = rand.nextInt(5);
            switch (rnd) {
                case 0:
                    drawingPaint.setStrokeWidth(4f);
                    break;
                case 1:
                    drawingPaint.setStrokeWidth(7f);
                    break;
                case 2:
                    drawingPaint.setStrokeWidth(10f);
                    break;
                case 3:
                    drawingPaint.setStrokeWidth(13f);
                    break;
                case 4:
                    drawingPaint.setStrokeWidth(16f);
                    break;
            }
        }
    }
    public boolean colorSwitch(int bttnClick){
        if(bttnClick%2!=0){
            colorSwitchOn = true;
        }
        else if(bttnClick%2==0){
            colorSwitchOn = false;
        }
        return colorSwitchOn;
    }
    public boolean sizeSwitch(int bttnClick){
        if(bttnClick%2!=0){
            sizeSwitchOn = true;
        }
        else if(bttnClick%2==0){
            sizeSwitchOn = false;
        }
        return sizeSwitchOn;
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

        if(drawingPath != null){
	        canvas.drawPath(drawingPath, drawingPaint);
        }
    }

	public void undoDraw(){
		drawingPath.reset();
		lines.remove(lines.size() - 1);
		invalidate();
	}

	public void clearCanvas(){
        drawingPath.reset();
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
    public void drawCircle() {
        drawingPath.addCircle(posX,posY,60, Path.Direction.CCW);
        canvasArea.drawCircle(posX, posY, 60, drawingPaint);
//        storedPaths.add(new Path(drawingPaint, new Paint(){
//            {
//                this.setColor(drawingPaint.getColor());
//                this.setStyle(drawingPaint.getStyle());
//                this.setStrokeJoin(drawingPaint.getStrokeJoin());
//                this.setStrokeWidth(drawingPaint.getStrokeWidth());
//                Switch();
//            }
//        }));
        invalidate();
    }

    public void drawRectangle() {
        drawingPath.addRect(posX,posY, posX+50, posY+350, Path.Direction.CCW);
//                      200  300  250  350
        canvasArea.drawRect(posX,posY, posX+50, posY+350, drawingPaint);
//        storedPaths.add(new Path(drawingPath, new Paint(){
//            {
//                this.setColor(drawingPaint.getColor());
//                this.setStyle(drawingPaint.getStyle());
//                this.setStrokeJoin(drawingPaint.getStrokeJoin());
//                this.setStrokeWidth(drawingPaint.getStrokeWidth());
//                Switch();
//            }
//        })
//        )
        ;
        invalidate();
    }
    public void drawTriangle() {
        drawingPath.moveTo(posX,posY);//x,y
        float tmpX = posX, tmpY = posY;
        posX = posX+100;
        posY = posY+200;
        drawingPath.lineTo(posX,posY);
        drawingPath.moveTo(posX,posY);//x+100, y+200
        posX = posX-200;
        drawingPath.lineTo(posX,posY);//x-100, y+200
        drawingPath.moveTo(posX,posY);
        posX = tmpX;
        posY = tmpY;
        drawingPath.lineTo(posX,posY);
        canvasArea.drawPath(drawingPath, drawingPaint);
//        storedPaths.add(new Path(drawingPath, new Paint(){
//            {
//                this.setColor(drawingPaint.getColor());
//                this.setStyle(drawingPaint.getStyle());
//                this.setStrokeJoin(drawingPaint.getStrokeJoin());
//                this.setStrokeWidth(drawingPaint.getStrokeWidth());
//                Switch();
//            }
//        }));
        invalidate();
    }
    public void change() {
        if (colorIndex==0) {
            this.setBackgroundColor(BLACK);
            colorIndex++;
        }
        else if (colorIndex==1) {
            this.setBackgroundColor(Color.argb(255, 247, 148, 29));
            colorIndex++;
        }
        else if (colorIndex==2) {
            this.setBackgroundColor(Color.argb(255, 255, 242, 0));
            colorIndex++;
        }
        else if (colorIndex==3) {
            this.setBackgroundColor(Color.RED);
            colorIndex++;
        }
        else if (colorIndex==4) {
            this.setBackgroundColor(Color.argb(255, 57, 181, 74));
            colorIndex++;
        }
        else if (colorIndex==5) {
            this.setBackgroundColor(Color.argb(255, 0, 174, 239));
            colorIndex++;
        }
        else if (colorIndex==6) {
            this.setBackgroundColor(Color.argb(255, 133, 96, 168));
            colorIndex++;
        }
        else if (colorIndex==7) {
            this.setBackgroundColor(Color.WHITE);
            colorIndex = 0;
        }
    }
}
