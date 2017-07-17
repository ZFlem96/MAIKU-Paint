package maiku.maikupaintapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CanvasView customCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCanvas = (CanvasView) findViewById(R.id.canvas);
    }

    public void clearButtonClick(View view){
        customCanvas.clearCanvas();
    }

    public void undoButtonClick(View view) { customCanvas.undoDraw(); }

    /*
    - Change Color
    - Change Size

    #######################
    # Five Extra Features #
    #######################
    1) Undo *Completed*
    2) Eraser
    3) Shapes
    4) Background Color
    5) Random Brush Color
     */
}
