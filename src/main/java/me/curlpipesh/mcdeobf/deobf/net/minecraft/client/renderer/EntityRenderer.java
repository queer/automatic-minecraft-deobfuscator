package me.curlpipesh.mcdeobf.deobf.net.minecraft.client.renderer;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

/**
 * @author c
 * @since 8/18/15
 */
public class EntityRenderer extends Deobfuscator {
    public EntityRenderer() {
        super("EntityRenderer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).stream().filter(s -> s.toLowerCase().contains("shaders/post/")).count() > 0;
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
