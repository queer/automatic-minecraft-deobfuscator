package me.curlpipesh.mcdeobf.deobf.net.minecraft.entity.player;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

import java.util.List;

/**
 * @author audrey
 * @since 8/24/15.
 */
public class EntityPlayer extends Deobfuscator {
    public EntityPlayer() {
        super("EntityPlayer");
    }

    @Override
    public boolean deobfuscate(byte[] classData) {
        List<String> constantPool = dumpConstantPoolStrings(new ClassReader(classData));
        return constantPool.contains("game.player.swim") && constantPool.contains("game.player.swim.splash")
                && constantPool.contains("naturalRegeneration");
    }

    @Override
    public ClassDef getClassDefinition(byte[] classData) {
        return null;
    }
}
