package com.pureGlow.pureGlow.Repositories;

import com.pureGlow.pureGlow.Entities.Sale;
import com.pureGlow.pureGlow.Entities.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {
    void deleteBySale(Sale sale);
}
