package me.curlpipesh.mcdeobf.deobf.net.minecraft.network;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NetworkManager extends Deobfuscator {
    public NetworkManager() {
        super("NetworkManager");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        // I really really really hope that this works ;-;
        return constantPool.contains("disconnect.timeout") && constantPool.contains("disconnect.genericReason")
                && constantPool.contains("disconnect.endOfStream") && constantPool.contains("Internal Exception: ");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
