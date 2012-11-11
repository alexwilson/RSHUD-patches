package net.minecraft.src;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.glu.GLU;

public class GRV_RenderMARKER extends Render {
	
	// ïœä∑çsóÒ
	public static FloatBuffer matModel = BufferUtils.createFloatBuffer(16);
	public static FloatBuffer matProjection = BufferUtils.createFloatBuffer(16);
	public static IntBuffer matViewport = BufferUtils.createIntBuffer(16);

	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderRoute((GRV_EntityMARKER)entity, d, d1, d2, f, f1);
	}

	
	public void renderRoute(GRV_EntityMARKER entity, double d, double d1, double d2, float f, float f1) {
		if (entity.playerEntity == null) return;
		
		Tessellator tessellator = Tessellator.instance;
		entity.markerPos[0] = null;
		entity.markerPos[1] = null;
		entity.markerPos[2] = null;
		
		// âÊñ è„ÇÃà íuÇãÅÇﬂÇÈ
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, matModel);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, matProjection);
		GL11.glGetInteger(GL11.GL_VIEWPORT, matViewport);
		GLU.gluProject(0F, 0F - entity.playerEntity.yOffset + 0.7F, 0F, 
				matModel, matProjection, matViewport, entity.playerPos);
		for (int i = 0; (i < 3) && (entity.routePos[i] != null); i++) {
			//
			FloatBuffer posMarker = BufferUtils.createFloatBuffer(3);
			GLU.gluProject(	(float)(entity.routePos[i][0] - entity.posX), 
							(float)(entity.routePos[i][1] - entity.posY), 
							(float)(entity.routePos[i][2] - entity.posZ), 
					matModel, matProjection, matViewport, posMarker);
//	        System.out.println(String.format("%d, %d, %d, %d", matViewport.get(0), matViewport.get(1), matViewport.get(2), matViewport.get(3)));
			entity.markerPos[i] = posMarker;
		}
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entity, double d, double d1, double d2, float f, float f1) {
	}
}
