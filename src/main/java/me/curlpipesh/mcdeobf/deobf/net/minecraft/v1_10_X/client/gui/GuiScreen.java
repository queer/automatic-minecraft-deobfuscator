package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class GuiScreen extends Deobfuscator {
    public GuiScreen() {
        super("GuiScreen");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("Invalid Item!") && constantPool.contains("Invalid Entity!")
                && constantPool.contains("stats.tooltip.type.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassDef def = new ClassDef(this);
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            switch(m.desc) {
                case "(IIF)V":
                    def.addMethod("drawScreen", m);
                    break;
                case "(CI)V":
                    def.addMethod("keyPress", m);
                    break;
                case "(III)V":
                    Iterator<AbstractInsnNode> i = m.instructions.iterator();
                    boolean isFirst = false;
                    while(i.hasNext()) {
                        AbstractInsnNode a = i.next();
                        if(a instanceof JumpInsnNode) {
                            if(a.getOpcode() == Opcodes.GOTO) {
                                def.addMethod("mouseClicked", m);
                                isFirst = true;
                                break;
                            }
                        }
                    }
                    if(!isFirst) {
                        def.addMethod("mouseReleased", m);
                    }
                    break;
                case "(IIIJ)V":
                    def.addMethod("mouseDownDrag", m);
                    break;
                case "()V":
                    if(AccessHelper.isPublic(m.access)) {
                        if(m.instructions.getFirst().getOpcode() == Opcodes.RETURN) {
                            def.addMethod("initGui", m);
                        }
                    }
                    break;
                case "()Z":
                    if(AccessHelper.isPublic(m.access)) {
                        def.addMethod("doesGuiPauseGame", m);
                    }
                    break;
            }
        }

        return def;
    }
}
