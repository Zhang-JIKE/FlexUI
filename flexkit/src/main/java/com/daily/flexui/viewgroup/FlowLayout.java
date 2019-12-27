package com.daily.flexui.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.daily.flexui.util.DisplayUtils;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {
    public int horizontalSpacing = DisplayUtils.dp2px(12);//水平间距
    public int verticalSpacing = DisplayUtils.dp2px(12);//竖直间距
    public ArrayList<Line> lineList;
    public FlowLayout(Context context) {
        super(context,null);
    }
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }
    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }
 
    /**
     * 分行：遍历所有的子View，判断哪几个子View在同一行(排座位表)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//一.测量需用多大
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //lineList.clear();
        lineList = new ArrayList<>();
        System.out.println("================onMeasure()方法");
        int width = MeasureSpec.getSize(widthMeasureSpec);//1.FlowLayout的宽度,也就是申请宽度
        int noPaddingWidth = width - getPaddingLeft() - getPaddingRight();//获取用于实际比较的宽度，就是除去2边的padding的宽度
        Line line = new Line();
        for(int i = 0; i < getChildCount();i++){//3.遍历所有的子View，拿子View的宽和noPaddingWidth进行比较
            View views = getChildAt(i);
            views.measure(0,0);//保证能够获取到宽高
            if(line.getViewList().size() == 0){//4.如果当前line中木有子View，则不用比较直接放入line中，因为要保证每行至少有一个子View;
                line.addLineView(views);
            }else if(line.getLineWidth() + horizontalSpacing + views.getMeasuredWidth() > noPaddingWidth){//5.如果当前line的宽+水平间距+子View的宽大于noPaddingWidth,则child需要换行
                lineList.add(line);
                line = new Line();
                line.addLineView(views);
            }else {
                line.addLineView(views);
            }
            if(i == getChildCount() - 1){ //6.如果当前child是最后的子View，那么需要保存最后的line对象
                lineList.add(line);
            }
        }
 
        int height = getPaddingTop() + getPaddingBottom();//申请高度
        for(int i = 0; i < lineList.size();i++){
            height +=lineList.get(i).getLineHeight();
        }
        height += (lineList.size() - 1) * verticalSpacing;
        setMeasuredDimension(width,height);//7.设置当前控件的宽高，或者向父VIew申请宽高
        System.out.println("======width="+width+",height="+height);
    }
 
    //// TODO: 2017/3/24 怎么能获取到宽高呢？
    /**
     * views.measure(0,0);//保证能够获取到宽高
     * view.getMeasureHeight();//得到控件高度，注意在创建的时候是得不到的
     * //保证能得到
            view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            //一般用完立即移除，因为只有该view的宽高改变都会再引起回调该方法
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
     */
 
    /**
     * 去摆放所有的子View，让每个人真正的坐到自己的位置上
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) { //二.画布就开销多大
        System.out.println("================onLayout()方法");
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        for(int i = 0; i < lineList.size();i++){//1.获取line的view的集合
            Line line = lineList.get(i);
            if(i > 0){//从第二行要加行距
                paddingTop += verticalSpacing + lineList.get(i - 1).getLineHeight();
            }
            ArrayList<View> viewList = line.getViewList();
            int reMainSpacing = getReMainSpacing(line);//计算空白空间
            int spacing = reMainSpacing / viewList.size();
            for(int j = 0; j < viewList.size(); j++){//2.获取每一行的子view的集合
                View view = viewList.get(j);
                int specWidth = MeasureSpec.makeMeasureSpec(view.getMeasuredWidth() + spacing, MeasureSpec.EXACTLY);
                view.measure(specWidth,MeasureSpec.UNSPECIFIED);////保证能够获取到宽高
                if(j == 0){//如果是第一个子view就放在左边就可以了
                    view.layout(paddingLeft,paddingTop,paddingLeft + view.getMeasuredWidth(),paddingTop + view.getMeasuredHeight());
                }else{
                    View viewLast = viewList.get(j - 1);
                    int left = viewLast.getRight() + horizontalSpacing;
                    view.layout(left,viewLast.getTop(),left+ view.getMeasuredWidth(),viewLast.getBottom());
                }
            }
        }
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("================onDraw()方法");
    }
 
    /**
     * 获取每行空白的宽度
     */
    public int getReMainSpacing(Line line){
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - line.getLineWidth();//测量的宽分别减去...
    }
 
    /**
     * 封装每行的数据
     */
    class Line{
        int width;
        int height;
        ArrayList<View> viewList;
 
        public Line() {
            viewList = new ArrayList<>();
        }
 
        /**
         * 换行的方法
         */
        public void addLineView(View child){
            if(!viewList.contains(child)){
                viewList.add(child);
                if(viewList.size() == 1){
                    width = child.getMeasuredWidth();
                }else{
                    width += child.getMeasuredWidth() + horizontalSpacing;
                }
            }
            height = Math.max(height,child.getMeasuredHeight());
        }
        /**
         * 获取当前行的宽度
         * @return
         */
        public int getLineWidth(){
            return width;
        }
 
        /**
         * 获取当前行的高度
         * @return
         */
        public int getLineHeight(){
            return height;
        }
 
        /**
         * 获取当前行的所有子view
         * @return
         */
        public ArrayList<View> getViewList(){
            return viewList;
        }
    }
}