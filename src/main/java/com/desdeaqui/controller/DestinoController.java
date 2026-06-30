package com.desdeaqui.controller;

import com.desdeaqui.model.Destino;
import com.desdeaqui.model.Guardado;
import com.desdeaqui.model.Tip;
import com.desdeaqui.model.Usuario;
import com.desdeaqui.service.UsuarioService;

import java.nio.file.Path;
import com.desdeaqui.repository.GuardadoRepository;
import com.desdeaqui.repository.TipRepository;
import com.desdeaqui.repository.ComentarioRepository;
import com.desdeaqui.repository.PuntuacionTipRepository;
import com.desdeaqui.service.DestinoService;
import com.desdeaqui.service.GuardadoService;
import com.desdeaqui.service.TipService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
/**
 * Controlador encargado de gestionar las vistas principales del sistema Desde Aquí.
 *
 * Esta clase maneja la pantalla de inicio con el mapa, la exploración de destinos,
 * los destinos guardados, el perfil del usuario y la carga de foto de perfil.
 *
 * Utiliza servicios y repositorios para obtener información de destinos, tips,
 * guardados, comentarios y puntuaciones.
 */
@Controller
public class DestinoController {

    @Autowired
    private DestinoService destinoService;

    @Autowired
    private GuardadoService guardadoService;

    @Autowired
    private TipService tipService;

    @Autowired
    private GuardadoRepository guardadoRepository;

    @Autowired
    private TipRepository tipRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PuntuacionTipRepository puntuacionTipRepository;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Muestra la pantalla principal del sistema.
     *
     * Verifica que exista un usuario activo en sesión. Si no hay sesión,
     * redirige al login.
     *
     * Carga la lista de destinos, los destinos guardados por el usuario
     * y un tip destacado del primer destino disponible.
     *
     * @param session sesión HTTP actual.
     * @param model modelo utilizado para enviar datos a la vista.
     * @return vista principal index.html o redirección al login.
     */
    @GetMapping("/")
    public String inicio(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        if (usuario == null) {
            return "redirect:/login";
        }

        List<Destino> destinos = destinoService.listarTodos();
        List<Guardado> guardados = guardadoService.listarPorUsuario(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("destinos", destinos);
        model.addAttribute("guardados", guardados);

        if (!destinos.isEmpty()) {
            List<Tip> tips = tipService.listarPorDestinoYCategoria(
                    destinos.get(0).getId(), "transporte");

            if (!tips.isEmpty()) {
                model.addAttribute("tipDestacado", tips.get(0));
            }
        }

        return "index";
    }

    
    /**
     * Muestra la pantalla de exploración de destinos.
     *
     * Permite filtrar destinos por zona, interés y presupuesto.
     * Los filtros son opcionales y se envían como parámetros en la URL.
     *
     * @param session sesión HTTP actual.
     * @param zona filtro opcional por zona geográfica.
     * @param interes filtro opcional por tipo de interés.
     * @param presupuesto filtro opcional por presupuesto.
     * @param model modelo utilizado para enviar datos a la vista.
     * @return vista explorar.html.
     */
    @GetMapping("/explorar")
    public String explorar(HttpSession session,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String interes,
            @RequestParam(required = false) String presupuesto,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        List<Destino> destinos = destinoService.filtrar(zona, interes, presupuesto);

        model.addAttribute("usuario", usuario);
        model.addAttribute("destinos", destinos);
        model.addAttribute("zonaActiva", zona != null ? zona : "");
        model.addAttribute("interesActivo", interes != null ? interes : "");
        model.addAttribute("presupuestoActivo", presupuesto != null ? presupuesto : "");

        return "explorar";
    }

    /**
     * Muestra la pantalla de destinos guardados del usuario.
     *
     * Si no hay usuario en sesión, redirige al login.
     *
     * @param session sesión HTTP actual.
     * @param model modelo utilizado para enviar datos a la vista.
     * @return vista guardados.html o redirección al login.
     */
    @GetMapping("/guardados")
    public String guardados(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("guardados", guardadoService.listarPorUsuario(usuario.getId()));

        return "guardados";
    }

    /**
     * Muestra el perfil del usuario autenticado.
     *
     * Carga los destinos guardados y las estadísticas del usuario:
     * cantidad de guardados, tips publicados, comentarios y puntuaciones.
     *
     * @param session sesión HTTP actual.
     * @param model modelo utilizado para enviar datos a la vista.
     * @return vista perfil.html o redirección al login.
     */
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        if (usuario == null) {
            return "redirect:/login";
        }

        List<Guardado> guardados = guardadoRepository.findByUsuarioId(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("guardados", guardados);

        model.addAttribute("totalGuardados", guardados.size());
        model.addAttribute("totalTips", tipRepository.contarPorAutor(usuario.getId()));
        model.addAttribute("totalComentarios", comentarioRepository.contarPorUsuario(usuario.getId()));
        model.addAttribute("totalPuntuaciones", puntuacionTipRepository.countByUsuarioId(usuario.getId()));

        return "perfil";
    }

    /**
     * Muestra el perfil del usuario autenticado.
     *
     * Carga los destinos guardados y las estadísticas del usuario:
     * cantidad de guardados, tips publicados, comentarios y puntuaciones.
     *
     * @param session sesión HTTP actual.
     * @param model modelo utilizado para enviar datos a la vista.
     * @return vista perfil.html o redirección al login.
     */
    @PostMapping("/guardados/agregar")
    public String agregarGuardado(@RequestParam(required = false) Integer destinoId,
            HttpSession session) {

        if (destinoId == null) {
            return "redirect:/";
        }

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        destinoService.buscarPorId(destinoId).ifPresent(destino -> guardadoService.guardarDestino(usuario, destino));

        return "redirect:/";
    }

    /**
     * Elimina un destino guardado por su identificador.
     *
     * @param id identificador del registro guardado.
     * @return redirección a la pantalla de guardados.
     */
    @PostMapping("/guardados/eliminar/{id}")
    public String eliminarGuardado(@PathVariable Integer id) {

        guardadoService.eliminarPorId(id);

        return "redirect:/guardados";
    }

    /**
     * Permite actualizar la foto de perfil del usuario.
     *
     * La imagen se guarda localmente en la carpeta uploads/perfiles.
     * Luego se almacena en el usuario la ruta pública del archivo para
     * poder mostrarla en la interfaz.
     *
     * @param foto archivo de imagen enviado desde el formulario.
     * @param session sesión HTTP actual.
     * @return redirección al perfil del usuario.
     * @throws IOException si ocurre un error al guardar el archivo.
     */
    @PostMapping("/perfil/foto")
    public String subirFotoPerfil(@RequestParam("foto") MultipartFile foto,
            HttpSession session) throws IOException {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!foto.isEmpty()) {
            String nombreArchivo = "usuario_" + usuario.getId() + "_" + foto.getOriginalFilename();

            Path carpeta = Paths.get("uploads/perfiles");
            Files.createDirectories(carpeta);

            Path rutaArchivo = carpeta.resolve(nombreArchivo);
            Files.copy(foto.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            usuario.setFotoPerfil("/uploads/perfiles/" + nombreArchivo);

            usuarioService.guardar(usuario);

            session.setAttribute("usuarioActivo", usuario);
        }

        return "redirect:/perfil";
    }
}