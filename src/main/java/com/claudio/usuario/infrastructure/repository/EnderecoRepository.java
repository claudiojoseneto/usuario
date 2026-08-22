package com.claudio.usuario.infrastructure.repository;


//import org.hibernate.boot.models.JpaAnnotations;
//import org.springframework.data.jpa.repository.JpaRepository;
import com.claudio.usuario.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
