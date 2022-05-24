package com.project.cruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DelegateLeaderRequest {
    @NotNull
    private Long partId;
    @NotNull
    private Long newLeaderId;
}
