package jp.co.sss.spring_test.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales_items")
public class SalesItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sales_items_gen")
	@SequenceGenerator(name = "seq_sales_items_gen", sequenceName = "seq_sales_items", allocationSize = 1)
	
	@Column(name = "sale_item_id")
	private Integer saleItemId;
	
	@Column(name = "sale_name")
	private String saleName;
	
	@Column
	private String description;
	
	@Column(name = "discount_rate")
	private Integer discountRate;
	
	@Column(name = "sales_img_path")
	private String salesImgPath;
	
	@Column(name = "start_month")
	private LocalDateTime startMonth;
	
	@Column(name = "end_month")
	private LocalDateTime endMonth;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	public Integer getSaleItemId() {
		return saleItemId;
	}

	public void setSaleItemId(Integer saleItemId) {
		this.saleItemId = saleItemId;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Integer discountRate) {
		this.discountRate = discountRate;
	}

	public String getSalesImgPath() {
		return salesImgPath;
	}

	public void setSalesImgPath(String salesImgPath) {
		this.salesImgPath = salesImgPath;
	}

	public LocalDateTime getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(LocalDateTime startMonth) {
		this.startMonth = startMonth;
	}

	public LocalDateTime getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(LocalDateTime endMonth) {
		this.endMonth = endMonth;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
}
