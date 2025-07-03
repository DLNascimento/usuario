package com.example.usuario.controller;

import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.business.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {


    private final UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(UsuarioDTO usuarioDTO) {

        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));

    }


}
