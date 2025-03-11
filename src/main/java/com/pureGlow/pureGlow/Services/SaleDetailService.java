package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.Sale;
import com.pureGlow.pureGlow.Entities.SaleDetail;
import com.pureGlow.pureGlow.Repositories.SaleDetailRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleDetailService implements Idao<SaleDetail, Long> {

    @Autowired
    private SaleDetailRepository saleDetailRepository;

    @Override
    public SaleDetail getById(Long id) {
        return this.saleDetailRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(SaleDetail obje) {
        this.saleDetailRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(SaleDetail obje) {
        this.saleDetailRepository.delete(obje);
    }

    @Transactional
    public void deleteBySale(Sale sale) {
        this.saleDetailRepository.deleteBySale(sale);
    }

    @Override
    public List<SaleDetail> findAll() {
        return this.saleDetailRepository.findAll();
    }
}

