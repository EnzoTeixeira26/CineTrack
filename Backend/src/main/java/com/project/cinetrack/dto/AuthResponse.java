package com.project.cinetrack.dto;

import com.project.cinetrack.modeloBD.Usuario;

public record AuthResponse(String token, Usuario usuario) {}