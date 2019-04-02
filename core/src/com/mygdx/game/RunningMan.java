package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class RunningMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState=0;
	int pause =0;
	float gravity = 0.2f;
	float velocity =0f;
	int manY = 0;
	Rectangle manRectangle;
	BitmapFont font;
	Texture dizzy;

	int score = 0;
	int gameState = 0;

	Random random;

	ArrayList<Integer> envelopeXs = new ArrayList<Integer>();
	ArrayList<Integer> envelopeYs = new ArrayList<Integer>();
	ArrayList<Rectangle> envelopeRectangles = new ArrayList<Rectangle>();
	Texture envelope;
	int envelopeCount;

	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight()/2;

		envelope = new Texture("envelope.png");
		bomb = new Texture("bomb.png");
		dizzy = new Texture("dizzy-1.png");

		random=new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

	}

	public void makeenvelope(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		envelopeYs.add((int)height);
		envelopeXs.add(Gdx.graphics.getWidth());

	}

	public void makeBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());

	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {
			// GAME IS LIVE

			//bombs
			if (bombCount < 250) {
				bombCount++;
			} else {
				bombCount = 0;
				makeBomb();
			}

			bombRectangles.clear();
			for (int i = 0; i < bombXs.size(); i++) {
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 8);
				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}

			//envelopes
			if (envelopeCount < 100) {
				envelopeCount++;
			} else {
				envelopeCount = 0;
				makeenvelope();
			}


			envelopeRectangles.clear();
			for (int i = 0; i < envelopeXs.size(); i++) {
				batch.draw(envelope, envelopeXs.get(i), envelopeYs.get(i));
				envelopeXs.set(i, envelopeXs.get(i) - 5);
				envelopeRectangles.add(new Rectangle(envelopeXs.get(i), envelopeYs.get(i), envelope.getWidth(), envelope.getHeight()));
			}

			if (Gdx.input.justTouched()) {
				velocity = -10;
			}

			if (pause < 4) {
				pause++;
			} else {
				pause = 0;

				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			if (manY <= 0) {
				manY = 0;
			}


		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}


		} else if (gameState == 2) {
			// GAME OVER
			if (Gdx.input.justTouched()) {
				gameState = 1;
				manY = Gdx.graphics.getHeight() / 2;
				score = 0;
				velocity = 0;
				envelopeXs.clear();
				envelopeYs.clear();
				envelopeRectangles.clear();
				envelopeCount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;

			}


		}

		if (gameState == 2) {
			batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		} else {
			batch.draw(man[manState],Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		manRectangle=new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());

		for (int i=0; i<envelopeRectangles.size(); i++){
			if(Intersector.overlaps(manRectangle,envelopeRectangles.get(i))){
				//Gdx.app.log("envelope!","Collision!");
				score++;
				envelopeRectangles.remove(i);
				envelopeXs.remove(i);
				envelopeYs.remove(i);
				break;
			}
		}

		for (int i=0; i<bombRectangles.size(); i++){
			if(Intersector.overlaps(manRectangle,bombRectangles.get(i))){
				//Gdx.app.log("Bomb!","BOOOOOOOM!");
				gameState=2;
			}
		}

		font.draw(batch,String.valueOf(score),100,200);

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
