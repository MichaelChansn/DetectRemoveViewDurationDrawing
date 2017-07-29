package com.ks.library.detect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.ks.library.log.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>
 *
 * @author kangsen
 * @version v9.0
 * @since 2017/7/29
 */

public class RemoveViewInDrawingDetectUtils {
    private static Method sMethod;
    private static Field sField;
    
    public static void bindRootView(Context activity) {
        if (!(activity instanceof Activity)) {
            LogUtil.e("context must be a activity");
            throw new RuntimeException("context must be a activity");
        }
        setViewChangeListeners((Activity) activity);
    }
    
    private static void setViewChangeListeners(Activity activity){
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        if(rootView == null){
            LogUtil.e("DecorView is null");
            return;
        }
        
        
        setViewChangeListener(rootView, getListener());
    }
    
    private static ViewGroup.OnHierarchyChangeListener getListener() {
        
        return new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                // 给新add的view添加监听器
                setViewChangeListener(child, this);
            }
            
            @Override
            public void onChildViewRemoved(View parent, View child) {
                // 删除view的时候正在drawing，直接抓取当前错误信息上报
                if (isDrawing(parent)) {
                    printStack((ViewGroup) parent, child);
                }
            }
        };
    }
    
    /**
     * HierarchyChangeListener 只能监听到viewGroup的直接子view变化，子view的子view无法监听，所以要遍历所有的viewgroup去设置
     * @param view 需要设置监听的view
     * @param changeListener 用于监听view增加和删除的监听器
     */
    private static void setViewChangeListener(View view, ViewGroup.OnHierarchyChangeListener changeListener) {
        if (view != null && changeListener != null && view instanceof ViewGroup) {
            ((ViewGroup) view).setOnHierarchyChangeListener(changeListener);
            if (((ViewGroup) view).getChildCount() > 0) {
                for (int index = 0; index < ((ViewGroup) view).getChildCount(); index++) {
                    setViewChangeListener(((ViewGroup) view).getChildAt(index), changeListener);
                }
            }
        }
    }
    
    /**
     * 判断当前的父viewGroup是否正在draw
     * @param parent 正在remove or add 的view的父viewGroup
     * @return true正在draw false不在绘制期
     */
    private static boolean isDrawing(View parent) {
        if (parent == null) {
            return false;
        }
        boolean isDrawing = false;
        try {
            if (sMethod == null) {
                sMethod = View.class.getDeclaredMethod("getViewRootImpl");
                sMethod.setAccessible(true);
            }
            Object viewRoot = sMethod.invoke(parent);
            if (viewRoot == null) {
                return false;
            }
            if (sField == null) {
                // ViewRootImpl只有一个实例，直接获取一次field即可
                sField = viewRoot.getClass().getDeclaredField("mIsDrawing");
                sField.setAccessible(true);
            }
        
            isDrawing = sField.getBoolean(viewRoot);
            LogUtil.d("isDrawing --> " + isDrawing);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDrawing;
    }
    
    private static String printStack(ViewGroup parent, View child){
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("some view is removing duration parent viewGroup is drawing");
        sb.append("\n");
        if(parent != null) {
            sb.append("parent class --> ");
            sb.append(parent.getClass().getCanonicalName());
            sb.append("\n");
            sb.append("parent id --> ");
            sb.append(parent.getId());
            sb.append("\n");
        }
        if(child != null) {
            sb.append("child class --> ");
            sb.append(child.getClass().getCanonicalName());
            sb.append("\n");
            sb.append("child id --> ");
            sb.append(child.getId());
            sb.append("\n");
        }
        for (StackTraceElement element: stackTraces) {
            sb.append(element.toString()).append("\n");
        }
        LogUtil.d("thread stack :\n" + sb.toString());
        return sb.toString();
    }
    
}
