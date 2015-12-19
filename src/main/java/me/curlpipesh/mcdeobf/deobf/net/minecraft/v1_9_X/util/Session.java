package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.util;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

/**
 * @author audrey
 * @since 12/7/15.
 */
public class Session extends Deobfuscator {
    public Session() {
        super("Session");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("token:");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef c = new ClassDef(this);
        cr.accept(cn, 0);

        c.addField("username", ((List<FieldNode>) cn.fields).get(0).name);

        return c;
    }
}
