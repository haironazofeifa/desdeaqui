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

    @PostMapping("/guardados/eliminar/{id}")
    public String eliminarGuardado(@PathVariable Integer id) {

        guardadoService.eliminarPorId(id);

        return "redirect:/guardados";
    }

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