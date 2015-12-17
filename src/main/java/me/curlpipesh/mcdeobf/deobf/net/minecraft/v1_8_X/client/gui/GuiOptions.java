package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class GuiOptions extends Deobfuscator {
    public GuiOptions() {
        super("GuiOptions");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("options.title") && constantPool.contains("options.stream")
                && constantPool.contains("options.video");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
