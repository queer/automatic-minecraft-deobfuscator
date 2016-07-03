package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_10_X.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.util.AccessHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class GuiMainMenu extends Deobfuscator {
    public GuiMainMenu() {
        super("GuiMainMenu");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        final List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("missingno") && constantPool.contains("menu.options")
                && constantPool.contains("menu.quit") && constantPool.contains("menu.singleplayer");
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassDef getClassDefinition(final byte[] classData) {
        final ClassDef def = new ClassDef(this);
        final ClassReader cr = new ClassReader(classData);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        // TODO: Don't care because inherited from GuiScreen?
        ((List<MethodNode>) cn.methods).stream()
                .filter(m -> AccessHelper.isVoid(m.desc) && AccessHelper.isPublic(m.access) && m.desc.equals("(IIF)V"))
                .forEach(m -> def.addMethod("drawScreen", m));
        return def;
    }
}
