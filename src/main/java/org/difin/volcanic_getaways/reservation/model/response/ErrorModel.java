package org.difin.volcanic_getaways.reservation.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ErrorModel {

    @Getter
    @Setter
    private List<Error> errors;

    @Getter
    private String help = "For support please contact support@volcanicgetaways.com";

    public ErrorModel(){
        errors = new ArrayList<>();
    }

    public ErrorModel(Exception e){
        errors = new ArrayList<>();
        Error error = new Error(e);
        errors.add(error);
    }

    public ErrorModel(String message){
        errors = new ArrayList<>();
        Error error = new Error(message);
        errors.add(error);
    }

    public void addError(Exception e){
        Error error = new Error(e);
        errors.add(error);
    }

    public void addError(String message){
        Error error = new Error(message);
        errors.add(error);
    }
}
