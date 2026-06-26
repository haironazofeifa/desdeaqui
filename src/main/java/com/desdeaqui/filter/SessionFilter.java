package com.desdeaqui.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class SessionFilter implements Filter {

    // Rutas que NO requieren sesión
    private static final String[] RUTAS_PUBLICAS = {
        "/login", "/registro", "/api/", "/css/", "/img/", "/js/", "/favicon.ico"
    };

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse res  = (HttpServletResponse) response;

        String ruta = req.getRequestURI();

        // Si la ruta es pública, dejamos pasar sin revisar sesión
        for (String publica : RUTAS_PUBLICAS) {
            if (ruta.startsWith(publica)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Revisamos si hay un usuario activo en la sesión
        HttpSession session = req.getSession(false);
        boolean autenticado = session != null
                           && session.getAttribute("usuarioActivo") != null;

        if (!autenticado) {
            res.sendRedirect("/login");
            return;
        }

        chain.doFilter(request, response);
    }
}