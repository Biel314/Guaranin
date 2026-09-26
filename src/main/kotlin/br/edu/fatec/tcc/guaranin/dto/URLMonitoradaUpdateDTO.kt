package br.edu.fatec.tcc.guaranin.dto

import br.edu.fatec.tcc.guaranin.validation.ValidIp
import br.edu.fatec.tcc.guaranin.validation.ValidUrl
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class URLMonitoradaUpdateDTO(
    @NotNull
    var id: Long,

    @NotBlank
    @ValidUrl
    @Size(max = 255)
    var url: String,

    @NotBlank
    @ValidIp
    @Size(max = 50)
    var ipAddress: String,
)
