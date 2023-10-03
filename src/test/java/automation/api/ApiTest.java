package automation.api;

import automation.app.*;
import automation.base.BaseTestNG;
import automation.controller.BookingController;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static automation.Config.*;
import static io.restassured.RestAssured.*;
public class ApiTest extends BaseTestNG {
    @Test
    public void addNewBookingTest() {

        Booking booking = Booking.newBuilder()
                .setFirstname("Johnny")
                .setLastname("Depp")
                .setTotalprice(222)
                .setDepositpaid(true)
                .setBookingdates(Bookingdates.builder().checkin("2018-01-01")
                        .checkout("2019-02-02").build())
                .setAdditionalneeds("Latte")
                .build();

        var response = given()
                .spec(REQ_SPEC)
                .header("Authorization", "Bearer " + getToken())
                .body(booking)
                .when()
                .post(getBaseURL().resolve("/booking"));

        response.then()
                .assertThat()
                .spec(RESP_SPEC).log().all();
        var body = response.as(CreateBookingResponse.class);
                SoftAssert softassert = new SoftAssert();
                softassert.assertEquals(body.getBooking().getFirstname(), booking.getFirstname());
                softassert.assertEquals(body.getBooking().getLastname(), booking.getLastname());
                softassert.assertEquals(body.getBooking().getTotalprice(), booking.getTotalprice());
                softassert.assertEquals(body.getBooking().isDepositpaid(), booking.isDepositpaid());
                softassert.assertEquals(body.getBooking().getAdditionalneeds(), booking.getAdditionalneeds());

                softassert.assertAll();
    }

    @Test
    public void getBookingIdTest() {

        given()
                .spec(REQ_SPEC)
                .when()
                .get(getBaseURL().resolve("/booking"))
                .then()
                .assertThat()
                .spec(RESP_SPEC)
                .body("bookingid", notNullValue())
                .log().all();
    }

    @Test
    public void partialUpdateBookingByIdTest() {

        int id = BookingController.getBookingIds().get(0).getBookingid();
        System.out.println("id parameter will be: " + id);

        Booking booking = Booking.newBuilder()
                .setTotalprice(666)
                .build();

        given()
                .spec(REQ_SPEC)
                .header("Cookie", "token=" + getToken())
                .body(booking)
                .when()
                .patch(getBaseURL().resolve("/booking/" + id))
                .then().assertThat()
                .spec(RESP_SPEC)
                .body("totalprice", equalTo(666))
                .log().all();
    }

    @Test
    public void updateBookingByIdTest() {

        int id = BookingController.getBookingIds().get(0).getBookingid();
        System.out.println("id parameter will be: " + id);

        Booking booking = Booking.newBuilder()
                .setFirstname("Bob")
                .setLastname("Black")
                .setTotalprice(222)
                .setDepositpaid(true)
                .setBookingdates(Bookingdates.builder().checkin("2022-01-01").checkout("2022-02-02").build())
                .setAdditionalneeds("Breakfast")
                .build();

        ValidatableResponse response =
                given()
                .spec(REQ_SPEC)
                .header("Cookie", "token=" + getToken())
                .body(booking)
                .when()
                .put(getBaseURL().resolve("/booking/" + id))
                .then()
                .assertThat()
                .spec(RESP_SPEC)
                .body("firstname", equalTo(booking.getFirstname()),
                        "lastname", equalTo(booking.getLastname()),
                        "totalprice", equalTo(booking.getTotalprice()),
                        "depositpaid", equalTo(booking.isDepositpaid()),
                        "bookingdates.checkin", equalTo("2022-01-01"),
                        "bookingdates.checkout", equalTo("2022-02-02"),
                        "additionalneeds", equalTo(booking.getAdditionalneeds()))
                .log().all();
    }

    @Test
    public void deleteBooking() {

        int id = BookingController.getBookingIds().get(0).getBookingid();
        System.out.println("id parameter will be: " + id);

        given()
                .spec(REQ_SPEC)
                .header("Cookie", "token=" + getToken())
                .when()
                .delete(getBaseURL().resolve("/booking/" + id))
                .then()
                .assertThat()
                .statusCode(201)
                .log().all();
    }
}
