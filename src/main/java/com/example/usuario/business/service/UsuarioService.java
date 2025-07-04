package com.example.usuario.business.service;

import com.example.usuario.business.converter.UsuarioConverter;
import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.infrastructure.entity.Usuario;
import com.example.usuario.infrastructure.exception.ConflictException;
import com.example.usuario.infrastructure.exception.ResourceNotFound;
import com.example.usuario.infrastructure.repository.UsuarioRepository;
import com.example.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

    public Usuario buscarUsuario(String email) {
        return usuarioRepository.findByEmail(email).
                orElseThrow(() -> new ResourceNotFound("Email não encontrado " + email));
    }


    public void deletaPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);

    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {

        // Extrai o email do token(Retirando a obrigatoriedade de passar o email)
        String email = jwtUtil.extractUsername(token.substring(7));

        // Criptografa a senha novamente caso tenha passado uma nova senha, se não, mantém o que já tinha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        // Busca os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.
                findByEmail(email).orElseThrow(
                        () -> new ResourceNotFound("Email não localizado"));

        // Mesclou os dados recebidos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.atualizaUsuario(usuarioDTO, usuarioEntity);

        // Salvou os dados do usuario convertido e depois pegou o retorno e converteu como DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }

}
