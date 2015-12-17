package me.curlpipesh.mcdeobf.json;


import lombok.RequiredArgsConstructor;
import me.curlpipesh.mcdeobf.deobf.ClassDef;

import java.util.List;
import java.util.Map;

/* Created on 12/17/2015 @ 1:44 PM */
@RequiredArgsConstructor
public class ClassMap {

    private final String deobfuscatedName;
    private final String obfuscatedName;
    private final String description;

    private final Map<String, String> fields;
    private final List<ClassDef.MethodDef> methods;

}
