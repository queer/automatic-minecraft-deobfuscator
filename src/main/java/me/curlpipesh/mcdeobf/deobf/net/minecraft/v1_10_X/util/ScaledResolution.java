package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.util;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * @author c
 * @since 8/19/15
 */
public class ScaledResolution extends Deobfuscator {
    public ScaledResolution() {
        super("ScaledResolution");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        // The ScaledResolution class has five fields -- three ints and two
        // doubles. Each of these fields also has a getter method, so we just
        // count up the number of each type of field/getter and pray like crazy
        // that it comes out to the right value.
        int intCount = 0;
        int doubleCount = 0;
        for(FieldNode fn : (List<FieldNode>) cn.fields) {
            if(fn.desc.equals("I")) {
                ++intCount;
            } else if(fn.desc.equals("D")) {
                ++doubleCount;
            }
        }
        for(MethodNode mn : (List<MethodNode>) cn.methods) {
            if(mn.desc.endsWith("I")) {
                ++intCount;
            } else if(mn.desc.endsWith("D")) {
                ++doubleCount;
            }
        }
        return intCount == 6 && doubleCount == 4;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        ClassDef c = new ClassDef(this);
        cr.accept(cn, 0);

        c.addMethod("getScale", ((List<MethodNode>) cn.methods).get(cn.methods.size() - 1));

        return c;
    }
}
