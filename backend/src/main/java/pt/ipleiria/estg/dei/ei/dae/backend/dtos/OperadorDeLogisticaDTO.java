package pt.ipleiria.estg.dei.ei.dae.backend.dtos;

import java.io.Serializable;

public class OperadorDeLogisticaDTO implements Serializable {
    private String username;
    private String password;
    private String name;
    private String email;

    public OperadorDeLogisticaDTO() {
    }

    public OperadorDeLogisticaDTO(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
