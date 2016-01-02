package in.grasshoper.field.product.data;

import in.grasshoper.core.GrassHoperMainConstants;
import in.grasshoper.field.tag.data.SubTagData;

import java.math.BigDecimal;
import java.util.Collection;

public class ProductData {
	private final Long id;
	private final String name;
	private final String productUid;
	private final String desc0;
	private final String desc1;
	private final String desc2;
	private final BigDecimal quantity;
	private final String quantityUnit;
	private final BigDecimal pricePerUnit;
	private final String currencyCode= GrassHoperMainConstants.CurrencyCode;
	private final Boolean isSoldOut;
	private final Boolean isActive;
	@SuppressWarnings("unused") private final Collection<SubTagData> packagingStyles; 
	@SuppressWarnings("unused") private final Collection<SubTagData> categories;
	@SuppressWarnings("unused") private final Collection<ProductImageData> productImages;
	@SuppressWarnings("unused") private final BigDecimal minimumQuantity;
	
	//template
	@SuppressWarnings("unused") private final Collection<SubTagData> allPkgingStyles;
	@SuppressWarnings("unused") private final Collection<SubTagData> allCategories;
	@SuppressWarnings("unused") private final Collection<SubTagData> allSortOrders;
	
	private ProductData(Long id, String name, String productUid, String desc0,
			String desc1, String desc2, BigDecimal quantity,
			String quantityUnit, Boolean isSoldOut, Boolean isActive,
			final BigDecimal pricePerUnit, final BigDecimal minimumQuantity, 
			final Collection<SubTagData> packagingStyles,
			final Collection<ProductImageData> productImages,
			final Collection<SubTagData> allPkgingStyles,
			final Collection<SubTagData> categories,
			final Collection<SubTagData> allCategories,
			final Collection<SubTagData> allSortOrders) {
		super();
		this.id = id;
		this.name = name;
		this.productUid = productUid;
		this.desc0 = desc0;
		this.desc1 = desc1;
		this.desc2 = desc2;
		this.quantity = quantity;
		this.quantityUnit = quantityUnit;
		this.isSoldOut = isSoldOut;
		this.isActive = isActive;
		this.pricePerUnit = pricePerUnit;
		this.minimumQuantity = minimumQuantity;
		this.packagingStyles = packagingStyles;
		this.productImages = productImages;
		this.allPkgingStyles = allPkgingStyles;
		this.allCategories = allCategories;
		this.categories = categories;
		this.allSortOrders = allSortOrders;
	}
	public static ProductData createNew(final Long id, String name, String productUid, String desc0,
			String desc1, String desc2, BigDecimal quantity,
			String quantityUnit, Boolean isSoldOut, Boolean isActive,
			final BigDecimal pricePerUnit, final BigDecimal minimumQuantity,
			final Collection<SubTagData> packagingStyles, 
			final Collection<SubTagData> categories,
			final Collection<ProductImageData> productImages) {
		final Collection<SubTagData> allPkgingStyles = null;
		final Collection<SubTagData> allCategories = null;
		final Collection<SubTagData> allsortOrders = null;
		return new ProductData(id, name, productUid, desc0, desc1, desc2, quantity,
				quantityUnit, isSoldOut, isActive, pricePerUnit, minimumQuantity, packagingStyles,
				productImages, allPkgingStyles, categories, allCategories, allsortOrders);
	}
	
	/*public static ProductData createNew(final ProductData productData, final Collection<SubTagData> packagingStyles,
			final Collection<ProductImageData> productImages) {
		final Collection<SubTagData> allPkgingStyles = null;
		return new ProductData(productData.id, productData.name, productData.productUid, productData.desc0, 
				productData.desc1, productData.desc2, productData.quantity, productData.quantityUnit, 
				productData.isSoldOut, productData.isActive, productData.pricePerUnit, productData.minimumQuantity,
				packagingStyles, productImages, allPkgingStyles);
	}*/
	
	public static ProductData tamplate(final Collection<SubTagData> allPkgingStyles, final Collection<SubTagData> allCategories,
			final Collection<SubTagData> allsortOrders){
		final Long id =null;
		final String name =null;
		final String productUid = null;
		final String desc0= null;
		final String desc1= null;
		final String desc2=null;
		final BigDecimal quantity= null;
		final String quantityUnit =null;
		final Boolean isSoldOut= null;
		final Boolean isActive = null;
		final BigDecimal pricePerUnit = null;
		final BigDecimal minimumQuantity =null;
		final Collection<SubTagData> packagingStyles = null;
		final Collection<SubTagData> categories = null;
		final Collection<ProductImageData> productImages = null;
		return new ProductData(id, name, productUid, desc0, desc1, desc2, quantity,
				quantityUnit, isSoldOut, isActive, pricePerUnit, minimumQuantity, packagingStyles,
				productImages, allPkgingStyles, categories, allCategories, allsortOrders);
	}
	
	public Long getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getProductUid() {
		return this.productUid;
	}
	public String getDesc0() {
		return this.desc0;
	}
	public String getDesc1() {
		return this.desc1;
	}
	public String getDesc2() {
		return this.desc2;
	}
	public BigDecimal getQuantity() {
		return this.quantity;
	}
	public String getQuantityUnit() {
		return this.quantityUnit;
	}
	public Boolean getIsSoldOut() {
		return this.isSoldOut;
	}
	public Boolean getIsActive() {
		return this.isActive;
	}
	public BigDecimal getPricePerUnit() {
		return this.pricePerUnit;
	}
	public String getCurrencyCode() {
		return this.currencyCode;
	}
	
	
}
