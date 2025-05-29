package com.project.code.Controller;

import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController @RequestMapping("/reviews")
public class ReviewController {
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private CustomerRepository customerRepository;

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable Long storeId, @PathVariable Long productId) {
        var rr = reviewRepository.findByStoreIdAndProductId(storeId, productId);
        return Map.of("reviews", rr.stream()
            .map(review -> {
                var oCus = customerRepository.findById(review.getCustomerId());
                String customerName = oCus.isPresent() ? oCus.get().getName() : "Unknown";
                return Map.of(
                "comment", review.getComment(),
                "rating", review.getRating(),
                "customerName", customerName);
            })
           .collect(Collectors.toList()));
    }
}
