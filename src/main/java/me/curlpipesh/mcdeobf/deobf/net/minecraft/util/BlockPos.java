package me.curlpipesh.mcdeobf.deobf.net.minecraft.util;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class BlockPos extends Deobfuscator {
    public BlockPos() {
        super("BlockPos");
    }

    @Override
    @SuppressWarnings({"unchecked", "Convert2streamapi"})
    public boolean deobfuscate(byte[] classData) {
        // Of course this has no Strings that we can check ;-;
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        boolean staticInstanceOfSelf = false;
        int intCount = 0;
        int longCount = 0;

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(isStatic(f.access) && isPublic(f.access) && isFinal(f.access)) {
                staticInstanceOfSelf = true;
            } else if(isInt(f.desc) && isStatic(f.access) && isPrivate(f.access)) {
                ++intCount;
            } else if(isLong(f.desc) && isStatic(f.access) && isPrivate(f.access)) {
                ++longCount;
            }
        }
        return staticInstanceOfSelf && intCount > 0 && longCount > 0;
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
