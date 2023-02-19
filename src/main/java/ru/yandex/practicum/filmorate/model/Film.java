package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;


@Builder
@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
