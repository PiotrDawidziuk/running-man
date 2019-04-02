package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RunningMan extends ApplicationAdapter {
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
	}

	@Override
	public void render () {

		batch.begin();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
