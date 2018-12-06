// 
// Decompiled by Procyon v0.5.30
// 

package com.bdoemu.gameserver.model.skills.buffs.effects;

import com.bdoemu.gameserver.model.creature.Creature;
import com.bdoemu.gameserver.model.skills.buffs.ABuffEffect;
import com.bdoemu.gameserver.model.skills.buffs.ActiveBuff;
import com.bdoemu.gameserver.model.skills.buffs.templates.BuffTemplate;
import com.bdoemu.gameserver.model.skills.templates.SkillT;
import com.bdoemu.gameserver.model.stats.elements.BuffElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TendencyAquireRateEffect extends ABuffEffect {
    @Override
    public Collection<ActiveBuff> initEffect(final Creature owner, final Collection<? extends Creature> targets, final SkillT skillT, final BuffTemplate buffTemplate) {
        final List<ActiveBuff> buffs = new ArrayList<ActiveBuff>();
        final Integer[] params = buffTemplate.getParams();
        final int tendencyValue = params[0];
        final Integer type = params[1];
        final BuffElement element = new BuffElement(tendencyValue);
        for (final Creature target : targets) {
            if (target.isPlayer()) {
                buffs.add(new ActiveBuff(skillT, buffTemplate, owner, target, element));
            }
        }
        return buffs;
    }

    @Override
    public void applyEffect(final ActiveBuff activeBuff) {
        final BuffElement element = activeBuff.getElement();
        final Creature target = activeBuff.getTarget();
        target.getGameStats().getTendencyEXP().addElement(element);
    }

    @Override
    public void endEffect(final ActiveBuff activeBuff) {
        final BuffElement element = activeBuff.getElement();
        final Creature target = activeBuff.getTarget();
        target.getGameStats().getTendencyEXP().removeElement(element);
    }
}
