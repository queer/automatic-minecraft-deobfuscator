package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class DamageSource extends Deobfuscator {
    public DamageSource() {
        super("DamageSource");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("explosion.player") && constantPool.contains("explosion")
                && constantPool.contains("mob") && constantPool.contains("player")
                && constantPool.contains("arrow") && constantPool.contains("fireball");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return new ClassDef(this);
    }
}
