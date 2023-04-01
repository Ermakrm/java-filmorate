package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.model.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Builder
@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    @NotNull
    private String description;
    @ReleaseDate
    @NotNull
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private Integer duration;
    private MPA mpa;
    private List<Genres> genres;
}
