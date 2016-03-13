package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.util;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class BlockPos extends Deobfuscator {
    public BlockPos() {
        super("BlockPos");
    }

    @Override
    @SuppressWarnings({"unchecked", "Convert2streamapi"})
    public boolean deobfuscate(byte[] classData) {
        // Of course this has no Strings that we can check ;-;
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        boolean staticInstanceOfSelf = false;
        int intCount = 0;
        int longCount = 0;

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(isStatic(f.access) && isPublic(f.access) && isFinal(f.access)) {
                staticInstanceOfSelf = true;
            } else if(isInt(f.desc) && isStatic(f.access) && isPrivate(f.access)) {
                ++intCount;
            } else if(isLong(f.desc) && isStatic(f.access) && isPrivate(f.access)) {
                ++longCount;
            }
        }
        return staticInstanceOfSelf && intCount > 0 && longCount > 0;
    }

    @Override
    @SuppressWarnings({"unchecked", "Duplicates"})
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef def = new ClassDef(this);
        cr.accept(cn, 0);

        Optional<Map.Entry<Deobfuscator, byte[]>> vec3i = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("Vec3i")).findFirst();
        if(!vec3i.isPresent()) {
            Main.getInstance().getLogger().severe("[BlockPos] Couldn't find Vec3i, bailing out.");
            return null;
        }
        ((List<FieldNode>) cn.fields).stream().filter(f -> f.desc.contains(vec3i.get().getKey().getObfuscatedDescription()))
                .forEach(f -> def.addField("vecPos", f.name));

        return def;
    }
}
