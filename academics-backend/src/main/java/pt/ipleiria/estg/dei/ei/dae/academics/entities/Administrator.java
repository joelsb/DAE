package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;

import java.io.Serializable;

//@Table(name="Administrators")
@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT a FROM Administrator a ORDER BY a.name" // JPQL
        )
})
public class Administrator extends User implements Serializable {
    public Administrator() {
    }

    public Administrator(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}
