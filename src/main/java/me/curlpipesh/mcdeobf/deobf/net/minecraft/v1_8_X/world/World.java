package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.world;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public class World extends Deobfuscator {
    public World() {
        super("World");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("MpServer") && c.contains("doDaylightCycle") && c.contains("reEntryProcessing")
                && c.contains("chunkCache");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
