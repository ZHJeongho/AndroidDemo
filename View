# View

View在Android的作用不用多说，堪比四大组件，应该比BroadcastReceiver和ContentProvider
的用的更广。作为一个Android的初学者，View是道坎啊~~~只能多思考，多理解，多写才能将View
熟练的掌握。

## 什么是View

  View是用户界面最基本的构建块，是所有控件的基类。

## View中的坐标

  坐标分为两种：Android坐标系和视图坐标系。

### Android坐标系

Android坐标系，是以屏幕左上角作为坐标原点，向右为X轴的正方向，向下为Y轴的正方向。

- 获取X轴坐标：MotionEvent类的getRawX()方法

  **event.getRawX()**

- 获取Y轴坐标：MotionEvent类的getRawY()方法

  **event.getRawY()**

### 视图坐标系

视图坐标系，描述子视图在父视图的位置。以父视图的左上角为原点，同样，向右为X轴的正方向，
向下为Y轴的正方向。

- 获取X轴坐标：MotionEvent类的getX()方法

  **event.getX()**

- 获取Y轴坐标：MotionEvent类的getY()方法

  **event.getY()**

## 获取View的位置参数

- View.getLeft(): 左上角横坐标 （左边界距离父视图左边界的距离）
- View.getTop(): 左上角纵坐标 （上边界距离父视图上边界的距离）
- View.getRight(): 右下角横坐标 （右边界距离父视图左边界的距离）
- View.getBottom(): 右下角纵坐标 （下边界距离父视图上边界的距离）
- View.getX(): View左上角横坐标

  > View.getX() = View.getLeft() + translationX

- View.getY(): View左上角纵坐标

  > View.getY() = View.getTop() + translationY


  translationX、translationY是子View左上角相对父View的偏移量，默认为0。

  **子View移动的时候，getLeft和getTop是不改变的，translationX和translationY发生变化。**

## View的滑动

## View事件分发
