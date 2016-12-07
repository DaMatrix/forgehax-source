/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.MethodNode
 */
package com.matt.forgehax.asm.helper;

import com.google.common.collect.Lists;
import com.matt.forgehax.asm.Names;
import com.matt.forgehax.asm.helper.AsmHelper;
import com.matt.forgehax.asm.helper.AsmMethod;
import java.util.List;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class ClassTransformer {
    protected final Names NAMES = Names.INSTANCE;
    public static final int CLASS_NAME_POS = 0;
    public static final int CLASS_HOOK_POS = 1;
    private List<String> errorLog = Lists.newArrayList("", "");
    public int methodCount = 0;
    public int foundMethods = 0;
    private boolean allMethodsPatched = true;

    public void setClassName(String realName, String runName) {
        this.errorLog.set(0, String.format("[ClassNames]: %s, %s\n", realName, runName));
        this.errorLog.set(1, String.format("%-100s | %-100s\n", "Targets", "Hooks"));
    }

    protected void registerHook(AsmMethod method) {
        ++this.methodCount;
        StringBuilder builder = new StringBuilder("");
        for (AsmMethod hook : method.getHooks()) {
            builder.append(hook.getName());
            builder.append(",");
        }
        this.errorLog.add(2, String.format("%-100s | %-100s\n", String.format("%s, %s :: %s", method.getName(), method.getObfuscatedName(), method.getDescriptor()), builder.toString()));
    }

    public void transform(ClassNode node) {
        for (MethodNode method : node.methods) {
            if (this.hasFoundAllMethods()) break;
            if (!this.onTransformMethod(method)) continue;
            ++this.foundMethods;
        }
    }

    protected void updatePatchedMethods(boolean result) {
        this.allMethodsPatched = this.allMethodsPatched && result;
    }

    public void markUnsuccessful() {
        this.allMethodsPatched = false;
    }

    public List<String> getErrorLog() {
        return this.errorLog;
    }

    public boolean hasFoundAllMethods() {
        return this.foundMethods == this.methodCount;
    }

    public boolean isSuccessful() {
        return this.allMethodsPatched && this.hasFoundAllMethods();
    }

    public AbstractInsnNode findPattern(String methodName, String nodeName, AbstractInsnNode start, int[] pattern, String mask) {
        AbstractInsnNode node = null;
        try {
            node = AsmHelper.findPattern(start, pattern, mask);
        }
        catch (Exception e) {
            this.log(methodName, nodeName + " error: %s\n", e.getMessage());
        }
        return node;
    }

    public /* varargs */ void log(String methodName, String text, Object ... args) {
        this.errorLog.add(String.format(String.format("[%s]: %s", methodName, text), args));
    }

    public abstract boolean onTransformMethod(MethodNode var1);
}

