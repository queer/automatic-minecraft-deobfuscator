package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.entity;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author c
 * @since 8/3/15
 */
public class Entity extends Deobfuscator {
    public Entity() {
        super("Entity");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("entityBaseTick") && c.contains("Checking entity block collision");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        int doublesFound = 0;

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(f.desc.equals("D")) {
                ++doublesFound;
                if(doublesFound > 1) {
                    switch(doublesFound) {
                        case 2:
                            def.addField("prevX", f.name);
                            break;
                        case 3:
                            def.addField("prevY", f.name);
                            break;
                        case 4:
                            def.addField("prevZ", f.name);
                            break;
                        case 5:
                            def.addField("curX", f.name);
                            break;
                        case 6:
                            def.addField("curY", f.name);
                            break;
                        case 7:
                            def.addField("curZ", f.name);
                            break;
                    }
                    if(doublesFound > 7) {
                        break;
                    }
                }
            }
        }

        Optional<Map.Entry<Deobfuscator, Byte[]>> entityAttributes = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("EntityAttributes")).findFirst();
        if(!entityAttributes.isPresent()) {
            Main.getInstance().getLogger().severe("[Entity] Couldn't find EntityAttributes, bailing out.");
            return null;
        }
        //noinspection Convert2streamapi
        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(AccessHelper.isPublic(m.access)) {
                // TODO: Find better way to do this...
                if(m.name.equals("az")) {
                    def.addMethod("isSneaking", m);
                }
            }
        }

        return def;
    }
}
