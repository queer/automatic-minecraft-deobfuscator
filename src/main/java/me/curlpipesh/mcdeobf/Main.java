package me.curlpipesh.mcdeobf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.deobf.Version;
import me.curlpipesh.mcdeobf.deobf.versions.Version1_8_X;
import me.curlpipesh.mcdeobf.deobf.versions.Version1_9_X;
import me.curlpipesh.mcdeobf.json.ClassMap;

import java.io.*;
import java.util.*;
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
    
    private final Map<String, Version> versionMap;

    private Version version;

    private Main() {
        classDefs = new CopyOnWriteArrayList<>();

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

        versionMap = new HashMap<>();
        versionMap.put("1_8_X", new Version1_8_X());
        versionMap.put("1_9_X", new Version1_9_X());
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance of this class.
     */
    public static Main getInstance() {
        return instance;
    }

    private void run(String jar, String gameVersion) {
        version = versionMap.getOrDefault(gameVersion, null);
        if(version == null) {
            throw new IllegalArgumentException("'" + gameVersion + "' is not a valid version!");
        }

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
            logger.info("Generating JSON Mappings...");
            generateMappings();
        }
    }

    private void generateMappings() {
        List<ClassMap> classMaps = new ArrayList<>();

        dataToMap.forEach((deobf, bytes) -> {
            ClassDef classDef = deobf.getClassDefinition(moveBytes(bytes));

            Map<String, String> fields = classDef == null ? null : classDef.getFields();
            List<ClassDef.MethodDef> methods = classDef == null ? null : classDef.getMethods();

            ClassMap classMap = new ClassMap(
                    deobf.getDeobfuscatedName(), deobf.getObfuscatedName(), deobf.getObfuscatedDescription(),
                    fields, methods);

            classMaps.add(classMap);
        });

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // gson.toJson(classMaps, new FileWriter("mappings.json");
            // does not end the Json file correctly for some reason, so we write the file later below

            String json = gson.toJson(classMaps);

            File file = new File("mappings" + version.getVersionNumber() +  ".json");

            if(file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
                logger.warning("An older mappings.json was found, deleting it");
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
            logger.info("The Json mappings have been generated");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] moveBytes(Byte[] bytes) {
        byte[] newBytes = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) {
            newBytes[i] = bytes[i];
        }
        return newBytes;
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
