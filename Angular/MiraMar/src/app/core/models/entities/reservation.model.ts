import type { Client } from './client.model';
import type { Room } from './room.model';

export interface Reservation {
  id: number;
  fechaInicio: string;
  fechaFin: string;
  cantidadPersonas: number;
  estado: string;
  createdAt: string;
  canceledAt?: string;
  client?: Client;
  room?: Room;
  clientId?: number;
  roomId?: number;
}

export interface ReservationCreateRequest {
  clientId?: number;
  roomTypeId: number;
  fechaInicio: string;
  fechaFin: string;
  cantidadPersonas: number;
}
