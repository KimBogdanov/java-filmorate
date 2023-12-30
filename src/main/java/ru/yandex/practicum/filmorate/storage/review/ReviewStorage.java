package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review createReview(Review review);

    Review updateReview(Review review);

    Integer deleteReviewById(int id);

    Optional<Review> getReviewById(int id);

    List<Review> getSortedReviews(Integer filmId, Integer count);

    List<Review> getAllReviews();

    Integer likeReview(int idReview, int idUser, int upRating);

    Integer dislikeReview(int idReview, int idUser, int downRating);

    Integer removeReviewEstimation(int idReview, int idUser);
}