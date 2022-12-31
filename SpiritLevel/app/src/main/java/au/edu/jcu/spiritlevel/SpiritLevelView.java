package au.edu.jcu.spiritlevel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class SpiritLevelView extends View {
    int width;
    int height;
    PointF center;
    PointF bubble;
    final Paint paint;
    final int BUBBLE_SIZE = 100;

    public SpiritLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLUE);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        center = new PointF();
        bubble = new PointF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        center.x = w / 2;
        center.y = h / 2;
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(bubble.x, bubble.y, BUBBLE_SIZE, paint);
    }

    public void setBubble(float x, float y){
        bubble.x = center.x + x / 9.81f * width;
        bubble.y = center.y + y / 9.81f * height;
        Log.i("SpiritLevelView", bubble.toString());

        invalidate();
    }
}
