package me.curlpipesh.mcdeobf.deobf.net.minecraft.network.packet;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class PacketClientChatMessage extends Deobfuscator {
    public PacketClientChatMessage() {
        super("PacketClientChatMessage");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        return cr.getInterfaces().length == 1 && cn.fields.size() == 1 &&
                ((List<FieldNode>) cn.fields).get(0).desc.equals("Ljava/lang/String;");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
