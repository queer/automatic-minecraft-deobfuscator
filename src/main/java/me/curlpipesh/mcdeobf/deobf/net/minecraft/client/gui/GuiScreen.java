package me.curlpipesh.mcdeobf.deobf.net.minecraft.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

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
        return constantPool.contains("java.awt.Desktop") && constantPool.contains("getDesktop")
                && constantPool.contains("Couldn't open link")
                && constantPool.contains("Tried to handle twitch user but couldn't find them!");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
