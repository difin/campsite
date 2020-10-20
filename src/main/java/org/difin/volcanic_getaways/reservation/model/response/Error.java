package org.difin.volcanic_getaways.reservation.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    private String error;
    private String technicalDetails;

    public Error(Exception e, String technicalDetails){
        this.error = e.getMessage();
        this.technicalDetails = technicalDetails;
    };
}
