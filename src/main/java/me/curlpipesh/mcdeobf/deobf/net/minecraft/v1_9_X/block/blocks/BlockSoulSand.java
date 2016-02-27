package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.block.blocks;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * @author audrey
 * @since 12/21/15.
 */
public class BlockSoulSand extends Deobfuscator {
    public BlockSoulSand() {
        super("BlockSoulSand");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        return new ClassReader(classData).getClassName().equals("aoh");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(AccessHelper.isPublic(m.access) && AccessHelper.isVoid(m.desc)
                    && !m.name.contains("init>")) {
                def.addMethod("onEntityCollidedWithBlock", m);
            }
        }

        return def;
    }
}
