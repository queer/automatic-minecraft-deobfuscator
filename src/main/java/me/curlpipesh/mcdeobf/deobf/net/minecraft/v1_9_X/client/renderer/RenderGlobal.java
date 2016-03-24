package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.client.renderer;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author audrey
 * @since 3/23/16.
 */
public class RenderGlobal extends Deobfuscator {
    public RenderGlobal() {
        super("RenderGlobal");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("C: %d/%d %sD: %d, L: %d, %s");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        final ClassDef def = new ClassDef(this);

        final Optional<Entry<Deobfuscator, byte[]>> entity = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("Entity")).findFirst();
        if(!entity.isPresent()) {
            Main.getInstance().getLogger().severe("[RenderGlobal] Couldn't find Entity, bailing out.");
            return null;
        }

        ((List<MethodNode>) cn.methods).stream()
                .filter(node -> node.desc.startsWith('(' + entity.get().getKey().getObfuscatedDescription())
                        && node.desc.endsWith("F)V") && node.desc.split(";").length == 3)
                .filter(node -> node.maxStack > 2)
                .forEach(node -> def.addMethod("renderEntitiesWithCulling", node));

        return def;
    }
}
