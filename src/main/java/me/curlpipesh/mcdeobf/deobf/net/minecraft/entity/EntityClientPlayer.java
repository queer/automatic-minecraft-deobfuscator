package me.curlpipesh.mcdeobf.deobf.net.minecraft.entity;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static me.curlpipesh.mcdeobf.util.AccessHelper.isPublic;
import static me.curlpipesh.mcdeobf.util.AccessHelper.isStatic;
import static me.curlpipesh.mcdeobf.util.AccessHelper.isVoid;

/**
 * @author c
 * @since 8/3/15
 */
public class EntityClientPlayer extends Deobfuscator {

    public EntityClientPlayer() {
        super("EntityClientPlayer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("minecraft:container") && c.contains("portal.trigger");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        ClassDef c = new ClassDef(cn.name, getDeobfuscatedName());

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(isPublic(m.access) && isVoid(m.desc)) {
                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    AbstractInsnNode node = i.next();
                    if(node instanceof LdcInsnNode) {
                        final Object ldc = ((LdcInsnNode) node).cst;
                        if(ldc instanceof Double) {
                            if((Double)ldc == -999.0) {
                                c.addMethod("onMotionUpdate", m.name);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return c;
    }
}
