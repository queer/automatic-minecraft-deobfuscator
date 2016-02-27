package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.client.renderer;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author audrey
 * @since 2/27/16.
 */
public class Framebuffer extends Deobfuscator {
    public Framebuffer() {
        super("Framebuffer");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        List<String> constants = dumpConstantPoolStrings(new ClassReader(classData));
        return constants.containsAll(Arrays.<String>asList("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT",
                "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT",
                "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER",
                "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(m.desc.equals("(IIZ)V") && AccessHelper.isPublic(m.access) && !m.name.equals("<init>")) {
                def.addMethod("framebufferRender", m);
            }
        }

        return def;
    }
}
