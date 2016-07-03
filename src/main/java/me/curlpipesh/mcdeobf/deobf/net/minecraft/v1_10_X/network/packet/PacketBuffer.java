package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * @author audrey
 * @since 12/17/15.
 */
public class PacketBuffer extends Deobfuscator {
    public PacketBuffer() {
        super("PacketBuffer");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        List<String> constants = dumpConstantPoolStrings(new ClassReader(classData));
        return constants.contains("VarInt too big")
                && constants.contains("VarLong too big")
                && constants.contains("The received encoded string buffer length is longer than maximum allowed (")
                && constants.contains("The received encoded string buffer length is less than zero! Weird string!")
                && constants.contains("The received string length is longer than maximum allowed (");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        ClassDef def = new ClassDef(this);
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(m.desc.equals("(I)Ljava/lang/String;") && AccessHelper.isPublic(m.access)) {
                def.addMethod("readStringFromBuffer", m);
            }
        }

        return def;
    }
}
