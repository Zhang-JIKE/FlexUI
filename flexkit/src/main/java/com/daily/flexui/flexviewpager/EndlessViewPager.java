package com.daily.flexui.flexviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daily.flexui.R;

import java.util.ArrayList;

public class EndlessViewPager extends FrameLayout {

    public FViewPager viewPager;

    public PagerIndicator indicator;

    public EndlessViewPager(Context context) {
        super(context);
    }

    public EndlessViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        View view = (View) View.inflate(getContext(),R.layout.view_endless_viewpager,this);
        indicator = view.findViewById(R.id.indicator_fdimhfedoffndufjikfmdnfuhfednjfnf);
        viewPager = view.findViewById(R.id.viewPager_khdfbwuefefbzbzsleudfewFbilwebraw);

    }

    public void setEndlessAdapter(final ArrayList<Integer> layoutIds ,EndlessAdapter adapter){
        indicator.setPageCount(layoutIds.size());
        viewPager.setAdapter(adapter);
        viewPager.setOnScrollListener(new FViewPager.OnScrollListener() {
            @Override
            public void onScroll(float offSet,int pos, boolean isRightSlide) {
                if(isRightSlide && offSet> layoutIds.size() - 1){
                    offSet = offSet - layoutIds.size();
                }
                indicator.setOffSetX(offSet);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //measureChildren(widthMeasureSpec,heightMeasureSpec);
    }
}
