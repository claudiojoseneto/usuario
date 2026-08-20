package com.claudio.usuario.controller;

import com.claudio.usuario.business.UsuarioService;
import com.claudio.usuario.business.dto.UsuarioDTO;
import com.claudio.usuario.infrastructure.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsario(@RequestBody UsuarioDTO usuarioDTO){
    return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));

    }
}
