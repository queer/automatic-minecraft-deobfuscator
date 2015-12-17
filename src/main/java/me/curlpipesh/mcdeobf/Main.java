package me.curlpipesh.mcdeobf;

import lombok.Getter;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.deobf.Version;
import me.curlpipesh.mcdeobf.deobf.versions.Version1_8_X;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author c
 * @since 8/3/15
 */
@SuppressWarnings("unused")
public class Main {
    /**
     * The singleton instance of Main. Guaranteed to never change.
     */
    private static final Main instance = new Main();

    @Getter
    private final Logger logger = Logger.getLogger("Deobfuscator");

    @Getter
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<Deobfuscator, Byte[]> dataToMap = new ConcurrentHashMap<>();

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "FieldCanBeLocal"})
    private final List<ClassDef> classDefs;
    
    private final Version version;

    private Main() {
        classDefs = new CopyOnWriteArrayList<>();
        version = new Version1_8_X();

        logger.setUseParentHandlers(false);
        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord logRecord) {
                System.out.println(String.format("[%s] [%s] %s", logRecord.getLoggerName(), logRecord.getLevel(),
                        logRecord.getMessage()));
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance of this class.
     */
    public static Main getInstance() {
        return instance;
    }

    // TODO: Actually use version...
    private void run(String jar, String gameVersion) {
        int successes = 0;
        int max = version.getDeobfuscators().size();
        try {
            JarFile jarFile = new JarFile(jar);
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements() && !version.getDeobfuscators().isEmpty()) {
                JarEntry entry = entries.nextElement();
                if(entry.getName().endsWith(".class")) {
                    try(InputStream is = jarFile.getInputStream(entry)) {
                        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                            byte[] buffer = new byte[0xFFFF];
                            int len;
                            while((len = is.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                            Deobfuscator d = deobfuscate(os.toByteArray());
                            if(d != null) {
                                logger.info("Deobfuscated class \"" + entry.getName().replaceAll(".class", "")
                                        + "\": " + d.getDeobfuscatedName());
                                ++successes;
                                d.setObfuscatedName(entry.getName().replaceAll(".class", ""));
                                byte[] osClone = os.toByteArray();
                                Byte[] bufferClone = new Byte[osClone.length];
                                // Oh FFS, System#arraycopy
                                for(int i = 0; i < osClone.length; i++) {
                                    bufferClone[i] = osClone[i];
                                }
                                dataToMap.put(d, bufferClone);
                            }
                        }
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        logger.info("Done! (" + successes + "/" + max + ")");
        //noinspection StatementWithEmptyBody
        if(successes < max) {
            logger.info("Had the following version.getDeobfuscators() remaining: ");
            version.getDeobfuscators().stream().forEach(d -> logger.info(d.getDeobfuscatedName()));
        } else if(successes > max) {
            logger.severe("Somehow had more successes than possible!?");
        } else {
            // TODO: Generate actual usable mappings
        }
    }

    private Deobfuscator deobfuscate(byte[] classBytes) {
        for(Deobfuscator d : version.getDeobfuscators()) {
            if(d.deobfuscate(classBytes)) {
                // This is supposed to stay in here. Please don't remove it ;-;
                //classDefs.add(d.getClassDefinition(classBytes));
                version.getDeobfuscators().remove(d);
                return d;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String jarPath = args[0];
        String version = args[1];
        // Yes, it'd be better to use args[0] here, but I don't feel like getting
        // that working with mvn right now
        instance.run(jarPath, version);
    }
}
