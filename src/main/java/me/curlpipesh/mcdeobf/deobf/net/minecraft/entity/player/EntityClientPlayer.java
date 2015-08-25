package me.curlpipesh.mcdeobf.deobf.net.minecraft.entity.player;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.isPublic;
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
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        ClassDef c = new ClassDef(cn.name, getDeobfuscatedName());

        // Reason for this is because it has 2 of the same method...
        boolean foundChatMessage = false;

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(isPublic(m.access) && isVoid(m.desc)) {
                MethodInsnNode lastMethodNode = null, secondLastNode = null;

                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    AbstractInsnNode node = i.next();
                    if(node instanceof LdcInsnNode) {
                        final Object ldc = ((LdcInsnNode) node).cst;
                        if(ldc instanceof Double) {
                            if((Double) ldc == -999.0) {
                                c.addMethod("onMotionUpdate", m.name);
                                break;
                            }
                        }
                    } else if(node instanceof MethodInsnNode) {
                        secondLastNode = lastMethodNode;
                        lastMethodNode = (MethodInsnNode) node;
                    }
                }

                if(lastMethodNode != null && !foundChatMessage && lastMethodNode.desc.equals(m.desc)
                        && m.desc.startsWith("(L") && m.desc.endsWith(";)V")) {
                    foundChatMessage = true;
                    //System.out.println("Found addChatMessage: " + m.name + m.desc);
                    c.addMethod("addChatMessage", m.name);
                }

                if(secondLastNode != null && secondLastNode.name.equals("<init>")
                        && secondLastNode.desc.equals("(Ljava/lang/String;)V") && secondLastNode.desc.equals(m.desc)) {
                    c.addMethod("sendChatMessage", m.name);
                }
            }
        }
        return c;
    }
}
