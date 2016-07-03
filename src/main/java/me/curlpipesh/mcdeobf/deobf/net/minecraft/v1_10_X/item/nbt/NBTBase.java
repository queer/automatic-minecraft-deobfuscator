package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.item.nbt;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.Arrays;
import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NBTBase extends Deobfuscator {
    public NBTBase() {
        super("NBTBase");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        String[] typeConstants = new String[] {
                "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT",
                "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]"
        };
        return Arrays.stream(typeConstants).filter(constantPool::contains).count() == typeConstants.length;
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
