package me.curlpipesh.mcdeobf.deobf.net.minecraft.item.inventory.container;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

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
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
