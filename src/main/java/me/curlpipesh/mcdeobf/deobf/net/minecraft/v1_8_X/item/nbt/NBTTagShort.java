package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.item.nbt;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.isShort;

/**
 * TODO This -- and related deobfuscators -- could probably be refactored into
 * a more generic "NBTTagType" deobfuscator that just takes a constructor
 * parameter that tells it what to look for
 *
 * @author audrey
 * @since 8/25/15.
 */
public class NBTTagShort extends Deobfuscator {
    public NBTTagShort() {
        super("NBTTagShort");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        List<String> constantPool = dumpConstantPoolStrings(cr);

        return constantPool.contains("") && constantPool.contains("s") && cn.fields.size() == 1
                && isShort(((FieldNode) cn.fields.get(0)).desc);
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
