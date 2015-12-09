package me.curlpipesh.mcdeobf.deobf.net.minecraft.util;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class Vec3i extends Deobfuscator {
    public Vec3i() {
        super("Vec3i");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        List<String> constantPool = dumpConstantPoolStrings(cr);

        boolean staticInstanceOfSelf = false;
        int intCount = 0;

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(isStatic(f.access) && isPublic(f.access) && isFinal(f.access)) {
                staticInstanceOfSelf = true;
            }
            if(isPrivate(f.access) && isInt(f.desc)) {
                ++intCount;
            }
        }

        return constantPool.contains("x") && constantPool.contains("y") && constantPool.contains("z")
                && staticInstanceOfSelf && intCount == 3;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        ClassDef def = new ClassDef(this);

        int getters = 0;
        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(m.desc.contains("()I")) {
                if(m.instructions.size() < 5) {
                    if(getters == 0) {
                        def.addMethod("getX", m.name);
                    } else if(getters == 1) {
                        def.addMethod("getY", m.name);
                    } else if(getters == 2) {
                        def.addMethod("getZ", m.name);
                    }
                    ++getters;
                }
            }
        }
        return def;
    }
}
