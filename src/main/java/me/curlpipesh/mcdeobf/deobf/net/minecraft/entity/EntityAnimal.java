package me.curlpipesh.mcdeobf.deobf.net.minecraft.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

/**
 * @author audrey
 * @since 8/24/15.
 */
public class EntityAnimal extends Deobfuscator {
    public EntityAnimal() {
        super("EntityAnimal");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        // I really hope that this works ^_^;;
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("InLove");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
