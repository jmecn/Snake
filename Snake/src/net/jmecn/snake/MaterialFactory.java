package net.jmecn.snake;

import org.apache.log4j.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.texture.Texture;

/**
 * 材质工厂，对游戏中所需使用的各种材质进行缓存，避免重复创建材质。
 * @author yanmaoyuan
 *
 */
public class MaterialFactory {
	
	static Logger log = Logger.getLogger(MaterialFactory.class);

	private AssetManager assetManager;
	
	public MaterialFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
		
		initFoodMaterials();
	}
	
	protected Material getUnshadedMaterial() {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		mat.setFloat("AlphaDiscardThreshold", 0.01f);
		return mat;
	}
	
	/**
	 * 食物的材质，公用一个纹理，只有七种不同的颜色。
	 */
	private Material[] foodMats;
	protected void initFoodMaterials() {
		foodMats = new Material[7];
		
		Texture texCircle = assetManager.loadTexture("Interface/circle.png");
		ColorRGBA[] colors = new ColorRGBA[] {
				ColorRGBA.Red,
				ColorRGBA.Blue,
				ColorRGBA.Brown,
				ColorRGBA.Pink,
				ColorRGBA.Green,
				ColorRGBA.Cyan,
				ColorRGBA.Orange,
		};
		
		for(int i=0; i<7; i++) {
			foodMats[i] = getUnshadedMaterial();
			foodMats[i].setTexture("ColorMap", texCircle);
			foodMats[i].setColor("Color", colors[i]);
		}
	}
	
	/**
	 * 随机获得7种材质中的一个
	 * @return
	 */
	public Material getRandomFoodMaterail() {
		int i = FastMath.rand.nextInt(7);
		return foodMats[i];
	}

	/**
	 * 蛇的材质。每条蛇的皮肤有这些组成部分：
	 * 活蛇：蛇头、蛇身、蛇尾
	 * 死蛇：蛇身
	 * 蛇身可以只有一种材质，也可能是规律变化的材质。比如“蓝-白-蓝-白-..”或者"黄-黑-白-黄-黑-白.."。
	 * 根据蛇身材质的不同，蛇死后留下的遗体会呈现不同的材质。
	 */
	static String TEXTURE_PATH = "Interface/Skin/skin_%s.png";
	static String HEAD_NAME = "%d_head";
	static String BODY_NAME = "%d_body%s";
	static String WRECK_NAME = "%d_wreck%s";
	static String TAIL_NAME = "%d_tail";
	
	/**
	 * 缓存不同皮肤、不同部位的材质。
	 */
	int capacity = 50;
	int length = 0;
	private String[] skinKeys = new String[capacity];
	private Material[] skinMats = new Material[capacity];
	
	protected Material getSkinMaterial(String name) {
		// 先在缓存中查找
		for( int i=0; i<length; i++) {
			if (skinKeys[i].equals(name)) {
				return skinMats[i];
			}
		}
		
		// 找不到，创建一个材质，并进行缓存。
		String res = String.format(TEXTURE_PATH, name);
		Material mat = getUnshadedMaterial();
		mat.setTexture("ColorMap", assetManager.loadTexture(res));
		
		log.info("创建材质: " + res);
		
		// 存入缓存
		skinKeys[length] = name;
		skinMats[length] = mat;
		length++;
		
		return mat;
	}
	
	public Material getHeadMat(Skin skin) {
		// 身体
		return getSkinMaterial(getHead(skin));
	}
	
	/**
	 * 身体的材质
	 * @param skin
	 * @param index
	 * @return
	 */
	public Material getBodyMat(Skin skin, int index) {
		if (skin.body > 1) {
			return getSkinMaterial(getBody(skin, index));
		} else {
			return getSkinMaterial(getBody(skin, 0));
		}
	}
	
	/**
	 * 残骸的材质
	 * @param skin
	 * @param index
	 * @return
	 */
	public Material getWreckMat(Skin skin, int index) {
		if (skin.wreck <= 0) {
			// 没有残骸?
			return null;
		} else if (skin.wreck == 1) {
			return getSkinMaterial(getWreck(skin, 0));
		} else {
			return getSkinMaterial(getWreck(skin, index));
		}
	}

	/**
	 * 尾巴的材质
	 * @param skin
	 * @return
	 */
	public Material getTailMat(Skin skin) {
		// 尾巴
		if (skin.tail == 1) {
			return getSkinMaterial(getTail(skin));
		}
		return null;
	}
	
	public static String getHead(Skin skin) {
		return String.format(HEAD_NAME, skin.id);
	}
	
	public static String getBody(Skin skin, int index) {
		if (skin.body == 1){
			return String.format(BODY_NAME, skin.id, "");
		} else if (skin.body >= 2) {
			index = index % skin.body + 1;
			return String.format(BODY_NAME, skin.id, index);
		} else {
			return null;
		}
	}
	
	public static String getWreck(Skin skin, int index) {
		if (skin.wreck == 1){
			return String.format(WRECK_NAME, skin.id, "");
		} else if (skin.wreck >= 2) {
			index = index % skin.wreck + 1;
			return String.format(WRECK_NAME, skin.id, index);
		} else {
			return null;
		}
	}
	
	public static String getTail(Skin skin) {
		if (skin.tail != 0)
			return String.format(TAIL_NAME, skin.id);
		else 
			return null;
	}
	
	public static void main(String[] args) {
		for(Skin skin : Skin.values()) {
			// 身体
			if (skin.body > 1) {
				for(int i=0; i<skin.body; i++)
				System.out.println(getBody(skin, i));
			} else {
				System.out.println(getBody(skin, 0));
			}
			// 头
			System.out.println(getHead(skin));
			
			// 尾巴
			if (skin.tail == 1) {
				System.out.println(getTail(skin));
			}
			
			// 残骸
			if (skin.wreck <= 0) {
				// 没有残骸?
			} else if (skin.wreck == 1) {
				System.out.println(getWreck(skin, 0));
			} else {
				for(int i=0; i<skin.wreck; i++)
					System.out.println(getWreck(skin, i));
			}
		}
	}
	
	/**
	 * 释放内存
	 */
	public void clear() {
		for(int i=0; i<foodMats.length; i++) {
			foodMats[i] = null;
		}
		foodMats = null;
		
		for(int i=0; i<length; i++) {
			skinKeys[i] = null;
			skinMats[i] = null;
		}
		skinKeys = null;
		skinMats = null;
	}
}
