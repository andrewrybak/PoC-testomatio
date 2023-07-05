package com.mc.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class PetDto {
    private Long id;
    private String name;
    private List<String> photoUrls;
    private String status;
}