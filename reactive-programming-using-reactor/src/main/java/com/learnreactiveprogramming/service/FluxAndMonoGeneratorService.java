package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log(); // db or a remote service call
    }

    public Mono<String> nameMono() {

        return Mono.just("alex")
                .log();
    }

    public Flux<String> namesFlux_map(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s) // 4-ALEX, 5-CHLOE
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_immutability() {

        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log();
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Mono<String> namesMono_map_filter(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log(); // Mono<List of A,L,E,X>
    }

    public Flux<String> namesMono_flatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log(); // Mono<List of A,L,E,X>
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList = List.of(charArray); // ALEX -> A,L,E,X
        return Mono.just(charList);
    }

    public Flux<String> namesFlux_flatmap(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
                .flatMap(s -> splitString(s)) // A,L,E,X,C,H,L,O,E
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_flatmap_async(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
                .flatMap(s -> splitString_withDelay(s)) // A,L,E,X,C,H,L,O,E
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_concatmap(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
//                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
                .concatMap(s -> splitString_withDelay(s)) // A,L,E,X,C,H,L,O,E
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_transform(int stringLength) {
        // filter the string whose length is greater than 3

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

//        Flux.empty();
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(s -> splitString(s)) // A,L,E,X,C,H,L,O,E
                .defaultIfEmpty("default")
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {
        // filter the string whose length is greater than 3

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("default")
                .transform(filterMap); // "D","E","F","A","U","L","T"

//        Flux.empty();
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                // A,L,E,X,C,H,L,O,E
                .switchIfEmpty(defaultFlux)
                .log(); // db or a remote service call
    }

    // ALEX -> Flux(A,L,E,X)
    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitString_withDelay(String name) {
        var charArray = name.split("");
//        var delay = new Random().nextInt(1000);
        var delay = 1000;
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name is: " + name);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> {
                    System.out.println("Mono name is: " + name);
                });
    }
}
