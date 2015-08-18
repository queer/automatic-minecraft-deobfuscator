package me.curlpipesh.mcdeobf.deobf.net.minecraft.world;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public class AbstractWorld extends Deobfuscator {
    public AbstractWorld() {
        super("AbstractWorld");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("Exception while updating neighbours") && c.contains("Block being updated")
                && c.contains("~~NULL~~") && c.contains("pendingBlockEntities");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
