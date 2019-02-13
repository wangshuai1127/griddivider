package wangshuai.com.griddivider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;


/**
 * Created by wangshuai on 2019/1/25.
 */

public class GridDivider extends RecyclerView.ItemDecoration {
    private static final String TAG = "GridDivider";

    private Paint mDividerPaint;
    private RecyclerView recyclerView;

    private GridLayoutManager layoutManagerGrid;
    private LinearLayoutManager layoutManagerLiner;

    private int ORIENTATION = 0;
    private int SPACE_TOP = 0;//顶部分割线宽度
    private int SPACE_BOTTOM = 0;//底部分割线宽度
    private int SPACE_LEFT = 0;//左测分割线宽度
    private int SPACE_RIGHT = 0;//右侧分割线宽度
    private int SPACE_MIDDLE_V = 0;//垂直分割线宽度
    private int SPACE_MIDDLE_H = 0;//水平分割线宽度

    private int B_LEFT_OUT;//左外侧边界
    private int B_LEFT_INNER;//左内侧边界
    private int B_TOP_OUT;//上外侧边界
    private int B_TOP_INNER;//上内侧边界
    private int B_RIGHT_OUT;//右外侧边界
    private int B_RIGHT_INNER;//右内侧边界
    private int B_BOTTOM_OUT;//下外侧边界
    private int B_BOTTOM_INNER;//下内侧边界

    private int DIVIDER_COLOR = Color.TRANSPARENT;

    public GridDivider(Context context, int divider_dp, @ColorInt int divider_color) {
        SPACE_LEFT = SPACE_TOP = SPACE_RIGHT = SPACE_BOTTOM = SPACE_MIDDLE_V = SPACE_MIDDLE_H = dp2px(context, divider_dp);
        this.DIVIDER_COLOR = divider_color;
    }

    public GridDivider(Context context, int bounds_dp, int middle_dp, @ColorInt int divider_color) {
        SPACE_LEFT = SPACE_TOP = SPACE_RIGHT = SPACE_BOTTOM = dp2px(context, bounds_dp);
        SPACE_MIDDLE_V = SPACE_MIDDLE_H = dp2px(context, middle_dp);
        this.DIVIDER_COLOR = divider_color;
    }

