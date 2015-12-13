package me.curlpipesh.mcdeobf.deobf.net.minecraft.client;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameSettings extends Deobfuscator {
    public GameSettings() {
        super("GameSettings");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        // Checks for >5 because there are a LOT of these in the GameSettings class...
        return dumpConstantPoolStrings(new ClassReader(classData)).stream()
                .filter(s -> s.contains("key.categories.")).count() > 5;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        MethodNode suspect = null;
        Optional<Map.Entry<Deobfuscator, Byte[]>> guiIngame = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equalsIgnoreCase("GuiIngame")).findFirst();
        if(!guiIngame.isPresent()) {
            Main.getInstance().getLogger().severe("[GameSettings] Couldn't find GuiIngame, bailing out.");
            return null;
        }

        {
            Byte[] stored = guiIngame.get().getValue();
            byte[] output = new byte[stored.length];
            for(int i = 0; i < stored.length; i++) {
                output[i] = stored[i];
            }
            ClassReader cr2 = new ClassReader(output);
            ClassNode cn2 = new ClassNode();
            cr2.accept(cn2, 0);
            for(MethodNode m : (List<MethodNode>) cn2.methods) {
                if(m.desc.endsWith("V") && AccessHelper.isPublic(m.access)) {
                    boolean demo = false;
                    boolean demoExpired = false;
                    boolean remainingTime = false;
                    Iterator<AbstractInsnNode> i = m.instructions.iterator();
                    while(i.hasNext()) {
                        AbstractInsnNode a = i.next();
                        if(a instanceof LdcInsnNode) {
                            if(((LdcInsnNode) a).cst instanceof String) {
                                String cst = (String) ((LdcInsnNode) a).cst;
                                switch(cst) {
                                    case "demo":
                                        demo = true;
                                        break;
                                    case "demo.demoExpired":
                                        demoExpired = true;
                                        break;
                                    case "demo.remainingTime":
                                        remainingTime = true;
                                        break;
                                }
                            }
                        }
                    }
                    if(demo && demoExpired && remainingTime) {
                        suspect = (MethodNode) cn2.methods.get(cn2.methods.indexOf(m) + 1);
                        break;
                    }
                }
            }
            if(suspect == null) {
                Main.getInstance().getLogger().severe("[GameSettings] Couldn't find suspect method, bailing out.");
                return null;
            }
            Iterator<AbstractInsnNode> i = suspect.instructions.iterator();
            while(i.hasNext()) {
                AbstractInsnNode a = i.next();
                if(a instanceof FieldInsnNode) {
                    if(((FieldInsnNode) a).desc.equals("Z")) {
                        def.addField("isIngameGuiInDebugMode", ((FieldInsnNode) a).name);
                        break;
                    }
                }
            }
        }

        int booleanCount = 0;
        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(f.desc.equals("Z")) {
                ++booleanCount;
                if(booleanCount == 2) {
                    def.addField("isViewBobbingEnabled", f.name);
                    break;
                }
            }
        }

        return def;
    }
}
