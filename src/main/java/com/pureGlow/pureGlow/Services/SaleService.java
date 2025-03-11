package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.Sale;
import com.pureGlow.pureGlow.Repositories.SaleRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService implements Idao<Sale, Long> {

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public Sale getById(Long id) {
        return this.saleRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(Sale obje) {
        this.saleRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(Sale obje) {
        this.saleRepository.delete(obje);
    }

    @Override
    public List<Sale> findAll() {
        return this.saleRepository.findAll();
    }
}

