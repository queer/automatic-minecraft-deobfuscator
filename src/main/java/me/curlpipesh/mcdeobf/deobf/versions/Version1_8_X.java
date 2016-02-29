package me.curlpipesh.mcdeobf.deobf.versions;

import lombok.Getter;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.deobf.Version;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.block.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.block.blockentity.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.block.blocks.BlockSoulSand;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.client.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.client.gui.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.client.renderer.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.entity.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.entity.player.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.item.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.item.inventory.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.item.inventory.container.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.item.nbt.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.network.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.network.packet.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.network.packet.client.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.network.packet.server.PacketServerChatMessage;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.util.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.world.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author audrey
 * @since 12/14/15.
 */
public class Version1_8_X implements Version {
    @Getter
    @SuppressWarnings({"SpellCheckingInspection", "MismatchedQueryAndUpdateOfCollection"})
    private final List<Deobfuscator> deobfuscators;

    public Version1_8_X() {
        deobfuscators = new CopyOnWriteArrayList<>();
        deobfuscators.addAll(Arrays.asList(
                new BlockEntityChest(),
                new BlockEntityEnderChest(),
                new BlockSoulSand(),
                new BlockEntity(),
                new FontRenderer(),
                new Gui(),
                new GuiChat(),
                new GuiIngame(),
                new GuiMainMenu(),
                new GuiMultiplayer(),
                new GuiOptions(),
                new GuiScreen(),
                new GuiSingleplayer(),
                new EntityRenderer(),
                new Framebuffer(),
                new GameSettings(),
                new Minecraft(),
                new EntityClientPlayer(),
                new EntityPlayer(),
                new EntityPlayerMP(),
                new DamageSource(),
                new Entity(),
                new EntityAgeable(),
                new EntityAnimal(),
                new EntityAttributes(),
                new EntityCreature(),
                new EntityLiving(),
                new EntityLivingBase(),
                new EntityMonster(),
                new Container(),
                new InventoryPlayer(),
                new NBTBase(),
                new NBTTagByte(),
                new NBTTagByteArray(),
                new NBTTagCompound(),
                new NBTTagDouble(),
                new NBTTagEnd(),
                new NBTTagFloat(),
                new NBTTagInt(),
                new NBTTagIntArray(),
                new NBTTagList(),
                new NBTTagLong(),
                new NBTTagShort(),
                new NBTTagString(),
                new Item(),
                new ItemStack(),
                new PacketClientChatMessage(),
                new PacketClientHandshake(),
                new PacketClientTabComplete(),
                new PacketServerChatMessage(),
                new Packet(),
                new PacketBuffer(),
                new EnumConnectionState(),
                new NetClientPlayHandler(),
                new NetworkManager(),
                new BlockPos(),
                new ChatComponentText(),
                new IChatComponent(),
                new ScaledResolution(),
                new Session(),
                new Vec3i(),
                new AbstractWorld(),
                new World(),
                new WorldProvider()));
    }

    @Override
    public String getVersionNumber() {
        return "1.8.X";
    }
}
