package com.fucar.service;

import com.fucar.entity.Review;
import com.fucar.repository.ReviewRepository;

import java.util.List;

public class ReviewService {

    private final ReviewRepository repo = new ReviewRepository();

    // =========================
    // CRUD methods
    // =========================

    public void addReview(Review review) {
        repo.save(review);
    }

    public void updateReview(Review review) {
        repo.update(review);
    }

    public void delete(Integer reviewID) {
        repo.delete(reviewID);
    }

    public Review getReviewById(Integer reviewID) {
        return repo.findById(reviewID);
    }

    public List<Review> getReviewsByCar(int carID) {
        return repo.findByCar(carID);
    }

    public List<Review> getAllReviews() {
        return repo.findAll();
    }
}
