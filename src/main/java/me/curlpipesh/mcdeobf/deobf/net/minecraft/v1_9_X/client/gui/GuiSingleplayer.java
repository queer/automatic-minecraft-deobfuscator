package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class GuiSingleplayer extends Deobfuscator {
    public GuiSingleplayer() {
        super("GuiSingleplayer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("selectWorld.title") && constantPool.contains("selectWorld.select")
                && constantPool.contains("selectWorld.delete");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
