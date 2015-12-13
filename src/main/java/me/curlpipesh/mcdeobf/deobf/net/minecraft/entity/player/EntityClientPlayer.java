package me.curlpipesh.mcdeobf.deobf.net.minecraft.entity.player;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        ClassDef c = new ClassDef(this);

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
                    c.addMethod("addChatMessage", m.name);
                }

                if(secondLastNode != null && secondLastNode.name.equals("<init>")
                        && secondLastNode.desc.equals("(Ljava/lang/String;)V") && secondLastNode.desc.equals(m.desc)) {
                    c.addMethod("sendChatMessage", m.name);
                }
            }
        }

        Optional<Map.Entry<Deobfuscator, Byte[]>> netClientPlayHandler = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("NetClientPlayHandler")).findFirst();
        if(!netClientPlayHandler.isPresent()) {
            Main.getInstance().getLogger().severe("[EntityClientPlayer] Couldn't find NetClientPlayHandler, bailing out.");
            return null;
        }

        ((List<FieldNode>) cn.fields).stream()
                .filter(f -> f.desc.contains(netClientPlayHandler.get().getKey().getObfuscatedDescription()))
                .forEach(f -> c.addField("netClientPlayHandler", f.name));

        return c;
    }
}
