package org.difin.volcanic_getaways.reservation.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Error {

    public Error(Exception e){
        error = e.getMessage();
    }

    private String error;
}
