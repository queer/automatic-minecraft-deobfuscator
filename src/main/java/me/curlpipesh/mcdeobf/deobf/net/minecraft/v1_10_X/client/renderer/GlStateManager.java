package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.renderer;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

/**
 * Created by TehNeon on 2016-02-29.
 */
public class GlStateManager extends Deobfuscator {

    public GlStateManager() {
        super("GlStateManager");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean deobfuscate(final byte[] classData) {
        int floatBuffersFound = 0;

        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        int floatBufferPosition = 0;
        for(final FieldNode f: (List<FieldNode>) cn.fields) {
            floatBufferPosition++;
            if(f.desc.equals("Ljava/nio/FloatBuffer;") && (floatBufferPosition == 1 || floatBufferPosition == 2)) {
                floatBuffersFound++;
            }
        }


        return floatBuffersFound >= 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        //noinspection UnnecessaryLocalVariable
        final ClassDef def = new ClassDef(this);

        return def;
    }
}
