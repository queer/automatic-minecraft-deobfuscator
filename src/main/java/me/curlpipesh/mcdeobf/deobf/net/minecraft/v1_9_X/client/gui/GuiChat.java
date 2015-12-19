package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
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

        boolean separateLogger = false;
        boolean enablesKeyboardRepeatEvents = false;
        boolean mouseDWheel = false;

        for(FieldNode f : (List<FieldNode>) cn.fields) {
            if(isPrivate(f.access) && isStatic(f.access) && isFinal(f.access) && f.desc.contains("Logger")) {
                separateLogger = true;
            }
        }

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            Iterator<AbstractInsnNode> i = m.instructions.iterator();
            while(i.hasNext()) {
                AbstractInsnNode a = i.next();
                if(a instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) a;
                    if(methodInsnNode.name.equalsIgnoreCase("enableRepeatEvents")) {
                        enablesKeyboardRepeatEvents = true;
                    }
                    if(methodInsnNode.name.equalsIgnoreCase("getEventDWheel")) {
                        mouseDWheel = true;
                    }
                }
            }
        }

        return separateLogger && enablesKeyboardRepeatEvents && mouseDWheel;
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        // TODO: IMPLEMENT THIS
        return new ClassDef(this);
    }
}
