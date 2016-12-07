/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  net.minecraft.launchwrapper.IClassTransformer
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin$MCVersion
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin$SortingIndex
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.tree.ClassNode
 */
package com.matt.forgehax.asm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.matt.forgehax.asm.ForgeHaxCoreMod;
import com.matt.forgehax.asm.ForgeHaxHooks;
import com.matt.forgehax.asm.helper.ClassTransformer;
import com.matt.forgehax.asm.patches.BlockPatch;
import com.matt.forgehax.asm.patches.BlockRendererDispatcherPatch;
import com.matt.forgehax.asm.patches.EntityPatch;
import com.matt.forgehax.asm.patches.EntityPlayerSPPatch;
import com.matt.forgehax.asm.patches.EntityRendererPatch;
import com.matt.forgehax.asm.patches.NetManager$4Patch;
import com.matt.forgehax.asm.patches.NetManagerPatch;
import com.matt.forgehax.asm.patches.RenderGlobalPatch;
import com.matt.forgehax.asm.patches.VertexBufferPatch;
import com.matt.forgehax.asm.patches.VisGraphPatch;
import com.matt.forgehax.asm.patches.WorldPatch;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

@IFMLLoadingPlugin.MCVersion(value="1.10.2")
@IFMLLoadingPlugin.SortingIndex(value=1001)
public class ForgeHaxTransformer
implements IClassTransformer {
    private Map<String, ClassTransformer> transformingClasses = Maps.newHashMap();

    public ForgeHaxTransformer() {
        //this.transformingClasses.put("net.minecraft.world.World", new WorldPatch());
        //this.transformingClasses.put("net.minecraft.entity.Entity", new EntityPatch());
        //this.transformingClasses.put("net.minecraft.client.renderer.RenderGlobal", new RenderGlobalPatch());
        //this.transformingClasses.put("net.minecraft.block.Block", new BlockPatch());
        //this.transformingClasses.put("net.minecraft.client.renderer.VertexBuffer", new VertexBufferPatch());
        //this.transformingClasses.put("net.minecraft.client.renderer.EntityRenderer", new EntityRendererPatch());
        //this.transformingClasses.put("net.minecraft.network.NetworkManager", new NetManagerPatch());
        //this.transformingClasses.put("net.minecraft.network.NetworkManager$4", new NetManager$4Patch());
        //this.transformingClasses.put("net.minecraft.client.renderer.chunk.VisGraph", new VisGraphPatch());
        //this.transformingClasses.put("net.minecraft.client.entity.EntityPlayerSP", new EntityPlayerSPPatch());
        //this.transformingClasses.put("net.minecraft.client.renderer.BlockRendererDispatcher", new BlockRendererDispatcherPatch());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public byte[] transform(String name, String realName, byte[] bytes) {
        byte[] arrby;
        block9 : {
            if (!this.transformingClasses.containsKey(realName)) return bytes;
            ArrayList log = Lists.newArrayList();
            ClassTransformer transformer = this.transformingClasses.get(realName);
            try {
                ForgeHaxCoreMod.print("Transforming class %s (%s)\n", realName, name);
                ClassNode classNode = new ClassNode();
                ClassReader classReader = new ClassReader(bytes);
                classReader.accept((ClassVisitor)classNode, 0);
                transformer.setClassName(realName, name);
                transformer.transform(classNode);
                ClassWriter classWriter = new ClassWriter(3);
                classNode.accept((ClassVisitor)classWriter);
                ForgeHaxHooks.setHooksLog(realName, transformer.getErrorLog(), transformer.methodCount);
                this.transformingClasses.remove(realName);
                arrby = classWriter.toByteArray();
                if (transformer.isSuccessful()) break block9;
                log.add("####################\n");
            }
            catch (Exception e) {
                block10 : {
                    try {
                        transformer.markUnsuccessful();
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        transformer.log("TransformException", "%s\n", sw.toString());
                        if (transformer.isSuccessful()) break block10;
                        log.add("####################\n");
                    }
                    catch (Throwable var12_16) {
                        if (!transformer.isSuccessful()) {
                            log.add("####################\n");
                            log.add(String.format("ERROR TRANSFORMING CLASS '%s'\n", realName));
                            Object[] arrobject = new Object[1];
                            arrobject[0] = transformer.hasFoundAllMethods() ? "true" : "false";
                            log.add(String.format("hasFoundAllMethods=%s\n", arrobject));
                            log.add(String.format("Found %d out of %d methods\n", transformer.foundMethods, transformer.methodCount));
                            log.add("Error log:");
                            for (String msg : transformer.getErrorLog()) {
                                log.add("\t" + msg);
                            }
                            log.add("\n");
                            log.add("####################\n");
                            ForgeHaxCoreMod.print(log);
                            try {
								throw var12_16;
							} catch (Throwable e1) {
								e1.printStackTrace();
							}
                        } else {
                            ForgeHaxCoreMod.print("Successfully transformed class '%s'", realName);
                        }
                        try {
							throw var12_16;
						} catch (Throwable e1) {
							e1.printStackTrace();
						}
                    }
                    log.add(String.format("ERROR TRANSFORMING CLASS '%s'\n", realName));
                    Object[] arrobject = new Object[1];
                    arrobject[0] = transformer.hasFoundAllMethods() ? "true" : "false";
                    log.add(String.format("hasFoundAllMethods=%s\n", arrobject));
                    log.add(String.format("Found %d out of %d methods\n", transformer.foundMethods, transformer.methodCount));
                    log.add("Error log:");
                    for (String msg : transformer.getErrorLog()) {
                        log.add("\t" + msg);
                    }
                    log.add("\n");
                    log.add("####################\n");
                    ForgeHaxCoreMod.print(log);
                    return bytes;
                }
                ForgeHaxCoreMod.print("Successfully transformed class '%s'", realName);
                return bytes;
            }
            log.add(String.format("ERROR TRANSFORMING CLASS '%s'\n", realName));
            Object[] arrobject = new Object[1];
            arrobject[0] = transformer.hasFoundAllMethods() ? "true" : "false";
            log.add(String.format("hasFoundAllMethods=%s\n", arrobject));
            log.add(String.format("Found %d out of %d methods\n", transformer.foundMethods, transformer.methodCount));
            log.add("Error log:");
            for (String msg : transformer.getErrorLog()) {
                log.add("\t" + msg);
            }
            log.add("\n");
            log.add("####################\n");
            ForgeHaxCoreMod.print(log);
            return arrby;
        }
        ForgeHaxCoreMod.print("Successfully transformed class '%s'", realName);
        return arrby;
    }
}

