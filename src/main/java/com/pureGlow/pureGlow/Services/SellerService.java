package com.pureGlow.pureGlow.Services;

import com.pureGlow.pureGlow.Entities.Seller;
import com.pureGlow.pureGlow.Entities.User;
import com.pureGlow.pureGlow.Repositories.SellerRepository;
import com.pureGlow.pureGlow.Services.dao.Idao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService implements Idao<Seller, Long> {

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public Seller getById(Long id) {
        return this.sellerRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(Seller obje) {
        this.sellerRepository.save(obje);
    }

    @Transactional
    @Override
    public void delete(Seller obje) {
        this.sellerRepository.delete(obje);
    }

    @Override
    public List<Seller> findAll() {
        return this.sellerRepository.findAll();
    }

    public Seller findByUser(User user){
        return sellerRepository.findByUser(user).orElse(null);
    }

    public boolean existsSales(Seller seller){
        return sellerRepository.existsBySales_Seller(seller);
    }
}

