package com.emredogan.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
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

	int score = 0;

	int scoringTube = 0;

	int velocity = 0;

	int gameState = 0;

	float gravity = 2;

	Texture toptube;
	Texture bottomtube;
	Texture gameover;

	float gap = 400;

	float maxTubeOffSet;

	Random randomGenerator;

	BitmapFont font;


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
		gameover = new Texture("gameover.png");

		maxTubeOffSet = Gdx.graphics.getHeight() / 2 - gap/2 - 100;

		randomGenerator = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

	//	shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();

		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		startGame();







	}

	public void startGame() {
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

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


		if (gameState == 1) {

			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;

				Gdx.app.log("Score", String.valueOf(score));

				if(scoringTube < numberOfTubes - 1) {
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}


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


			if (birdY > 0) {

				velocity += gravity;
				birdY -= velocity;

			} else {
				gameState = 2;
			}








		} else if (gameState == 0) {


			if(Gdx.input.justTouched()) {

				gameState = 1;

			}
		} else if (gameState == 2) {

			batch.draw(gameover, Gdx.graphics.getWidth() / 2 -gameover.getWidth()/2, Gdx.graphics.getHeight() / 2 - gameover.getHeight()/2 );

			if(Gdx.input.justTouched()) {

				gameState = 1;

				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;


			}

		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}



		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2,birdY);

		font.draw(batch,String.valueOf(score), 100,200);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() /2, birdY + birds[flapState].getHeight() /2, birds[flapState].getWidth() /2);





	//	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	//	shapeRenderer.setColor(Color.BLUE);

	//	shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for(int i=0; i< numberOfTubes; i++) {

		//	shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],toptube.getWidth(),toptube.getHeight());
		//	shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i],bottomtube.getWidth(),bottomtube.getHeight());

			if (Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])) {

				gameState = 2;


			}
		}

	//	shapeRenderer.end();







	}
	

}
