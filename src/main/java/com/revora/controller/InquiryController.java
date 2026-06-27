package com.revora.controller;

import com.revora.model.Car;
import com.revora.model.Inquiry;
import com.revora.model.User;
import com.revora.repository.CarRepository;
import com.revora.repository.InquiryRepository;
import com.revora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    // Submit an inquiry
    @PostMapping
    public ResponseEntity<?> submitInquiry(@RequestBody InquiryRequest request) {
        Inquiry inquiry = new Inquiry();
        inquiry.setMessage(request.getMessage());
        
        // Format current date e.g. "10 June 2026"
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        inquiry.setInquiryDate(sdf.format(new Date()));
        inquiry.setStatus("Pending");

        if (request.getUserId() != null) {
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            userOpt.ifPresent(user -> {
                inquiry.setUser(user);
                inquiry.setName(user.getUsername());
                inquiry.setEmail(user.getEmail());
            });
        } else {
            inquiry.setName(request.getName());
            inquiry.setEmail(request.getEmail());
        }

        if (request.getCarId() != null) {
            Optional<Car> carOpt = carRepository.findById(request.getCarId());
            carOpt.ifPresent(inquiry::setCar);
        }

        // Simulating/Logging email notification to the head owner's mail address
        System.out.println("\n=========================================================================");
        System.out.println("[EMAIL NOTIFICATION] Routing message to Owner at sivabharathik38@gmail.com");
        System.out.println("Sender Name: " + inquiry.getName());
        System.out.println("Sender Email: " + inquiry.getEmail());
        System.out.println("Message: " + inquiry.getMessage());
        System.out.println("=========================================================================\n");

        return ResponseEntity.ok(inquiryRepository.save(inquiry));
    }

    // Get inquiries (Optional userId query param)
    @GetMapping
    public ResponseEntity<List<Inquiry>> getInquiries(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            return ResponseEntity.ok(inquiryRepository.findByUserId(userId));
        }
        return ResponseEntity.ok(inquiryRepository.findAll());
    }

    // Send a reply (Admin)
    @PutMapping("/{id}/reply")
    public ResponseEntity<?> replyToInquiry(@PathVariable Long id, @RequestBody ReplyRequest replyRequest) {
        Optional<Inquiry> inquiryOpt = inquiryRepository.findById(id);
        if (inquiryOpt.isPresent()) {
            Inquiry inquiry = inquiryOpt.get();
            inquiry.setReply(replyRequest.getReply());
            inquiry.setStatus("Replied");
            return ResponseEntity.ok(inquiryRepository.save(inquiry));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquiry not found");
    }

    // Delete inquiry
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInquiry(@PathVariable Long id) {
        if (inquiryRepository.existsById(id)) {
            inquiryRepository.deleteById(id);
            return ResponseEntity.ok("Inquiry deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquiry not found");
    }

    // Request DTO classes
    public static class InquiryRequest {
        private Long userId;
        private Long carId;
        private String name;
        private String email;
        private String message;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getCarId() { return carId; }
        public void setCarId(Long carId) { this.carId = carId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class ReplyRequest {
        private String reply;

        public String getReply() { return reply; }
        public void setReply(String reply) { this.reply = reply; }
    }
}
