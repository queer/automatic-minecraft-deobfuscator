package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.client.renderer;

import me.curlpipesh.mcdeobf.Main;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TehNeon on 2016-02-29.
 */
public class GlStateManager extends Deobfuscator {

    public GlStateManager() {
        super("GlStateManager");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        int floatBuffersFound = 0;

        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        int floatBufferPosition = 0;
        for(FieldNode f: (List<FieldNode>) cn.fields) {
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
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        return def;
    }
}
