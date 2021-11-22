package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by dmitri on 2021-11-21.
 */
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
}
