package me.curlpipesh.mcdeobf.deobf.net.minecraft.item.inventory;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class InventoryPlayer extends Deobfuscator {
    public InventoryPlayer() {
        super("InventoryPlayer");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("container.inventory") && constantPool.contains("Adding item to inventory");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
