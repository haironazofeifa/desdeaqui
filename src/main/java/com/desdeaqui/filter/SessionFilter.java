package com.desdeaqui.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Filtro encargado de proteger las rutas privadas del sistema.
 *
 * Este filtro intercepta las peticiones HTTP antes de que lleguen
 * a los controladores. Su función principal es verificar si existe
 * un usuario activo en la sesión.
 *
 * Si la ruta es pública, permite continuar sin revisar sesión.
 * Si la ruta es privada y no hay usuario autenticado, redirige al login.
 */
@Component
public class SessionFilter implements Filter {

    /**
     * Rutas que pueden ser accedidas sin iniciar sesión.
     *
     * Incluye las pantallas de login, registro, recursos estáticos
     * como CSS, imágenes, JavaScript y algunos endpoints de API.
     */
    private static final String[] RUTAS_PUBLICAS = {
        "/login", "/registro", "/api/", "/css/", "/img/", "/js/", "/favicon.ico"
    };

    /**
     * Método que se ejecuta automáticamente en cada petición HTTP.
     *
     * Primero verifica si la ruta solicitada es pública. Si lo es,
     * deja pasar la petición sin validar sesión.
     *
     * Si la ruta no es pública, revisa si existe una sesión activa
     * con el atributo "usuarioActivo". Si no existe, redirige al login.
     *
     * @param request petición recibida por el servidor.
     * @param response respuesta que se enviará al navegador.
     * @param chain cadena de filtros que permite continuar con la petición.
     * @throws IOException si ocurre un error de entrada o salida.
     * @throws ServletException si ocurre un error durante el filtrado.
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

                            // Se obtiene la sesión actual sin crear una nueva.
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