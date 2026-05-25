package jp.co.sss.spring_test.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import jp.co.sss.spring_test.entity.User;

@Component
public class LoginCheckFilter extends HttpFilter {

	@Override
	public void doFilter(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        FilterChain chain)
	        throws IOException, ServletException {

		String requestURL = request.getRequestURI();
		String method = request.getMethod();

		// ★ POSTは全部通す
		if (method.equals("POST")) {
		    chain.doFilter(request, response);
		    return;
		}

		// ログイン・登録は通す
		if (requestURL.contains("/login") ||
		    requestURL.contains("/register") ||
		    requestURL.contains("/css") ||
		    requestURL.contains("/images")) {

		    chain.doFilter(request, response);
		    return;
    	}

	    HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user");

	    if (user == null) {
	        response.sendRedirect("/spring_test/login");
	        return;
	    }

	    chain.doFilter(request, response);
	}
}
