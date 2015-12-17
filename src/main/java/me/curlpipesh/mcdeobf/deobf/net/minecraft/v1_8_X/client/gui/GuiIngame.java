package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public class GuiIngame extends Deobfuscator {
    public GuiIngame() {
        super("GuiIngame");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("overlayMessage") && c.contains("bossHealth");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        for(MethodNode m : (List<MethodNode>)cn.methods) {
            if(m.desc.equals("(F)V") && AccessHelper.isPublic(m.access)) {
                def.addMethod("renderGameOverlay", m);
                break;
            }
        }

        return def;
    }
}
