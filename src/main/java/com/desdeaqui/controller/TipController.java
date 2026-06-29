package com.desdeaqui.controller;

import com.desdeaqui.model.Comentario;
import com.desdeaqui.model.Tip;
import com.desdeaqui.model.Usuario;
import com.desdeaqui.service.ComentarioService;
import com.desdeaqui.service.DestinoService;
import com.desdeaqui.service.PuntuacionTipService;
import com.desdeaqui.service.TipService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TipController {

    @Autowired
    private TipService tipService;

    @Autowired
    private PuntuacionTipService puntuacionService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private DestinoService destinoService;

    @GetMapping("/tips")
    public List<Map<String, Object>> obtenerTips(
            @RequestParam Integer destinoId,
            @RequestParam String categoria,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        return tipService.listarPorDestinoYCategoria(destinoId, categoria)
                .stream()
                .map(tip -> {
                    Map<String, Object> mapa = new LinkedHashMap<>();

                    Integer miPuntuacion = 0;

                    if (usuario != null) {
                        miPuntuacion = puntuacionService.obtenerPuntuacionDelUsuario(
                                tip.getId(),
                                usuario.getId());
                    }

                    // Dentro del .map() de obtenerTips, agregá esto al final:
                    boolean esMioTip = usuario != null
                            && (tip.getAutor() != null && tip.getAutor().getId().equals(usuario.getId()) ||
                                    "ADMIN".equals(usuario.getRol().getNombre()));
                    mapa.put("esMio", esMioTip);

                    mapa.put("id", tip.getId());
                    mapa.put("contenido", tip.getContenido());
                    mapa.put("promedio", puntuacionService.obtenerPromedio(tip.getId()));
                    mapa.put("total", puntuacionService.obtenerTotal(tip.getId()));
                    mapa.put("miPuntuacion", miPuntuacion);
                    mapa.put("autor", tip.getAutor() != null
                            ? tip.getAutor().getNombre()
                            : "Viajero anónimo");
                    mapa.put("fotoAutor", tip.getAutor() != null
                            ? tip.getAutor().getFotoPerfil()
                            : null);
                    mapa.put("hace", tiempoRelativo(tip.getCreadoEn()));
                    mapa.put("totalComentarios", comentarioService.contarPorTip(tip.getId()));

                    return mapa;
                })
                .collect(Collectors.toList());
    }

    private String tiempoRelativo(LocalDateTime fecha) {
        if (fecha == null)
            return "hace un tiempo";

        long minutos = Duration.between(fecha, LocalDateTime.now()).toMinutes();

        if (minutos < 1)
            return "justo ahora";
        if (minutos < 60)
            return "hace " + minutos + " min";

        long horas = minutos / 60;
        if (horas < 24)
            return "hace " + horas + (horas == 1 ? " hora" : " horas");

        long dias = horas / 24;
        if (dias < 7)
            return "hace " + dias + (dias == 1 ? " día" : " días");

        long semanas = dias / 7;
        if (semanas < 4)
            return "hace " + semanas + (semanas == 1 ? " semana" : " semanas");

        long meses = dias / 30;
        if (meses < 12)
            return "hace " + meses + (meses == 1 ? " mes" : " meses");

        long años = dias / 365;
        return "hace " + años + (años == 1 ? " año" : " años");
    }

    @GetMapping("/tips/{id}/comentarios")
    public List<Map<String, Object>> obtenerComentarios(@PathVariable Integer id,
            HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        return comentarioService.listarPorTip(id)
                .stream()
                .map(c -> {
                    Map<String, Object> mapa = new LinkedHashMap<>();
                    mapa.put("id", c.getId());
                    mapa.put("usuario", c.getUsuario().getNombre());
                    mapa.put("fotoUsuario", c.getUsuario() != null
                            ? c.getUsuario().getFotoPerfil()
                            : null);
                    mapa.put("contenido", c.getContenido());
                    mapa.put("hace", tiempoRelativo(c.getCreadoEn()));

                    // true si es el autor o es admin
                    boolean esMio = usuario != null && (c.getUsuario().getId().equals(usuario.getId()) ||
                            "ADMIN".equals(usuario.getRol().getNombre()));
                    mapa.put("esMio", esMio);

                    return mapa;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/tips/{id}/comentarios")
    public Map<String, Object> publicarComentario(@PathVariable Integer id,
            @RequestParam String contenido,
            HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");
        Map<String, Object> respuesta = new LinkedHashMap<>();

        if (usuario == null) {
            respuesta.put("error", "NO_AUTH");
            return respuesta;
        }
        if (contenido == null || contenido.trim().isEmpty()) {
            respuesta.put("error", "CONTENIDO_VACIO");
            return respuesta;
        }
        if (contenido.trim().length() > 500) {
            respuesta.put("error", "CONTENIDO_LARGO");
            return respuesta;
        }

        tipService.buscarPorId(id).ifPresent(tip -> {
            Comentario nuevo = comentarioService.guardar(tip, usuario, contenido.trim());
            respuesta.put("id", nuevo.getId()); // ← id necesario para el botón eliminar
            respuesta.put("usuario", usuario.getNombre());
            respuesta.put("fotoUsuario", usuario.getFotoPerfil());
            respuesta.put("contenido", contenido.trim());
            respuesta.put("hace", "justo ahora");
        });

        return respuesta;
    }

    @PostMapping("/tips/{id}/puntuar")
    public Map<String, Object> puntuar(@PathVariable Integer id,
            @RequestParam Integer estrellas,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");

        Map<String, Object> respuesta = new LinkedHashMap<>();

        if (usuario == null) {
            respuesta.put("error", "NO_AUTH");
            return respuesta;
        }

        if (estrellas < 1 || estrellas > 5) {
            respuesta.put("error", "ESTRELLAS_INVALIDAS");
            return respuesta;
        }

        tipService.buscarPorId(id).ifPresent(tip -> puntuacionService.puntuar(tip, usuario, estrellas));

        respuesta.put("promedio", puntuacionService.obtenerPromedio(id));
        respuesta.put("total", puntuacionService.obtenerTotal(id));
        respuesta.put("miPuntuacion", estrellas);

        return respuesta;
    }

    // ── Viajero publica un tip ──────────────────────────────
    @PostMapping("/tips")
    public Map<String, Object> publicarTip(@RequestParam Integer destinoId,
            @RequestParam String categoria,
            @RequestParam String contenido,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");
        Map<String, Object> respuesta = new LinkedHashMap<>();

        if (usuario == null) {
            respuesta.put("error", "NO_AUTH");
            return respuesta;
        }
        if (contenido == null || contenido.trim().isEmpty()) {
            respuesta.put("error", "CONTENIDO_VACIO");
            return respuesta;
        }
        if (contenido.trim().length() > 500) {
            respuesta.put("error", "CONTENIDO_LARGO");
            return respuesta;
        }

        destinoService.buscarPorId(destinoId).ifPresent(destino -> {
            Tip tip = new Tip(contenido.trim(), categoria, destino, usuario);
            Tip guardado = tipService.guardar(tip); // ← guardá el resultado
            respuesta.put("ok", true);
            respuesta.put("id", guardado.getId()); // ← devolvé el id
            respuesta.put("autor", usuario.getNombre());
            respuesta.put("fotoAutor", usuario.getFotoPerfil());
            respuesta.put("contenido", contenido.trim());
            respuesta.put("hace", "justo ahora");
        });

        return respuesta;
    }

    // ── Eliminar tip ────────────────────────────────────────
    @PostMapping("/tips/{id}/eliminar")
    public Map<String, Object> eliminarTip(@PathVariable Integer id,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");
        Map<String, Object> respuesta = new LinkedHashMap<>();

        if (usuario == null) {
            respuesta.put("error", "NO_AUTH");
            return respuesta;
        }

        tipService.buscarPorId(id).ifPresent(tip -> {
            boolean esAutor = tip.getAutor() != null &&
                    tip.getAutor().getId().equals(usuario.getId());
            boolean esAdmin = "ADMIN".equals(usuario.getRol().getNombre());

            if (esAutor || esAdmin) {
                tipService.eliminar(id);
                respuesta.put("ok", true);
            } else {
                respuesta.put("error", "SIN_PERMISO");
            }
        });

        if (respuesta.isEmpty()) {
            respuesta.put("error", "NO_ENCONTRADO");
        }

        return respuesta;
    }

    // ── Eliminar comentario ─────────────────────────────────
    @PostMapping("/comentarios/{id}/eliminar")
    public Map<String, Object> eliminarComentario(@PathVariable Integer id,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");
        Map<String, Object> respuesta = new LinkedHashMap<>();

        if (usuario == null) {
            respuesta.put("error", "NO_AUTH");
            return respuesta;
        }

        // Necesitás buscar el comentario por id
        comentarioService.buscarPorId(id).ifPresent(comentario -> {
            boolean esAutor = comentario.getUsuario().getId().equals(usuario.getId());
            boolean esAdmin = "ADMIN".equals(usuario.getRol().getNombre());

            if (esAutor || esAdmin) {
                comentarioService.eliminar(id);
                respuesta.put("ok", true);
            } else {
                respuesta.put("error", "SIN_PERMISO");
            }
        });

        if (respuesta.isEmpty()) {
            respuesta.put("error", "NO_ENCONTRADO");
        }

        return respuesta;
    }
}