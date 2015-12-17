package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.network;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 12/7/15.
 */
public class EnumConnectionState extends Deobfuscator {
    public EnumConnectionState() {
        super("EnumConnectionState");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        List<String> constantPool = dumpConstantPoolStrings(cr);
        return constantPool.contains(" is already assigned to protocol ")
                && constantPool.contains(" is already known to ID ")
                && AccessHelper.isEnum(cr.getAccess());
    }

    @Override
    public ClassDef getClassDefinition(final byte[] classData) {
        return null;
    }
}
