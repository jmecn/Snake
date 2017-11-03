package net.jmecn.snake;

/**
 * 皮肤的定义
 * @author yanmaoyuan
 *
 */
public enum Skin {
	SKIN_1(1, 1, 2, 1, 1.828f, 1.25f),// 蜜蜂
	SKIN_2(2, 2, 0, 0),// 小黄人
	SKIN_3(3, 1, 0, 1),// 大岩蛇
	SKIN_4(4, 2, 0, 0),// 路飞
	SKIN_5(5, 1, 1, 1),// 皮卡丘
	SKIN_6(6, 1, 0, 0, 1.147f, 1f),// 紫
	SKIN_7(7, 1, 0, 0, 1.147f, 1f),// 黄
	SKIN_8(8, 1, 0, 0, 1.147f, 1f),// 绿
	SKIN_9(9, 1, 0, 0, 1.147f, 1f),// 红
	SKIN_10(10, 1, 0, 0, 1.147f, 1f),// 蓝
	SKIN_11(11, 1, 0, 0),// 中国
	SKIN_12(12, 1, 1, 0),// 不认识1
	SKIN_13(13, 2, 0, 0),// 不认识2
	SKIN_14(14, 2, 0, 0),// 不认识3
	SKIN_15(15, 2, 0, 0),// 不认识4
	SKIN_16(16, 1, 0, 0),// 不认识5
	SKIN_17(17, 3, 0, 0),// 不认识6
	SKIN_18(18, 2, 0, 0);// 加拿大
	
	public int id;
	public int body;
	public int wreck;
	public int tail;
	public float headXScale;
	public float headYScale;
	
	private Skin(int id, int body, int wreck, int tail) {
		this.id = id;
		this.body = body;
		this.wreck = wreck;
		this.tail = tail;
		this.headXScale = 1f;
		this.headYScale = 1f;
	}

	private Skin(int id, int body, int wreck, int tail, float headXScale,
			float headYScale) {
		this.id = id;
		this.body = body;
		this.wreck = wreck;
		this.tail = tail;
		this.headXScale = headXScale;
		this.headYScale = headYScale;
	}
}