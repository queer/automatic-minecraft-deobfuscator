package me.curlpipesh.mcdeobf.deobf.net.minecraft.client.gui;

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
public class GuiChat extends Deobfuscator {
    public GuiChat() {
        super("GuiChat");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        boolean nonStaticInstanceOfSelf = false;
        boolean separateLogger = false;

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(isProtected(f.access) && !isStatic(f.access) && f.desc.contains(cn.name)) {
                nonStaticInstanceOfSelf = true;
            }
            if(isPrivate(f.access) && isStatic(f.access) && isFinal(f.access) && f.desc.contains("Logger")) {
                separateLogger = true;
            }
        }

        return nonStaticInstanceOfSelf && separateLogger;
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
