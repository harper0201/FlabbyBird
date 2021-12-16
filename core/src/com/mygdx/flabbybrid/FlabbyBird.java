package com.mygdx.flabbybrid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlabbyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameStart;
	Texture gameOver;
	float birdY = 0;
	float velocity = 0;
	float gravity = 0.5f;
	float gap = 400;
	Texture topTube;
	Texture bottomTube;
	float maxTubeOffset;
	Random ramdomGenerotor;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float []tubeX = new float[numberOfTubes];
	float []tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Circle birdCircle;
	//ShapeRenderer shapeRenderer;
	Rectangle[] topTubeRectangles;
	Rectangle[] buttomTubeRectangles;
	int score = 0;
	int scoreTube = 0;
	int highscore = 0;
	BitmapFont Score;
	BitmapFont HighScore;
	Music music;
	Preferences prefs;
	gameState gamestate;
	bird birds;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gameOver = new Texture("gameover.png");
		gameStart = new Texture("gamestart.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		ramdomGenerotor = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		buttomTubeRectangles = new Rectangle[numberOfTubes];
		Score = new BitmapFont();
		Score.setColor(Color.WHITE);
		Score.getData().setScale(5);
		HighScore = new BitmapFont();
		HighScore.setColor(Color.WHITE);
		HighScore.getData().setScale(5);
		prefs = Gdx.app.getPreferences("game preferences");
		gamestate = new gameState();
		birds = new bird();
		startGame();
		//shapeRenderer = new ShapeRenderer();
	}
	public void startGame() {
		birdY  = Gdx.graphics.getHeight()/2 - birds.getBird().getHeight()/2;
		for(int i = 0; i < numberOfTubes; i++){
			tubeOffset[i] =
					(ramdomGenerotor.nextFloat() - 0.5f) *(Gdx.graphics.getHeight()- gap - 200);
			tubeX[i] =
					Gdx.graphics.getWidth()/2 - topTube.getWidth() + Gdx.graphics.getWidth() + i*distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			buttomTubeRectangles[i] = new Rectangle();
		}
		playMusic();
	}

	public void playMusic(){
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gamestate.getGamestate() == State.Play) {
			batch.draw(birds.getBird(),Gdx.graphics.getWidth()/2 - birds.getBird().getWidth()/2, birdY);
			birdCircle = new Circle();
			birdCircle.set(Gdx.graphics.getWidth()/2,birdY + birds.getBird().getHeight()/2,
					birds.getBird().getWidth()/2);
			//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.RED);
			//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
			Score.draw(batch,String.valueOf(score),100,200);
			if(tubeX[scoreTube] < Gdx.graphics.getWidth()/2){
				score++;
				if(scoreTube < numberOfTubes - 1){
					scoreTube++;
				}
				else{
					scoreTube = 0;
				}
			}
			if (Gdx.input.justTouched()) {
				velocity = -10;
			}
			for(int i = 0 ; i < numberOfTubes; i++){
				if(tubeX[i] < -topTube.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] =
							(ramdomGenerotor.nextFloat() - 0.5f) *(Gdx.graphics.getHeight()- gap - 200);
				}
				else{
					tubeX[i] = tubeX[i] - tubeVelocity;
				}
				batch.draw(topTube,tubeX[i],
						Gdx.graphics.getHeight()/2+ gap/2 + tubeOffset[i]);
				batch.draw(bottomTube,tubeX[i],
						Gdx.graphics.getHeight()/2-gap/2-bottomTube.getHeight() + tubeOffset[i]);
				topTubeRectangles[i] =
						new Rectangle(tubeX[i],
								Gdx.graphics.getHeight()/2+ gap/2 + tubeOffset[i],
								topTube.getWidth(),topTube.getHeight());
				buttomTubeRectangles[i] = new Rectangle(tubeX[i],
						Gdx.graphics.getHeight()/2-gap/2-bottomTube.getHeight() + tubeOffset[i],
						bottomTube.getWidth(),bottomTube.getHeight());
				//shapeRenderer.rect(tubeX[i],
				//		Gdx.graphics.getHeight()/2+ gap/2 + tubeOffset[i],
				//		topTube.getWidth(),topTube.getHeight());
				//shapeRenderer.rect(tubeX[i],
				//		Gdx.graphics.getHeight()/2-gap/2-bottomTube.getHeight() + tubeOffset[i],
				//		bottomTube.getWidth(),bottomTube.getHeight());
				//shapeRenderer.end();
				if(Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,buttomTubeRectangles[i])){
					Gdx.app.log("collision","yes");
					gamestate.setGamestate(State.Over);
				}
			}
			if(birdY > 0 ){
				velocity = velocity + gravity;
				birdY -= velocity;
			}
			else{
				gamestate.setGamestate(State.Over);
			}
		} else if (gamestate.getGamestate() == State.Intro) {
			batch.draw(gameStart, Gdx.graphics.getWidth() / 2 - gameStart.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameStart.getHeight() / 2);
			if (Gdx.input.isTouched()) {
				gamestate.setGamestate(State.Play);
			}
		}else if(gamestate.getGamestate() == State.Over){
			if(score > highscore){
				prefs.putInteger("highScore",score);
				prefs.flush();
			}
			highscore = prefs.getInteger("highScore");
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
			Score.draw(batch,"Score:"+ String.valueOf(score),400,300);
			HighScore.draw(batch,"HighScore:"+ String.valueOf(highscore),350,1500);
			if (Gdx.input.justTouched()) {
				gamestate.setGamestate(State.Intro);
				startGame();
				score = 0;
				scoreTube = 0;
				velocity = 0;
			}
		}
		birds.setFlapState();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		music.dispose();

	}
}
