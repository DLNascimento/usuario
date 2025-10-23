package com.example.usuario.business.service;

import com.example.usuario.business.converter.UsuarioConverter;
import com.example.usuario.business.dto.EnderecoDTO;
import com.example.usuario.business.dto.TelefoneDTO;
import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.infrastructure.entity.Endereco;
import com.example.usuario.infrastructure.entity.Telefone;
import com.example.usuario.infrastructure.entity.Usuario;
import com.example.usuario.infrastructure.exception.ConflictException;
import com.example.usuario.infrastructure.exception.ResourceNotFoundException;
import com.example.usuario.infrastructure.repository.EnderecoRepository;
import com.example.usuario.infrastructure.repository.TelefoneRepository;
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
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
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

    public UsuarioDTO buscarUsuario(String email) {

        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).
                    orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email)));

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
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
                        () -> new ResourceNotFoundException("Email não localizado"));

        // Mesclou os dados recebidos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.atualizaUsuario(usuarioDTO, usuarioEntity);

        // Salvou os dados do usuario convertido e depois pegou o retorno e converteu como DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }

    public EnderecoDTO atualizaDadosEndereco(Long idEndereco, EnderecoDTO dto) {

        Endereco entity = enderecoRepository.findById(idEndereco).
                orElseThrow(() -> new ResourceNotFoundException("Id não encontrado" + idEndereco));

        Endereco endereco = usuarioConverter.atualizaEndereco(dto, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));

    }

    public TelefoneDTO atualizaDadosTelefone(Long idTelefone, TelefoneDTO dto) {

        Telefone entity = telefoneRepository.findById(idTelefone).
                orElseThrow(() -> new ResourceNotFoundException("Id não encontrado" + idTelefone));
        Telefone telefone = usuarioConverter.atualizaTelefone(dto, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastroNovoEndereco(String token, EnderecoDTO dto) {

        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Email não encontrado " + email));
        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        Endereco enderecoEntity = enderecoRepository.save(endereco);
        return usuarioConverter.paraEnderecoDTO(enderecoEntity);
    }

    public TelefoneDTO cadastroNovoTelefone(String token, TelefoneDTO dto){

        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(
                "Email não encontrado " + email));
        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        Telefone telefoneEntity = telefoneRepository.save(telefone);
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);
    }

}
