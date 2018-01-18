package com.example.transcend.gamewithball;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by transcend on 01.01.2018.
 */
public class SceneFragment extends Fragment {

    private final int LEFT = 0;
    private final int TOP = 1;
    private final int RIGHT = 2;
    private final int BOTTOM =3;

    private View ballView;
    private View platformView;
    private View backgroundView;

    private static final String TAG = "SceneFragment";
    private boolean CONTINUE = false;

    public static SceneFragment newInstance(){
        return new SceneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_scene,container,false);

        ballView = view.findViewById(R.id.ball);
        backgroundView = view.findViewById(R.id.background);
        platformView = view.findViewById(R.id.platform);

        Toast.makeText(getContext(),"Tap to Start",Toast.LENGTH_SHORT).show();

        platformView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event){
                PointF current = new PointF(event.getX(),event.getY());

                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE: {
                        if((platformView.getX() + current.x)<(backgroundView.getRight()-platformView.getWidth())){
                            platformView.setX(platformView.getX() + current.x);
                        }else{
                            platformView.setX(backgroundView.getRight()-platformView.getWidth());
                        }
                        break;
                    }
                    default: {break;}
                }

                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CONTINUE==false){
                    Toast.makeText(getContext(),"Move platform around, to play with a ball :)",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),"Tap again to stop", Toast.LENGTH_SHORT).show();
                    CONTINUE = true;
                    float ballYStart = ballView.getY();
                    float ballXStart = ballView.getX();
                    runAnimation(ballYStart,ballXStart,BOTTOM);
                } else{
                    Toast.makeText(getContext(),"Tap to Start",Toast.LENGTH_SHORT).show();
                    CONTINUE = false;
                }
            }
        });
        return view;
    }

    private float getNext(int angle, double b){ // position on the screen
        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));
        double s = b/cos;
        double a = sin*s;
        return (float) a;
    }

    private void runAnimation(float ballYStart,float ballXStart, int direction){

        Random r = new Random();
        int angle = r.nextInt(20) + 25;

        final float ballYEnd;
        final float ballXEnd;
        final int nextDirection;

        switch (direction) {
            case LEFT:{
                float YEnd = getNext(angle,Math.abs(ballView.getLeft() - backgroundView.getLeft()));
                ballXEnd = (float) backgroundView.getLeft();
                ballYEnd = YEnd;
                break;}
            case TOP:{
                ballXEnd = getNext(angle,Math.abs(ballView.getTop() - backgroundView.getTop()));
                ballYEnd = (float) backgroundView.getTop();
                break;}
            case RIGHT:{
                float YEnd = getNext(angle,Math.abs(ballView.getRight() - backgroundView.getRight()));
                ballXEnd = (float) backgroundView.getRight() - ballView.getWidth();
                ballYEnd = YEnd;
                break;}
            case BOTTOM:{
                ballXEnd = getNext(angle,Math.abs(ballView.getBottom() - backgroundView.getBottom()));
                float YEnd = (float) backgroundView.getBottom() - ballView.getWidth();
                if((ballXEnd>=(platformView.getX() - ballView.getWidth())) && (ballXEnd<=(platformView.getX()+platformView.getWidth())) && (YEnd >=platformView.getY())){
                    ballYEnd = platformView.getY() - ballView.getWidth();
                }else{ballYEnd = YEnd;}
                break;}
            default:{
                ballXEnd = getNext(angle,Math.abs(ballView.getBottom() - backgroundView.getBottom()));
                float YEnd = (float) backgroundView.getBottom() - ballView.getWidth();
                if((ballXEnd>=(platformView.getX()-ballView.getWidth())) && (ballXEnd<=(platformView.getX()+platformView.getWidth())) && (YEnd >=platformView.getY())){
                    ballYEnd = platformView.getY() - ballView.getWidth();
                }else{ballYEnd = YEnd;}
                break;}
        }
        int tempInt;
        do {
            tempInt = r.nextInt(4);
        }while(tempInt==direction);
        nextDirection = tempInt;

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(ballView,"y", ballYStart, ballYEnd);
        yAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(ballView,"x", ballXStart, ballXEnd);
        xAnimator.setInterpolator(new AccelerateInterpolator());

        animatorSet.playTogether(yAnimator,xAnimator);
        if(CONTINUE == true){
        animatorSet.start();
        } else{
            animatorSet.cancel();
        }

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                runAnimation(ballYEnd,ballXEnd,nextDirection);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}
