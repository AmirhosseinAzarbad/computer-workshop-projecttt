package ir.speedy.computerworkshopproject.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class AdminRegPass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String adminRegistrationPassword;

    public AdminRegPass(String adminRegistrationPassword) {
        this.adminRegistrationPassword = adminRegistrationPassword;
    }
}
