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
	Texture[] birds;
	Texture gameStart;
	Texture gameOver;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 1;
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
	BitmapFont Score;
	Music music;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birdCircle = new Circle();
		//shapeRenderer = new ShapeRenderer();
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gameOver = new Texture("gameover.png");
		gameStart = new Texture("gamestart.png");
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();

		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		ramdomGenerotor = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		buttomTubeRectangles = new Rectangle[numberOfTubes];
		Score = new BitmapFont();
		Score.setColor(Color.WHITE);
		Score.getData().setScale(5);
		Preferences prefs = Gdx.app.getPreferences("game preferences");
		startGame();

	}
	public void startGame() {
		birdY  = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		for(int i = 0; i < numberOfTubes; i++){
			tubeOffset[i] =
					(ramdomGenerotor.nextFloat() - 0.5f) *(Gdx.graphics.getHeight()- gap - 200);
			tubeX[i] =
					Gdx.graphics.getWidth()/2 - topTube.getWidth() + Gdx.graphics.getWidth() + i*distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			buttomTubeRectangles[i] = new Rectangle();
		}
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gameState == 1) {
			batch.draw(birds[flapState],Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY);
			birdCircle.set(Gdx.graphics.getWidth()/2,birdY + birds[flapState].getHeight()/2,
					birds[flapState].getWidth()/2);
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
				velocity = -20;
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
				if(Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,buttomTubeRectangles[i])){
					Gdx.app.log("collision","yes");
					gameState = 2;
				}
			}
			if(birdY > 0 ){
				velocity = velocity + gravity;
				birdY -= velocity;
			}
			else{
				gameState = 2;
			}
		} else if (gameState == 0) {
			batch.draw(gameStart, Gdx.graphics.getWidth() / 2 - gameStart.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameStart.getHeight() / 2);
			if (Gdx.input.isTouched()) {
				gameState = 1;
			}
		}else if(gameState ==2){
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
			Score.draw(batch,"Score:"+ String.valueOf(score),400,300);
			if (Gdx.input.justTouched()) {
				gameState = 0;
				startGame();
				score = 0;
				scoreTube = 0;
				velocity = 0;
			}
		}

		if(flapState == 0){
			flapState = 1;
		}
		else{
			flapState = 0;
		}

		batch.end();
		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		music.dispose();

	}
}
