package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.item.nbt;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NBTTagByteArray extends Deobfuscator {
    public NBTTagByteArray() {
        super("NBTTagByteArray");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains(" bytes]");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
