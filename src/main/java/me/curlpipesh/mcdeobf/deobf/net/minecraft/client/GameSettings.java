package me.curlpipesh.mcdeobf.deobf.net.minecraft.client;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

public class GameSettings extends Deobfuscator {
    public GameSettings() {
        super("GameSettings");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        // Checks for >5 because there are a LOT of these in the GameSettings class...
        return dumpConstantPoolStrings(new ClassReader(classData)).stream()
                .filter(s -> s.contains("key.categories.")).count() > 5;
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
