package com.reactivespring.repository;

import com.reactivespring.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Created by dmitri on 2022-01-04.
 */
public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {

    Flux<Review> findReviewsByMovieInfoId(Long movieInfoId);
}
