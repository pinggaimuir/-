package biz.web.fetch;

public class ObjectFormatManager {
	/** 电子产品 */
	public static final int Electronic_Products = 1001;
	public static final int ALL = 1000;

	public static ObjectFormat getObjectFormat(int type) {
		ObjectFormat obj = null;
		switch (type) {
		case Electronic_Products:
			obj = new ElectronicProducts();
			break;
		case ALL:
			obj = new AllGoods();
			break;

		default:
			break;
		}
		return obj;
	}
}
