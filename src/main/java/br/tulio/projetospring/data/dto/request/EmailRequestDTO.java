package br.tulio.projetospring.data.dto.request;

import java.util.Objects;

public class EmailRequestDTO {

    private String to;
    private String subject;
    private String body;

    public EmailRequestDTO() {}

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmailRequestDTO that)) return false;
        return Objects.equals(getTo(), that.getTo()) && Objects.equals(getSubject(), that.getSubject()) && Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTo(), getSubject(), getBody());
    }
}
