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

/**
 * Controlador encargado de administrar las funciones exclusivas del rol ADMIN.
 *
 * Desde esta clase se gestionan los módulos administrativos del sistema:
 * destinos, tips y usuarios. Todas las rutas inician con "/admin" y verifican
 * que el usuario tenga el rol ADMIN antes de permitir el acceso.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DestinoService destinoService;

    @Autowired
    private TipService tipService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Verifica si el usuario activo en sesión tiene rol de administrador.
     *
     * @param session sesión HTTP actual.
     * @return true si el rol activo es ADMIN, false en caso contrario.
     */
    private boolean esAdmin(HttpSession session) {
        String rol = (String) session.getAttribute("rolActivo");
        return "ADMIN".equals(rol);
    }

    /// ══════════════════════════════════════════════
    // CRUD DESTINOS
    // ══════════════════════════════════════════════

    /**
     * Muestra la lista de destinos registrados en el sistema.
     * Solo puede acceder un usuario con rol ADMIN.
     */
    @GetMapping("/destinos")
    public String listarDestinos(HttpSession session, Model model) {
        if (!esAdmin(session))
            return "redirect:/";
        model.addAttribute("destinos", destinoService.listarTodos());
        return "admin/destinos";
    }

    /**
     * Muestra el formulario para registrar un nuevo destino.
     */

    @GetMapping("/destinos/nuevo")
    public String nuevoDestino(HttpSession session, Model model) {
        if (!esAdmin(session))
            return "redirect:/";
        model.addAttribute("destino", new Destino());
        return "admin/destino-form";
    }

    /**
     * Guarda un nuevo destino en la base de datos.
     *
     * Recibe los datos enviados desde el formulario administrativo y crea
     * un objeto Destino para enviarlo al servicio correspondiente.
     */
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

    /**
     * Muestra el formulario para editar un destino existente.
     *
     * @param id identificador del destino a editar.
     */
    @GetMapping("/destinos/editar/{id}")
    public String editarDestino(HttpSession session,
            @PathVariable Integer id,
            Model model) {
        if (!esAdmin(session))
            return "redirect:/";

        destinoService.buscarPorId(id).ifPresent(d -> model.addAttribute("destino", d));
        return "admin/destino-form";
    }

    /**
     * Actualiza un destino existente en la base de datos.
     *
     * @param id identificador del destino a actualizar.
     */
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

    /**
     * Actualiza los datos de un destino existente.
     *
     * Busca el destino por su id, modifica sus atributos y guarda los cambios
     * mediante el servicio de destinos.
     */
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

    /**
     * Elimina un destino del sistema.
     *
     * @param id identificador del destino que se desea eliminar.
     */
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

    /**
     * Lista los tips asociados a un destino específico.
     *
     * @param id identificador del destino.
     */
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

    /**
     * Elimina un tip de un destino específico.
     *
     * @param did identificador del destino.
     * @param tid identificador del tip.
     */
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

    /**
     * Muestra la lista de usuarios registrados en el sistema.
     */
    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {
        if (!esAdmin(session)) {
            return "redirect:/";
        }

        model.addAttribute("usuarios", usuarioService.listarTodos());

        return "admin/usuarios";
    }


    /**
     * Elimina un usuario del sistema.
     *
     * Antes de eliminar, valida que el administrador no pueda eliminar
     * su propio usuario activo.
     *
     * @param id identificador del usuario que se desea eliminar.
     */
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
