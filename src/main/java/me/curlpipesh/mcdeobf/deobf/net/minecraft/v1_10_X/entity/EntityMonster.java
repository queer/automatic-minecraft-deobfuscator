package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

/**
 * @author audrey
 * @since 8/24/15.
 */
public class EntityMonster extends Deobfuscator {
    public EntityMonster() {
        super("EntityMonster");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        // TODO: Better method of identification
        return new ClassReader(classData).getClassName().startsWith("yp");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
