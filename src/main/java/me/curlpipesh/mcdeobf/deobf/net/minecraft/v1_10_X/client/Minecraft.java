package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

@SuppressWarnings("Duplicates")
public class Minecraft extends Deobfuscator {
    public Minecraft() {
        super("Minecraft");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("gl_caps[EXT_gpu_program_parameters]");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        final ClassDef c = new ClassDef(this);
        cr.accept(cn, 0);
        final String guiScreen = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("GuiScreen")).findFirst().get().getKey()
                .getObfuscatedDescription();
        int intCounter = 0;

        for(final MethodNode m : (List<MethodNode>) cn.methods) {
            if(m.desc.contains(guiScreen)) {
                c.addMethod("displayGuiScreen", m);
            } else if(isPrivate(m.access) && m.desc.equals("()V")) {
                final Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    final AbstractInsnNode node = i.next();
                    if(node instanceof LdcInsnNode) {
                        if(((LdcInsnNode) node).cst instanceof String) {
                            if(((String) ((LdcInsnNode) node).cst).equalsIgnoreCase("LWJGL Version: {}")) {
                                c.addMethod("startGame", m);
                                break;
                            }
                            if(((String) ((LdcInsnNode) node).cst).equalsIgnoreCase("Manually triggered debug crash")) {
                                c.addMethod("runGame", m);
                                break;
                            }
                        }
                    }
                }
            } else if(isPublic(m.access) && isStatic(m.access) && m.desc.equals("()L" + cn.name + ";")) {
                c.addMethod("getMinecraft", m);
            } else if(isPublic(m.access) && m.desc.contains("()Ljava/lang/String;")) {
                c.addMethod("getVersion", m);
            }
        }

        // TODO: Refactor this to be less hideously inefficient
        for(final FieldNode f : (List<FieldNode>) cn.fields) {
            if(Main.getInstance().getDataToMap().entrySet().stream()
                    .filter(d -> d.getKey().getDeobfuscatedName().equals("World")).findFirst().get().getKey()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("theWorld", f.name);
            }
            if(Main.getInstance().getDataToMap().entrySet().stream()
                    .filter(d -> d.getKey().getDeobfuscatedName().equals("EntityClientPlayer")).findFirst().get().getKey()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("thePlayer", f.name);
            }
            if(Main.getInstance().getDataToMap().entrySet().stream()
                    .filter(d -> d.getKey().getDeobfuscatedName().equals("GameSettings")).findFirst().get().getKey()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("gameSettings", f.name);
            }
            if(Main.getInstance().getDataToMap().entrySet().stream()
                    .filter(d -> d.getKey().getDeobfuscatedName().equals("FontRenderer")).findFirst().get().getKey()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("fontRenderer", f.name);
            }
            if(Main.getInstance().getDataToMap().entrySet().stream()
                    .filter(d -> d.getKey().getDeobfuscatedName().equals("EntityRenderer")).findFirst().get().getKey()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("entityRenderer", f.name);
            }
            if(Main.getInstance().getDataToMap().entrySet().stream()
                    .filter(d -> d.getKey().getDeobfuscatedName().equals("GuiScreen")).findFirst().get().getKey()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("currentScreen", f.name);
            }
            if(Main.getInstance().getDataToMap().entrySet().stream()
                    .filter(d -> d.getKey().getDeobfuscatedName().equals("Session")).findFirst().get().getKey()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("session", f.name);
            }
            if(f.desc.contains("Ljava/io/File;") && isPublic(f.access)) {
                c.addField("mcDataDir", f.name);
            } else if(f.desc.equals("I") && intCounter < 2) {
                if(intCounter == 0) {
                    c.addField("width", f.name);
                } else {
                    c.addField("height", f.name);
                }
                ++intCounter;
            }
        }
        return c;
    }
}
