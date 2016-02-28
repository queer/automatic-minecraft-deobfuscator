package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.util;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * Created by TehNeon on 2016-02-27.
 */
public class AxisAlignedBB extends Deobfuscator {
    public AxisAlignedBB() {
        super("AxisAlignedBB");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("box[");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassDef def = new ClassDef(this);

        def.addField("minX", ((List<FieldNode>) cn.fields).get(0).name);
        def.addField("minY", ((List<FieldNode>) cn.fields).get(1).name);
        def.addField("minZ", ((List<FieldNode>) cn.fields).get(2).name);
        def.addField("maxX", ((List<FieldNode>) cn.fields).get(3).name);
        def.addField("maxY", ((List<FieldNode>) cn.fields).get(4).name);
        def.addField("maxZ", ((List<FieldNode>) cn.fields).get(5).name);


        int methodsFound = 0;
        for (MethodNode m : (List<MethodNode>) cn.methods) {
            if (AccessHelper.isPublic(m.access)) {
                methodsFound++;

                // TODO: Possibly map out methods: offset
                // System.out.println(m.name + " - " + m.desc + " - " + methodsFound);

                switch (methodsFound) {
                    case 8:
                        def.addMethod("expand", m);
                        break;
                    case 10:
                        def.addMethod("union", m);
                        break;
                }
            }
        }

        return def;
    }
}
