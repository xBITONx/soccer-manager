package cz.muni.fi.pa165.mvc.security;

import cz.muni.fi.pa165.soccermanager.dto.ManagerDTO;
import cz.muni.fi.pa165.soccermanager.facade.ManagerFacade;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

/**
 * @author 445720 Martin Hamernik
 * @version 12/1/2017.
 */

@WebFilter(urlPatterns =  {"/team/*", "/player/*", "/league/*", "/match/*", "/manager/*"})
public class ProtectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        ManagerDTO manager = (ManagerDTO) request.getSession().getAttribute("authenticatedUser");

        if (manager == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
