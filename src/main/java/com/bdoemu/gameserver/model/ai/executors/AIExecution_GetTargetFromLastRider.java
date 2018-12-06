package com.bdoemu.gameserver.model.ai.executors;

import com.bdoemu.gameserver.model.ai.model.AIHandler;
import com.eclipsesource.json.JsonObject;

public class AIExecution_GetTargetFromLastRider extends AIExecution {
    // state 	 - string
    // blendTime - float
    /**
     * AI Executor constructor.
     * It reads a specific AI Executor attributes from
     * an Black Desert Online AI XML file.
     *
     * @param handler Owner of Executor.
     * @param element AI XML of current executor.
     */
    public AIExecution_GetTargetFromLastRider(AIHandler handler, JsonObject element) {
        super(handler, element);
    }

    @Override
    public void execute(int deltaTime) {

    }
}