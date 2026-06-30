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

/**
 * Controlador REST encargado de gestionar los tips del sistema.
 *
 * Esta clase expone endpoints bajo la ruta "/api" para obtener tips,
 * publicar tips, puntuar consejos, gestionar comentarios y eliminar
 * contenido cuando el usuario tenga permisos.
 *
 * Las respuestas se devuelven principalmente como mapas o listas de mapas,
 * permitiendo que el frontend las consuma mediante JavaScript y fetch().
 */

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

     /**
     * Obtiene los tips de un destino según una categoría específica.
     *
     * Además del contenido del tip, también devuelve información adicional
     * como autor, foto del autor, promedio de estrellas, total de puntuaciones,
     * puntuación del usuario activo, cantidad de comentarios y permisos de eliminación.
     *
     * @param destinoId identificador del destino.
     * @param categoria categoría del tip.
     * @param session sesión HTTP actual.
     * @return lista de tips representados como mapas de datos.
     */
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

    /**
     * Calcula un texto de tiempo relativo a partir de una fecha.
     *
     * Por ejemplo: "justo ahora", "hace 5 min", "hace 2 horas",
     * "hace 3 días", entre otros.
     *
     * @param fecha fecha de creación del registro.
     * @return texto con el tiempo transcurrido.
     */
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

    /**
     * Obtiene los comentarios asociados a un tip.
     *
     * Devuelve la información necesaria para mostrar cada comentario en el modal,
     * incluyendo usuario, foto, contenido, tiempo relativo y si el usuario actual
     * puede eliminarlo.
     *
     * @param id identificador del tip.
     * @param session sesión HTTP actual.
     * @return lista de comentarios representados como mapas.
     */
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

    /**
     * Publica un nuevo comentario en un tip.
     *
     * Valida que el usuario tenga sesión activa, que el contenido no esté vacío
     * y que no supere el límite de caracteres permitido.
     *
     * @param id identificador del tip.
     * @param contenido texto del comentario.
     * @param session sesión HTTP actual.
     * @return mapa con la respuesta de éxito o error.
     */
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

    /**
     * Registra o actualiza la puntuación de un usuario sobre un tip.
     *
     * Cada usuario puede puntuar un tip del 1 al 5. Si ya había puntuado antes,
     * el sistema actualiza la puntuación existente.
     *
     * @param id identificador del tip.
     * @param estrellas cantidad de estrellas asignadas.
     * @param session sesión HTTP actual.
     * @return mapa con el nuevo promedio, total de puntuaciones y puntuación del usuario.
     */
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

    /**
     * Permite que un viajero publique un nuevo tip.
     *
     * El tip se asocia al destino indicado, a la categoría seleccionada
     * y al usuario activo en sesión.
     *
     * @param destinoId identificador del destino.
     * @param categoria categoría del tip.
     * @param contenido texto del tip.
     * @param session sesión HTTP actual.
     * @return mapa con la información del tip publicado o un error.
     */
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

    /**
     * Elimina un tip si el usuario tiene permisos.
     *
     * Solo puede eliminarlo el autor del tip o un usuario con rol ADMIN.
     *
     * @param id identificador del tip.
     * @param session sesión HTTP actual.
     * @return mapa con respuesta de éxito o error.
     */
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

    /**
     * Elimina un comentario si el usuario tiene permisos.
     *
     * Solo puede eliminarlo el autor del comentario o un usuario administrador.
     *
     * @param id identificador del comentario.
     * @param session sesión HTTP actual.
     * @return mapa con respuesta de éxito o error.
     */
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