package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.Product;
import com.pureGlow.pureGlow.Repositories.ProductRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements Idao<Product, Long> {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product getById(Long id) {
        return this.productRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(Product obje) {
        this.productRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(Product obje) {
        this.productRepository.delete(obje);
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public boolean existsSaleWithProduct(Product product) {
        return productRepository.existsBySaleDetails_Product(product);
    }

    public List<Product> processExcel(MultipartFile file) throws IOException {
        List<Product> products = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Saltar la primera fila (encabezados)
                }

                Product product = new Product();
                product.setName(row.getCell(0).getStringCellValue()); // Columna 0: Nombre
                product.setDescription(row.getCell(1).getStringCellValue()); // Columna 1: Descripción
                product.setPrice((float) row.getCell(2).getNumericCellValue()); // Columna 2: Precio

                // Si tienes una columna para la imagen (en base64), puedes procesarla aquí
                // product.setImagen(Base64.getDecoder().decode(row.getCell(3).getStringCellValue()));

                products.add(product);
            }
        }

        return products;
    }

    @Transactional
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }
}

