/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldNode
 *  org.objectweb.asm.tree.MethodNode
 */
package com.matt.forgehax.asm.helper;

import com.matt.forgehax.asm.helper.AsmClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class AsmHelper {
    public static final Map<Class<?>, Class<?>> primitiveToWrapper = new HashMap();
    public static final Map<Class<?>, Class<?>> wrapperToPrimitive = new HashMap();
    public static final Map<Class<?>, Character> primitiveToDescriptor = new HashMap();

    public static void outputBytecode(AbstractInsnNode start) throws Exception {
        StringBuilder hexPattern = new StringBuilder();
        StringBuilder mask = new StringBuilder();
        StringBuilder deciPattern = new StringBuilder();
        AbstractInsnNode next = start;
        do {
            hexPattern.append("0x");
            if (next.getOpcode() != -1) {
                mask.append("x");
                hexPattern.append(Integer.toHexString(next.getOpcode()));
                deciPattern.append(String.format("%4d", next.getOpcode()));
            } else {
                mask.append("?");
                hexPattern.append("00");
                deciPattern.append("NULL");
            }
            hexPattern.append("\\");
            deciPattern.append(", ");
        } while ((next = next.getNext()) != null);
        throw new Exception("Pattern (base10): " + deciPattern.toString() + "\n" + "Pattern (base16): " + hexPattern.toString() + "\n" + "Mask: " + mask.toString() + "\n");
    }

    public static int[] convertPattern(String pattern) {
        if (pattern.startsWith("\u0000")) {
            pattern = pattern.substring(1);
        }
        String[] hex = pattern.split("\u0000");
        int[] buff = new int[hex.length];
        int index = 0;
        for (String number : hex) {
            if (number.startsWith("0x")) {
                number = number.substring(2);
            } else if (number.startsWith("x")) {
                number = number.substring(1);
            }
            buff[index++] = Integer.parseInt(number, 16);
        }
        return buff;
    }

    public static AbstractInsnNode findPattern(AbstractInsnNode start, int[] pattern, char[] mask) throws Exception {
        if (start != null && pattern.length == mask.length) {
            int found = 0;
            AbstractInsnNode next = start;
            do {
                int i;
                switch (mask[found]) {
                    case 'x': {
                        if (next.getOpcode() == pattern[found]) {
                            ++found;
                            break;
                        }
                        for (i = 1; i <= found - 1; ++i) {
                            next = next.getPrevious();
                        }
                        found = 0;
                        break;
                    }
                    default: {
                        ++found;
                    }
                }
                if (found < mask.length) continue;
                for (i = 1; i <= found - 1; ++i) {
                    next = next.getPrevious();
                }
                return next;
            } while ((next = next.getNext()) != null && found < mask.length);
        }
        throw new Exception("Failed to match pattern");
    }

    public static AbstractInsnNode findPattern(AbstractInsnNode start, String pattern, String mask) throws Exception {
        return AsmHelper.findPattern(start, AsmHelper.convertPattern(pattern), mask.toCharArray());
    }

    public static AbstractInsnNode findPattern(AbstractInsnNode start, int[] pattern, String mask) throws Exception {
        return AsmHelper.findPattern(start, pattern, mask.toCharArray());
    }

    public static AbstractInsnNode findStart(AbstractInsnNode start) {
        AbstractInsnNode next = start;
        do {
            if (next.getOpcode() == -1) continue;
            return next;
        } while ((next = next.getNext()) != null);
        return null;
    }

    public static String getClassData(ClassNode node) {
        StringBuilder builder = new StringBuilder("METHODS:\n");
        for (MethodNode method : node.methods) {
            builder.append("\t");
            builder.append(method.name);
            builder.append(method.desc);
            builder.append("\n");
        }
        builder.append("\nFIELDS:\n");
        for (FieldNode field : node.fields) {
            builder.append("\t");
            builder.append(field.desc);
            builder.append(" ");
            builder.append(field.name);
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String objectToDescriptor(Object obj) {
        if (obj instanceof String) {
            return (String)obj;
        }
        if (obj instanceof AsmClass) {
            AsmClass clazz = (AsmClass)obj;
            return String.format("L%s;", clazz.getRuntimeName());
        }
        if (obj instanceof Class) {
            return Type.getDescriptor((Class)((Class)obj));
        }
        return "";
    }

    static {
        primitiveToWrapper.put(Boolean.TYPE, Boolean.class);
        primitiveToWrapper.put(Byte.TYPE, Byte.class);
        primitiveToWrapper.put(Short.TYPE, Short.class);
        primitiveToWrapper.put(Character.TYPE, Character.class);
        primitiveToWrapper.put(Integer.TYPE, Integer.class);
        primitiveToWrapper.put(Long.TYPE, Long.class);
        primitiveToWrapper.put(Float.TYPE, Float.class);
        primitiveToWrapper.put(Double.TYPE, Double.class);
        primitiveToWrapper.put(Void.TYPE, Void.class);
        wrapperToPrimitive.put(Boolean.class, Boolean.TYPE);
        wrapperToPrimitive.put(Byte.class, Byte.TYPE);
        wrapperToPrimitive.put(Short.class, Short.TYPE);
        wrapperToPrimitive.put(Character.class, Character.TYPE);
        wrapperToPrimitive.put(Integer.class, Integer.TYPE);
        wrapperToPrimitive.put(Long.class, Long.TYPE);
        wrapperToPrimitive.put(Float.class, Float.TYPE);
        wrapperToPrimitive.put(Double.class, Double.TYPE);
        wrapperToPrimitive.put(Void.class, Void.TYPE);
        primitiveToDescriptor.put(Boolean.TYPE, Character.valueOf('Z'));
        primitiveToDescriptor.put(Byte.TYPE, Character.valueOf('B'));
        primitiveToDescriptor.put(Short.TYPE, Character.valueOf('S'));
        primitiveToDescriptor.put(Character.TYPE, Character.valueOf('C'));
        primitiveToDescriptor.put(Integer.TYPE, Character.valueOf('I'));
        primitiveToDescriptor.put(Long.TYPE, Character.valueOf('J'));
        primitiveToDescriptor.put(Float.TYPE, Character.valueOf('F'));
        primitiveToDescriptor.put(Double.TYPE, Character.valueOf('D'));
        primitiveToDescriptor.put(Void.TYPE, Character.valueOf('V'));
    }
}

