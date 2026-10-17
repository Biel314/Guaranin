package br.edu.fatec.tcc.guaranin.exception

import jakarta.persistence.EntityNotFoundException

/**
 * Exceção personalizada disparada quando um [br.edu.fatec.tcc.guaranin.model.Usuario]
 * não é encontrado no repositório de dados.
 *
 * Herda de [EntityNotFoundException] e pode ser tratada na camada de controle
 * para retornar o status HTTP 404 (Not Found) por meio de [org.springframework.http.ResponseEntity].
 *
 * @property message mensagem descritiva da exceção, com valor padrão "Usuario not found!"
 */
class UsuarioNotFoundException(
    message: String = "Usuario not found!"
) : EntityNotFoundException(message)