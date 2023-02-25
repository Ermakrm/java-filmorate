package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class User {

    private Integer id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;
    private String name;
    @Past
    @NotNull
    private LocalDate birthday;
    @Builder.Default
    Set<Integer> friends = new HashSet<>();
}
