package com.revora.controller;

import com.revora.model.Booking;
import com.revora.model.Car;
import com.revora.model.User;
import com.revora.repository.BookingRepository;
import com.revora.repository.CarRepository;
import com.revora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    // Create a new booking
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        Optional<Car> carOpt = carRepository.findById(request.getCarId());

        if (userOpt.isPresent() && carOpt.isPresent()) {
            Booking booking = new Booking(userOpt.get(), carOpt.get(), request.getBookingDate(), "Pending");
            return ResponseEntity.ok(bookingRepository.save(booking));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid User ID or Car ID");
    }

    // Get bookings (Optional filtering by userId)
    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            return ResponseEntity.ok(bookingRepository.findByUserId(userId));
        }
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    // Update status (Admin approve/reject)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(status);
            return ResponseEntity.ok(bookingRepository.save(booking));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
    }

    // Cancel booking
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return ResponseEntity.ok("Booking cancelled successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
    }

    // Request DTO class
    public static class BookingRequest {
        private Long userId;
        private Long carId;
        private String bookingDate;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getCarId() { return carId; }
        public void setCarId(Long carId) { this.carId = carId; }
        public String getBookingDate() { return bookingDate; }
        public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    }
}
