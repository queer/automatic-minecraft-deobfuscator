package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.item.nbt;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NBTTagList extends Deobfuscator {
    public NBTTagList() {
        super("NBTTagList");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("Missing type on ListTag")
                && constantPool.contains("Tried to read NBT tag with too high complexity, depth > 512");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
