package me.curlpipesh.mcdeobf.deobf.net.minecraft.block.blockentity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class BlockEntityEnderChest extends Deobfuscator {
    public BlockEntityEnderChest() {
        super("BlockEntityEnderChest");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        List<String> constantPool = dumpConstantPoolStrings(cr);
        return constantPool.contains("random.chestopen") && constantPool.contains("random.chestclosed")
                && cr.getInterfaces().length == 1;
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return new ClassDef(this);
    }
}
