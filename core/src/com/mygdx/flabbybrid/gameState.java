package com.mygdx.flabbybrid;

enum State
{
    Intro,Play,Over;
}
public class gameState {
    private State gamestate = State.Intro;
    public State getGamestate(){
        return gamestate;
    }
    public void setGamestate(State newSate){
        gamestate = newSate;
    }

}
