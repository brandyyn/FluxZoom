package flux.zoom.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public class ModelExplorersScope extends ModelBase {

    private static final float TEXTURE_SIZE = 16.0F;

    public ModelExplorersScope() {
        this.textureWidth = 16;
        this.textureHeight = 16;
    }

    public void render(float size) {
        boolean cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_CULL_FACE);

        renderCuboid(-1.0F, 0.5F, -1.0F, 1.0F, 5.5F, 1.0F, new float[] { 0, 2, 2, 7 },
                new float[] { 0, 2, 2, 7 }, new float[] { 0, 2, 2, 7 }, new float[] { 0, 2, 2, 7 },
                new float[] { 0, 0, 2, 2 }, null, size);
        renderCuboid(-1.1F, -5.6F, -1.1F, 1.1F, 0.6F, 1.1F, new float[] { 0, 7, 2, 13 },
                new float[] { 0, 7, 2, 13 }, new float[] { 0, 7, 2, 13 }, new float[] { 0, 7, 2, 13 },
                new float[] { 0, 5, 2, 7 }, new float[] { 0, 13, 2, 15 }, size);

        if (cullFace) {
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }

    private static void renderCuboid(float x1, float y1, float z1, float x2, float y2, float z2, float[] north,
            float[] east, float[] south, float[] west, float[] up, float[] down, float size) {
        x1 *= size;
        y1 *= size;
        z1 *= size;
        x2 *= size;
        y2 *= size;
        z2 *= size;

        if (north != null) drawFace(x1, y2, z1, x2, y2, z1, x2, y1, z1, x1, y1, z1, north, 0.0F, 0.0F, -1.0F);
        if (east != null) drawFace(x2, y2, z1, x2, y2, z2, x2, y1, z2, x2, y1, z1, east, 1.0F, 0.0F, 0.0F);
        if (south != null) drawFace(x2, y2, z2, x1, y2, z2, x1, y1, z2, x2, y1, z2, south, 0.0F, 0.0F, 1.0F);
        if (west != null) drawFace(x1, y2, z2, x1, y2, z1, x1, y1, z1, x1, y1, z2, west, -1.0F, 0.0F, 0.0F);
        if (up != null) drawFace(x1, y2, z2, x2, y2, z2, x2, y2, z1, x1, y2, z1, up, 0.0F, 1.0F, 0.0F);
        if (down != null) drawFace(x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, down, 0.0F, -1.0F, 0.0F);
    }

    private static void drawFace(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3,
            float z3, float x4, float y4, float z4, float[] uv, float normalX, float normalY, float normalZ) {
        float u1 = uv[0] / TEXTURE_SIZE;
        float v1 = uv[1] / TEXTURE_SIZE;
        float u2 = uv[2] / TEXTURE_SIZE;
        float v2 = uv[3] / TEXTURE_SIZE;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(normalX, normalY, normalZ);
        tessellator.addVertexWithUV(x1, y1, z1, u1, v1);
        tessellator.addVertexWithUV(x2, y2, z2, u2, v1);
        tessellator.addVertexWithUV(x3, y3, z3, u2, v2);
        tessellator.addVertexWithUV(x4, y4, z4, u1, v2);
        tessellator.draw();
    }
}
