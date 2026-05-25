package jp.co.sss.spring_test.form;

import jakarta.validation.constraints.Pattern;

public class PurchaseForm {
	
	 @Pattern(regexp = "^$|[0-9]{15}", message = "{purchase.cardNumber1.pattern}")
	 private String cardNumber1;
	 
	 @Pattern(regexp = "^$|[0-9]{15}", message = "{purchase.cardNumber2.pattern}")
	 private String cardNumber2;

	 public String getCardNumber1() {
		 return cardNumber1;
	 }

	 public void setCardNumber1(String cardNumber1) {
		 this.cardNumber1 = cardNumber1;
	 }

	 public String getCardNumber2() {
		 return cardNumber2;
	 }

	 public void setCardNumber2(String cardNumber2) {
		 this.cardNumber2 = cardNumber2;
	 }
}