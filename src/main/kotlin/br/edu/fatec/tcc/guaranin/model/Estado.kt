package br.edu.fatec.tcc.guaranin.model

enum class Estado(val estado: String) {
    SAUDAVEL("SAUDAVEL"),
    NEUTRO("NEUTRO"),
    DOENTE("DOENTE"),
    MORTO("MORTO")
}