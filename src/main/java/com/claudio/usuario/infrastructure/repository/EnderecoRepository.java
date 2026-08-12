package com.claudio.usuario.infrastructure.repository;

import com.claudio.aprendendo_spring.infrastructure.entity.Endereco;
//import org.hibernate.boot.models.JpaAnnotations;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
