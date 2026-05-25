package jp.co.sss.spring_test.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.spring_test.entity.User;
import jp.co.sss.spring_test.form.LoginFormWithValidation;
import jp.co.sss.spring_test.form.RegisterFormWithValidation;
import jp.co.sss.spring_test.repository.UserRepository;

@Controller
public class LoginController {
	
	@Autowired
    UserRepository userRepository;

	//ログイン画面表示
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String showLogin(Model model) {
	    model.addAttribute("loginForm", new LoginFormWithValidation());
	    return "login/login";
    }
    
 	//ログイン処理
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(
            @Valid @ModelAttribute("loginForm") LoginFormWithValidation form,
            BindingResult result,
            HttpSession session,
            Model model
    ) {

        // 入力チェックエラー
        if (result.hasErrors()) {
        	model.addAttribute("loginForm", form);
            return "login/login";
        }

        User user = userRepository.findByEmailAndPasswords(
                form.getEmail(),
                form.getPasswords()
        );

        if (user != null) {
            session.setAttribute("user", user);

            System.out.println("email=" + form.getEmail());
            System.out.println("password=" + form.getPasswords());

            return "redirect:/";
        } else {
            // ログイン失敗
            result.reject("login.failed");
            model.addAttribute("loginForm", form);
            return "login/login";
        }
    }
    
    //登録画面表示
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String showRegister(Model model) {
        model.addAttribute("registerForm", new RegisterFormWithValidation());
        return "login/register";
    }

    //登録処理
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(
            @Valid  @ModelAttribute("registerForm") RegisterFormWithValidation form,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {
        	model.addAttribute("registerForm", form);
            return "login/register";
        }

        // パスワード一致チェック
        if (!form.getPasswords().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "register.password.mismatch");
            model.addAttribute("registerForm", form);
            return "login/register";
        }

        User user = new User();
        user.setUserName(form.getUserName());
        user.setEmail(form.getEmail());
        user.setPasswords(form.getPasswords());

        userRepository.save(user);

        return "redirect:/login";
    }
    
    //ログアウト
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
