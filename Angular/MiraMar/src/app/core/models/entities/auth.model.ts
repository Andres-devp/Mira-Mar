export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  rol: string;
}

export interface RegisterRequest {
  nombre: string;
  usuario: string;
  email: string;
  contrasena: string;
  contrasenaConfirm: string;
}

export interface RegisterResponse {
  mensaje: string;
}

export interface AuthErrorResponse {
  error: string;
}
