package com.desdeaqui.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // Sube una imagen a Cloudinary y devuelve la URL segura (https)
    @SuppressWarnings("unchecked")
    public String subirImagen(MultipartFile archivo, String carpeta) throws IOException {
        Map<String, Object> opciones = ObjectUtils.asMap(
                "folder", carpeta,
                "resource_type", "image");

        Map<String, Object> resultado = cloudinary.uploader().upload(archivo.getBytes(), opciones);

        return (String) resultado.get("secure_url");
    }
}