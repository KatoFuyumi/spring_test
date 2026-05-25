package jp.co.sss.spring_test.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterFormWithValidation {

	    @NotBlank(message = "{register.userName.required}")
	    private String userName;

	    @NotBlank(message = "{register.email.required}")
	    @Email(message = "{register.email.format}")
	    private String email;

	    @NotBlank(message = "{register.password.required}")
	    @Size(min = 8, message = "{register.password.size}")
	    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{register.password.pattern}")
	    private String passwords;

	    @NotBlank(message = "{register.confirmPassword.required}")
	    private String confirmPassword;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPasswords() {
			return passwords;
		}

		public void setPasswords(String passwords) {
			this.passwords = passwords;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
	}