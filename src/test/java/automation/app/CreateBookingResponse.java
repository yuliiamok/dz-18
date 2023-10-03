package automation.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookingResponse {
    private int bookingid;
    private Booking booking;
    private Bookingdates bookingdates;

}
