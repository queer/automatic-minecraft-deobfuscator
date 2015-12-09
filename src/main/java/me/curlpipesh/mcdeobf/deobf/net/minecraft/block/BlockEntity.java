package me.curlpipesh.mcdeobf.deobf.net.minecraft.block;

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
public class BlockEntity extends Deobfuscator {
    public BlockEntity() {
        super("BlockEntity");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("Skipping BlockEntity with id ");
    }

    @Override
    @SuppressWarnings({"unchecked", "Duplicates"})
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef def = new ClassDef(this);
        cr.accept(cn, 0);
        Optional<Map.Entry<Deobfuscator, Byte[]>> blockPos = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("BlockPos")).findFirst();
        if(!blockPos.isPresent()) {
            Main.getInstance().getLogger().severe("[BlockEntity] Couldn't find BlockPos, bailing out.");
            return null;
        }
        ((List<FieldNode>) cn.fields).stream().filter(f -> f.desc.contains(blockPos.get().getKey().getObfuscatedDescription()))
                .forEach(f -> def.addField("blockPos", f.name));
        return def;
    }
}
