DROP DATABASE IF EXISTS guaranin;

CREATE DATABASE guaranin
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE guaranin;

-- =====================================================
-- USUARIO
-- =====================================================

CREATE TABLE tb_usuario (
    id_usuario BINARY(16) NOT NULL,

    apelido VARCHAR(26) NOT NULL,

    email VARCHAR(254) NOT NULL,

    password VARCHAR(255) NOT NULL,

    PRIMARY KEY (id_usuario),

    UNIQUE KEY uk_usuario_email (email)
);

-- =====================================================
-- MASCOTE
-- =====================================================

CREATE TABLE tb_mascote (
    id_mascote BINARY(16) NOT NULL,

    id_usuario BINARY(16) NOT NULL,

    nome VARCHAR(255),

    estado VARCHAR(50) NOT NULL,

    humor VARCHAR(25) NOT NULL,

    nivel INT NOT NULL,

    experiencia INT NOT NULL,

    pontos_vida INT NOT NULL,

    data_morte DATE,

    PRIMARY KEY (id_mascote),

    CONSTRAINT fk_mascote_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES tb_usuario(id_usuario)
        ON DELETE CASCADE
);

-- =====================================================
-- META
-- =====================================================

CREATE TABLE tb_meta (
    id_meta BINARY(16) NOT NULL,

    id_usuario_criador BINARY(16) NOT NULL,

    titulo VARCHAR(50) NOT NULL,

    descricao VARCHAR(200) NOT NULL,

    vencimento_dias INT NOT NULL,

    limite_acesso SMALLINT,

    tempo_uso INT,

    PRIMARY KEY (id_meta),

    CONSTRAINT fk_meta_criador
        FOREIGN KEY (id_usuario_criador)
        REFERENCES tb_usuario(id_usuario)
        ON DELETE CASCADE
);

-- =====================================================
-- USUARIO_META
-- =====================================================

CREATE TABLE tb_usuario_meta (
    id_usuario_meta BINARY(16) NOT NULL,

    id_usuario BINARY(16) NOT NULL,

    id_meta BINARY(16) NOT NULL,

    status VARCHAR(25) NOT NULL,

    data_inicio DATE,

    data_conclusao DATE,

    PRIMARY KEY (id_usuario_meta),

    UNIQUE KEY uk_usuario_meta (
        id_usuario,
        id_meta
    ),

    CONSTRAINT fk_usuario_meta_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES tb_usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_meta_meta
        FOREIGN KEY (id_meta)
        REFERENCES tb_meta(id_meta)
        ON DELETE CASCADE
);

-- =====================================================
-- URL MONITORADA
-- =====================================================

CREATE TABLE tb_url_monitorado (
    id_url_monitorado BINARY(16) NOT NULL,

    link VARCHAR(4050) NOT NULL,

    ip VARCHAR(50),

    padrao BOOLEAN NOT NULL,

    PRIMARY KEY (id_url_monitorado),

    UNIQUE KEY uk_url_link (link)
);

-- =====================================================
-- USUARIO_URL
-- =====================================================

CREATE TABLE tb_usuario_url (
    id_usuario_url BINARY(16) NOT NULL,

    id_usuario BINARY(16) NOT NULL,

    id_url_monitorado BINARY(16) NOT NULL,

    qtd_acesso SMALLINT NOT NULL,

    tempo_ativo INT NOT NULL,

    PRIMARY KEY (id_usuario_url),

    UNIQUE KEY uk_usuario_url (
        id_usuario,
        id_url_monitorado
    ),

    CONSTRAINT fk_usuario_url_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES tb_usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_url_url
        FOREIGN KEY (id_url_monitorado)
        REFERENCES tb_url_monitorado(id_url_monitorado)
        ON DELETE CASCADE
);

-- =====================================================
-- URL HISTORICO
-- =====================================================

CREATE TABLE tb_url_historico (
    id_url_historico BINARY(16) NOT NULL,

    id_usuario_url BINARY(16) NOT NULL,

    data_hora_acesso DATETIME NOT NULL,

    PRIMARY KEY (id_url_historico),

    CONSTRAINT fk_url_historico_usuario_url
        FOREIGN KEY (id_usuario_url)
        REFERENCES tb_usuario_url(id_usuario_url)
        ON DELETE CASCADE
);

-- =====================================================
-- APLICATIVO MONITORADO
-- =====================================================

CREATE TABLE tb_aplicativo_monitorado (
    id_aplicativo_monitorado BINARY(16) NOT NULL,

    pacote VARCHAR(255) NOT NULL,

    nome VARCHAR(50) NOT NULL,

    padrao BOOLEAN NOT NULL,

    PRIMARY KEY (id_aplicativo_monitorado),

    UNIQUE KEY uk_aplicativo_pacote (pacote)
);

-- =====================================================
-- USUARIO_APLICATIVO
-- =====================================================

CREATE TABLE tb_usuario_aplicativo (
    id_usuario_aplicativo BINARY(16) NOT NULL,

    id_usuario BINARY(16) NOT NULL,

    id_aplicativo_monitorado BINARY(16) NOT NULL,

    qtd_acesso SMALLINT NOT NULL,

    tempo_ativo INT NOT NULL,

    PRIMARY KEY (id_usuario_aplicativo),

    UNIQUE KEY uk_usuario_aplicativo (
        id_usuario,
        id_aplicativo_monitorado
    ),

    CONSTRAINT fk_usuario_aplicativo_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES tb_usuario(id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_aplicativo_aplicativo
        FOREIGN KEY (id_aplicativo_monitorado)
        REFERENCES tb_aplicativo_monitorado(id_aplicativo_monitorado)
        ON DELETE CASCADE
);

-- =====================================================
-- APLICATIVO HISTORICO
-- =====================================================

CREATE TABLE tb_aplicativo_historico (
    id_aplicativo_historico BINARY(16) NOT NULL,

    id_usuario_aplicativo BINARY(16) NOT NULL,

    data_hora_acesso DATETIME NOT NULL,

    PRIMARY KEY (id_aplicativo_historico),

    CONSTRAINT fk_aplicativo_historico_usuario_aplicativo
        FOREIGN KEY (id_usuario_aplicativo)
        REFERENCES tb_usuario_aplicativo(id_usuario_aplicativo)
        ON DELETE CASCADE
);

-- =====================================================
-- HISTORICO DE SESSOES
-- =====================================================

CREATE TABLE historico_sessoes_uso (
    id BIGINT NOT NULL AUTO_INCREMENT,

    usuario_id BINARY(16) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_historico_sessao_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES tb_usuario(id_usuario)
        ON DELETE CASCADE
);