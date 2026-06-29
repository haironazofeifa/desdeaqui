package com.desdeaqui.controller;

import com.desdeaqui.model.Destino;
import com.desdeaqui.model.Tip;
import com.desdeaqui.model.Usuario;
import com.desdeaqui.service.UsuarioService;
import com.desdeaqui.service.DestinoService;
import com.desdeaqui.service.TipService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DestinoService destinoService;

    @Autowired
    private TipService tipService;

    @Autowired
    private UsuarioService usuarioService;

    // ── Verificar rol ──────────────────────────────
    private boolean esAdmin(HttpSession session) {
        String rol = (String) session.getAttribute("rolActivo");
        return "ADMIN".equals(rol);
    }

    // ══════════════════════════════════════════════
    // CRUD DESTINOS
    // ══════════════════════════════════════════════

    // Listar
    @GetMapping("/destinos")
    public String listarDestinos(HttpSession session, Model model) {
        if (!esAdmin(session))
            return "redirect:/";
        model.addAttribute("destinos", destinoService.listarTodos());
        return "admin/destinos";
    }

    // Mostrar formulario nuevo
    @GetMapping("/destinos/nuevo")
    public String nuevoDestino(HttpSession session, Model model) {
        if (!esAdmin(session))
            return "redirect:/";
        model.addAttribute("destino", new Destino());
        return "admin/destino-form";
    }

    // Guardar nuevo
    @PostMapping("/destinos/guardar")
    public String guardarDestino(HttpSession session,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String imagen,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String interes,
            @RequestParam(required = false) String presupuesto) {

        if (!esAdmin(session))
            return "redirect:/";

        Destino destino = new Destino();
        destino.setNombre(nombre);
        destino.setDescripcion(descripcion);
        destino.setImagen(imagen);
        destino.setZona(zona);
        destino.setInteres(interes);
        destino.setPresupuesto(presupuesto);

        destinoService.guardar(destino);

        return "redirect:/admin/destinos?exito=Destino+creado+correctamente";
    }

    // Mostrar formulario editar
    @GetMapping("/destinos/editar/{id}")
    public String editarDestino(HttpSession session,
            @PathVariable Integer id,
            Model model) {
        if (!esAdmin(session))
            return "redirect:/";

        destinoService.buscarPorId(id).ifPresent(d -> model.addAttribute("destino", d));
        return "admin/destino-form";
    }

    // Guardar edición
    @PostMapping("/destinos/actualizar/{id}")
    public String actualizarDestino(HttpSession session,
            @PathVariable Integer id,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String imagen,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String interes,
            @RequestParam(required = false) String presupuesto) {
        if (!esAdmin(session))
            return "redirect:/";

        destinoService.buscarPorId(id).ifPresent(d -> {
            d.setNombre(nombre);
            d.setDescripcion(descripcion);
            d.setImagen(imagen);
            d.setZona(zona);
            d.setInteres(interes);
            d.setPresupuesto(presupuesto);

            destinoService.guardar(d);
        });
        return "redirect:/admin/destinos?exito=Destino+actualizado+correctamente";
    }

    // Eliminar
    @PostMapping("/destinos/eliminar/{id}")
    public String eliminarDestino(HttpSession session,
            @PathVariable Integer id) {
        if (!esAdmin(session))
            return "redirect:/";
        destinoService.eliminar(id);
        return "redirect:/admin/destinos?exito=Destino+eliminado+correctamente";
    }

    // ══════════════════════════════════════════════
    // CRUD TIPS
    // ══════════════════════════════════════════════

    // Listar tips de un destino
    @GetMapping("/destinos/{id}/tips")
    public String listarTips(HttpSession session,
            @PathVariable Integer id,
            Model model) {
        if (!esAdmin(session))
            return "redirect:/";

        destinoService.buscarPorId(id).ifPresent(d -> {
            model.addAttribute("destino", d);
            model.addAttribute("tips", tipService.listarPorDestino(id));
        });
        return "admin/tips";
    }

    // Agregar tip
    @PostMapping("/destinos/{id}/tips/guardar")
    public String guardarTip(HttpSession session,
            @PathVariable Integer id,
            @RequestParam String contenido,
            @RequestParam String categoria) {
        if (!esAdmin(session))
            return "redirect:/";

        Usuario autor = (Usuario) session.getAttribute("usuarioActivo");

        destinoService.buscarPorId(id).ifPresent(d -> {
            tipService.guardar(new Tip(contenido, categoria, d, autor));
        });
        return "redirect:/admin/destinos/" + id + "/tips";
    }

    // Eliminar tip
    @PostMapping("/destinos/{did}/tips/eliminar/{tid}")
    public String eliminarTip(HttpSession session,
            @PathVariable Integer did,
            @PathVariable Integer tid) {
        if (!esAdmin(session))
            return "redirect:/";
        tipService.eliminar(tid);
        return "redirect:/admin/destinos/" + did + "/tips";
    }

    // ══════════════════════════════════════════════
    // CRUD USUARIOS
    // ══════════════════════════════════════════════

    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {
        if (!esAdmin(session)) {
            return "redirect:/";
        }

        model.addAttribute("usuarios", usuarioService.listarTodos());

        return "admin/usuarios";
    }

    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(HttpSession session,
            @PathVariable Integer id) {
        if (!esAdmin(session)) {
            return "redirect:/";
        }

        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");

        if (usuarioActivo != null && usuarioActivo.getId().equals(id)) {
            return "redirect:/admin/usuarios?error=No+puedes+eliminar+tu+propio+usuario";
        }

        usuarioService.eliminar(id);

        return "redirect:/admin/usuarios?exito=Usuario+eliminado+correctamente";
    }
}
