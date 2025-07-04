package com.example.usuario.business.service;

import com.example.usuario.business.converter.UsuarioConverter;
import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.infrastructure.entity.Usuario;
import com.example.usuario.infrastructure.exception.ConflictException;
import com.example.usuario.infrastructure.exception.ResourceNotFound;
import com.example.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        System.out.println("SENHA RECEBIDA: " + usuarioDTO.getSenha());
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);

    }

    public void emailExiste(String email) {

        try {
            boolean existe = validarEmail(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado! " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado! " + e.getCause());
        }

    }

    public boolean validarEmail(String email) {

        return usuarioRepository.existsByEmail(email);

    }

    public Usuario buscarUsuario(String email){
        return usuarioRepository.findByEmail(email).
                orElseThrow(() -> new ResourceNotFound("Email não encontrado " + email));
    }


}
