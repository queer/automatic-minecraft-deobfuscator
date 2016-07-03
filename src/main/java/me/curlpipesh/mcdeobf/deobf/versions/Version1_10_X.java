package me.curlpipesh.mcdeobf.deobf.versions;

import lombok.Getter;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.deobf.Version;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.block.BlockEntity;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.block.blockentity.BlockEntityChest;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.block.blockentity.BlockEntityEnderChest;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.block.blocks.BlockSoulSand;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.GameSettings;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.Minecraft;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.gui.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.renderer.EntityRenderer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.renderer.Framebuffer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.renderer.GlStateManager;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.renderer.RenderGlobal;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.renderer.entity.Render;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.renderer.entity.RendererLivingEntity;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.entity.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.entity.player.EntityClientPlayer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.entity.player.EntityPlayer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.entity.player.EntityPlayerMP;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.item.Item;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.item.ItemStack;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.item.inventory.InventoryPlayer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.item.inventory.container.Container;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.item.nbt.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.EnumConnectionState;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.NetClientPlayHandler;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.NetworkManager;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.Packet;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.PacketBuffer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.client.PacketClientChatMessage;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.client.PacketClientHandshake;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.client.PacketClientTabComplete;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.server.PacketServerChatMessage;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.util.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.world.AbstractWorld;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.world.World;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.world.WorldProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author audrey
 * @since 7/3/16.
 */
public class Version1_10_X implements Version {
    @Getter
    @SuppressWarnings({"SpellCheckingInspection", "MismatchedQueryAndUpdateOfCollection"})
    private final List<Deobfuscator> deobfuscators;
    
    @Getter
    @SuppressWarnings({"SpellCheckingInspection", "MismatchedQueryAndUpdateOfCollection"})
    private final List<Deobfuscator> totalDeobfuscators;
    
    public Version1_10_X() {
        deobfuscators = new CopyOnWriteArrayList<>();
        totalDeobfuscators = new ArrayList<>();
        
        deobfuscators.addAll(Arrays.asList(new BlockEntityChest(),
                new BlockEntityEnderChest(),
                new BlockEntity(),
                new BlockSoulSand(),
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
                //new GlStateManager(),
                new RenderGlobal(),
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
                new WorldProvider(),
                new AxisAlignedBB(),
                new Render(),
                new RendererLivingEntity(),
                new GlStateManager()
        ));
        totalDeobfuscators.addAll(deobfuscators);
    }
    
    @Override
    public String getVersionNumber() {
        return "1.10.X";
    }
    
    public Deobfuscator getDeobfuscatorByName(final String name) {
        for (final Deobfuscator deobfuscator : getTotalDeobfuscators()) {
            if (deobfuscator.getDeobfuscatedName().equalsIgnoreCase(name)) {
                return deobfuscator;
            }
        }
        
        return null;
    }
    
    public Deobfuscator getDeobfuscator(final Class clazz) {
        for (final Deobfuscator deobfuscator : getTotalDeobfuscators()) {
            if (clazz.isInstance(deobfuscator)) {
                return deobfuscator;
            }
        }
        
        return null;
    }
}
