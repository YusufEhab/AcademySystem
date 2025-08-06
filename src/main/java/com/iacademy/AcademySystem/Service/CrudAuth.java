package com.iacademy.AcademySystem.Service;

import com.iacademy.AcademySystem.Dto.AuthLogin;
import com.iacademy.AcademySystem.Dto.AuthResponse;
import com.iacademy.AcademySystem.Dto.RegisterRequest;

public interface CrudAuth {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthLogin request);
}
