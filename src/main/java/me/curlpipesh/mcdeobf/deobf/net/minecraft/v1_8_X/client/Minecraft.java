package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.client;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
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
    public boolean deobfuscate(byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("X-Minecraft-Username");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef c = new ClassDef(this);
        cr.accept(cn, 0);
        String guiScreen = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("GuiScreen")).findFirst().get().getKey()
                .getObfuscatedDescription();
        int intCounter = 0;

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(isPrivate(m.access) && m.desc.equals("()V")) {
                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    AbstractInsnNode node = i.next();
                    if(node instanceof LdcInsnNode) {
                        if(((LdcInsnNode) node).cst instanceof String) {
                            if(((String) ((LdcInsnNode) node).cst).equalsIgnoreCase("LWJGL Version: ")) {
                                c.addMethod("startGame", m);
                                break;
                            }
                        }
                    }
                }
            } else if(isPublic(m.access) && isVoid(m.desc)) {
                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    AbstractInsnNode node = i.next();
                    if(node instanceof LdcInsnNode) {
                        if(((LdcInsnNode) node).cst instanceof String) {
                            if(((String) ((LdcInsnNode) node).cst).equalsIgnoreCase("Manually triggered debug crash")) {
                                c.addMethod("runGame", m);
                                break;
                            }
                        }
                    }
                }
            } else if(isPublic(m.access) && isStatic(m.access) && m.desc.equals("()L" + cn.name + ";")) {
                c.addMethod("getMinecraft", m);
            } else if(m.desc.contains("(" + guiScreen + ")V")) {
                c.addMethod("displayGuiScreen", m);
            }
        }

        // TODO: Refactor this to be less hideously inefficient
        for(FieldNode f : (List<FieldNode>) cn.fields) {
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
            if(f.desc.contains("Ljava/io/File;") && AccessHelper.isPublic(f.access)) {
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
