export interface Client {
  id: number;
  nombre: string;
  usuario: string;
  contrasena?: string;
  email: string;
  telefono?: string;
  fotoPerfil?: string;
}
