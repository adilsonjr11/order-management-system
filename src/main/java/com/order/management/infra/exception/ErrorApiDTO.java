package com.order.management.infra.exception;

import com.order.management.model.dto.ResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErrorApiDTO extends ResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;

    private String error;

    @JsonIgnore
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;

    public ErrorApiDTO(String message) {
        this.message = message;
    }
}
