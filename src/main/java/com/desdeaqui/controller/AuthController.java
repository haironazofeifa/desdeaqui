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

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;   // necesario para asignar rol VIAJERO

    // ── Mostrar pantalla de login ──────────────────
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // ── Procesar el formulario de login ───────────
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

    // ── Registro ───────────────────────────────────
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

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

    // ── Cerrar sesión ──────────────────────────────
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();           // elimina todos los datos de la sesión
        return "redirect:/login";
    }
}