package maiku.maikupaintapp;

import android.content.Context;
import android.graphics.Color;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.MenuPopupWindow;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Random;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CanvasView customCanvas;
    int cSwitch = 0, sSwitch = 0;
    public MenuItem colorChanger;
    private boolean hide = true;
    private TableLayout tools;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tools = (TableLayout)findViewById(R.id.toolsTableLayout);
        tools.setVisibility(View.INVISIBLE);
        customCanvas = (CanvasView) findViewById(R.id.canvas);
        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        customCanvas.setBackgroundColor(randomAndroidColor);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

//    public void clearButtonClick(View view){
//        customCanvas.clearCanvas();
//    }

//    public void undoButtonClick(View view) { customCanvas.undoDraw(); }
    public void randomColorSwitch(View v){
        customCanvas.colorSwitch(cSwitch);
        cSwitch++;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.clearOption:
                customCanvas.clearCanvas();
                return true;
            case R.id.undoOption:
                customCanvas.undoDraw();
                return true;
            case R.id.toolsOption:
                //Toggle tools visibility
                if(hide){
                    tools.setVisibility(View.VISIBLE);
                    hide = false;
                }
                else if(!hide){
                    hide = true;
                    tools.setVisibility(View.INVISIBLE);
                }
                return true;
            case R.id.save:
                boolean saveSuccessful = customCanvas.saveImage();
                //String success = saveSuccessful ? "Image Saved!" : "Image Save Failed!";
                String tsdw = "If saving worked properly, the image would be saved, but it doesn't, so it didn't.";
                //Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, tsdw, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }

    public void randomSizeSwitch(View v){
        customCanvas.sizeSwitch(sSwitch);
        sSwitch++;
    }
    public void drawRectangle(View v) {
        customCanvas.drawRectangle();
    }
    public void drawCircle(View v) {
        customCanvas.drawCircle();
    }
    public void drawTriangle(View v) {
        customCanvas.drawTriangle();
    }
    public void change(View v) {
        customCanvas.change();
    }

    /*
    - Change Color (JJJ)
    - Change Size (JJJ)
    - Save Images (R) *Completed??*

    #######################
    # Five Extra Features #
    #######################
    1) Undo *Completed*
    2) Eraser (JJJ)
    3) Shapes (Z)
    4) Background Color (Z)
        - Background Color variable for the eraser
    5) Random Brush Color (Z)
     */
}
