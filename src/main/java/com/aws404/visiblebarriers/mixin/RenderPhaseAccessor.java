package com.aws404.visiblebarriers.mixin;

import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPhase.class)
public interface RenderPhaseAccessor {
    @Accessor("VIEW_OFFSET_Z_LAYERING")
    static RenderPhase.Layering getProjectionLayering() {
        return null;
    }

    @Accessor("TRANSLUCENT_TRANSPARENCY")
    static RenderPhase.Transparency getTranslucentTransparency() {
        return null;
    }

    @Accessor("COLOR_MASK")
    static RenderPhase.WriteMaskState getColorMask() {
        return null;
    }

    @Accessor("DISABLE_CULLING")
    static RenderPhase.Cull disableCulling() {
        return null;
    }

    @Accessor("ALWAYS_DEPTH_TEST")
    static RenderPhase.DepthTest alwaysDepthTest() {
        return null;
    }

    @Accessor("ONE_TENTH_ALPHA")
    static RenderPhase.Alpha oneTenthAlpha() {
        return null;
    }

    @Accessor("NO_FOG")
    static RenderPhase.Fog noFog() {
        return null;
    }

    @Accessor("OUTLINE_TARGET")
    static RenderPhase.Target outlineTarget() {
        return null;
    }

    @Accessor("ENABLE_DIFFUSE_LIGHTING")
    static RenderPhase.DiffuseLighting enableDiffuseLighting() {
        return null;
    }

    @Accessor("ENABLE_OVERLAY_COLOR")
    static RenderPhase.Overlay enableOverlayColoring() {
        return null;
    }

}
