package com.mygdx.flabbybrid;

import com.badlogic.gdx.AbstractGraphics;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlabbyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 2;
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
	Rectangle[] buttonTubeRectangles;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birdCircle = new Circle();
		//shapeRenderer = new ShapeRenderer();
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdY  = Gdx.graphics.getHeight()/2 - birds[0].getWidth()/2;
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		ramdomGenerotor = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		buttonTubeRectangles = new Rectangle[numberOfTubes];
		for(int i = 0; i < numberOfTubes; i++){
			tubeOffset[i] =
					(ramdomGenerotor.nextFloat() - 0.5f) *(Gdx.graphics.getHeight()- gap - 200);
			tubeX[i] =
					Gdx.graphics.getWidth()/2 - topTube.getWidth() + Gdx.graphics.getWidth() + i*distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			buttonTubeRectangles[i] = new Rectangle();

		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gameState != 0) {
			if (Gdx.input.justTouched()) {
				velocity = -30;

			}
			for(int i = 0 ; i < numberOfTubes; i++){
				if(tubeX[i] < -topTube.getWidth()){
					tubeX[i] = numberOfTubes * distanceBetweenTubes;
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
				buttonTubeRectangles[i] = new Rectangle(tubeX[i],
						Gdx.graphics.getHeight()/2-gap/2-bottomTube.getHeight() + tubeOffset[i],
						bottomTube.getWidth(),bottomTube.getHeight());
			}
			if(birdY > 0 || velocity < 0 ){
				velocity = velocity + gravity;
				birdY -= velocity;
			}
		} else {
			if (Gdx.input.isTouched()) {
				gameState = 1;
			}
		}

		if(flapState == 0){
			flapState = 1;
		}
		else{
			flapState = 0;
		}

		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY + birds[flapState].getHeight()/2,
				birds[flapState].getWidth()/2);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for (int i = 0; i < numberOfTubes; i++){
			//shapeRenderer.rect(tubeX[i],
			//		Gdx.graphics.getHeight()/2+ gap/2 + tubeOffset[i],
			//		topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeX[i],
			//		Gdx.graphics.getHeight()/2-gap/2-bottomTube.getHeight() + tubeOffset[i],
			//		bottomTube.getWidth(),bottomTube.getHeight());
			if(Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,buttonTubeRectangles[i])){
				Gdx.app.log("Collosion","Yes!");
			}
		}
		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();

	}
}
