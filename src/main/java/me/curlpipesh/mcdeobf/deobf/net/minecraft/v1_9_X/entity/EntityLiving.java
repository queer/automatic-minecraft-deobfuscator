package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/24/15.
 */
public class EntityLiving extends Deobfuscator {
    public EntityLiving() {
        super("EntityLiving");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("checkDespawn") && constantPool.contains("targetSelector")
                && constantPool.contains("goalSelector") && constantPool.contains("mobGriefing");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
