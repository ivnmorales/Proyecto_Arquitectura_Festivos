package calendario.api.infraestructura.persistencia.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo")
public class TipoEntidad {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;

    public TipoEntidad() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
