package it.polimi.expogame.support;

import android.graphics.Point;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;


/**
 * Created by andrea on 07/03/15.
 */
public class TutorialAnimationManager {

    private TextView textSpeakMascot;
    private ImageView mascot;
    private Point screenSize;
    private FrameLayout parent;

    private Handler startHandler = new Handler();
    private Handler nextHandler = new Handler();
    private ArrayList<String> tutorialStrings;
    private Animation enterAnimation;

    private static final long UPDATE_INTERVAL = 2500;

    public TutorialAnimationManager(TextView textSpeakMascot, ImageView mascot, Point screenSize, FrameLayout parent, ArrayList<String> tutorialStrings){
        this.textSpeakMascot = textSpeakMascot;
        this.mascot = mascot;
        this.screenSize = screenSize;
        this.parent = parent;
        this.tutorialStrings = tutorialStrings;
        setupStartAnimation();
    }


    public void startEnterAnimation(){
        mascot.startAnimation(enterAnimation);
    }

    private void setupStartAnimation(){
        int width = screenSize.x;
        float to =  ((float)width)/3;
        enterAnimation = new TranslateAnimation(width, to,
                0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        enterAnimation.setDuration(3000);  // animation duration
        enterAnimation.setFillAfter(true);
        enterAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mascot.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startHandler.postDelayed(new UpdateTextRunnable(tutorialStrings),UPDATE_INTERVAL);
                textSpeakMascot.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        parent.addView(mascot);
    }





    /**
     * Private class in order to update the text for the tutorial
     */
    private class UpdateTextRunnable implements Runnable{

        private ArrayList<String> texts;

        public UpdateTextRunnable(ArrayList<String> texts){
            this.texts = texts;
        }

        @Override
        public void run() {
            //if i have some text to show update
            if(texts.size() >0) {
                textSpeakMascot.setText(texts.remove(0));
                nextHandler.postDelayed(new UpdateTextRunnable(texts), UPDATE_INTERVAL);
            }else{
                //else start animation out
                textSpeakMascot.setVisibility(View.INVISIBLE);
                int width = screenSize.x;
                float to =  ((float)width)/3;
                TranslateAnimation outAnimation = new TranslateAnimation(to, width,
                        0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
                outAnimation.setDuration(3000);  // animation duration
                outAnimation.setFillAfter(true);
                mascot.startAnimation(outAnimation);
            }
        }
    }
}