package com.desdeaqui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
/**
 * Clase de configuración web del proyecto Desde Aquí.
 *
 * Permite que los archivos guardados en la carpeta local "uploads"
 * puedan ser accedidos desde el navegador mediante la ruta "/uploads/**".
 *
 * Esta configuración se utiliza principalmente para mostrar imágenes
 * cargadas por el usuario, como fotos de perfil u otros recursos subidos
 * desde la aplicación.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registra un manejador de recursos estáticos personalizado.
     *
     * En este caso, se toma la carpeta local "uploads" del proyecto
     * y se expone públicamente mediante la ruta "/uploads/**".
     *
     * Ejemplo:
     * Si existe un archivo en:
     * uploads/foto.png
     *
     * Se puede acceder desde el navegador con:
     * http://localhost:8081/uploads/foto.png
     *
     * @param registry registro usado por Spring para configurar rutas de recursos estáticos.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rutaUploads = Paths.get("uploads").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(rutaUploads);
    }
}