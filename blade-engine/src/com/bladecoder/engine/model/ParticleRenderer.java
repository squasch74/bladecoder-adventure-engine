/*******************************************************************************
 * Copyright 2014 Rafael Garcia Moreno.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bladecoder.engine.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.bladecoder.engine.assets.EngineAssetManager;
import com.bladecoder.engine.util.RectangleRenderer;
import com.bladecoder.engine.util.SerializationHelper;
import com.bladecoder.engine.util.SerializationHelper.Mode;

public class ParticleRenderer implements ActorRenderer {

	private final static float DEFAULT_DIM = 200;

	private final ParticleEffect effect = new ParticleEffect();

	private float lastAnimationTime = 0;

	private Polygon bbox;
	
	private String particleName;
	private String atlasName;
	
	private TextureAtlas atlasTex;
	
	private float tmpPosX = 0;
	private float tmpPosY = 0;

	public ParticleRenderer() {

	}

	@Override
	public void update(float delta) {
		effect.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch, float x, float y, float scale, Color tint) {

		if (effect.getEmitters().size > 0) {
			
			if(tmpPosX != x || tmpPosY != y) {
				tmpPosX = x / scale;
				tmpPosY = y / scale;
				
				effect.setPosition(tmpPosX, tmpPosY);
			}
					
			batch.setTransformMatrix(batch.getTransformMatrix().scale(scale, scale, 1.0f));
			
			if(tint != null)
				batch.setColor(tint);
			
			effect.draw(batch);
			
			if(tint != null)
				batch.setColor(Color.WHITE);
			
			batch.setTransformMatrix(batch.getTransformMatrix().scale(1 / scale, 1 / scale, 1.0f));
		} else {
			x = x - getWidth() / 2 * scale;
			RectangleRenderer.draw(batch, x, y, getWidth() * scale, getHeight() * scale, Color.RED);
		}
	}

	@Override
	public float getWidth() {
		return DEFAULT_DIM;
	}

	@Override
	public float getHeight() {
		return DEFAULT_DIM;
	}

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}
	
	public String getAtlasName() {
		return atlasName;
	}

	public void setAtlasName(String atlasName) {
		this.atlasName = atlasName;
	}

	@Override
	public void updateBboxFromRenderer(Polygon bbox) {
		this.bbox = bbox;
		
		computeBbox();
	}
	
	private void computeBbox() {
		if (bbox == null)
			return;

		if (bbox.getVertices() == null || bbox.getVertices().length != 8) {
			bbox.setVertices(new float[8]);
		}

		float[] verts = bbox.getVertices();

		verts[0] = -getWidth() / 2;
		verts[1] = 0f;
		verts[2] = -getWidth() / 2;
		verts[3] = getHeight();
		verts[4] = getWidth() / 2;
		verts[5] = getHeight();
		verts[6] = getWidth() / 2;
		verts[7] = 0f;
		bbox.dirty();
	}

	@Override
	public void loadAssets() {
		EngineAssetManager.getInstance().loadAtlas(getAtlasName());
	}

	@Override
	public void retrieveAssets() {
		atlasTex = EngineAssetManager.getInstance().getTextureAtlas(getAtlasName());
		
		effect.load(EngineAssetManager.getInstance().getParticle(getParticleName()), atlasTex);
		effect.start();
		effect.update(lastAnimationTime);

		computeBbox();
	}

	@Override
	public void dispose() {
		EngineAssetManager.getInstance().disposeAtlas(getAtlasName());
	}

	@Override
	public void write(Json json) {
		
		if (SerializationHelper.getInstance().getMode() == Mode.MODEL) {
			json.writeValue("atlasName", getAtlasName());
			json.writeValue("particleName", getParticleName());
		} else {		
			json.writeValue("lastAnimationTime", lastAnimationTime);
		}
	}

	@Override
	public void read(Json json, JsonValue jsonData) {	
		if (SerializationHelper.getInstance().getMode() == Mode.MODEL) {
			setAtlasName(json.readValue("atlasName", String.class, jsonData));
			setParticleName(json.readValue("particleName", String.class, jsonData));
		} else {		
			lastAnimationTime = json.readValue("lastAnimationTime", Float.class, jsonData);
		}
	}
}