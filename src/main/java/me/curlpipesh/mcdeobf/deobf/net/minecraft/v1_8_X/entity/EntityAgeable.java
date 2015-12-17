package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class EntityAgeable extends Deobfuscator {
    public EntityAgeable() {
        super("EntityAgeable");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("Age") && constantPool.contains("ForcedAge");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
