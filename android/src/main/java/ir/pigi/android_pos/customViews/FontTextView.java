package ir.pigi.android_pos.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import ir.pigi.android_pos.R;

public class FontTextView extends AppCompatTextView {

    private int numberType = 0;
    public FontTextView(Context context) {
        super(context);
        Typeface face= Typeface.createFromAsset(context.getAssets(), "IRANSans.ttf");
        this.setTypeface(face);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        numberType = a.getInt(R.styleable.FontTextView_NumberType,0);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "IRANSans.ttf");
        this.setTypeface(face);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView, defStyle, 0);
        numberType = a.getInt(R.styleable.FontTextView_NumberType,0);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "IRANSans.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        if(numberType == 0)
            super.setText(replaceEngToArabicNumbers(text), type);
        else
            super.setText(text,type);
    }

    @Override
    public CharSequence getText() {
        if(numberType==0)
            return replaceArabicToEngNumbers(super.getText());
        else
            return super.getText();
    }

    private String replaceEngToArabicNumbers(CharSequence original) {
        if (original != null) {
            return original.toString().replaceAll("1","١")
                    .replaceAll("2","٢")
                    .replaceAll("3","٣")
                    .replaceAll("4","٤")
                    .replaceAll("5","٥")
                    .replaceAll("6","٦")
                    .replaceAll("7","٧")
                    .replaceAll("8","٨")
                    .replaceAll("9","٩")
                    .replaceAll("0","٠");
        }

        return null;
    }

    private String replaceArabicToEngNumbers(CharSequence original) {
        if (original != null) {
            return original.toString().replaceAll("١","1")
                    .replaceAll("٢","2")
                    .replaceAll("٣","3")
                    .replaceAll("٤","4")
                    .replaceAll("٥","5")
                    .replaceAll("٦","6")
                    .replaceAll("٧","7")
                    .replaceAll("٨","8")
                    .replaceAll("٩","9")
                    .replaceAll("٠","0");
        }

        return null;
    }

}