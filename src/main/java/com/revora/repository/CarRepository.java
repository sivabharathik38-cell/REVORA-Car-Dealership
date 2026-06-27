package com.revora.repository;

import com.revora.model.Car;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT DISTINCT c.brand FROM Car c")
    List<String> findDistinctBrands();

    @Query("SELECT c FROM Car c WHERE " +
           "(:brand IS NULL OR :brand = 'All brands' OR c.brand = :brand) AND " +
           "(:fuels IS NULL OR c.fuel IN :fuels) AND " +
           "(:transmissions IS NULL OR c.transmission IN :transmissions) AND " +
           "(:maxPrice IS NULL OR c.price <= :maxPrice) AND " +
           "(:search IS NULL OR :search = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Car> searchCars(
            @Param("brand") String brand,
            @Param("fuels") List<String> fuels,
            @Param("transmissions") List<String> transmissions,
            @Param("maxPrice") Long maxPrice,
            @Param("search") String search,
            Sort sort
    );
}
