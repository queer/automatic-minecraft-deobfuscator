package me.curlpipesh.mcdeobf.deobf.net.minecraft.v1_9_X.entity.player;

import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import org.objectweb.asm.ClassReader;

/**
 * @author audrey
 * @since 12/12/15.
 */
public class EntityPlayerMP extends Deobfuscator {
    public EntityPlayerMP() {
        super("EntityPlayerMP");
    }

    @Override
    public boolean deobfuscate(final byte[] classData) {
        return dumpConstantPoolStrings(new ClassReader(classData)).contains("http://skins.minecraft.net/MinecraftSkins/%s.png");
    }

    @Override
    public ClassDef getClassDefinition(final byte[] classData) {
        return null;
    }
}
