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
package com.bladecoder.engine.actions;

import com.bladecoder.engine.actions.Param.Type;
import com.bladecoder.engine.anim.SpriteScaleTween;
import com.bladecoder.engine.anim.Tween;
import com.bladecoder.engine.model.SpriteActor;
import com.bladecoder.engine.model.VerbRunner;
import com.bladecoder.engine.model.World;
import com.bladecoder.engine.util.InterpolationMode;

@ActionDescription(name = "ScaleAnim", value = "Proportional Scale animation for sprite actors")
public class ScaleAction implements Action {
	@ActionPropertyDescription("The target actor")
	@ActionProperty(type = Type.SPRITE_ACTOR)
	private String actor;

	@ActionProperty(required = true)
	@ActionPropertyDescription("The target scale")
	private float scale;

	@ActionProperty(required = true, defaultValue = "1.0")
	@ActionPropertyDescription("Duration of the animation in seconds")
	private float speed;

	@ActionProperty
	@ActionPropertyDescription("The The times to repeat")
	private int count = -1;

	@ActionProperty(required = true)
	@ActionPropertyDescription("If this param is 'false' the transition is showed and the action continues inmediatly")
	private boolean wait = true;

	@ActionProperty(required = true, defaultValue = "NO_REPEAT")
	@ActionPropertyDescription("The repeat mode")
	private Tween.Type repeat = Tween.Type.NO_REPEAT;

	@ActionProperty
	@ActionPropertyDescription("The interpolation mode")
	private InterpolationMode interpolation;

	private World w;

	@Override
	public void init(World w) {
		this.w = w;
	}

	@Override
	public boolean run(VerbRunner cb) {
		SpriteActor a = (SpriteActor) w.getCurrentScene().getActor(actor, true);

		SpriteScaleTween t = new SpriteScaleTween();
		t.start(a, repeat, count, scale, scale, speed, interpolation, wait ? cb : null);

		a.addTween(t);

		return wait;
	}

}
