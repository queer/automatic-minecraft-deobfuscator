package me.curlpipesh.mcdeobf.deobf.net.minecraft.item;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class ItemStack extends Deobfuscator {
    public ItemStack() {
        super("ItemStack");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("minecraft:air") && constantPool.contains("Unbreakable")
                && constantPool.contains("attribute.name.") && constantPool.contains("attribute.modifier.take.");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
