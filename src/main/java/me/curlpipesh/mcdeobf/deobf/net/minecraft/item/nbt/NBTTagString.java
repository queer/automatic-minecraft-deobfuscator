package me.curlpipesh.mcdeobf.deobf.net.minecraft.item.nbt;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NBTTagString extends Deobfuscator {
    public NBTTagString() {
        super("NBTTagString");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("Empty string not allowed");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
