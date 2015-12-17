package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class EntityCreature extends Deobfuscator {
    public EntityCreature() {
        super("EntityCreature");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("Fleeing speed bonus")
                // Because this class contains this UUID for some reason...
                && constantPool.contains("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
