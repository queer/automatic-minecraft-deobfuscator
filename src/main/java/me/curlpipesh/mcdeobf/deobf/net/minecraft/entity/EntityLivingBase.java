package me.curlpipesh.mcdeobf.deobf.net.minecraft.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/24/15.
 */
public class EntityLivingBase extends Deobfuscator {
    public EntityLivingBase() {
        super("EntityLivingBase");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("livingEntityBaseTick") && constantPool.contains("doMobLoot")
                && constantPool.contains("HurtByTimestamp") && constantPool.contains("Attributes");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
