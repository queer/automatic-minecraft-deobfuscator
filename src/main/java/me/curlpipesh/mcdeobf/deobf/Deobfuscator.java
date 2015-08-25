package me.curlpipesh.mcdeobf.deobf;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.ClassReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author c
 * @since 8/3/15
 */
public abstract class Deobfuscator {
    @Getter
    private final String deobfuscatedName;

    @Getter
    @Setter
    private String obfuscatedName = "";

    public Deobfuscator(String deobfName) {
        deobfuscatedName = deobfName;
    }

    /**
     * Attempts to deobfuscate the given class data to see if it's actually the
     * class that this deobfuscator matches. If the class actually does match,
     * then {@link #getClassDefinition(byte[])} will be called on the same set
     * of bytes in order to actually get the obfuscation mappings.
     *
     * @param classData A byte array representing the class to deobfuscate
     * @return <tt>true</tt> if the class is a match, <tt>false</tt> otherwise
     */
    public abstract boolean deobfuscate(byte[] classData);

    /**
     * Returns a {@link ClassDef} object that holds the derived obfuscation
     * mappings for this class. Must be called if and <b>only</b> if
     * {@link #deobfuscate(byte[])} returned <tt>true</tt>.
     *
     * @param classData A byte array representing the class to map
     * @return A <tt>ClassDef</tt> object that contains the obfuscation
     *         mappings
     */
    public abstract ClassDef getClassDefinition(byte[] classData);

    public final String getObfuscatedDescription() {
        return "L" + obfuscatedName + ";";
    }

    protected final List<String> dumpConstantPoolStrings(ClassReader cr) {
        List<String> constantPoolStrings = new ArrayList<>();
        int len = cr.getItemCount();
        for(int i = 0; i < len; i++) {
            try {
                char[] buffer = new char[0xFFFF];
                Object o = cr.readConst(i, buffer);
                if(o instanceof String) {
                    constantPoolStrings.add((String) o);
                }
            } catch(Exception e) {
                if(!(e instanceof ArrayIndexOutOfBoundsException)) {
                    e.printStackTrace();
                }
            }
        }
        return constantPoolStrings;
    }
}
