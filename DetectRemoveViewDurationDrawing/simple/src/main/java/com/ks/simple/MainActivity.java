package com.ks.simple;

import java.util.Random;

import com.ks.library.detect.RemoveViewInDrawingDetectUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ViewGroup v_1;
    ViewGroup v_2;
    ViewGroup v_1_1;
    ViewGroup v_2_1;
    Button add_view_group_1;
    Button add_view_group_2;
    Button add_view_group_1_1;
    Button add_view_group_2_1;
    
    View.OnClickListener clickListener;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    
    private void init(){
        v_1 = (ViewGroup) findViewById(R.id.viewGroup_1);
        v_2 = (ViewGroup) findViewById(R.id.viewGroup_2);
        v_1_1 = (ViewGroup) findViewById(R.id.viewGroup_1_1);
        v_2_1 = (ViewGroup) findViewById(R.id.viewGroup_2_1);
        add_view_group_1 = (Button) findViewById(R.id.add_view_group_1);
        add_view_group_2 = (Button) findViewById(R.id.add_view_group_1_1);
        add_view_group_1_1 = (Button) findViewById(R.id.add_view_group_2);
        add_view_group_2_1 = (Button) findViewById(R.id.add_view_group_2_1);
        clickListener = new View.OnClickListener() {
            Random random = new Random();
            @Override
            public void onClick(View v) {
                
                switch (v.getId()) {
                    case R.id.add_view_group_1:
                        addViews(v_1,random.nextInt(10));
                        break;
                    case R.id.add_view_group_2:
                        addViews(v_2,random.nextInt(20));
                        break;
                    case R.id.add_view_group_1_1:
                        addViews(v_1_1,random.nextInt(30));
                        break;
                    case R.id.add_view_group_2_1:
                        addViews(v_2_1,random.nextInt(40));
                        break;
                }
                
            }
            
        };
        // HierarchyChangeListener 只能监听到viewGroup的直接子view变化，子view的子view无法监听
        //        v_1.setOnHierarchyChangeListener(viewChangeListener);
        //        v_2.setOnHierarchyChangeListener(viewChangeListener);
        add_view_group_1.setOnClickListener(clickListener);
        add_view_group_2.setOnClickListener(clickListener);
        add_view_group_1_1.setOnClickListener(clickListener);
        add_view_group_2_1.setOnClickListener(clickListener);
        
        RemoveViewInDrawingDetectUtils.bindRootView(this);
        
        
    }
    
    
    private void addViews(ViewGroup parent, int nums) {
        for (int i = 0; i < nums; i++) {
            MyTextView textView = new MyTextView(MainActivity.this);
            int randomId = (int) (SystemClock.currentThreadTimeMillis()>>>33);
            textView.setText("" + randomId);
            textView.setId(randomId);
            parent.addView(textView);
            animation(textView);
        }
    }
    
    private void animation(final View view){
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setStartOffset(200);
        animation.setDuration(2000);
        view.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                if(view.getParent() != null){
                    ((ViewGroup)view.getParent()).removeView(view);
                }
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
        });
        
        view.startAnimation(animation);
    }
    
    
    
    private class MyTextView extends TextView {
        
        public MyTextView(Context context) {
            super(context);
        }
        
        public MyTextView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }
        
        public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
        
        Random random = new Random();
        
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(this.getParent() != null && random.nextBoolean()){
                ((ViewGroup)this.getParent()).removeViewAt(0);
            }
        }
    }
}
