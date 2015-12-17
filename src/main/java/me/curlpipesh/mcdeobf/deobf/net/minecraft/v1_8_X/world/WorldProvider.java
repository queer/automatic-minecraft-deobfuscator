package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.world;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

/**
 * @author c
 * @since 8/3/15
 */
public class WorldProvider extends Deobfuscator {
    public WorldProvider() {
        super("WorldProvider");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        int arrc = 0;
        int ps = 0;
        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(f.desc.contains("[")) {
                if(isPublic(f.access) && isStatic(f.access)) {
                    ++ps;
                }
                ++arrc;
            }
        }

        return arrc == 3 && ps == 1 && isAbstract(cn.access);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef c = new ClassDef(this);
        cr.accept(cn, 0);

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(AccessHelper.isProtected(f.access) && f.desc.equals("[F")) {
                c.addField("lightBrightnessTable", f.name);
            }
        }

        return c;
    }
}
