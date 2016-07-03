package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.client;

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
    public boolean deobfuscate(final byte[] classData) {
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        return cr.getInterfaces().length == 1 && cn.fields.size() == 1 &&
                ((List<FieldNode>) cn.fields).get(0).desc.equals("Ljava/lang/String;")
                && cn.methods.size() > 4;
    }

    @Override
    public ClassDef getClassDefinition(final byte[] classData) {
        final ClassDef def = new ClassDef(this);
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        def.addField("chatMessage", ((FieldNode)cn.fields.get(0)).name);
        return def;
    }
}
