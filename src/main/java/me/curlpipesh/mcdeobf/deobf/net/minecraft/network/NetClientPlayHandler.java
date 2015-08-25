package me.curlpipesh.mcdeobf.deobf.net.minecraft.network;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NetClientPlayHandler extends Deobfuscator {
    public NetClientPlayHandler() {
        super("NetClientPlayHandler");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("MC|Brand") && constantPool.contains("disconnect.lost")
                && constantPool.contains("level://");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
