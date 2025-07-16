package br.tulio.projetospring.data.dto.security;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private String fullName;

    public AccountCredentialsDTO() {}

    public AccountCredentialsDTO(String userName, String password, String fullName) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AccountCredentialsDTO that)) return false;
        return Objects.equals(getUserName(), that.getUserName()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getFullName(), that.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getPassword(), getFullName());
    }
}
