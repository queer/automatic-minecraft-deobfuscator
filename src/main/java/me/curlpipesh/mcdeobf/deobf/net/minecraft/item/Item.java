package me.curlpipesh.mcdeobf.deobf.net.minecraft.item;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class Item extends Deobfuscator {
    public Item() {
        super("Item");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("item.") && constantPool.contains("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
