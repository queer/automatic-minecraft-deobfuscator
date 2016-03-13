package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.world;

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
 * @author c
 * @since 8/3/15
 */
public class AbstractWorld extends Deobfuscator {
    public AbstractWorld() {
        super("AbstractWorld");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("Exception while updating neighbours") && c.contains("Block being updated")
                && c.contains("~~NULL~~") && c.contains("pendingBlockEntities");
    }

    @Override
    @SuppressWarnings({"unchecked", "Duplicates"})
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef def = new ClassDef(this);
        cr.accept(cn, 0);

        Optional<Map.Entry<Deobfuscator, byte[]>> entity = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("Entity")).findFirst();
        Optional<Map.Entry<Deobfuscator, byte[]>> blockEntity = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("BlockEntity")).findFirst();
        Optional<Map.Entry<Deobfuscator, byte[]>> worldProvider = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("WorldProvider")).findFirst();
        if(!entity.isPresent()) {
            Main.getInstance().getLogger().severe("[AbstractWorld] Couldn't find Entity, bailing out.");
            return null;
        }
        if(!blockEntity.isPresent()) {
            Main.getInstance().getLogger().severe("[AbstractWorld] Couldn't find BlockEntity, bailing out.");
            return null;
        }
        if(!worldProvider.isPresent()) {
            Main.getInstance().getLogger().severe("[AbstractWorld] Couldn't find WorldProvider, bailing out.");
            return null;
        }

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(f.signature != null && f.signature.contains(entity.get().getKey().getObfuscatedDescription()) && f.desc.contains("List")) {
                def.addField("loadedEntities", f.name);
            } else if(f.signature != null && f.signature.contains(blockEntity.get().getKey().getObfuscatedDescription()) && f.desc.contains("List")) {
                def.addField("loadedBlockEntities", f.name);
            } else if(f.desc.contains(worldProvider.get().getKey().getObfuscatedDescription())) {
                def.addField("worldProvider", f.name);
            }
        }

        return def;
    }
}