    public GridDivider(Context context, int left_dp, int top_dp, int right_dp, int bottom_dp, int middleV_dp, int middleH_dp, int divider_color) {
        this.SPACE_LEFT = dp2px(context, left_dp);
        this.SPACE_TOP = dp2px(context, top_dp);
        this.SPACE_RIGHT = dp2px(context, right_dp);
        this.SPACE_BOTTOM = dp2px(context, bottom_dp);
        this.SPACE_MIDDLE_H = dp2px(context, middleH_dp);
        this.SPACE_MIDDLE_V = dp2px(context, middleV_dp);
        this.DIVIDER_COLOR = divider_color;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (DIVIDER_COLOR !=1){
            return;
        }
        if (mDividerPaint == null) {
            mDividerPaint = new Paint();
            mDividerPaint.setColor(DIVIDER_COLOR);
            mDividerPaint.setStyle(Paint.Style.FILL);
        }
        init(parent);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //计算边界
            calculateBounds(parent.getChildAt(i));
            //绘制左分割线
            c.drawRect(B_LEFT_OUT, B_TOP_OUT, B_LEFT_INNER, B_BOTTOM_OUT, mDividerPaint);
            //绘制右分割线
            c.drawRect(B_RIGHT_INNER, B_TOP_OUT, B_RIGHT_OUT, B_BOTTOM_OUT, mDividerPaint);
            //绘制上分割线
            c.drawRect(B_LEFT_OUT, B_TOP_OUT, B_RIGHT_OUT, B_TOP_INNER, mDividerPaint);
            //绘制下部分割线
            c.drawRect(B_LEFT_OUT, B_BOTTOM_INNER, B_RIGHT_OUT, B_BOTTOM_OUT, mDividerPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        init(parent);
        int offset_top = 0;
        int offset_bottom = 0;
        int offset_left = 0;
        int offset_right = 0;
        if (layoutManagerGrid != null) {
            //垂直布局
            if (ORIENTATION == LinearLayoutManager.VERTICAL) {
                //设置顶部间距
                if (getSpanGroupIndex(view) == 0) {
                    offset_top = SPACE_TOP;//第一行
                } else {
                    offset_top = SPACE_MIDDLE_H / 2;//中间行
                }
                //设置底部间距
                if (isBottomIndex(view)) {
                    offset_bottom = SPACE_BOTTOM;//最后一行
                } else {
                    offset_bottom = SPACE_MIDDLE_H / 2;//中间行
                }
                //设置左边间距
                if (getSpanIndex(view) == 0) {
                    offset_left = SPACE_LEFT;//最左边列
                } else {
                    offset_left = SPACE_MIDDLE_V / 2;//中间列
                }
                //设置右边间距
                if (isRightIndex(view)) {
                    offset_right = SPACE_RIGHT;//最右边列
                } else {
                    offset_right = SPACE_MIDDLE_V / 2;//中间列
                }
            }
            //水平布局
            else {
                //设置左侧间距
                if (getSpanGroupIndex(view) == 0) {
                    offset_left = SPACE_LEFT;//第一列
                } else {
                    offset_left = SPACE_MIDDLE_V / 2;//中间列
                }
                //设置右侧
                if (isBottomIndex(view)) {
                    offset_right = SPACE_RIGHT;//最后一行
                } else {
                    offset_right = SPACE_MIDDLE_V / 2;//中间行
                }
                //设置顶部间距
                if (getSpanIndex(view) == 0) {
                    offset_top = SPACE_TOP;//最顶部列
                } else {
                    offset_top = SPACE_MIDDLE_H / 2;//中间列
                }
                //设置底部间距
                if (isRightIndex(view)) {
                    offset_bottom = SPACE_BOTTOM;//最底部列
                } else {
                    offset_bottom = SPACE_MIDDLE_H / 2;//中间列
                }
            }
        } else if (layoutManagerLiner != null) {
            if (ORIENTATION == LinearLayoutManager.VERTICAL) {
                offset_left = SPACE_LEFT;
                offset_right = SPACE_RIGHT;
                offset_top = SPACE_MIDDLE_H / 2;
                offset_bottom = SPACE_MIDDLE_H / 2;
                if (getViewPosition(view) == 0) {
                    offset_top = SPACE_TOP;
                }
                if (getViewPosition(view) == layoutManagerLiner.getItemCount() - 1) {
                    offset_bottom = SPACE_BOTTOM;
                }
            } else {
                offset_left = SPACE_MIDDLE_V / 2;
                offset_right = SPACE_MIDDLE_V / 2;
                offset_top = SPACE_TOP;
                offset_bottom = SPACE_BOTTOM;
                if (getViewPosition(view) == 0) {
                    offset_left = SPACE_LEFT;
                }
                if (getViewPosition(view) == layoutManagerLiner.getItemCount() - 1) {
                    offset_right = SPACE_RIGHT;
                }
            }
        }
        //项目需求自定义最后一项为加载更多，返回0
        if ( getViewPosition(view) == parent.getLayoutManager().getItemCount() - 1) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(offset_left, offset_top, offset_right, offset_bottom);
        }
    }

