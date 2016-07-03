package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.entity;

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
 * @since 8/24/15.
 */
public class EntityLivingBase extends Deobfuscator {
    public EntityLivingBase() {
        super("EntityLivingBase", DeobfuscatorPriority.HIGH);
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        final List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("livingEntityBaseTick") && constantPool.contains("doMobLoot")
                && constantPool.contains("HurtByTimestamp") && constantPool.contains("Attributes");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ClassDef getClassDefinition(final byte[] classData) {
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        final ClassDef def = new ClassDef(this);

        final Optional<Entry<Deobfuscator, byte[]>> entity = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("Entity")).findFirst();
        if(!entity.isPresent()) {
            Main.getInstance().getLogger().severe("[EntityLivingBase] Couldn't find Entity, bailing out.");
            return null;
        }

        ((List<MethodNode>) cn.methods).stream().filter(node -> node.desc.equals("(L"
                + entity.get().getKey().getObfuscatedName() + ";)Z"))
                .filter(node -> node.maxStack > 2)
                .forEach(node -> def.addMethod("canEntityBeSeen", node));

        return def;
    }
}
