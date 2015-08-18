package me.curlpipesh.mcdeobf.deobf.net.minecraft.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public class GuiIngame extends Deobfuscator {
    public GuiIngame() {
        super("GuiIngame");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("overlayMessage") && c.contains("bossHealth");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
