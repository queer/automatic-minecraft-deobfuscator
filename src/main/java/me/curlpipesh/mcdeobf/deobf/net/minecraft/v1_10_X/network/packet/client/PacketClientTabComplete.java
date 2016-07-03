package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.client;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

/**
 * @author audrey
 * @since 12/21/15.
 */
public class PacketClientTabComplete extends Deobfuscator {
    public PacketClientTabComplete() {
        super("PacketClientTabComplete");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        // Fuck it
        return cr.getClassName().equals("ii");
    }

    @Override
    public ClassDef getClassDefinition(final byte[] classData) {
        return new ClassDef(this);
    }
}
