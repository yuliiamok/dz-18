package automation.controller;

import automation.app.BookingId;
import automation.base.BaseTestNG;

import java.util.Arrays;
import java.util.List;

import static automation.Config.getBaseURL;
import static io.restassured.RestAssured.given;

public class BookingController extends BaseTestNG {


    public static List<BookingId> getBookingIds() {
        return Arrays.asList(given()
                .spec(REQ_SPEC)
                .when()
                .get(getBaseURL().resolve("/booking")).as(BookingId[].class));

    }
}
