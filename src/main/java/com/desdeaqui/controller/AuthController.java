package com.desdeaqui.controller;

import com.desdeaqui.model.Rol;
import com.desdeaqui.model.Usuario;
import com.desdeaqui.repository.RolRepository;
import com.desdeaqui.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador encargado de gestionar la autenticación de usuarios.
 *
 * Esta clase controla las vistas y acciones relacionadas con:
 * inicio de sesión, registro de nuevos usuarios y cierre de sesión.
 *
 * Utiliza HttpSession para guardar el usuario autenticado y su rol activo,
 * permitiendo controlar el acceso a las diferentes secciones del sistema.
 */
@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;   // necesario para asignar rol VIAJERO

    /**
     * Muestra la pantalla de inicio de sesión.
     *
     * @return vista login.html.
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    /**
     * Procesa el formulario de inicio de sesión.
     *
     * Valida el correo y la contraseña contra la base de datos.
     * Si las credenciales son correctas, guarda el usuario y su rol
     * dentro de la sesión HTTP.
     *
     * Si el usuario es administrador, lo redirige al panel administrativo.
     * Si es viajero, lo redirige a la página principal.
     *
     * @param correo correo ingresado por el usuario.
     * @param contrasena contraseña ingresada por el usuario.
     * @param session sesión HTTP donde se guardan los datos del usuario.
     * @param model modelo usado para enviar mensajes de error a la vista.
     * @return redirección según el rol o vista login si hay error.
     */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String contrasena,
                                HttpSession session,
                                Model model) {

        Usuario usuario = usuarioService.validarLogin(correo, contrasena);

        if (usuario == null) {
            // Credenciales incorrectas → volvemos al login con mensaje de error
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }

        // Guardar el usuario en sesión para usarlo en toda la aplicación
        session.setAttribute("usuarioActivo", usuario);
        session.setAttribute("rolActivo", usuario.getRol().getNombre());

        // Redirigir según el rol
        if (usuario.getRol().getNombre().equals("ADMIN")) {
            return "redirect:/admin/destinos";
        }

        return "redirect:/";
    }

    /**
     * Muestra la pantalla de registro.
     *
     * @return vista registro.html.
     */
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    /**
     * Procesa el formulario de registro de un nuevo usuario.
     *
     * Valida que las contraseñas coincidan, que tengan una longitud mínima
     * y que el correo no exista previamente en la base de datos.
     *
     * Por defecto, todo usuario registrado desde esta pantalla recibe
     * el rol VIAJERO.
     *
     * @param nombre nombre completo del usuario.
     * @param correo correo electrónico del usuario.
     * @param contrasena contraseña ingresada.
     * @param confirmar confirmación de la contraseña.
     * @param model modelo usado para enviar mensajes de error o éxito.
     * @return vista registro si hay error o vista login si el registro fue exitoso.
     */
    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String nombre,
                                   @RequestParam String correo,
                                   @RequestParam String contrasena,
                                   @RequestParam String confirmar,
                                   Model model) {

        // Validar que las contraseñas coincidan
        if (!contrasena.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "registro";
        }

        // Validar longitud mínima
        if (contrasena.length() < 6) {
            model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
            return "registro";
        }

        // Validar que el correo no esté en uso
        if (usuarioService.correoExiste(correo)) {
            model.addAttribute("error", "Ya existe una cuenta con ese correo.");
            return "registro";
        }

        // Buscar el rol VIAJERO en la base de datos
        Rol rolViajero = rolRepository.findByNombre("VIAJERO");

        // Crear y guardar el nuevo usuario
        Usuario nuevo = new Usuario(nombre, correo, contrasena, rolViajero);
        usuarioService.guardar(nuevo);

        // Redirigir al login con mensaje de éxito
        model.addAttribute("exito", "¡Cuenta creada! Ya podés iniciar sesión.");
        return "login";
    }

    /**
     * Cierra la sesión del usuario activo.
     *
     * Elimina todos los datos almacenados en HttpSession y redirige
     * nuevamente a la pantalla de login.
     *
     * @param session sesión HTTP actual.
     * @return redirección a login.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();           // elimina todos los datos de la sesión
        return "redirect:/login";
    }
}