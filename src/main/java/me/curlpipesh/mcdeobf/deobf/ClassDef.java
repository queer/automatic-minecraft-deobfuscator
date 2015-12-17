package me.curlpipesh.mcdeobf.deobf;

import lombok.Data;
import me.curlpipesh.mcdeobf.Main;
import org.objectweb.asm.tree.MethodNode;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author c
 * @since 8/3/15
 */
@Data
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
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
    //private final Map<String, String> methods = new LinkedHashMap<>();
    private final List<MethodDef> methods = new LinkedList<>();

    public ClassDef(Deobfuscator d) {
        deobfuscatedName = d.getDeobfuscatedName();
        obfuscatedName = d.getObfuscatedName();
    }

    public void addField(String deobfuscatedName, String obfuscatedName) {
        fields.put(deobfuscatedName, obfuscatedName);
    }

    public void addMethod(String deobfuscatedName, MethodNode m) {
        //methods.put(deobfuscatedName, obfuscatedName);
        if(methods.stream().filter(d -> d.deobfName.equals(deobfuscatedName) && d.desc.equals(m.desc)).count() > 0) {
            Main.getInstance().getLogger().warning("Ignoring duplicated method definition for " + deobfuscatedName + ": " + m.name + m.desc);
            return;
        }
        methods.add(new MethodDef(m.name, deobfuscatedName, m.desc));
    }

    @Data
    public static class MethodDef {
        private String name;
        private String deobfName;
        private String desc;

        public MethodDef(String name, String deobfName, String desc) {
            this.name = name;
            this.deobfName = deobfName;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return deobfName + " " + "[" + name + desc + "]";
        }
    }
}
