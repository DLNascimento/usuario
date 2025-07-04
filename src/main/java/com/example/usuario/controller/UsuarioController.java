package com.example.usuario.controller;

import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.business.service.UsuarioService;
import com.example.usuario.infrastructure.entity.Usuario;
import com.example.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {


    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {

        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));

    }

    @PostMapping("/login")
    public String logarUsuario(@RequestBody UsuarioDTO usuarioDTO) {

        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(),
                        usuarioDTO.getSenha()));
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<Usuario> buscarUsuario(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuario(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaPorEmail(@PathVariable String email){
        usuarioService.deletaPorEmail(email);
        return ResponseEntity.ok().build();
    }

}
