package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.network.packet.server;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * @author audrey
 * @since 12/17/15.
 */
public class PacketServerChatMessage extends Deobfuscator {
    public PacketServerChatMessage() {
        super("PacketServerChatMessage");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        return cn.fields.size() == 2 && ((FieldNode) cn.fields.get(1)).desc.equals("B") && AccessHelper.isPrivate(((FieldNode) cn.fields.get(1)).access);
    }

    @Override
    public ClassDef getClassDefinition(final byte[] classData) {
        ClassDef def = new ClassDef(this);
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        def.addField("chatMessageComponent", ((FieldNode) cn.fields.get(0)).name);
        def.addField("chatMessageType", ((FieldNode) cn.fields.get(1)).name);
        return def;
    }
}
