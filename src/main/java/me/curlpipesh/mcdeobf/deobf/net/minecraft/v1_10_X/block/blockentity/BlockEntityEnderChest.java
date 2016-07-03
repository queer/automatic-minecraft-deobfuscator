package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.block.blockentity;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

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
        // TODO: Better way etc.
        return cr.getClassName().equals("apy");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return new ClassDef(this);
    }
}
