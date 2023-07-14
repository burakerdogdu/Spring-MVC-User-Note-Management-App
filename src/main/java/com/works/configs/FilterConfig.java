package com.works.configs;

import com.works.props.User;
import com.works.services.TinkEncDec;

import com.works.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FilterConfig implements Filter {

    final TinkEncDec tinkEncDec;
    final UserService service;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();
        String freeUrls[] = {"/", "/userLogin"};
        boolean loginStatus = true;
        for( String item : freeUrls ) {
            if(url.equals(item)) {
                loginStatus = false;
                break;
            }
        }

        if (loginStatus) {
            // Cookie Control
            if( req.getCookies() != null ) {
                Cookie[] cookies = req.getCookies();
                for( Cookie cookie : cookies ) {
                    if (cookie.getName().equals("user")) {
                        String val = tinkEncDec.decrypt(cookie.getValue());//controllerdan gelen şifreli id verisini çözüp
                        // single methoduna göndererek User u objesini dolduurmak için kullanılıyor
                        User u = service.single(Integer.parseInt(val));
                        if ( u != null )
                            req.getSession().setAttribute("user", u);
                        break;
                    }
                }
            }
            // session control
            boolean status = req.getSession().getAttribute("user") == null;
            if( status ) {
                // redirect Login
                res.sendRedirect("/");
            }else {
                User u = (User) req.getSession().getAttribute("user");
                req.setAttribute("user", u);
                chain.doFilter(req, res);
            }
        }else {
            chain.doFilter(req, res);
        }


    }

}