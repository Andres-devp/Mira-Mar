export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  userId: number;
  role: string;
  username: string;
  expiresIn: number;
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

