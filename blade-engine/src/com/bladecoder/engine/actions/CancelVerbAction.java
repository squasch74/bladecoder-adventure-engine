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

import java.util.HashMap;

import com.bladecoder.engine.actions.Param.Type;
import com.bladecoder.engine.model.BaseActor;
import com.bladecoder.engine.model.Verb;
import com.bladecoder.engine.model.VerbManager;
import com.bladecoder.engine.model.World;
import com.bladecoder.engine.util.EngineLogger;

/**
 * Cancels a running verb.
 * 
 * @author rgarcia
 */
public class CancelVerbAction implements Action {
	public static final String INFO = "Stops the named verb if it is in execution.";
	public static final Param[] PARAMS = {
			new Param("actor", "The target actor", Type.ACTOR, false),
			new Param("verb", "The verb to stop", Type.STRING, true),
			new Param("target", "If the verb is 'use', the target actor",
					Type.ACTOR) };

	String actorId;
	String verb;
	String target;

	@Override
	public void setParams(HashMap<String, String> params) {
		actorId = params.get("actor");
		verb = params.get("verb");
		target = params.get("target");
	}

	@Override
	public boolean run(ActionCallback cb) {

		Verb v = null;

		if (actorId != null) {
			BaseActor a = World.getInstance().getCurrentScene()
					.getActor(actorId, true);
			v = a.getVerb(verb, target);
		}

		if (v == null) {
			v = World.getInstance().getCurrentScene().getVerb(verb);
		}

		if (v == null) {
			v = VerbManager.getWorldVerbs().get(verb);
		}

		if (v != null)
			v.cancel();
		else
			EngineLogger.error("Cannot find VERB: " + verb + " for ACTOR: " + actorId);

		return false;
	}

	@Override
	public String getInfo() {
		return INFO;
	}

	@Override
	public Param[] getParams() {
		return PARAMS;
	}

}
