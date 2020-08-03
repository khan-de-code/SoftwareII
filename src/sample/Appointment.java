package sample;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Appointment {
    private final Customer customer;
    private final String title;
    private final String description;
    private final String location;
    private final String contact;
    private final String type;
    private final String url;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final int appointmentId;


    public Appointment(Customer customer, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end, int appointmentId) {
        this.customer = customer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.appointmentId = appointmentId;
    }

    public String getCustomerName(){
        return customer.getCustomerName();
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public OffsetDateTime getStart() {
        ZoneId currentZone = ZoneId.systemDefault();

        return start.atZone(currentZone).toOffsetDateTime();
    }

    public OffsetDateTime getEnd() {
        ZoneId currentZone = ZoneId.systemDefault();

        return end.atZone(currentZone).toOffsetDateTime();
    }

    public int getAppointmentId() {
        return appointmentId;
    }


    @Override
    public String toString() {
        return "Appointment{" +
                "customer=" + customer +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", contact='" + contact + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", appointmentId=" + appointmentId +
                '}';
    }
}
