package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.network.packet;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.isInterface;
import static me.curlpipesh.mcdeobf.util.AccessHelper.isVoid;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class Packet extends Deobfuscator {
    public Packet() {
        super("Packet");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        int voidCount = 0;

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(isVoid(m.desc)) {
                ++voidCount;
            }
        }

        return isInterface(cn.access) && voidCount == cn.methods.size() && !cn.methods.isEmpty()
                && (cn.signature != null && cn.signature.contains("<"));
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
