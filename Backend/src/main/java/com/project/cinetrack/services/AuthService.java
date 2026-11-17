package com.project.cinetrack.services;

import com.project.cinetrack.dto.AuthResponse;
import com.project.cinetrack.dto.LoginRequest;
import com.project.cinetrack.dto.RegisterRequest;
import com.project.cinetrack.exceptions.BusinessException;
import com.project.cinetrack.modeloBD.Usuario;
import com.project.cinetrack.repositories.UsuarioRepository;
import com.project.cinetrack.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ListaService listaService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       ListaService listaService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.listaService = listaService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.email())) {
            throw new BusinessException("Já existe um usuário com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(req.nome());
        usuario.setEmail(req.email());
        usuario.setSenhaHash(passwordEncoder.encode(req.senha()));
        usuario.setProvider("LOCAL");
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(usuario);

        try {
            listaService.criar(salvo.getId(), "Favoritos", "FAVORITOS", null, false);
            listaService.criar(salvo.getId(), "Watchlist", "WATCHLIST", null, false);
        } catch (Exception ignored) {
        }

        String token = jwtService.gerarToken(salvo);
        return new AuthResponse(token, salvo);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.senha()));
        } catch (BadCredentialsException e) {
            throw new BusinessException("E-mail ou senha inválidos.");
        }

        Usuario usuario = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new BusinessException("E-mail ou senha inválidos."));

        String token = jwtService.gerarToken(usuario);
        return new AuthResponse(token, usuario);
    }
}