package me.curlpipesh.mcdeobf.deobf.net.minecraft.block;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

/**
 * @author audrey
 * @since 8/24/15.
 */
public class BlockEntity extends Deobfuscator {
    public BlockEntity() {
        super("BlockEntity");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("Skipping BlockEntity with id ");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
