package me.curlpipesh.mcdeobf.deobf.net.minecraft.util;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 12/7/15.
 */
public class Session extends Deobfuscator {
    public Session() {
        super("Session");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("token:");
    }

    @Override
    public ClassDef getClassDefinition(final byte[] classData) {
        return null;
    }
}
