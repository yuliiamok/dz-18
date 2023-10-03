package automation.app;

import lombok.Builder;
import lombok.Data;

@Data

public class BookingRequest {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private Bookingdates bookingdates;
    private String additionalneeds;

    public BookingRequest() {
    }

}
