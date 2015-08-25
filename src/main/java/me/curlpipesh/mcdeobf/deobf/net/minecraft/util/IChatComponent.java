package me.curlpipesh.mcdeobf.deobf.net.minecraft.util;


import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

/* Created on 8/19/2015 @ 3:44 AM */
public class IChatComponent extends Deobfuscator {

    public IChatComponent(){
        super("IChatComponent");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        return cn.innerClasses.size() == 1 &&
                (cn.signature != null && cn.signature.startsWith("Ljava/lang/Object;Ljava/lang/Iterable<L"));
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        ClassReader cr = new ClassReader(classData);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        ClassDef c = new ClassDef(cn.name, getDeobfuscatedName());

        return c;
    }
}
