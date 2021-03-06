package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.network;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NetClientPlayHandler extends Deobfuscator {
    public NetClientPlayHandler() {
        super("NetClientPlayHandler");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("MC|Brand") && constantPool.contains("disconnect.lost")
                && constantPool.contains("level://");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef c = new ClassDef(this);
        cr.accept(cn, 0);


        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(AccessHelper.isPublic(m.access) && m.desc.equals("(" + Main.getInstance().getDataToMap().entrySet()
                    .stream().filter(d -> d.getKey().getDeobfuscatedName().equals("Packet"))
                    .findFirst().get().getKey().getObfuscatedDescription() + ")V")) {
                c.addMethod("sendPacket", m);
            }
        }

        return c;
    }
}
