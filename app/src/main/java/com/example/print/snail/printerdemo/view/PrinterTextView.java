package com.example.print.snail.printerdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/**
 * Created by snail on 2017/8/5.
 */

public class PrinterTextView extends android.support.v7.widget.AppCompatTextView {
    private StringBuffer stringBuffer = new StringBuffer();
    private Rect textRect = new Rect();
    private String[] arr;
    private int textCount;
    private int currentIndex = -1;

    /**
     * 每个字出现的时间
     */
    private int duration = 300;
    private ValueAnimator textAnimation;
    private TextAnimationListener textAnimationListener;

    public void setTextAnimationListener(TextAnimationListener l) {
        this.textAnimationListener = l;
    }


    public PrinterTextView(Context context) {
        super(context);
    }

    public PrinterTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PrinterTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (stringBuffer != null) {
            drawText(canvas, stringBuffer.toString());
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas, String textString) {
        //设置文字绘制区域
        textRect.left = getPaddingLeft();
        textRect.top = getPaddingTop();
        textRect.right = getWidth() - getPaddingRight();
        textRect.bottom = getHeight() - getPaddingBottom();
        Paint.FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //文字绘制到整个布局的中心位置
        canvas.drawText(textString, getPaddingLeft(), baseline, getPaint());
    }


    /**
     * 文字逐个显示动画  通过插值的方式改变数据源
     */

    private void initAnimation() {
        //从0到textCount - 1  是设置从第一个字到最后一个字的变化因子
        textAnimation = ValueAnimator.ofInt(0, textCount - 1);
        //执行总时间就是每个字的时间乘以字数
        textAnimation.setDuration(textCount * duration);
        //匀速显示文字
        textAnimation.setInterpolator(new LinearInterpolator());
        textAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int index = (int) animation.getAnimatedValue();
                //过滤去重，保证每个字只重绘一次
                if (currentIndex != index) {
                    stringBuffer.append(arr[index]);

                    currentIndex = index;
                    //所有文字都显示完成之后进度回调结束动画
                    if (currentIndex == (textCount - 1)) {
                        if (textAnimationListener != null) {
                            textAnimationListener.animationFinish();
                        }
                    }
                    //下面两种方式都可以
                    setText(stringBuffer.toString());
//                    invalidate();
                }
            }
        });
    }


    /**
     * 设置逐渐显示的字符串
     *
     * @param textString
     * @return
     */

    public PrinterTextView setTextString(String textString) {
        if (textString != null) {
            //总字数
            textCount = textString.length();
            //存放单个字的数组
            arr = new String[textCount];
            for (int i = 0; i < textCount; i++) {
                arr[i] = textString.substring(i, i + 1);
            }
            initAnimation();
        }
        return this;
    }

    /**
     * 开启动画
     *
     * @return
     */
    public PrinterTextView startPrintAnimation() {
        if (textAnimation != null) {
            //动画开启的时候参数都设置成初始状态
            stringBuffer.setLength(0);
            currentIndex = -1;
            textAnimation.start();
        }
        return this;
    }

    /**
     * 停止动画
     *
     * @return
     */
    public PrinterTextView stopPrintAnimation() {
        if (textAnimation != null) {
            textAnimation.end();
        }
        return this;
    }

    /**
     * 回调接口
     */
    public interface TextAnimationListener {
        void animationFinish();
    }

}
