package com.reactivespring.repository;

import com.reactivespring.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by dmitri on 2022-01-04.
 */
public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {
}
