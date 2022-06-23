package com.project.cruit.dto;

import com.project.cruit.controller.PartApiController;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class GetInvolvePartsResponse {
    private Set<PartDto> involvedParts;
}