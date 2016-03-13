package me.curlpipesh.mcdeobf.deobf;

import java.util.List;

/**
 * @author audrey
 * @since 12/14/15.
 */
public interface Version {
    String getVersionNumber();

    List<Deobfuscator> getDeobfuscators();

    Deobfuscator getDeobfuscatorByName(String name);

    Deobfuscator getDeobfuscator(Class clazz);
}
