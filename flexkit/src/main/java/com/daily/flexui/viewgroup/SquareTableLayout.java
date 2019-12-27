package com.daily.flexui.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;

public class SquareTableLayout extends BaseViewGroup {

    //列数
    private int colmn;

    //行数
    private int row;

    //间距
    private float space;

    private float viewWidth = DisplayUtils.dp2px(24);

    public SquareTableLayout(Context context) {
        super(context);
    }

    public SquareTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SquareTableLayout, 0, 0);
        colmn = array.getInt(R.styleable.SquareTableLayout_squaretablelayout_col,1);
        row = array.getInt(R.styleable.SquareTableLayout_squaretablelayout_row,1);
        space = array.getDimension(R.styleable.SquareTableLayout_squaretablelayout_space, DisplayUtils.dp2px(4));
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = (width - (colmn+1)*space)/colmn;

        for(int i=0;i<getChildCount();i++){
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    public int getWrapContentWidth() {
        return (int) (space*(colmn+1)+colmn*viewWidth);
    }

    @Override
    public int getWrapContentHeight() {
        return (int) (space*(row+1)+row*viewWidth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i=0;i<getChildCount();i++){
            int colIndex = getColIndex(i);
            int rowIndex = getRowIndex(i);

            View view = getChildAt(i);
            int left = (int) ((colIndex + 1) * space + colIndex * viewWidth);
            int top = (int) ((rowIndex + 1) * space + rowIndex * viewWidth);
            int right = (int) (left + viewWidth);
            int bottom = (int) (top + viewWidth);
            view.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        row = calculateRowNumber();
    }

    private int calculateRowNumber(){
        int row = 0;
        int viewNum = getChildCount();
        if(viewNum <= colmn){
            row = 1;
        } else {
            while(viewNum>0){
                viewNum -= colmn;
                row ++;
            }
        }
        return row;
    }

    private int getRowIndex(int index){
        return index/colmn;
    }

    private int getColIndex(int index){
        return ((index)%colmn);
    }
}
