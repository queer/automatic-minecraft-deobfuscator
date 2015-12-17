package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_8_X.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class Gui extends Deobfuscator {
    public Gui() {
        super("Gui");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("textures/gui/options_background.png")
                && constantPool.contains("textures/gui/container/stats_icons.png")
                && constantPool.contains("textures/gui/icons.png");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
