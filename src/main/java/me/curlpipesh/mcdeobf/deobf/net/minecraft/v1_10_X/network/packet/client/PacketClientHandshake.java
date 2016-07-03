package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet.client;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

/**
 * @author audrey
 * @since 12/7/15.
 */
public class PacketClientHandshake extends Deobfuscator {
    public PacketClientHandshake() {
        super("PacketClientHandshake");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(final byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        return cr.getInterfaces().length == 1 && cn.fields.size() == 4
                && ((List<FieldNode>) cn.fields).get(0).desc.equals("I")
                && ((List<FieldNode>) cn.fields).get(1).desc.equals("Ljava/lang/String;")
                && ((List<FieldNode>) cn.fields).get(2).desc.equals("I");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef c = new ClassDef(this);
        cr.accept(cn, 0);

        c.addField("hostname", ((List<FieldNode>) cn.fields).get(1).name);
        c.addField("port", ((List<FieldNode>) cn.fields).get(2).name);

        return c;
    }
}
