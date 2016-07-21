#自定义View

## TouchSlop

判定View滚动的最小距离，小于这个距离系统认为用户并不是想要滑到View。

通过ViewConfiguration类的getScaleTouchSlop()方法获取

``` java
  int touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
```

 > ViewConfiguration私有构造，只能通过ViewConfiguration.get(context)获取ViewConfiguration
 实例

 > 默认touchSlop为8dp

## onMeasure

## onLayout

## onDraw



onMeasure: 通常情况下，只会执行一次。手动调用requestLayout()会触发onMeasure(),onLayout()方法。
onDraw: 通过invalidate()/postInvalidate()方法来触发。

### *requestLayout()*

``` java
/**
 * Call this when something has changed which has invalidated the
 * layout of this view. This will schedule a layout pass of the view
 * tree. This should not be called while the view hierarchy is currently in a layout
 * pass ({@link #isInLayout()}. If layout is happening, the request may be honored at the
 * end of the current layout pass (and then layout will run again) or after the current
 * frame is drawn and the next layout occurs.
 *
 * <p>Subclasses which override this method should call the superclass method to
 * handle possible request-during-layout errors correctly.</p>
 */
```

当View发生改变时，需要废止掉该View的布局，重新布局时调用。如果该View
正在执行布局，此请求需要等此次布局结束后，才能够执行。

> 英语渣渣~~~~~
