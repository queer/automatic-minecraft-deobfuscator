package me.curlpipesh.mcdeobf.deobf.net.minecraft.client.gui;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public class FontRenderer extends Deobfuscator {
    public FontRenderer() {
        super("FontRenderer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> c = dumpConstantPoolStrings(new ClassReader(classData));
        return c.contains("0123456789abcdefklmknor") || c.contains("0123456789abcdef");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
