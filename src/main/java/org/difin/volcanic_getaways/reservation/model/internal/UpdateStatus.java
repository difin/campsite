package org.difin.volcanic_getaways.reservation.model.internal;

public enum UpdateStatus {

    SUCCESS("Reservation was updated successfully"),
    NOT_FOUND("Reservation couldn't be updated because there is no active reservation for provided booking reference");

    public final String label;

    UpdateStatus(String label) {
        this.label = label;
    }
}
