package com.emredogan.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import sun.rmi.runtime.Log;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
//	ShapeRenderer shapeRenderer;

	float birdY = 0;

	int flapState = 0;

	Circle birdCircle;

	int velocity = 0;

	int gameState = 0;

	float gravity = 2;

	Texture toptube;
	Texture bottomtube;

	float gap = 400;

	float maxTubeOffSet;

	Random randomGenerator;


	float tubeVelocity = 4;


	int numberOfTubes = 4;

	float[] tubeX = new float[numberOfTubes];

	float[] tubeOffSet = new float[numberOfTubes];



	float distanceBetweenTubes;

	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");

		maxTubeOffSet = Gdx.graphics.getHeight() / 2 - gap/2 - 100;

		randomGenerator = new Random();

		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

	//	shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();

		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		for(int i=0; i< numberOfTubes; i++) {


			tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth()/2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i *distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

		}





	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if (gameState != 0) {


			if(Gdx.input.justTouched()) {

				velocity = -30;

			}

			for(int i=0; i< numberOfTubes; i++) {


				if(tubeX[i] < - toptube.getWidth()) {

					tubeX[i] += numberOfTubes * distanceBetweenTubes;

					tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {

					tubeX[i] -= tubeVelocity;

				}




				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i]);

				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],toptube.getWidth(),toptube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i],bottomtube.getWidth(),bottomtube.getHeight());


			}


			if (birdY > 0 || velocity < 0) {

				velocity += gravity;
				birdY -= velocity;

			}








		} else {


			if(Gdx.input.justTouched()) {

				gameState = 1;
			}
		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}



		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2,birdY);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() /2, birdY + birds[flapState].getHeight() /2, birds[flapState].getWidth() /2);



	//	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	//	shapeRenderer.setColor(Color.BLUE);

	//	shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for(int i=0; i< numberOfTubes; i++) {

		//	shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],toptube.getWidth(),toptube.getHeight());
		//	shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i],bottomtube.getWidth(),bottomtube.getHeight());

			if (Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])) {

				Gdx.app.log("Collision","YES");

			}
		}

		shapeRenderer.end();







	}
	

}
