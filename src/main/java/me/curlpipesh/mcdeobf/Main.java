package me.curlpipesh.mcdeobf;

import lombok.Getter;
import me.curlpipesh.mcdeobf.deobf.ClassDef;
import me.curlpipesh.mcdeobf.deobf.Deobfuscator;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.client.GameSettings;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.client.Minecraft;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.client.gui.FontRenderer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.client.gui.GuiIngame;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.client.renderer.EntityRenderer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.entity.Entity;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.entity.EntityClientPlayer;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.world.AbstractWorld;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.world.World;
import me.curlpipesh.mcdeobf.deobf.net.minecraft.world.WorldProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<Deobfuscator, Byte[]> dataToMap = new ConcurrentHashMap<>();

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<ClassDef> classDefs;

    private Main() {
        deobfuscators = new CopyOnWriteArrayList<>();
        classDefs = new CopyOnWriteArrayList<>();
        add(
                new Minecraft(),
                new FontRenderer(),
                new EntityRenderer(),
                new GuiIngame(),
                new GameSettings(),
                new Entity(),
                new EntityClientPlayer(),
                new AbstractWorld(),
                new World(),
                new WorldProvider()
        );
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
                                System.out.println("Deobfuscated class \"" + entry.getName().replaceAll(".class", "")
                                        + "\": " + d.getDeobfuscatedName());
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
    }

    private Deobfuscator deobfuscate(byte[] classBytes) {
        for(Deobfuscator d : deobfuscators) {
            if(d.deobfuscate(classBytes)) {
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
