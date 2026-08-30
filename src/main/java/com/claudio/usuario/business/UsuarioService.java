package com.claudio.usuario.business;

import com.claudio.usuario.business.converter.UsuarioConverter;
import com.claudio.usuario.business.dto.EnderecoDTO;
import com.claudio.usuario.business.dto.TelefoneDTO;
import com.claudio.usuario.business.dto.UsuarioDTO;
import com.claudio.usuario.infrastructure.entity.Endereco;
import com.claudio.usuario.infrastructure.entity.Telefone;
import com.claudio.usuario.infrastructure.entity.Usuario;
import com.claudio.usuario.infrastructure.exceptions.ConflictException;
import com.claudio.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.claudio.usuario.infrastructure.repository.EnderecoRepository;
import com.claudio.usuario.infrastructure.repository.TelefoneRepository;
import com.claudio.usuario.infrastructure.repository.UsuarioRepository;
import com.claudio.usuario.infrastructure.security.JwtUtil;
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

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public void emailExiste(String email){
        try {
            boolean existe = vericaEmailExistente(email);
            if (existe)
                throw new ConflictException("Email já cadastrado" + email);
        }catch (ConflictException e){
            throw new ConflictException("E-mail já cadastrado", e.getCause());
        }
    }
    public boolean vericaEmailExistente (String email){
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        try{
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.findByEmail(email)
                        .orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado " + email)
                        )
        );
    }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);

    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        //Aqui, buscamos o e-mail do usuário através do token (tirar obrigatoriedade do e-mail )
        String email = jwtUtil.extrairEmailToken(token.substring(7));


        //Criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null );

        //Busca os dados do usuário no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));


        //Mesclou os dados que recebemos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);


        //Salvou dos dados do usuário convertido e depois pegou o retorno e converteu para usuário DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id nãoencontrado" + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO,entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));

    }
    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO){

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Id nãoencontrado" + idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));

    }


}
