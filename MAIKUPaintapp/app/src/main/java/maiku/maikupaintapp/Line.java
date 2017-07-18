package maiku.maikupaintapp;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Randy McClure on 7/17/2017.
 */

public class Line {
	private Path path;
	private Paint paint;

	public Line(Path path, Paint paint) {
		this.path = path;
		this.paint = paint;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
}
