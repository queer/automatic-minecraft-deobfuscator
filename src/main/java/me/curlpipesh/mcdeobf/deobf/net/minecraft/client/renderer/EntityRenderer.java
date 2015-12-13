package me.curlpipesh.mcdeobf.deobf.net.minecraft.client.renderer;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author c
 * @since 8/18/15
 */
public class EntityRenderer extends Deobfuscator {
    public EntityRenderer() {
        super("EntityRenderer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).stream().filter(s -> s.toLowerCase().contains("shaders/post/")).count() > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        Optional<Map.Entry<Deobfuscator, Byte[]>> entityPlayer = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("EntityPlayer")).findFirst();
        if(!entityPlayer.isPresent()) {
            Main.getInstance().getLogger().severe("[EntityRenderer] Couldn't find EntityPlayer, bailing out.");
            return null;
        }

        for(MethodNode m : (List<MethodNode>)cn.methods) {
            if(m.desc.equals("(IFJ)V") && AccessHelper.isPrivate(m.access)) {
                def.addMethod("doWorldRender", m.name);
            } else if(m.desc.equals("(F)V") && AccessHelper.isPrivate(m.access)) {
                boolean hasInstanceof = false;
                boolean dealsWithPlayer = false;
                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    AbstractInsnNode a = i.next();
                    if(a instanceof TypeInsnNode) {
                        if(a.getOpcode() == Opcodes.INSTANCEOF) {
                            hasInstanceof = true;
                        } else if(a.getOpcode() == Opcodes.CHECKCAST) {
                            if(((TypeInsnNode)a).desc.contains(entityPlayer.get().getKey().getObfuscatedDescription())) {
                                dealsWithPlayer = true;
                            }
                        }
                    }
                }
                if(hasInstanceof && dealsWithPlayer) {
                    def.addMethod("applyViewBobbing", m.name);
                }
            } else if(m.desc.equals("()V") && AccessHelper.isPublic(m.access)) {
                int count3553 = 0;
                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    AbstractInsnNode a = i.next();
                    if(a instanceof LdcInsnNode) {
                        if(((LdcInsnNode) a).cst instanceof Integer) {
                            if(((Integer) ((LdcInsnNode) a).cst) == 3553) {
                                ++count3553;
                            }
                        }
                    }
                }
                if(count3553 == 4) {
                    def.addMethod("enableLightmap", m.name);
                    def.addMethod("disableLightmap", ((List<MethodNode>)cn.methods).get(cn.methods.indexOf(m) - 1).name);
                }
            }
        }

        return def;
    }
}
