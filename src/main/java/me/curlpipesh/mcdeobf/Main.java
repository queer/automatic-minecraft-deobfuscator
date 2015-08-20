package me.curlpipesh.mcdeobf;

import lombok.Getter;
import me.curlpipesh.mcdeobf.deobf.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.client.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.client.gui.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.client.renderer.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.entity.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.util.*;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.world.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.jar.*;
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
    private final List<Deobfuscator> deobfuscators;

    @Getter
    private final Logger logger = Logger.getLogger("Deobfuscator");

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<Deobfuscator, Byte[]> dataToMap = new ConcurrentHashMap<>();

    @SuppressWarnings( {"MismatchedQueryAndUpdateOfCollection", "FieldCanBeLocal"})
    private final List<ClassDef> classDefs;

    private Main() {
        deobfuscators = new CopyOnWriteArrayList<>();
        classDefs = new CopyOnWriteArrayList<>();
        add(
                new FontRenderer(),
                new GuiIngame(),
                new EntityRenderer(),
                new GameSettings(),
                new Minecraft(),
                new Entity(),
                new EntityClientPlayer(),
                new ChatComponentText(),
                new IChatComponent(),
                new ScaledResolution(),
                new AbstractWorld(),
                new World(),
                new WorldProvider()
        );
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

    private void add(Deobfuscator... d) {
        Arrays.stream(d).forEach(deobfuscators::add);
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance of this class.
     */
    public static Main getInstance() {
        return instance;
    }

    private void run(String jar) {
        int successes = 0;
        int max = deobfuscators.size();
        try {
            JarFile jarFile = new JarFile(jar);
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()) {
                if(deobfuscators.size() == 0) {
                    break;
                }
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
                                Byte[] bufferClone = new Byte[buffer.length];
                                // Oh FFS, System#arraycopy
                                for(int i = 0; i < buffer.length; i++) {
                                    bufferClone[i] = buffer[i];
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
    }

    private Deobfuscator deobfuscate(byte[] classBytes) {
        for(Deobfuscator d : deobfuscators) {
            if(d.deobfuscate(classBytes)) {
                // This is supposed to stay in here. Please don't remove it ;-;
                //classDefs.add(d.getClassDefinition(classBytes));
                deobfuscators.remove(d);
                return d;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        final String minecraftJarPath = "/ssd/.minecraft/versions/1.8.8/1.8.8.jar";
        instance.run(minecraftJarPath);
    }
}
