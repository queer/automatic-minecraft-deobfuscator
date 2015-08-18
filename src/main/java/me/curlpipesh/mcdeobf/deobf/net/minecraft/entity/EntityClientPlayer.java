package me.curlpipesh.mcdeobf.deobf.net.minecraft.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public class EntityClientPlayer extends Deobfuscator {
    public EntityClientPlayer() {
        super("EntityClientPlayer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("minecraft:container") && c.contains("portal.trigger");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
