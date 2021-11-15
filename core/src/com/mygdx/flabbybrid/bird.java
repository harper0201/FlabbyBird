package com.mygdx.flabbybrid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

import java.util.Set;

public class bird {
    private Texture[] birds;
    private int flapState = 0;
    private Circle birdCircle;
    public bird(){
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
    }
    public Texture getBird(){
        return birds[flapState];
    }

    public void setFlapState() {
        if(flapState == 0){
            flapState = 1;
        }
        else{
            flapState = 0;
        }
    }
}
