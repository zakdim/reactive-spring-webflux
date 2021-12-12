package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by dmitri on 2021-12-12.
 */
@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

    private MoviesInfoService moviesInfoService;

    public MoviesInfoController(MoviesInfoService moviesInfoService) {
        this.moviesInfoService = moviesInfoService;
    }

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMovieInfos() {
        return moviesInfoService.getAllMovieInfos().log();
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo) {

        return moviesInfoService.addMovieInfo(movieInfo).log();
    }
}
