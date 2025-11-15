package com.fucar.service;

import com.fucar.entity.Review;
import com.fucar.repository.ReviewRepository;

import java.util.List;

public class ReviewService {

    private final ReviewRepository repo = new ReviewRepository();

    public void addReview(Review review) {
        repo.save(review);
    }

    public List<Review> getReviewsByCar(int carID) {
        return repo.findByCar(carID);
    }
}
