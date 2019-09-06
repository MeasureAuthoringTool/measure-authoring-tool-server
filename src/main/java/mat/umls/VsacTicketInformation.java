package mat.umls;

import java.util.Date;

public class VsacTicketInformation {

    private String ticket;

    private Date timeout;

    public VsacTicketInformation() {

    }

    public VsacTicketInformation(String eightHourTicketForUser, Date date) {
        this.ticket = eightHourTicketForUser;
        this.timeout = date;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getTimeout() {
        return timeout;
    }

    public void setTimeout(Date timeout) {
        this.timeout = timeout;
    }

}