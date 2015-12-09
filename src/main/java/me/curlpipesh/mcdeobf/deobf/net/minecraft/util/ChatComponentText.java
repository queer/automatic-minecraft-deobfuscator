package me.curlpipesh.mcdeobf.deobf.net.minecraft.util;


import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;

import static me.curlpipesh.mcdeobf.util.AccessHelper.isPublic;

/* Created on 8/19/2015 @ 3:09 PM */
public class ChatComponentText extends Deobfuscator {

    public ChatComponentText(){
        super("ChatComponentText");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("TextComponent{text='");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        ClassDef c = new ClassDef(this);

        //Reason for this is because it has 2 of the same method...
        boolean foundGetText = false;

        for(MethodNode m : (List<MethodNode>) cn.methods) {
            if(isPublic(m.access)) {
                Iterator<AbstractInsnNode> i = m.instructions.iterator();
                while(i.hasNext()) {
                    AbstractInsnNode node = i.next();
                    if(node instanceof FieldInsnNode){
                        final FieldInsnNode fn = (FieldInsnNode)node;
                        if(fn.desc.equals("Ljava/lang/String;") && m.desc.equals("()Ljava/lang/String;") &&
                                !foundGetText && m.instructions.size() < 10){
                            foundGetText = true;
                            c.addMethod("getUnformattedText", m.name);
                        }
                    }
                }
            }
        }
        return c;
    }

}
