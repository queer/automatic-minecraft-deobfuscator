package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.item.inventory.container;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.isAbstract;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class Container extends Deobfuscator {
    public Container() {
        super("Container");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        // So this actually works for some reason.
        return dumpConstantPoolStrings(cr).contains("Listener already listening") && isAbstract(cn.access);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassDef def = new ClassDef(this);
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        String player = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("EntityPlayer")).findFirst().get().getKey()
                .getObfuscatedDescription();
        String itemStack = Main.getInstance().getDataToMap().entrySet().stream()
                .filter(d -> d.getKey().getDeobfuscatedName().equals("ItemStack")).findFirst().get().getKey()
                .getObfuscatedDescription();
        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(m.desc.contains("I)") && m.desc.contains(player) && m.desc.contains(itemStack)) {
                def.addMethod("getStackInSlot", m);
            }
        }
        return def;
    }
}
