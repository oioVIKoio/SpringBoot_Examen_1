package pe.edu.tecsup.examen1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_recuperacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRecuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    public TokenRecuperacion(String token, Usuario usuario, LocalDateTime fechaExpiracion) {
        this.token = token;
        this.usuario = usuario;
        this.fechaExpiracion = fechaExpiracion;
    }
}