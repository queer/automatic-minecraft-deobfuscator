package me.curlpipesh.mcdeobf.deobf.net.minecraft.item.nbt;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.isArray;
import static me.curlpipesh.mcdeobf.util.AccessHelper.isInt;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NBTTagIntArray extends Deobfuscator {
    public NBTTagIntArray() {
        super("NBTTagIntArray");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        List<String> constantPool = dumpConstantPoolStrings(cr);

        return constantPool.contains(",") && cn.fields.size() == 1 && isInt(((FieldNode) cn.fields.get(0)).desc)
                && isArray(((FieldNode) cn.fields.get(0)).desc);
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
