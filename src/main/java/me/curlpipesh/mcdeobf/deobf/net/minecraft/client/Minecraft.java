package me.curlpipesh.mcdeobf.deobf.net.minecraft.client;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

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
        ClassDef c = new ClassDef(cn.name, getDeobfuscatedName());
        cr.accept(cn, 0);
        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(m.exceptions.size() == 2) {
                if(m.exceptions.contains("LWJGLException") && m.exceptions.contains("IOException")) {
                    c.addMethod("startGame", m.name);
                    continue;
                }
            }
            if(isPublic(m.access) && isVoid(m.desc)) {
                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    AbstractInsnNode node = i.next();
                    if(node instanceof LdcInsnNode) {
                        if(((LdcInsnNode) node).cst instanceof String) {
                            if(((String) ((LdcInsnNode) node).cst).equalsIgnoreCase("Manually triggered debug crash")) {
                                c.addMethod("runGame", m.name);
                                break;
                            }
                        }
                    }
                }
            }
            if(isPublic(m.access) && isStatic(m.access) && m.desc.equals("()L" + cn.name + ";")) {
                c.addMethod("getMinecraft", m.name);
            }
        }
        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(Main.getInstance().getDeobfuscators().stream()
                    .filter(d -> d.getDeobfuscatedName().equals("World")).findFirst().get()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("theWorld", f.name);
            }
            if(Main.getInstance().getDeobfuscators().stream()
                    .filter(d -> d.getDeobfuscatedName().equals("EntityClientPlayer")).findFirst().get()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("thePlayer", f.name);
            }
            if(Main.getInstance().getDeobfuscators().stream()
                    .filter(d -> d.getDeobfuscatedName().equals("GameSettings")).findFirst().get()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("gameSettings", f.name);
            }
            if(Main.getInstance().getDeobfuscators().stream()
                    .filter(d -> d.getDeobfuscatedName().equals("FontRenderer")).findFirst().get()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("fontRenderer", f.name);
            }
            if(Main.getInstance().getDeobfuscators().stream()
                    .filter(d -> d.getDeobfuscatedName().equals("EntityRenderer")).findFirst().get()
                    .getObfuscatedDescription().equalsIgnoreCase(f.desc)) {
                c.addField("entityRenderer", f.name);
            }
        }
        return null;
    }
}
