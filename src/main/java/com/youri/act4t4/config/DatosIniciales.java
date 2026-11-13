
package com.youri.act4t4.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.youri.act4t4.models.Categoria;
import com.youri.act4t4.repositorio.CategoriaRepositorio;

@Component
public class DatosIniciales implements CommandLineRunner {

    @Autowired
    private CategoriaRepositorio categoriaRepo;

    @Override
    public void run(String... args) {
        if (categoriaRepo.count() == 0) {
            for (String nombre : new String[] { "Tecnologia", "Bebidas", "Papeleria" }) {
                Categoria categoria = new Categoria();
                categoria.setNombre(nombre);
                categoriaRepo.save(categoria);
            }
        }
    }
}