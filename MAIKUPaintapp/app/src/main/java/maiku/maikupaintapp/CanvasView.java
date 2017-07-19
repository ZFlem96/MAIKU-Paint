package maiku.maikupaintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.graphics.Color.BLACK;

/**
 * Created by Randy McClure on 7/13/2017.
 */

public class CanvasView extends View {
    public int backgroundColorID =R.color.white;
    public Bitmap canvasBitmap;
    private Canvas canvasArea;
    private ArrayList<Line> lines;
    private Path drawingPath;
    private Paint drawingPaint;
    private float posX, posY;
    private static final float MOVEMENT_TOLERANCE = 5;
    private boolean colorSwitchOn = false, sizeSwitchOn = false;
	private final String PACKAGE_NAME = "maiku.maikupaintapp";

    public void setBackgroundColorID(int color){
    backgroundColorID = color;
}
    public int getBackgroundColorID(){
    return backgroundColorID;
}

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
    public void Switch(){
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
        if(lines.size() != 0){
            lines.remove(lines.size() - 1);
        }
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

        drawingPaint = new Paint(drawingPaint);
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

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //Save Image
    public boolean saveImage(){
	    boolean imageSaved = false;

		File imagePath = getOutputPath();
	    if(imagePath != null){
		    try{
			    FileOutputStream output = new FileOutputStream(imagePath);
			    canvasBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
			    output.close();
			    imageSaved = true;
		    }
		    catch(FileNotFoundException e){
			    Log.e("File not found: ", e.getMessage());
		    }
		    catch(IOException e){
			    Log.e("Error accessing file: ", e.getMessage());
		    }
		}
		return imageSaved;
    }

    private File getOutputPath(){
	    File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
			    "/Android/data/" +
	            PACKAGE_NAME +
	            "/images");

	    if(!storageDir.exists()){
		    if(!storageDir.mkdirs()){
			    return null;
		    }
	    }

		String imageName = (new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date())) + ".png";
	    File filePath = new File(storageDir.getPath() + File.separator + imageName);
	    return filePath;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void drawCircle() {
        Path path = new Path();
        path.moveTo(posX,posY);
        path.addCircle(posX,posY,60, Path.Direction.CCW);
        canvasArea.drawCircle(posX, posY, 60, drawingPaint);
        lines.add(new Line(path, new Paint(){
            {
                this.setColor(drawingPaint.getColor());
                this.setStyle(drawingPaint.getStyle());
                this.setStrokeJoin(drawingPaint.getStrokeJoin());
                this.setStrokeWidth(drawingPaint.getStrokeWidth());
                Switch();
            }
        }));
        invalidate();
    }

    public void drawRectangle() {
        Path path = new Path();
        path.moveTo(posX,posY);
        path.addRect(posX,posY, posX+50, posY+350, Path.Direction.CCW);
//                      200  300  250  350
        canvasArea.drawRect(posX,posY, posX+50, posY+350, drawingPaint);
        lines.add(new Line(drawingPath, new Paint(){
            {
                this.setColor(drawingPaint.getColor());
                this.setStyle(drawingPaint.getStyle());
                this.setStrokeJoin(drawingPaint.getStrokeJoin());
                this.setStrokeWidth(drawingPaint.getStrokeWidth());
                Switch();
            }
        }));
        invalidate();
    }
    public void drawTriangle() {
        Path path = new Path();
        path.moveTo(posX,posY);//x,y
        float tmpX = posX, tmpY = posY;
        posX = posX+100;
        posY = posY+200;
        path.lineTo(posX,posY);
        path.moveTo(posX,posY);//x+100, y+200
        posX = posX-200;
        path.lineTo(posX,posY);//x-100, y+200
        path.moveTo(posX,posY);
        posX = tmpX;
        posY = tmpY;
        path.lineTo(posX,posY);
        canvasArea.drawPath(path, drawingPaint);
        lines.add(new Line(path, new Paint(){
            {
                this.setColor(drawingPaint.getColor());
                this.setStyle(drawingPaint.getStyle());
                this.setStrokeJoin(drawingPaint.getStrokeJoin());
                this.setStrokeWidth(drawingPaint.getStrokeWidth());
                Switch();
            }
        }));
        invalidate();
    }
    public void change() {
        int[] androidColors= getResources().getIntArray(R.array.androidcolors);
        backgroundColorID = androidColors[new Random().nextInt(androidColors.length)];
        this.setBackgroundColor(backgroundColorID);

    }

    public void setBrushSize(float brushSize) {
        drawingPaint.setStrokeWidth(brushSize);
    }

    public void setBrushColor(int brushColor){
        drawingPaint.setColor(getResources().getColor(brushColor));
    }

    public void setBrushColor(){
        drawingPaint.setColor(getResources().getColor(backgroundColorID));
    }
}
