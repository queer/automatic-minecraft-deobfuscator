package me.curlpipesh.mcdeobf.deobf.net.minecraft.entity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public class Entity extends Deobfuscator {
    public Entity() {
        super("Entity");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("entityBaseTick") && c.contains("Checking entity block collision");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
