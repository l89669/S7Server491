package com.bdoemu.gameserver.scripts.ai;

import com.bdoemu.commons.thread.ThreadPool;
import com.bdoemu.commons.utils.Rnd;
import com.bdoemu.gameserver.model.ai.deprecated.*;
import com.bdoemu.gameserver.model.creature.Creature;
import com.bdoemu.gameserver.model.actions.enums.*;
import com.bdoemu.gameserver.model.chat.enums.EChatNoticeType;
import com.bdoemu.gameserver.model.misc.enums.ETradeCommerceType;
import com.bdoemu.gameserver.model.weather.enums.EWeatherFactorType;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author H1X4
 */

@SuppressWarnings("all")
@IAIName("magic_heart")
public class Ai_magic_heart extends CreatureAI {
	public Ai_magic_heart(Creature actor, Map<Long, Integer> aiVariables) {
		super(actor, aiVariables);
	}

	protected void InitialState(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xAE5FEAC2L /*InitialState*/);
		setVariable(0x1C56093CL /*_isHealHP*/, getVariable(0xBA6515B5L /*AI_isHealHP*/));
		setVariable(0x6F2C7A5CL /*_isHealMP*/, getVariable(0xB3C21780L /*AI_isHealMP*/));
		setVariable(0x762AB710L /*_UnSummonTime*/, getVariable(0xCC757928L /*AI_UnSummonTime*/));
		setVariable(0x803DA7C1L /*_SkillReuseCycleTime*/, getVariable(0x53F5FA38L /*AI_SkillReuseCycleTime*/));
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, 0);
		setVariable(0x9326AD79L /*_SummonStartTime*/, 0);
		setVariable(0x51EDA18AL /*_SummonEndTime*/, 0);
		setVariable(0x35069BD4L /*_SkillStartTime*/, 0);
		setVariable(0x23B06213L /*_SkillEndTime*/, 0);
		setVariable(0x6DDCA962L /*_IngTime*/, 0);
		setVariable(0x9326AD79L /*_SummonStartTime*/, getTime());
		setVariable(0x35069BD4L /*_SkillStartTime*/, getTime());
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait(blendTime), 500));
	}

	protected void Logic(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.none);
		setState(0x9A053101L /*Logic*/);
		scheduleState(state -> Wait(blendTime), 500);
	}

	protected void Wait(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x866C7489L /*Wait*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwner());
		setVariable(0x6DDCA962L /*_IngTime*/, getTime());
		setVariable(0x51EDA18AL /*_SummonEndTime*/, getVariable(0x6DDCA962L /*_IngTime*/) - getVariable(0x9326AD79L /*_SummonStartTime*/));
		setVariable(0x23B06213L /*_SkillEndTime*/, getVariable(0x6DDCA962L /*_IngTime*/) - getVariable(0x35069BD4L /*_SkillStartTime*/));
		if (getVariable(0x51EDA18AL /*_SummonEndTime*/) > getVariable(0x762AB710L /*_UnSummonTime*/)) {
			if (changeState(state -> UnSummon(blendTime)))
				return;
		}
		if (checkOwnerEvadeEmergency()) {
			if (changeState(state -> Wait(blendTime)))
				return;
		}
		if (!checkOwnerEvadeEmergency() && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 2500) {
			if (changeState(state -> Chase_Run(blendTime)))
				return;
		}
		if (!checkOwnerEvadeEmergency() && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 2500) {
			if (changeState(state -> Teleport(blendTime)))
				return;
		}
		if (getVariable(0x1C56093CL /*_isHealHP*/) == 1 && getVariable(0x23B06213L /*_SkillEndTime*/) > getVariable(0x803DA7C1L /*_SkillReuseCycleTime*/)) {
			if (findTarget(EAIFindTargetType.OwnerPlayer, EAIFindType.normal, true, object -> getDistanceToTarget(object) <= 400)) {
				if (changeState(state -> Heal_HP(blendTime)))
					return;
			}
		}
		if (getVariable(0x6F2C7A5CL /*_isHealMP*/) == 1 && getVariable(0x23B06213L /*_SkillEndTime*/) > getVariable(0x803DA7C1L /*_SkillReuseCycleTime*/)) {
			if (findTarget(EAIFindTargetType.OwnerPlayer, EAIFindType.normal, true, object -> getDistanceToTarget(object) <= 400)) {
				if (changeState(state -> Heal_MP(blendTime)))
					return;
			}
		}
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait(blendTime), 1000 + Rnd.get(-500,500)));
	}

	protected void FailFindPath(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x80CB99B0L /*FailFindPath*/);
		doTeleport(EAIMoveDestType.OwnerPosition, 200, 0, 1, 1);
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait(blendTime), 500));
	}

	protected void Teleport(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x88C3A72EL /*Teleport*/);
		doTeleport(EAIMoveDestType.OwnerPosition, 200, 0, 1, 1);
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait(blendTime), 500));
	}

	protected void Chase_Run(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.walk);
		setState(0xD4573013L /*Chase_Run*/);
		if (isTargetLost()) {
			if (changeState(state -> Logic(blendTime)))
				return;
		}
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwner());
		setVariable(0x6DDCA962L /*_IngTime*/, getTime());
		setVariable(0x51EDA18AL /*_SummonEndTime*/, getVariable(0x6DDCA962L /*_IngTime*/) - getVariable(0x9326AD79L /*_SummonStartTime*/));
		if (getVariable(0x51EDA18AL /*_SummonEndTime*/) > getVariable(0x762AB710L /*_UnSummonTime*/)) {
			if (changeState(state -> UnSummon(blendTime)))
				return;
		}
		if (checkOwnerEvadeEmergency()) {
			if (changeState(state -> Wait(blendTime)))
				return;
		}
		if (!checkOwnerEvadeEmergency() && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 2500) {
			if (changeState(state -> Teleport(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 200) {
			if (changeState(state -> Wait(blendTime)))
				return;
		}
		doAction(4062859220L /*RUN*/, blendTime, onDoActionEnd -> move(EAIMoveDestType.OwnerPosition, 0, 200, 0, 0, 1, false, ENaviType.ground, () -> {
			return false;
		}, onExit -> scheduleState(state -> Chase_Run(blendTime), 500)));
	}

	protected void Heal_HP(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.action);
		setState(0x224D2786L /*Heal_HP*/);
		setVariable(0x35069BD4L /*_SkillStartTime*/, getTime());
		doAction(1818367746L /*HEAL_HP*/, blendTime, onDoActionEnd -> changeState(state -> Wait(blendTime)));
	}

	protected void Heal_MP(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.action);
		setState(0x89F506C3L /*Heal_MP*/);
		setVariable(0x35069BD4L /*_SkillStartTime*/, getTime());
		doAction(2146786465L /*HEAL_MP*/, blendTime, onDoActionEnd -> changeState(state -> Wait(blendTime)));
	}

	protected void UnSummon(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.dead);
		setState(0xCC02DCCFL /*UnSummon*/);
		doAction(2544805566L /*DIE*/, blendTime, onDoActionEnd -> scheduleState(state -> UnSummon(blendTime), 1000));
	}

	@Override
	public EAiHandlerResult HandleTakeDamage(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		if (changeState(state -> Wait(0.3)))
			return EAiHandlerResult.CHANGE_STATE;
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleTakeTeamDamage(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleOnOwnerDead(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		if (changeState(state -> UnSummon(0.1)))
			return EAiHandlerResult.CHANGE_STATE;
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleOnRifleDead(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		if (changeState(state -> UnSummon(0.1)))
			return EAiHandlerResult.CHANGE_STATE;
		return EAiHandlerResult.BYPASS;
	}
}
