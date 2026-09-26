package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.validation.ValidIp
import br.edu.fatec.tcc.guaranin.validation.ValidUrl
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class URLMonitoradaCreateDTO(
    @NotBlank
    @ValidUrl
    @Size(max = 255)
    val url: String,

    @NotBlank
    @Size(max = 50)
    @ValidIp
    val ipAddress: String
)
