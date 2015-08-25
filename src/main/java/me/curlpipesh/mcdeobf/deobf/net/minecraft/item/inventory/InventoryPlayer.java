package me.curlpipesh.mcdeobf.deobf.net.minecraft.item.inventory;

import jdk.nashorn.internal.codegen.types.Type;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

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
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
