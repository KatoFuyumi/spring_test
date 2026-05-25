package jp.co.sss.spring_test.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginFormWithValidation {
	
	@NotBlank(message = "{login.email.required}")
    @Email(message = "{login.email.format}")
    private String email;

    @NotBlank(message = "{login.password.required}")
    @Size(min = 8, message = "{login.password.size}")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{login.password.pattern}")
    private String passwords;

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
}
