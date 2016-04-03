package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.client;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

public class GameSettings extends Deobfuscator {
    public GameSettings() {
        super("GameSettings");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).stream()
                .filter(s -> s.contains("key.categories.")).count() > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        final ClassDef def = new ClassDef(this);

        def.addField("isIngameGuiInDebugMode", "ar");
        int booleanCount = 0;
        for(final FieldNode f : (List<FieldNode>) cn.fields) {
            if(f.desc.equals("Z")) {
                ++booleanCount;
                if(booleanCount == 2) {
                    def.addField("isViewBobbingEnabled", f.name);
                    break;
                }
            }
        }

        return def;
    }
}
