# View滑动7种方式

**View滑动思想：**
触摸到View时，记录触摸点坐标；滑动的时候，获取当前的坐标与上一时刻的坐标的偏移量，改变View
的坐标，达到View滑动的效果。

## layout

在MOVE的时候去调用layout刷新View的位置。

使用getX(),getY()获取相对于父视图的位置，计算偏移量，调用layout设置View的位置即可。

``` java
@Override
public boolean onTouchEvent(MotionEvent event) {
    final int action = event.getAction();
    int x = (int) event.getX();
    int y = (int) event.getY();
    switch (action & MotionEvent.ACTION_MASK){
        case MotionEvent.ACTION_DOWN:
            mLastX = x;
            mLastY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            int offsetX = x - mLastX;
            int offsetY = y - mLastY;
            layout(getLeft() + offsetX,
                    getTop() + offsetY,
                    getRight() + offsetX,
                    getBottom() + offsetY);
            break;
        case MotionEvent.ACTION_UP:
            break;
    }

    return true;
}
```

使用getRawX(),getRawY()获取相对Android坐标系的坐标，必须在每次设置完View位置的时候，去**更新下mLastX,mLastY的值**。

``` java
@Override
public boolean onTouchEvent(MotionEvent event) {
    final int action = event.getAction();
    int x = (int) event.getRawX();
    int y = (int) event.getRawY();
    switch (action & MotionEvent.ACTION_MASK){
        case MotionEvent.ACTION_DOWN:
            mLastX = x;
            mLastY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            int offsetX = x - mLastX;
            int offsetY = y - mLastY;
            layout(getLeft() + offsetX,
                    getTop() + offsetY,
                    getRight() + offsetX,
                    getBottom() + offsetY);
            mLastX = x;
            mLastY = y;
            break;
        case MotionEvent.ACTION_UP:
            break;
    }

    return true;
}
```

**为什么getX() getY()不需要更新mLastX,mLastY的值？**

getX(),getY()是相对自身视图的位置。

getRawX(),getRawY()是相对Android坐标系的，每次移动后如果不更新mLastX,mLastY的值，就会将
上一次的数据累加到本次偏移量计算中，所以需要更新mLastX,mLastY的值。

## offsetLeftAndRight offsetTopAndBottom

同上，在MotionEvent.ACTION_MOVE中设置offsetLeftAndRight和offsetTopAndBottom

``` java
@Override
public boolean onTouchEvent(MotionEvent event) {
    final int action = event.getAction();

    int x = (int) event.getX();
    int y = (int) event.getY();
    switch (action & MotionEvent.ACTION_MASK){
        case MotionEvent.ACTION_DOWN:
            mLastX = x;
            mLastY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            int offsetX = x - mLastX;
            int offsetY = y - mLastY;

            offsetLeftAndRight(offsetX);
            offsetTopAndBottom(offsetY);
            break;
        case MotionEvent.ACTION_UP:
            break;
    }

    return true;
}
```
## LatoutParams

## scrollTo / ScrollBy

- ScrollBy

  scrollBy是View的content进行移动，所以必须要getParent获取到他的父View，再调用父View的
  scrollBy()方法，使其移动，但是这种方法的弊端就是父View的所有子View都是进行相应的移动。
``` java
@Override
public boolean onTouchEvent(MotionEvent event) {
    final int action = event.getAction();

    int x = (int) event.getX();
    int y = (int) event.getY();
    switch (action & MotionEvent.ACTION_MASK){
        case MotionEvent.ACTION_DOWN:
            mLastX = x;
            mLastY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            int offsetX = x - mLastX;
            int offsetY = y - mLastY;
            //设置参数为-offset
            //scrollBy相当于坐标系移动
            ((View)getParent()).scrollBy(-offsetX, -offsetY);
            break;
        case MotionEvent.ACTION_UP:
            break;
    }

    return true;
}
```

- scrollTo

  同理，scrollTo()是取绝对坐标(Android坐标系)，再使其父View调用scrollTo()方法进行移动。
``` java
@Override
public boolean onTouchEvent(MotionEvent event) {
    final int action = event.getAction();
    int x = (int) event.getRawX();
    int y = (int) event.getRawY();

    switch (action & MotionEvent.ACTION_MASK){
        case MotionEvent.ACTION_DOWN:
            mLastX = x;
            mLastY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            int offsetX = x - mLastX;
            int offsetY = y - mLastY;

            ((View)getParent()).scrollTo(-offsetX, -offsetY);
            break;
        case MotionEvent.ACTION_UP:
            break;
    }

    return true;
}
```
## Scroller

使用Scroller基本就是分三个步骤：

1. 初始化Scroller  

  ``` java
  Scroller Scroller = new Scroller(context);

  ```

2. 重写computeScroll()方法

  ``` java
@Override
public void computeScroll() {
  //返回true 表示动画没有执行完毕
  if (mScroller.computeScrollOffset()){
      ((View)getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      invalidate();
  }
}

  ```

3. 调用startScroll方法

> startScroll(int startX, int startY, int dx, int dy)

> startScroll(int startX, int startY, int dx, int dy, int duration)

实现平滑移动，就需要调用上面两个方法其中的一个，当然只差一个参数而已。

``` java
case MotionEvent.ACTION_UP:
    View parent = (View) getParent();
    //松手后，父view会平滑移动回原位
    mScroller.startScroll(parent.getScrollX(), parent.getScrollY(),
            -parent.getScrollX(), -parent.getScrollY());
    invalidate();
```

执行startScroll方法的时候，才会触发computeScroll()。

invalidate -> draw -> computeScroll

才会循环取mScrollX， mScrollY，达到平移的效果。

## 属性动画

## ViewDragHelper

**见另外一篇ViewDragHelper文章**
