package com.bdoemu.core.network.sendable;

import com.bdoemu.commons.network.SendByteBuffer;
import com.bdoemu.core.network.sendable.utils.WriteCharacterInfo;
import com.bdoemu.gameserver.model.creature.enums.EVehicleType;
import com.bdoemu.gameserver.model.creature.player.Player;
import com.bdoemu.gameserver.model.creature.player.appearance.holders.AppearanceFaceHolder;
import com.bdoemu.gameserver.model.creature.player.appearance.holders.AppearanceHairHolder;
import com.bdoemu.gameserver.model.creature.player.duel.PVPController;
import com.bdoemu.gameserver.model.creature.player.duel.PvpMatch;
import com.bdoemu.gameserver.model.creature.player.enums.EEquipSlot;
import com.bdoemu.gameserver.model.creature.player.itemPack.PlayerBag;
import com.bdoemu.gameserver.model.creature.player.itemPack.PlayerEquipments;
import com.bdoemu.gameserver.model.creature.servant.Servant;
import com.bdoemu.gameserver.model.creature.servant.ServantEquipOnOff;
import com.bdoemu.gameserver.model.creature.servant.enums.ERidingSlotType;
import com.bdoemu.gameserver.model.items.Item;
import com.bdoemu.gameserver.model.skills.packageeffects.enums.EChargeUserType;
import com.bdoemu.gameserver.model.stats.containers.PlayerGameStats;
import com.bdoemu.gameserver.model.world.Location;

public class SMEnterPlayerCharacterToField extends WriteCharacterInfo {
    private final Player player;

    public SMEnterPlayerCharacterToField(final Player player) {
        this.player = player;
    }

