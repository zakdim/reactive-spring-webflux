package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by dmitri on 2021-11-21.
 */
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

    Flux<MovieInfo> findByYear(Integer year);

    // Find by name case-insensitive
    @Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
    Mono<MovieInfo> findByName(String name);
}
