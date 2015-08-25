package me.curlpipesh.mcdeobf.deobf.net.minecraft.item.nbt;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.*;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class NBTTagEnd extends Deobfuscator {
    public NBTTagEnd() {
        super("NBTTagEnd");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        List<String> constantPool = dumpConstantPoolStrings(cr);

        boolean byteThatReturnsZero = false;

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(isByte(m.desc)) {
                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                int count = 0;
                boolean iconstZero = false;
                boolean ireturn = false;
                while(i.hasNext()) {
                    ++count;
                    AbstractInsnNode a = i.next();
                    if(a.getOpcode() == Opcodes.ICONST_0) {
                        iconstZero = true;
                    }
                    if(a.getOpcode() == Opcodes.IRETURN) {
                        ireturn = true;
                    }
                }
                byteThatReturnsZero = iconstZero && ireturn;
            }
        }

        return constantPool.contains("END") && byteThatReturnsZero;
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
