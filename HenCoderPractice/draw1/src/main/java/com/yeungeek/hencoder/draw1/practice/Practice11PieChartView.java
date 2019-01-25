package com.yeungeek.hencoder.draw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.yeungeek.hencoder.draw1.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

public class Practice11PieChartView extends View {
    Pair<Integer, Integer> screenSize;
    private Paint mPaint;
    private int width;
    private int height;
    private Path mPath;
    private int lineHeight;


    String title = "饼图";
    Paint paint = new Paint();
    List<Model> models = new ArrayList<>();
    int radius = 150; //半径
    int lineLength1 = 50;   //线1
    int lineLength2 = 80;   //线2——水平线
    int textOffset = 20;

    //选中的扇形的偏移
    int selectedOffset = 20;

    public Practice11PieChartView(Context context) {
        super(context);
        init(context);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        screenSize = AndroidUtils.getScreenSize(context);
//        width = screenSize.first / 2;
//        height = screenSize.second / 2;
        mPaint = new Paint();
        mPath = new Path();

        models.add(new Model("Lollipop", 50, Color.RED));
        models.add(new Model("Marshmallow", 30, Color.YELLOW));
        models.add(new Model("Froyo", 10, Color.GREEN));
        models.add(new Model("Gingerbread", 5, Color.BLUE));
        models.add(new Model("Ice Cream Sandwich", 10, Color.GRAY));
        models.add(new Model("Jelly Bean", 35, Color.parseColor("#009789")));
        models.add(new Model("Kitkat", 40, Color.parseColor("#1b97f3")));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图
//        int x = getWidth();
//        int y = getHeight();
//
//        mPaint.setAntiAlias(true);
//
//        mPaint.setColor(Color.WHITE);
//        mPaint.setTextSize(45);
//        mPaint.setTextAlign(Paint.Align.CENTER);
//        canvas.drawText("饼图", x / 2, y - 50, mPaint);

        if (paint == null) paint = new Paint();
        paint.setAntiAlias(true);

        int x = getWidth();
        int y = getHeight();

        float sum = 0;
        for (Model model : models) {
            sum += model.num;
        }

        //绘制标题
        paint.setTextSize(30);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(title, x / 2, y - 30, paint); //标题离底部40px

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.STROKE);

        float startAngle = -180;
        float halfAngle = 0;

        boolean isSelected;

        for (int i = 0; i < models.size(); i++) {

            Model model = models.get(i);


            if (i == 0) {
                isSelected = true;
            } else
                isSelected = false;

            int offset = isSelected ? selectedOffset : 0;

            float scale = model.getNum() / sum;
            float angle = scale * 360;
            //圆弧中心的角度
            halfAngle = startAngle + angle / 2;
            //圆弧中心的位置
            float half_x = (float) (x / 2 + (radius + offset) * Math.cos(halfAngle * Math.PI / 180));
            float half_y = (float) (y / 2 + (radius + offset) * Math.sin(halfAngle * Math.PI / 180));
            //圆弧引出的线1的结束点
            float line_1_end_x = (float) (x / 2 + (radius + lineLength1 + offset) * Math.cos(halfAngle * Math.PI /
                    180));
            float line_1_end_y = (float) (y / 2 + (radius + lineLength1 + offset) * Math.sin(halfAngle * Math.PI /
                    180));
            //圆弧引出的线2的结束点
            float line_2_end_x = Math.abs(halfAngle) <= 90 ? line_1_end_x + lineLength2 : line_1_end_x - lineLength2;
            float line_2_end_y = line_1_end_y;
            //文字中心点
            float text_x = Math.abs(halfAngle) <= 90 ? line_2_end_x + textOffset : line_2_end_x - textOffset;
            float text_y = line_1_end_y;


            //椭圆四边的位置
            float rectLeft = (float) (x / 2 - radius + offset * Math.cos(halfAngle * Math.PI / 180));
            float rectRight = (float) (x / 2 + radius + offset * Math.cos(halfAngle * Math.PI / 180));
            float rectTop = (float) (y / 2 - radius + offset * Math.sin(halfAngle * Math.PI / 180));
            float rectBottom = (float) (y / 2 + radius + offset * Math.sin(halfAngle * Math.PI / 180));


            paint.setColor(model.getColor());
            canvas.drawArc(new RectF(rectLeft, rectTop, rectRight, rectBottom), startAngle, angle, true, paint); // 绘制扇形

            //绘制线和文字
            Path path = new Path();
            path.moveTo(half_x, half_y);
            path.lineTo(line_1_end_x, line_1_end_y);
            path.lineTo(line_2_end_x, line_2_end_y);
            canvas.drawPath(path, textPaint);
            if (Math.abs(halfAngle) <= 90) textPaint.setTextAlign(Paint.Align.LEFT);
            else textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(model.getTip(), text_x, text_y, textPaint);

            startAngle += angle;
        }
    }

    class Model {
        int num;
        String tip;
        int color;

        public Model(String tip, int num) {
            this.num = num;
            this.tip = tip;
        }

        public Model(String tip, int num, int color) {
            this.num = num;
            this.tip = tip;
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }
    }
}
