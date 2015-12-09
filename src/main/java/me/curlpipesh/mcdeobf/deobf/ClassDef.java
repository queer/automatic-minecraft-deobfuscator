package me.curlpipesh.mcdeobf.deobf;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author c
 * @since 8/3/15
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Data
public class ClassDef {
    private final String deobfuscatedName;
    private final String obfuscatedName;
    /**
     * List of deobfuscated fields. The key is the deobfuscated name, and the
     * value is the obfuscated name.
     */
    private final Map<String, String> fields = new LinkedHashMap<>();
    /**
     * List of deobfuscated methods. The key is the deobfuscated name, and the
     * value is the obfuscated name.
     */
    private final Map<String, String> methods = new LinkedHashMap<>();

    public ClassDef(Deobfuscator d) {
        deobfuscatedName = d.getDeobfuscatedName();
        obfuscatedName = d.getObfuscatedName();
    }

    public void addField(String deobfuscatedName, String obfuscatedName) {
        fields.put(deobfuscatedName, obfuscatedName);
    }

    public void addMethod(String deobfuscatedName, String obfuscatedName) {
        methods.put(deobfuscatedName, obfuscatedName);
    }
}
