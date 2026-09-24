package br.edu.fatec.tcc.guaranin.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.Id

@Entity
class URLMonitorada {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @NotBlank
    @Size(max = 255)
    @Column(name = "url", nullable = false, length = 255, unique = true)
    var url: String = ""

    @Size(max = 50)
    @Column(name = "ip_address", nullable = false, length = 50)
    var ipAddress: String = ""
}
