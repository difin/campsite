package org.difin.volcanic_getaways.reservation.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateModel {

    @Getter
    @Setter
    @JsonFormat(pattern="uuuu-MMM-dd")
    private LocalDate availableDate;
}
