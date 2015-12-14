package me.curlpipesh.mcdeobf.deobf.net.minecraft.item.inventory;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class InventoryPlayer extends Deobfuscator {
    public InventoryPlayer() {
        super("InventoryPlayer");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("container.inventory") && constantPool.contains("Adding item to inventory");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassDef def = new ClassDef(this);
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(f.desc.equals("I")) {
                def.addField("currentSlot", f.name);
            }
        }

        return def;
    }
}
