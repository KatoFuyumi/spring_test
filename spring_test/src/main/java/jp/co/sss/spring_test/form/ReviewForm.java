package jp.co.sss.spring_test.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewForm {
	
	private Integer productId;

	@NotBlank(message = "{review.name.required}")
	@Size(max = 50, message = "{review.name.size}")
    private String name;

    @NotBlank(message = "{review.comment.required}")
    @Size(max = 300, message = "{review.comment.size}")
    private String comment;
    
    @Email(message = "{review.email.format}")
    private String email;

	@NotNull(message = "{review.rating.required}")
    private Integer rating;
	
	   public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
}
