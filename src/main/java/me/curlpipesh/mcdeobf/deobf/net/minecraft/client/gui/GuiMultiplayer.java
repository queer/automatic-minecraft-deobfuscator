package me.curlpipesh.mcdeobf.deobf.net.minecraft.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/25/15.
 */
public class GuiMultiplayer extends Deobfuscator {
    public GuiMultiplayer() {
        super("GuiMultiplayer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("selectServer.edit") && constantPool.contains("selectServer.delete")
                && constantPool.contains("selectServer.select");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
