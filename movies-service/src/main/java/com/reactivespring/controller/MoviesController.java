package com.reactivespring.controller;

import com.reactivespring.domain.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

/**
 * Created by dmitri on 2022-01-09.
 */
@Controller
@RequestMapping("/v1/movies")
public class MoviesController {

    @GetMapping("/{id}")
    public Mono<Movie> retrieveMovieById(@PathVariable("id") String movieId) {

        return null;
    }
}
