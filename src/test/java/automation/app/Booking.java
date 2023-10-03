package automation.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private Bookingdates bookingdates;
    private String additionalneeds;

    public static Builder newBuilder() {
        return new Booking().new Builder();
    }

    public class Builder {
        protected Builder() {
        }
        public Builder setFirstname(String firstname){
            Booking.this.firstname = firstname;
            return this;
        }
        public Builder setLastname(String lastname){
            Booking.this.lastname = lastname;
            return this;
        }
        public Builder setTotalprice(int totalprice){
            Booking.this.totalprice = totalprice;
            return this;
        }
        public Builder setDepositpaid(boolean depositpaid){
            Booking.this.depositpaid = depositpaid;
            return this;
        }
        public Builder setAdditionalneeds(String additionalneeds){
            Booking.this.additionalneeds = additionalneeds;
            return this;
        }
        public Builder setBookingdates(Bookingdates bookingdates) {
            Booking.this.bookingdates = bookingdates;
            return this;
        }

        public Booking build() {
            return Booking.this;
        }
    }

}
