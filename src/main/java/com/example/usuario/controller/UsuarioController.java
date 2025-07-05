package com.example.usuario.controller;

import com.example.usuario.business.dto.EnderecoDTO;
import com.example.usuario.business.dto.TelefoneDTO;
import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.business.service.UsuarioService;
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

    @PostMapping("/endereco")
    public ResponseEntity<EnderecoDTO> novoEndereco(@RequestBody EnderecoDTO dto,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.cadastroNovoEndereco(token, dto));
    }

    @PostMapping("/telefone")
    public ResponseEntity<TelefoneDTO> novoTelefone(@RequestBody TelefoneDTO dto,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.cadastroNovoTelefone(token, dto));

    }


    @GetMapping
    public ResponseEntity<UsuarioDTO> buscarUsuario(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuario(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaPorEmail(@PathVariable String email) {
        usuarioService.deletaPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                                           @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, usuarioDTO));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizaDadosEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                             @RequestParam("id") Long id) {
        return ResponseEntity.ok(usuarioService.atualizaDadosEndereco(id, enderecoDTO));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizaDadosTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                             @RequestParam("id") Long id) {
        return ResponseEntity.ok(usuarioService.atualizaDadosTelefone(id, telefoneDTO));
    }

}
