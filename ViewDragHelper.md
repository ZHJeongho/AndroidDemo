# ViewDragHelper

看书 + Hongyang大神博客学习总结，对于每个callback中的方法都尝试了一哈！

## What is ViewDragHelper
Google给的解释：

``` java
/**
 * ViewDragHelper is a utility class for writing custom ViewGroups. It offers a number
 * of useful operations and state tracking for allowing a user to drag and reposition
 * views within their parent ViewGroup.
 */
```
**自定义ViewGroup时非常有用的一个类，它提供一系列操作以及状态追踪，允许用户在ViewGroup内拖拉和重新定位Views。**（渣英语~~）

## How to use ViewDragHelper

使用ViewDragHelper一般分三个步骤：

1. 使用静态工厂去初始化一个ViewDragHelper对象。
2. 重写ViewGroup类中的onInterceptTouchEvent和onTouchEvent方法
3. 实现ViewDragHelper.Callback中的方法

#### 初始化ViewDragHelper对象

源码如下：
```java
/**
 * @param forParent 要监控的ViewGroup 一般采用内部类的形式 所以一般为this
 * @param sensitivity 灵敏度
 * @param cb 回调
 * @return 一个ViewDragHelper实例
 */
public static ViewDragHelper create(ViewGroup forParent, float sensitivity, Callback cb) {
    final ViewDragHelper helper = create(forParent, cb);
    helper.mTouchSlop = (int) (helper.mTouchSlop * (1 / sensitivity));
    return helper;
}
```
```java
mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);

也可以使用匿名类的方式

mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return false;
    }
});
```

#### 重写ViewGroup类中的方法

这个是必须的，使事件由ViewDragHelper处理
``` java
@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
    return mDragHelper.shouldInterceptTouchEvent(ev);
}

@Override
public boolean onTouchEvent(MotionEvent event) {
    mDragHelper.processTouchEvent(event);
    return true;
}
```

#### 实现ViewDragHelper.Callback中的方法

- tryCaptureView  捕捉子View，使其可以拖动，返回true就可以
- clampViewPositionVertical，clampViewPositionHorizontal  子View移动边界的限定
- onViewReleased  手指抬起时触发 第一个参数是释放的View对象
- onEdgeDragStarted  边缘拖动开始时触发
- onViewPositionChanged 被捕捉的子View位置发生变化时触发
- getViewHorizontalDragRange、getViewVerticalDragRange
- onViewDragStateChanged  子View的drag状态发生变化时触发
  > 拖动： ViewDragHelper.STATE_DRAGGING    1
  >
  > 自动滚动：ViewDragHelper.STATE_SETTLING     2
  >
  > 空闲：ViewDragHelper.STATE_IDLE     0
- onViewCaptured
- onEdgeTouched
- onEdgeLock
- getOrderedChildIndex



完整代码如下：

```java
package com.jeongho.textimageview;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Jeongho on 2016/7/27.
 */
public class DragLayout extends LinearLayout{

    private ViewDragHelper mDragHelper;
    private DragCallback mCallback;

    //任意移动View
    private View mDragView;
    //自动复位View
    private View mAutoResetView;
    //边缘移动View
    private View mEdgeView;

    private int mOriginalTop;
    private int mOriginalLeft;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCallback = new DragCallback();
        mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
        //设置左边边缘跟踪
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }


    class DragCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDragView || child == mAutoResetView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == mAutoResetView){
                mDragHelper.settleCapturedViewAt(mOriginalLeft, mOriginalTop);
                invalidate();
            }
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mDragHelper.captureChildView(mEdgeView, pointerId);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            Log.d("DragState" , state + "");

            //拖动：ViewDragHelper.STATE_DRAGGING  1
            //自动滚动：ViewDragHelper.STATE_SETTLING  2
            //空闲：ViewDragHelper.STATE_IDLE  0

            //drag过程： drag -> idle
            //edge过程： drag -> idle
            //autoReset过程:  drag -> settle -> idle
        }

        /**
         * capturedChild位置发生变化的时候调用
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //Log.d("onViewPositionChanged", "left: " + left + ", top: " + toptop);
            //Log.d("onViewPositionChanged", "dx: " + dx + ", dy: " + dy);
        }

        /**
         * capturedChild被捕捉时调用
         * @param capturedChild
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            int count = getChildCount();
            for (int i = 0; i < count; i++){
                if (getChildAt(i) == capturedChild){
                    Log.d("onViewCaptured", i + "");
                }
            }
        }

        /**
         * 触摸边界时，并且没有子View被捕捉时调用
         * 有子View被捕捉的时候 无法调用
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            Log.d("onEdgeTouched", "touched");
        }

        /**
         * onEdgeTouched之后触发，返回true
         * 防止多次边缘移动 造成冲突
         * @param edgeFlags
         * @return
         */
        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }

        /**
         * 决定子View的Z-index
         * @param index
         * @return
         */
        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        /**
         * 返回子View水平拖动范围，单位为像素
         * 水平方向无法运动返回0
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //初始化View
        mDragView = getChildAt(0);
        mAutoResetView = getChildAt(1);
        mEdgeView = getChildAt(2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mOriginalLeft = mAutoResetView.getLeft();
        mOriginalTop = mAutoResetView.getTop();
    }

    //必须重写，并且调用invalidate方法
    //invalidate -> draw -> computeScroll
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)){
            invalidate();
        }
    }
}

```
