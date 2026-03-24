export interface Reservation {
  id: number;
  fechaInicio: string;
  fechaFin: string;
  cantidadPersonas: number;
  estado: string;
  createdAt: string;
  canceledAt?: string;
  clientId: number;
  roomId: number;
}
