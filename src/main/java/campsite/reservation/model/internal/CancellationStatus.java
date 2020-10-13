package campsite.reservation.model.internal;

public enum CancellationStatus {

    SUCCESS("Reservation was cancelled successfully"),
    NOT_FOUND("Reservation couldn't be cancelled because there is no active reservation for provided booking reference");

    public final String label;

    CancellationStatus(String label) {
        this.label = label;
    }
}
