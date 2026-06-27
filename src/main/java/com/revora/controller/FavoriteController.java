package com.revora.controller;

import com.revora.model.Car;
import com.revora.model.Favorite;
import com.revora.model.User;
import com.revora.repository.CarRepository;
import com.revora.repository.FavoriteRepository;
import com.revora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    // Toggle favorite (highly convenient for heart toggles!)
    @PostMapping("/toggle")
    public ResponseEntity<?> toggleFavorite(@RequestBody FavoriteRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        Optional<Car> carOpt = carRepository.findById(request.getCarId());

        if (userOpt.isPresent() && carOpt.isPresent()) {
            Optional<Favorite> existing = favoriteRepository.findByUserIdAndCarId(request.getUserId(), request.getCarId());
            if (existing.isPresent()) {
                favoriteRepository.delete(existing.get());
                return ResponseEntity.ok("Removed from favorites");
            } else {
                Favorite fav = new Favorite(userOpt.get(), carOpt.get());
                favoriteRepository.save(fav);
                return ResponseEntity.ok("Added to favorites");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid User ID or Car ID");
    }

    // Get favorites for a user
    @GetMapping
    public ResponseEntity<List<Favorite>> getFavorites(@RequestParam Long userId) {
        return ResponseEntity.ok(favoriteRepository.findByUserId(userId));
    }

    // Delete a favorite by ID (for favorites page)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long id) {
        if (favoriteRepository.existsById(id)) {
            favoriteRepository.deleteById(id);
            return ResponseEntity.ok("Favorite removed successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite not found");
    }

    public static class FavoriteRequest {
        private Long userId;
        private Long carId;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getCarId() { return carId; }
        public void setCarId(Long carId) { this.carId = carId; }
    }
}
