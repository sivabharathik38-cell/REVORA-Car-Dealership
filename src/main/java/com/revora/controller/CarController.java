package com.revora.controller;

import com.revora.model.Car;
import com.revora.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    // Get all cars
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carRepository.findAll());
    }

    // Get distinct brands
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands() {
        return ResponseEntity.ok(carRepository.findDistinctBrands());
    }

    // Get single car details
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        Optional<Car> carOpt = carRepository.findById(id);
        if (carOpt.isPresent()) {
            return ResponseEntity.ok(carOpt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
    }

    // Dynamic search and filter
    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) List<String> fuel,
            @RequestParam(required = false) List<String> transmission,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "Latest") String sort
    ) {
        // Build sort specifications
        Sort sortOrder = Sort.by(Sort.Direction.DESC, "id");
        if ("Price Low to High".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.ASC, "price");
        } else if ("Price High to Low".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "price");
        }

        // Handle empty lists as null for JPA
        List<String> fuelList = (fuel != null && !fuel.isEmpty()) ? fuel : null;
        List<String> transmissionList = (transmission != null && !transmission.isEmpty()) ? transmission : null;

        List<Car> results = carRepository.searchCars(brand, fuelList, transmissionList, maxPrice, query, sortOrder);
        return ResponseEntity.ok(results);
    }

    // Add new car (Admin)
    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        // Set standard fields if missing
        if (car.getPriceText() == null || car.getPriceText().isEmpty()) {
            car.setPriceText("₹ " + (car.getPrice() / 100000.0) + " Lakh");
        }
        return ResponseEntity.ok(carRepository.save(car));
    }

    // Update car details (Admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody Car carUpdate) {
        Optional<Car> carOpt = carRepository.findById(id);
        if (carOpt.isPresent()) {
            Car car = carOpt.get();
            car.setName(carUpdate.getName());
            car.setBrand(carUpdate.getBrand());
            car.setPrice(carUpdate.getPrice());
            car.setPriceText("₹ " + (carUpdate.getPrice() / 100000.0) + " Lakh");
            if (carUpdate.getFuel() != null) car.setFuel(carUpdate.getFuel());
            if (carUpdate.getTransmission() != null) car.setTransmission(carUpdate.getTransmission());
            if (carUpdate.getEngine() != null) car.setEngine(carUpdate.getEngine());
            if (carUpdate.getPower() != null) car.setPower(carUpdate.getPower());
            if (carUpdate.getSeating() != null) car.setSeating(carUpdate.getSeating());
            if (carUpdate.getBody() != null) car.setBody(carUpdate.getBody());
            if (carUpdate.getBoot() != null) car.setBoot(carUpdate.getBoot());
            if (carUpdate.getSafety() != null) car.setSafety(carUpdate.getSafety());
            if (carUpdate.getImg() != null) {
                car.setImg(carUpdate.getImg());
                car.setImg1(carUpdate.getImg());
            }
            if (carUpdate.getImg1() != null) car.setImg1(carUpdate.getImg1());
            if (carUpdate.getImg2() != null) car.setImg2(carUpdate.getImg2());
            if (carUpdate.getImg3() != null) car.setImg3(carUpdate.getImg3());
            if (carUpdate.getImg4() != null) car.setImg4(carUpdate.getImg4());
            return ResponseEntity.ok(carRepository.save(car));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
    }

    // Delete car (Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return ResponseEntity.ok("Car deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
    }
}
