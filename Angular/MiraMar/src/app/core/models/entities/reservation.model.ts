export interface Reservation {
  id: number;
  fechaInicio: string;
  fechaFin: string;
  cantidadPersonas: number;
  estado: string;
  createdAt: string;
  canceledAt?: string;
  client?: {
    id: number;
    nombre: string;
    usuario: string;
    email: string;
  };
  room?: {
    id: number;
    nombre: string;
  };
}