    protected void writeBody(final SendByteBuffer buffer) {
        final Location location = this.player.getLocation();
        final PlayerBag playerBag = this.player.getPlayerBag();
        final PlayerGameStats gameStats = this.player.getGameStats();
        buffer.writeQ(this.player.getObjectId());
        buffer.writeD(this.player.getCreationIndex());
        buffer.writeH(1);
        buffer.writeD(this.player.getGameObjectId());
        buffer.writeD(-1024);
        buffer.writeF(0);
        buffer.writeF(0);
        buffer.writeF(0);
        buffer.writeF(location.getX());
        buffer.writeF(location.getZ());
        buffer.writeF(location.getY());
        buffer.writeF(location.getCos());
        buffer.writeD(0);
        buffer.writeF(location.getSin());
        buffer.writeH(this.player.getCreatureId());
        buffer.writeH(0);
        buffer.writeF(1);
        buffer.writeF(gameStats.getHp().getMaxHp());
        buffer.writeD(0);
        buffer.writeQ(this.player.getGuildCache());
        buffer.writeQ(0L);
        buffer.writeH(0);
        buffer.writeQ(this.player.getCache());
        buffer.writeC(2);
        buffer.writeD(this.player.getActionStorage().getActionHash());
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeH(0);
        buffer.writeC(this.player.getActionStorage().getApplySpeedBuffType().ordinal());
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeH(0);
        buffer.writeH(0);
        buffer.writeH(0);
        buffer.writeH(0);
        buffer.writeB(new byte[600]);
        buffer.writeQ(this.player.getAccountData().getChargeUserStorage().getChargeUserEffectEndTime(EChargeUserType.DyeingPackage));
        buffer.writeQ(this.player.getAccountId());
        buffer.writeD(this.player.getAccountData().getUserBasicCacheCount());
        buffer.writeQ(this.player.getObjectId());
        buffer.writeD(this.player.getBasicCacheCount());
        buffer.writeD(this.player.getPcCustomizationCacheCount());
        buffer.writeD(this.player.getEquipSlotCacheCount());
        buffer.writeC(this.player.getPlayerRenderStorage().getRenderBits());
        final ServantEquipOnOff servantEquipOnOff = this.player.getServantController().getServantEquipOnOff();
        buffer.writeD(servantEquipOnOff.getVehicleEquipOnOff());
        buffer.writeD(servantEquipOnOff.getCamelEquipOnOff());
        buffer.writeD(servantEquipOnOff.getUnk1EquipOnOff());
        buffer.writeD(servantEquipOnOff.getUnk2EquipOnOff());
        buffer.writeD(servantEquipOnOff.getShipEquipOnOff());
        buffer.writeD(this.player.getAvatarEquipOnOff());
        buffer.writeD(this.player.getPcNonSavedCacheCount());
        buffer.writeD(0);
        buffer.writeH(0);
        buffer.writeC(this.player.canPvP());
        buffer.writeC(this.player.isPvPEnable());
        final PlayerEquipments equipment = playerBag.getEquipments();
        final Item mainItem = equipment.getItem(0);
        buffer.writeH((mainItem != null) ? mainItem.getItemId() : 0);
        buffer.writeH((mainItem != null) ? mainItem.getEnchantLevel() : 0);
        buffer.writeD(-1016);
        buffer.writeD(0);
        buffer.writeH(0);
        buffer.writeC(EVehicleType.None.ordinal());
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeC(ERidingSlotType.None.ordinal());
        buffer.writeD(this.player.getTendency());
        buffer.writeD(0);
        buffer.writeQ(this.player.getPreemptiveStrikeTime());
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeD(-1024);
        buffer.writeC(1);
        buffer.writeD(this.player.getLevel());
        buffer.writeD(this.player.getTitleHandler().getTitleId());
        buffer.writeC(this.player.getGuildMemberRankType().ordinal());
        buffer.writeC(0);
        final Servant servant = this.player.getCurrentVehicle();
        buffer.writeQ((servant != null) ? servant.getObjectId() : 0L);
        buffer.writeH(0);
        buffer.writeQ(0L);
        buffer.writeD(this.player.getActivityPoints());
        buffer.writeD(this.player.getLearnSkillCacheCount());
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeD(-1024);
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeD(-1024);
        buffer.writeD(0);
        buffer.writeD(0);
        buffer.writeD(0);
        final PVPController pvpController = this.player.getPVPController();
        buffer.writeC(pvpController.getPvpMatchState().getId());
        final PvpMatch pvpMatch = this.player.getPVPController().getPvpMatch();
        buffer.writeD((pvpMatch != null) ? pvpMatch.getMatchObjectId() : 0);
        buffer.writeQ(pvpController.getObjectId());
        buffer.writeC((pvpMatch != null) ? pvpMatch.getPvpType().getId() : 0);
        buffer.writeD(this.player.getPVPController().getLocalWarTeamType().ordinal());
        buffer.writeD(this.player.getPVPController().getLocalWarPoints());
        buffer.writeD(-2);
        buffer.writeC(0);
        buffer.writeC(0); // 432
        buffer.writeC(0); // 471
        buffer.writeC(0); // 478
        buffer.writeD(this.player.getAccountData().getUserBasicCacheCount());
        buffer.writeS(this.player.getLoginAccount().getFamily(), 62);
        buffer.writeS(this.player.getAccountData().getComment(), 402);
        buffer.writeD(this.player.getBasicCacheCount());
        buffer.writeS(this.player.getName(), 62);
        final AppearanceFaceHolder face = this.player.getPlayerAppearance().getFace();
        final AppearanceHairHolder hairs = this.player.getPlayerAppearance().getHairs();
        buffer.writeC(face.getId());
        buffer.writeC(hairs.getId());
        buffer.writeC(face.getBeard().getId());
        buffer.writeC(face.getMustache().getId());
        buffer.writeC(face.getWhiskers().getId());
        buffer.writeC(face.getEyebrows().getId());
        buffer.writeD(this.player.getEquipSlotCacheCount());
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.rightHand.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.leftHand.getId()));
        this.writeEquipData(buffer, null);
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.chest.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.glove.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.boots.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.helm.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.lantern.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.avatarChest.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.avatarGlove.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.avatarBoots.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.avatarHelm.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.avatarWeapon.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.avatarSubWeapon.getId()));
        this.writeEquipData(buffer, null);
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.avatarUnderWear.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.faceDecoration1.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.faceDecoration2.getId()));
        this.writeEquipData(buffer, equipment.getItem(EEquipSlot.faceDecoration3.getId()));
        buffer.writeH(1); // was 3
        buffer.writeS("", 704);
        buffer.writeC(this.player.getZodiac().getId());
        buffer.writeD(-1024);
        buffer.writeQ(this.player.getAccountData().getPlayedTime() / 1000L / 60L);
        buffer.writeQ(this.player.getPlayedTime() / 1000L / 60L);
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeQ(0L);
        buffer.writeD(this.player.getEnchantFailCount());
        buffer.writeD(0);
        buffer.writeD(2);
        buffer.writeH(0);
        buffer.writeH(-2);
        buffer.writeC(1);
        buffer.writeC(1); // was 0
        buffer.writeC(0);
        buffer.writeC(0);
        buffer.writeD(this.player.getBattlePoints());
        buffer.writeD(this.player.getLifePoints());
        buffer.writeD(this.player.getSpecialPoints());
        buffer.writeC(0);
    }
}
