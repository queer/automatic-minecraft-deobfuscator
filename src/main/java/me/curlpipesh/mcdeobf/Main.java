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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Handler;
import java.util.logging.Level;
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
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "FieldCanBeLocal"})
    private final List<ClassDef> classDefs;
    private final Map<String, Version> versionMap;
    @Getter
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<Deobfuscator, byte[]> dataToMap = new ConcurrentHashMap<>();
    private Version version;

    private long startTime;
    private long endTime;

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

    public static void main(String[] args) {
        String jarPath = args[0];
        String version = args[1];
        // Yes, it'd be better to use args[0] here, but I don't feel like getting
        // that working with mvn right now
        instance.logger.setLevel(Level.CONFIG);
        instance.startTime = System.currentTimeMillis();
        instance.logger.config("Starting to deobfuscate at " + instance.startTime + "ms"); // TODO: Change this to a date format
        instance.run(jarPath, version);
        instance.endTime = System.currentTimeMillis();
        instance.logger.config("Ended deobfuscation at " + instance.endTime + "ms taking " + (instance.endTime - instance.startTime) + "ms"); // TODO: Change this to a date format
    }

    private void run(String jar, String gameVersion) {
        version = versionMap.getOrDefault(gameVersion, null);
        if(version == null) {
            throw new IllegalArgumentException("'" + gameVersion + "' is not a valid version!");
        }

        AtomicInteger successes = new AtomicInteger(0);
        int max = version.getDeobfuscators().size();
        Map<String, byte[]> bytes = new ConcurrentHashMap<>();

        long a = System.currentTimeMillis();
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
                            bytes.put(entry.getName(), os.toByteArray());
                        }
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        logger.config("JAR load: " + (System.currentTimeMillis() - a) + "ms");


        a = System.currentTimeMillis();
        bytes.entrySet().parallelStream().forEach(entry -> {
            String name = entry.getKey();
            byte[] e = entry.getValue();
            Deobfuscator d = deobfuscate(e);
            if(d != null) {
                logger.info("Deobfuscated class \"" + name.replaceAll(".class", "")
                        + "\": " + d.getDeobfuscatedName());
                successes.incrementAndGet();
                d.setObfuscatedName(name.replaceAll(".class", ""));
                dataToMap.put(d, e);
            }
        });
        logger.config("Deobfuscate: " + (System.currentTimeMillis() - a) + "ms");
        logger.info("Done! (" + successes + "/" + max + ")");
        //noinspection StatementWithEmptyBody
        if(successes.get() < max) {
            logger.info("Had the following version.getDeobfuscators() remaining: ");
            version.getDeobfuscators().stream().forEach(d -> logger.info(d.getDeobfuscatedName()));
        } else if(successes.get() > max) {
            logger.severe("Somehow had more successes than possible!?");
        } else {
            logger.info("Generating JSON Mappings...");
            a = System.currentTimeMillis();
            generateMappings();
            logger.config("Mappings: " + (System.currentTimeMillis() - a) + "ms");
        }
    }

    private void generateMappings() {
        List<ClassMap> classMaps = new ArrayList<>();

        dataToMap.forEach((deobf, bytes) -> {
            ClassDef classDef = deobf.getClassDefinition(bytes);

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

            File file = new File("mappings" + version.getVersionNumber() + ".json");

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
}
