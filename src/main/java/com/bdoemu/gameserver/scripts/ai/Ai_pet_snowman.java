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
@IAIName("pet_snowman")
public class Ai_pet_snowman extends CreatureAI {
	public Ai_pet_snowman(Creature actor, Map<Long, Integer> aiVariables) {
		super(actor, aiVariables);
	}

	protected void InitialState(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xAE5FEAC2L /*InitialState*/);
		setVariable(0x444DFF4EL /*_isFindPathCompleted*/, 0);
		setVariable(0xFDC61BCCL /*_FailFindPathCount*/, 0);
		setVariable(0x940229E0L /*_isState*/, 1);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, 0);
		setVariable(0x42A6EC2DL /*_isRunning*/, 0);
		setVariable(0x69C27CA9L /*_Attachable_Type*/, 0);
		setVariable(0xBA414EEEL /*_isChaseMode*/, 1);
		setVariable(0xB4AEEB95L /*_isGetItemMode*/, 1);
		setVariable(0xC3671716L /*_isFindThatMode*/, 0);
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, 0);
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, 0);
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 0);
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, 0);
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, 0);
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, 0);
		setVariable(0x676FFCB1L /*_Skill0_Type*/, 0);
		setVariable(0xCC1D827CL /*_Skill1_Type*/, 0);
		setVariable(0x940229E0L /*_isState*/, 1);
		setVariable(0x940229E0L /*_isState*/, 2);
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Logic(blendTime), 1000));
	}

	protected void Wait_Logic(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.none);
		setState(0x9048C900L /*Wait_Logic*/);
		if (getVariable(0x940229E0L /*_isState*/) == 1) {
			if (changeState(state -> Wait_House(blendTime)))
				return;
		}
		if (getVariable(0x940229E0L /*_isState*/) == 2) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		changeState(state -> Wait_Logic(blendTime));
	}

	protected void Wait_House(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xCAB2CF45L /*Wait_House*/);
		if(getCallCount() == 4) {
			if (changeState(state -> Walk_Random_House(blendTime)))
				return;
		}
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_House(blendTime), 1000));
	}

	protected void Walk_Random_House(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.walk);
		setState(0x26903310L /*Walk_Random_House*/);
		doAction(847017389L /*WALK*/, blendTime, onDoActionEnd -> move(EAIMoveDestType.Random, 0, 0, 0, 100, 200, true, ENaviType.ground, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_House(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_House_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> Wait_House(blendTime), 1500)));
	}

	protected void Move_Return_House(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.return_);
		setState(0x1ED7CB14L /*Move_Return_House*/);
		doAction(4171429047L /*RUN_FAST*/, blendTime, onDoActionEnd -> recoveryAndReturn(() -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_House(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_House_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> Wait_House(blendTime), 1500)));
	}

	protected void FailFindPath_House_Logic(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xB5462542L /*FailFindPath_House_Logic*/);
		setVariable(0xFDC61BCCL /*_FailFindPathCount*/, getVariable(0xFDC61BCCL /*_FailFindPathCount*/) + 1);
		if (getVariable(0xFDC61BCCL /*_FailFindPathCount*/) > 3 && getVariable(0x870CD143L /*_IsPartyMember*/) == 0) {
			if (changeState(state -> FailFindPath_House(0.3)))
				return;
		}
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_House(blendTime), 500));
	}

	protected void FailFindPath_House(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xF6FA9865L /*FailFindPath_House*/);
		doTeleport(EAIMoveDestType.Random, 0, 0, 1, 1);
		doAction(3467964157L /*TELEPORT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_House(blendTime), 500));
	}

	protected void Wait_Summon_Logic(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.none);
		setState(0xBD8D8164L /*Wait_Summon_Logic*/);
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon0(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon1(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon2(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon3(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon4(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon5(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon6(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon7(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon8(0.3)))
					return;
			}
		}
		if (false) {
			if(Rnd.getChance(10)) {
				if (changeState(state -> Wait_Summon9(0.3)))
					return;
			}
		}
		if(Rnd.getChance(15)) {
			if (changeState(state -> Random_Move(0.3)))
				return;
		}
		changeState(state -> Wait_Summon(blendTime));
	}

	protected void Wait_Summon(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x87DC6143L /*Wait_Summon*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(blendTime)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(2514775444L /*WAIT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon(blendTime), 1000));
	}

	protected void Wait_Summon0(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xD4D1FCACL /*Wait_Summon0*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(1838314136L /*WAIT_CHASE0*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon0(blendTime), 1000));
	}

	protected void Wait_Summon1(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x17263E70L /*Wait_Summon1*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(3907794696L /*WAIT_CHASE1*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon1(blendTime), 1000));
	}

	protected void Wait_Summon2(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xD96B4A3FL /*Wait_Summon2*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(3615267321L /*WAIT_CHASE2*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon2(blendTime), 1000));
	}

	protected void Wait_Summon3(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x841F6462L /*Wait_Summon3*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(1992840723L /*WAIT_CHASE3*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon3(blendTime), 1000));
	}

	protected void Wait_Summon4(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x2C3975A6L /*Wait_Summon4*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(2660320149L /*WAIT_CHASE4*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon4(blendTime), 1000));
	}

	protected void Wait_Summon5(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xC9A47BFBL /*Wait_Summon5*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(6925940L /*WAIT_CHASE5*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon5(blendTime), 1000));
	}

	protected void Wait_Summon6(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xA4D94001L /*Wait_Summon6*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(1001580925L /*WAIT_CHASE6*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon6(blendTime), 1000));
	}

	protected void Wait_Summon7(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x7F4DCB3L /*Wait_Summon7*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(1543026602L /*WAIT_CHASE7*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon7(blendTime), 1000));
	}

	protected void Wait_Summon8(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x40DF8313L /*Wait_Summon8*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(664553885L /*WAIT_CHASE8*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon8(blendTime), 1000));
	}

	protected void Wait_Summon9(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x456E06A6L /*Wait_Summon9*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(711309503L /*WAIT_CHASE9*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon9(blendTime), 1000));
	}

	protected void Random_Move(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.walk);
		setState(0x9B819965L /*Random_Move*/);
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(blendTime)))
				return;
		}
		if(Rnd.getChance(10)) {
			if (changeState(state -> Wait_Summon_Logic(blendTime)))
				return;
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x6AD923A2L /*_Skill1_UsableDistance*/))) {
				if (changeState(state -> FindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 2 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindThat(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 100)) {
				if (changeState(state -> AddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 5 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 1200)) {
				if (changeState(state -> ChaseAddAggro(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < 400)) {
				if (changeState(state -> FindEnemy(0.3)))
					return;
			}
		}
		if (false && getVariable(0xCC1D827CL /*_Skill1_Type*/) == 3 && getVariable(0xC3671716L /*_isFindThatMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType1, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x18B37A64L /*_Skill1_DetectDistance*/))) {
				if (changeState(state -> ChaseFindEnemy(0.3)))
					return;
			}
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 400) {
			if (changeState(state -> ChaseOwner_MoveLv1(0.3)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(847017389L /*WALK*/, blendTime, onDoActionEnd -> move(EAIMoveDestType.Random, 0, 0, 0, 50, 350, false, ENaviType.ground, () -> {
			return false;
		}, onExit -> scheduleState(state -> Wait_Summon_Logic(blendTime), 1000)));
	}

	protected void ChaseGetItem(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.chase);
		setState(0x102DF441L /*ChaseGetItem*/);
		if (isTargetLost()) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		if (target != null && getDistanceToTarget(target) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/)) {
			if (changeState(state -> GetItem(blendTime)))
				return;
		}
		doAction(1885143706L /*RUN_FAST_CHASE*/, blendTime, onDoActionEnd -> chase(10, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_Summon(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_Summon_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> ChaseGetItem(blendTime), 500)));
	}

	protected void GetItem(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x3E8C153BL /*GetItem*/);
		doAction(960059183L /*WAIT_CHASE*/, blendTime, onDoActionEnd -> scheduleState(state -> ChaseOwner_MoveLv2(blendTime), 2000));
	}

	protected void ChaseFindThatCollect(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.chase);
		setState(0xDBBFC18EL /*ChaseFindThatCollect*/);
		if (isTargetLost()) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		if (target != null && getDistanceToTarget(target) < 100) {
			if (changeState(state -> FindThatCollect(blendTime)))
				return;
		}
		doAction(1885143706L /*RUN_FAST_CHASE*/, blendTime, onDoActionEnd -> chase(10, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_Summon(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_Summon_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> ChaseFindThatCollect(blendTime), 500)));
	}

	protected void FindThatCollect(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xD5DBE232L /*FindThatCollect*/);
		if(getCallCount() == 3) {
			if (changeState(state -> ChaseOwner_MoveLv2(0.3)))
				return;
		}
		doAction(960059183L /*WAIT_CHASE*/, blendTime, onDoActionEnd -> scheduleState(state -> FindThatCollect(blendTime), 2000));
	}

	protected void ChaseFindThat(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.chase);
		setState(0x340C9EB7L /*ChaseFindThat*/);
		if (isTargetLost()) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		if (target != null && getDistanceToTarget(target) < 100) {
			if (changeState(state -> FindThat(blendTime)))
				return;
		}
		doAction(1885143706L /*RUN_FAST_CHASE*/, blendTime, onDoActionEnd -> chase(10, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_Summon(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_Summon_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> ChaseFindThat(blendTime), 500)));
	}

	protected void FindThat(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xC4921917L /*FindThat*/);
		doAction(960059183L /*WAIT_CHASE*/, blendTime, onDoActionEnd -> scheduleState(state -> FindThat1(blendTime), 2000));
	}

	protected void FindThat1(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xDE66B4E8L /*FindThat1*/);
		if(getCallCount() == 1) {
			if (changeState(state -> ChaseOwner_MoveLv2(0.3)))
				return;
		}
		doAction(960059183L /*WAIT_CHASE*/, blendTime, onDoActionEnd -> scheduleState(state -> FindThat1(blendTime), 2000));
	}

	protected void ChaseAddAggro(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.chase);
		setState(0xFF23B5E6L /*ChaseAddAggro*/);
		if (isTargetLost()) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		if (target != null && getDistanceToTarget(target) < 100) {
			if (changeState(state -> AddAggro(blendTime)))
				return;
		}
		doAction(1885143706L /*RUN_FAST_CHASE*/, blendTime, onDoActionEnd -> chase(10, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_Summon(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_Summon_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> ChaseAddAggro(blendTime), 500)));
	}

	protected void AddAggro(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x629CFD1EL /*AddAggro*/);
		doAction(960059183L /*WAIT_CHASE*/, blendTime, onDoActionEnd -> scheduleState(state -> AddAggro1(blendTime), 2000));
	}

	protected void AddAggro1(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x48018046L /*AddAggro1*/);
		if(getCallCount() == 1) {
			if (changeState(state -> ChaseOwner_MoveLv2(0.3)))
				return;
		}
		doAction(960059183L /*WAIT_CHASE*/, blendTime, onDoActionEnd -> scheduleState(state -> AddAggro1(blendTime), 1000));
	}

	protected void ChaseFindEnemy(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.chase);
		setState(0x129CC58EL /*ChaseFindEnemy*/);
		if (isTargetLost()) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		if (target != null && getDistanceToTarget(target) < 100) {
			if (changeState(state -> FindEnemy(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 300) {
			if (changeState(state -> FindEnemy(blendTime)))
				return;
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 1000) {
			if (changeState(state -> ChaseOwner_MoveLv1(blendTime)))
				return;
		}
		doAction(1885143706L /*RUN_FAST_CHASE*/, blendTime, onDoActionEnd -> chase(10, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_Summon(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_Summon_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> ChaseFindEnemy(blendTime), 500)));
	}

	protected void FindEnemy(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xC114E9ADL /*FindEnemy*/);
		setVariable(0x3E491457L /*_Skill0_UsableDistance*/, getPetSkillUsableDistance(0));
		setVariable(0x6AD923A2L /*_Skill1_UsableDistance*/, getPetSkillUsableDistance(1));
		setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, getPetSkillDetectDistance(0));
		if (getVariable(0xF4E090DL /*_Skill0_DetectDistance*/) == -1) {
			setVariable(0xF4E090DL /*_Skill0_DetectDistance*/, 1000);
		}
		setVariable(0x18B37A64L /*_Skill1_DetectDistance*/, getPetSkillDetectDistance(1));
		setVariable(0x10042F0L /*_Skill0_TargetType0*/, getPetSkillTargetType(0));
		setVariable(0x30CD4CFAL /*_Skill1_TargetType1*/, getPetSkillTargetType(1));
		setVariable(0xCC1D827CL /*_Skill1_Type*/, getPetSkillType(1));
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0x3E491457L /*_Skill0_UsableDistance*/))) {
				if (changeState(state -> GetItem(blendTime)))
					return;
			}
		}
		if (false && getVariable(0xB4AEEB95L /*_isGetItemMode*/) == 1) {
			if (findTarget(EAIFindTargetType._Skill0_TargetType0, EAIFindType.normal, false, object -> getDistanceToTarget(object) < getVariable(0xF4E090DL /*_Skill0_DetectDistance*/))) {
				if (changeState(state -> ChaseGetItem(blendTime)))
					return;
			}
		}
		if (target != null && getDistanceToTarget(target) > getVariable(0x18B37A64L /*_Skill1_DetectDistance*/)) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		if(getCallCount() == 3) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		if (getVariable(0xBA414EEEL /*_isChaseMode*/) == 1 && getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 1000) {
			if (changeState(state -> ChaseOwner_MoveLv1(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 10000) {
			if (changeState(state -> FailFindPath_Summon(blendTime)))
				return;
		}
		doAction(2065994425L /*FIND_DUNGEON_ING*/, blendTime, onDoActionEnd -> scheduleState(state -> FindEnemy(blendTime), 2000));
	}

	protected void ChaseOwner_MoveLv1(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.walk);
		setState(0x643D5C4CL /*ChaseOwner_MoveLv1*/);
		if (isTargetLost()) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 280) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 500) {
			if (changeState(state -> ChaseOwner_MoveLv2(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(blendTime)))
				return;
		}
		doAction(4117314026L /*RUN_CHASE*/, blendTime, onDoActionEnd -> move(EAIMoveDestType.OwnerPosition, 0, 0, 0, 40, 100, false, ENaviType.ground, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_Summon(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_Summon_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> ChaseOwner_MoveLv1(blendTime), 1500)));
	}

	protected void ChaseOwner_MoveLv2(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.walk);
		setState(0xF1003A81L /*ChaseOwner_MoveLv2*/);
		if (isTargetLost()) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 4000) {
			if (changeState(state -> OwnerTeleport(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 1000) {
			if (changeState(state -> ChaseOwner_MoveLv3(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 280) {
			if (changeState(state -> ChaseOwner_MoveLv1(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(blendTime)))
				return;
		}
		doAction(1885143706L /*RUN_FAST_CHASE*/, blendTime, onDoActionEnd -> move(EAIMoveDestType.OwnerPosition, 0, 0, 0, 40, 100, false, ENaviType.ground, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_Summon(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_Summon_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> ChaseOwner_MoveLv2(blendTime), 1500)));
	}

	protected void ChaseOwner_MoveLv3(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.walk);
		setState(0xD22A0FD1L /*ChaseOwner_MoveLv3*/);
		if (isTargetLost()) {
			if (changeState(state -> Wait_Summon(blendTime)))
				return;
		}
		setVariable(0xCBEEF8C7L /*_ownerDistance*/, getDistanceToOwnerNotFormation());
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) > 4000) {
			if (changeState(state -> OwnerTeleport(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 280) {
			if (changeState(state -> ChaseOwner_MoveLv3_EndLogic(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 0) {
			if (changeState(state -> FailFindPath_Summon(blendTime)))
				return;
		}
		doAction(810794540L /*RUN_FAST2_CHASE*/, blendTime, onDoActionEnd -> move(EAIMoveDestType.OwnerPosition, 0, 0, 0, 40, 100, false, ENaviType.ground, () -> {
			setVariable(0x444DFF4EL /*_isFindPathCompleted*/, isFindPathCompleted());
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0 && getVariable(0xFDC61BCCL /*_FailFindPathCount*/) >= 3) {
				if (changeState(state -> FailFindPath_Summon(0.3)))
					return true;
			}
			if (getVariable(0x444DFF4EL /*_isFindPathCompleted*/) == 0) {
				if (changeState(state -> FailFindPath_Summon_Logic(0.3)))
					return true;
			}
			return false;
		}, onExit -> scheduleState(state -> ChaseOwner_MoveLv3(blendTime), 1500)));
	}

	protected void ChaseOwner_MoveLv3_EndLogic(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.none);
		setState(0xB0085DA2L /*ChaseOwner_MoveLv3_EndLogic*/);
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 280) {
			if (changeState(state -> ChaseOwner_MoveLv3_End(blendTime)))
				return;
		}
		if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) >= 280) {
			if (changeState(state -> ChaseOwner_MoveLv3(blendTime)))
				return;
		}
		changeState(state -> ChaseOwner_MoveLv3_EndLogic(blendTime));
	}

	protected void ChaseOwner_MoveLv3_End(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xA4E10205L /*ChaseOwner_MoveLv3_End*/);
		doAction(881069330L /*RUN_STOP*/, blendTime, onDoActionEnd -> {
			if (getVariable(0xCBEEF8C7L /*_ownerDistance*/) < 280) {
				if (changeState(state -> ChaseOwner_MoveLv1(blendTime)))
					return;
			}
			scheduleState(state -> Wait_Summon(blendTime), 1000);
		});
	}

	protected void OwnerTeleport(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0xCB263523L /*OwnerTeleport*/);
		doTeleport(EAIMoveDestType.OwnerPosition, 100, 0, 1, 1);
		doAction(3467964157L /*TELEPORT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon(blendTime), 1500));
	}

	protected void FailFindPath_Summon_Logic(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x4992DCF6L /*FailFindPath_Summon_Logic*/);
		setVariable(0xFDC61BCCL /*_FailFindPathCount*/, getVariable(0xFDC61BCCL /*_FailFindPathCount*/) + 1);
		if (getVariable(0xFDC61BCCL /*_FailFindPathCount*/) > 3) {
			if (changeState(state -> FailFindPath_Summon(0.3)))
				return;
		}
		doAction(960059183L /*WAIT_CHASE*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon(blendTime), 1500));
	}

	protected void FailFindPath_Summon(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.idle);
		setState(0x4FB597EBL /*FailFindPath_Summon*/);
		doTeleport(EAIMoveDestType.OwnerPosition, 100, 0, 1, 1);
		doAction(3467964157L /*TELEPORT*/, blendTime, onDoActionEnd -> scheduleState(state -> Wait_Summon(blendTime), 1500));
	}

	protected void Die(double blendTime) {
		Creature target = getTarget();
		setBehavior(EAIBehavior.dead);
		setState(0x90DBFD38L /*Die*/);
		doAction(2544805566L /*DIE*/, blendTime, onDoActionEnd -> scheduleState(state -> Die(blendTime), 5000));
	}

	@Override
	public EAiHandlerResult HandleTargetInMyArea(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult HandleTakeDamage(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult handlePetFollowMaster(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		setVariable(0xBA414EEEL /*_isChaseMode*/, 1);
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult handlePetWaitMaster(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		setVariable(0xBA414EEEL /*_isChaseMode*/, 0);
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult handlePetGetItemOn(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		setVariable(0xB4AEEB95L /*_isGetItemMode*/, 1);
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult handlePetGetItemOff(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		setVariable(0xB4AEEB95L /*_isGetItemMode*/, 0);
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult handlePetFindThatOn(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		setVariable(0xC3671716L /*_isFindThatMode*/, 1);
		return EAiHandlerResult.BYPASS;
	}

	@Override
	public EAiHandlerResult handlePetFindThatOff(Creature sender, Integer[] eventData) {
		_sender = sender;
		Creature target = sender;
		setVariable(0xC3671716L /*_isFindThatMode*/, 0);
		return EAiHandlerResult.BYPASS;
	}
}