    //初始化参数
    private void init(RecyclerView parent) {
        if (recyclerView == null) {
            recyclerView = parent;
            if (parent.getLayoutManager() instanceof GridLayoutManager) {
                layoutManagerGrid = (GridLayoutManager) parent.getLayoutManager();
                ORIENTATION = layoutManagerGrid.getOrientation();
            } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                layoutManagerLiner = (LinearLayoutManager) parent.getLayoutManager();
                ORIENTATION = layoutManagerLiner.getOrientation();
            }
        }
    }
    //计算分割线边界尺寸
    private void calculateBounds(View childView) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
        B_LEFT_INNER = childView.getLeft() - params.leftMargin;
        B_TOP_INNER = childView.getTop() - params.topMargin;
        B_RIGHT_INNER = childView.getRight() + params.rightMargin;
        B_BOTTOM_INNER = childView.getBottom() + params.bottomMargin;
        int leftDividerWidth = 0;
        int rightDividerWidth = 0;
        int topDividerHeight = 0;
        int bottomDividerHeight = 0;
        if (ORIENTATION == LinearLayoutManager.VERTICAL) {
            if (layoutManagerGrid != null) {
                //网格垂直布局
                leftDividerWidth = getSpanIndex(childView) == 0 ? SPACE_LEFT : SPACE_MIDDLE_V / 2;
                rightDividerWidth = isRightIndex(childView) ? SPACE_RIGHT : SPACE_MIDDLE_V / 2;
                topDividerHeight = getSpanGroupIndex(childView) == 0 ? SPACE_TOP : SPACE_MIDDLE_H / 2;
                bottomDividerHeight = isBottomIndex(childView) ? SPACE_BOTTOM : SPACE_MIDDLE_H / 2;
            } else if (layoutManagerLiner != null) {
                //线性垂直布局
                leftDividerWidth = SPACE_LEFT;
                rightDividerWidth = SPACE_RIGHT;
                topDividerHeight = getViewPosition(childView) == 0 ? SPACE_TOP : SPACE_MIDDLE_H / 2;
                bottomDividerHeight = getViewPosition(childView) == layoutManagerLiner.getItemCount() - 1 ? SPACE_BOTTOM : SPACE_MIDDLE_H / 2;
            }
        } else {
            if (layoutManagerGrid != null) {
                //网格水平布局
                leftDividerWidth = getSpanGroupIndex(childView) == 0 ? SPACE_LEFT : SPACE_MIDDLE_V / 2;
                rightDividerWidth = isBottomIndex(childView) ? SPACE_RIGHT : SPACE_MIDDLE_V / 2;
                topDividerHeight = getSpanIndex(childView) == 0 ? SPACE_TOP : SPACE_MIDDLE_H / 2;
                bottomDividerHeight = isRightIndex(childView) ? SPACE_BOTTOM : SPACE_MIDDLE_H / 2;

            } else if (layoutManagerLiner != null) {
                //线性水平布局
                leftDividerWidth = getViewPosition(childView) == 0 ? SPACE_LEFT : SPACE_MIDDLE_V / 2;
                rightDividerWidth = getViewPosition(childView) == layoutManagerLiner.getItemCount() - 1 ? SPACE_RIGHT : SPACE_MIDDLE_V / 2;
                topDividerHeight = SPACE_TOP;
                bottomDividerHeight = SPACE_BOTTOM;
            }
        }
        B_LEFT_OUT = B_LEFT_INNER - leftDividerWidth;
        B_TOP_OUT = B_TOP_INNER - topDividerHeight;
        B_RIGHT_OUT = B_RIGHT_INNER + rightDividerWidth;
        B_BOTTOM_OUT = B_BOTTOM_INNER + bottomDividerHeight;
    }

    //获取view在adapter的位置
    private int getViewPosition(View childview) {
        return recyclerView.getChildAdapterPosition(childview);
    }

    //获取view在视图中的行数
    private int getSpanGroupIndex(View childview) {
        return layoutManagerGrid.getSpanSizeLookup().getSpanGroupIndex(getViewPosition(childview), layoutManagerGrid.getSpanCount());
    }

    //获取view在视图中的列数
    private int getSpanIndex(View childview) {
        return layoutManagerGrid.getSpanSizeLookup().getSpanIndex(getViewPosition(childview), layoutManagerGrid.getSpanCount());
    }

    //判断view是否占据最右侧
    private boolean isRightIndex(View childview) {
        int curPosition = getViewPosition(childview);
        int curSpanGroupSize = 0;
        for (int i = getSpanIndex(childview); i >= 0; i--) {
            curSpanGroupSize += layoutManagerGrid.getSpanSizeLookup().getSpanSize(curPosition - i);
        }
        return curSpanGroupSize == layoutManagerGrid.getSpanCount();
    }

    //是否是最后一行的view
    private boolean isBottomIndex(View childeview) {
        int maxSize = layoutManagerGrid.getSpanSizeLookup().getSpanGroupIndex(layoutManagerGrid.getItemCount() - 1, layoutManagerGrid.getSpanCount());
        return maxSize == layoutManagerGrid.getSpanSizeLookup().getSpanGroupIndex(getSpanGroupIndex(childeview), layoutManagerGrid.getSpanCount());
    }

    //dp转px
    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}


