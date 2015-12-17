package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.entity.player;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author audrey
 * @since 8/24/15.
 */
public class EntityPlayer extends Deobfuscator {
    public EntityPlayer() {
        super("EntityPlayer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("game.player.swim") && constantPool.contains("game.player.swim.splash")
                && constantPool.contains("naturalRegeneration");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        final ClassDef def = new ClassDef(this);

        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        Optional<Map.Entry<Deobfuscator, Byte[]>> container = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("Container")).findFirst();
        Optional<Map.Entry<Deobfuscator, Byte[]>> inventoryPlayer = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("Container")).findFirst();
        if(!container.isPresent()) {
            Main.getInstance().getLogger().severe("[EntityPlayer] Couldn't find Container, bailing out.");
            return null;
        }
        if(!container.isPresent()) {
            Main.getInstance().getLogger().severe("[EntityPlayer] Couldn't find InventoryPlayer, bailing out.");
            return null;
        }

        for(FieldNode f : (List<FieldNode>)cn.fields) {
            if(f.desc.contains(container.get().getKey().getObfuscatedDescription())) {
                def.addField("inventoryContainer", f.name);
            } else if(f.desc.contains(inventoryPlayer.get().getKey().getObfuscatedDescription())) {
                def.addField("inventoryPlayer", f.name);
            }
        }

        return def;
    }
}
