package in.grasshoper.field.product.data;

public class ProductImageData {
	@SuppressWarnings("unused")private final Long id;
	@SuppressWarnings("unused")private final Long productId;
	@SuppressWarnings("unused")private final String imageUrl;
	@SuppressWarnings("unused")private final Integer displayOrder;
	@SuppressWarnings("unused")private final String label;
	@SuppressWarnings("unused")private final Boolean isActive;
	private ProductImageData(Long id, Long productId, String imageUrl,
			Integer displayOrder, String label, Boolean isActive) {
		super();
		this.id = id;
		this.productId = productId;
		this.imageUrl = imageUrl;
		this.displayOrder = displayOrder;
		this.label = label;
		this.isActive = isActive;
	}
	
	public static ProductImageData createNew(Long id, Long productId, String imageUrl,
			Integer displayOrder, String label, Boolean isActive) {
		return new ProductImageData(id, productId, imageUrl, displayOrder, label, isActive);
	}
}
