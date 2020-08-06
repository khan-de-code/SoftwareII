package sample;

public class ReportsAppointmentTypes {
    private final int count;
    private final String appointmentName;

    public ReportsAppointmentTypes(int count, String appointmentName) {
        this.count = count;
        this.appointmentName = appointmentName;
    }

    public int getCount() {
        return count;
    }

    public String getAppointmentName() {
        return appointmentName;
    }
}
