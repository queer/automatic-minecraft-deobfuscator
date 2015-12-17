package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public class FontRenderer extends Deobfuscator {
    public FontRenderer() {
        super("FontRenderer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("0123456789abcdefklmknor") || c.contains("0123456789abcdef");
    }

    @Override
    @SuppressWarnings({"unchecked", "Convert2streamapi"})
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        for(FieldNode f : (List<FieldNode>)cn.fields) {
            if(f.desc.equals("I") && AccessHelper.isPublic(f.access)) {
                def.addField("fontHeight", f.name);
            }
        }

        for(MethodNode m : (List<MethodNode>)cn.methods) {
            if(m.desc.equals("(Ljava/lang/String;)I") && AccessHelper.isPublic(m.access)) {
                def.addMethod("getStringWidth", m);
            } else if(m.desc.equals("(Ljava/lang/String;FFIZ)V") && AccessHelper.isPublic(m.access)) {
                def.addMethod("drawString", m);
            }
        }

        return def;
    }
}
