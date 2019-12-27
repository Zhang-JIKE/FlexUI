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

    private FViewPager viewPager;

    private PagerIndicator indicator;

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

        initViewPager();
    }

    private void initViewPager(){
        final ArrayList<Integer> res = new ArrayList<>();
        res.add(R.layout.item_banner);
        res.add(R.layout.item_banner2);
        res.add(R.layout.item_banner);
        res.add(R.layout.item_banner2);

        EndlessAdapter<ArrayList<Integer>> endlessAdapter = new EndlessAdapter<>(res,
                res, new EndlessAdapter.OnCreateItemListener() {
            @Override
            public void onCreateItem(View view, int pos) {

            }
        });

        indicator.setPageCount(res.size());

        viewPager.setAdapter(endlessAdapter);
        viewPager.setOnScrollListener(new FViewPager.OnScrollListener() {
            @Override
            public void onScroll(float offSet,int pos, boolean isRightSlide) {
                if(isRightSlide && offSet> res.size() - 1){
                    offSet = offSet - res.size();
                }
                indicator.setOffSetX(offSet);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }
}
