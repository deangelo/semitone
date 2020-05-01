/*
 * Semitone - tuner, metronome, and piano for Android
 * Copyright (C) 2019  Andy Tockman <andy@tck.mn>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package mn.tck.semitone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.TextView;
import android.util.AttributeSet;

import static java.lang.Integer.max;
import static java.lang.Math.abs;
import static java.lang.Math.min;

public class CentErrorView extends TextView {

    private Paint centerPaint, linePaint;
    private String cents;
    double error;

    public CentErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        centerPaint = new Paint();
        centerPaint.setColor(Color.WHITE);
        centerPaint.setStrokeWidth(1);

        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(2);

        cents = context.getResources().getString(R.string.cents);

        error = -1000;
    }

    public void setError(CharSequence note, double error) {
        this.error = error;

        String error_string;
        error_string = String.format("%+.2f %s", error*100, cents);

        SpannableString spannable = new SpannableString(note + "\n" + error_string);
        spannable.setSpan(new RelativeSizeSpan(0.2f),
                note.length()+1, note.length()+1+error_string.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannable);

        // fade from black to green, starting with 10 cents deviation
        int g = (int)(Math.max(0, 0.1 - abs(this.error))*1500);
        // set background color g=0..100 as we approach 0 cents difference
        this.setBackgroundColor( 0xFF000000 + ((g/10) << 16) + (g << 8) + (g/10));
    }

    @Override protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (this.error != -1000) {
            int width = getWidth(), height = getHeight(), middle = width / 2;

            // draw middle indicator
            canvas.drawLine(middle, 0, middle, height / 6, centerPaint);
            canvas.drawLine(middle, height * 5 / 6, middle, height, centerPaint);

            // draw error position
            int xpos = middle + (int) (error * width);
            canvas.drawLine(xpos, 0, xpos, height, linePaint);
        }
    }

}
