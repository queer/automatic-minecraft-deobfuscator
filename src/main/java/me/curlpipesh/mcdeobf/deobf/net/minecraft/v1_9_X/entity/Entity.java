package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.entity;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author c
 * @since 8/3/15
 */
public class Entity extends Deobfuscator {
    public Entity() {
        super("Entity", DeobfuscatorPriority.HIGH);
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        final List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("entityBaseTick") && c.contains("Checking entity block collision");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        final ClassDef def = new ClassDef(this);

        int doublesFound = 0;
        int floatsFound = 0;
        int boolsFound = 0;

        for (final FieldNode f : (List<FieldNode>) cn.fields) {
            if (f.desc.equals("D")) {
                ++doublesFound;

                if (doublesFound > 1 && doublesFound <= 10) {
                    switch (doublesFound) {
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
                        case 8:
                            def.addField("motionX", f.name);
                            break;
                        case 9:
                            def.addField("motionY", f.name);
                            break;
                        case 10:
                            def.addField("motionZ", f.name);
                            break;
                    }
                }
            }
            if (f.desc.equals("F")) {
                ++floatsFound;
                switch (floatsFound) {
                    case 1:
                        def.addField("rotationYaw", f.name);
                        break;
                    case 2:
                        def.addField("rotationPitch", f.name);
                        break;
                    case 3:
                        def.addField("prevRotationYaw", f.name);
                        break;
                    case 4:
                        def.addField("prevRotationPitch", f.name);
                        break;
                }
            }
            if (f.desc.equals("Z")) {
                ++boolsFound;

                switch (boolsFound) {
                    case 3:
                        def.addField("onGround", f.name);
                        break;
                    case 4:
                        def.addField("isCollidedHorizontally", f.name);
                        break;
                    case 5:
                        def.addField("isCollidedVertically", f.name);
                        break;
                    case 6:
                        def.addField("isCollided", f.name);
                        break;
                    case 7:
                        def.addField("velocityChanged", f.name);
                        break;
                    case 8:
                        def.addField("isInWeb", f.name);
                        break;
                    case 9:
                        def.addField("isOutsideBorder", f.name);
                        break;
                    case 10:
                        def.addField("isDead", f.name);
                        break;
                    case 11:
                        def.addField("noClip", f.name);
                        break;
                }
            }
        }

        final Optional<Entry<Deobfuscator, byte[]>> entityAttributes = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("EntityAttributes")).findFirst();
        if (!entityAttributes.isPresent()) {
            Main.getInstance().getLogger().severe("[Entity] Couldn't find EntityAttributes, bailing out.");
            return null;
        }

        //noinspection Convert2streamapi
        for (final MethodNode m : (List<MethodNode>) cn.methods) {
            if (AccessHelper.isPublic(m.access)) {
                // TODO: Find better way to do this...
                if (m.name.equals("aJ")) {
                    def.addMethod("isSneaking", m);
                }
                if (m.name.equals("aO")) {
                    def.addMethod("isInAir", m);
                }
                if (m.name.equals("f")) {
                    def.addMethod("setGlowing", m);
                }
            }
        }
        
        return def;
    }
}
