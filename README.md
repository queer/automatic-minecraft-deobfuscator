# Automatic Minecraft Deobfuscator

Little project that is intended to automatically deobfuscate classes in a Minecraft JAR file. The intent is that by doing this, it becomes unnecessary to wait on the people who make MCP/Forge.

The intent is that this will generate obfuscation mappings automatically for you, so that instrumenting the game's bytecode at runtime (or statically changing it) becomes easier so that you don't have to hunt for classes or wait for MCP/Forge.