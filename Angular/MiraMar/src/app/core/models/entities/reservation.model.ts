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
  clientNombre?: string;
  roomNombre?: string;
}

export interface ReservationCreateRequest {
  clientId?: number;
  roomTypeId: number;
  fechaInicio: string;
  fechaFin: string;
  roomTypeName?: string;
  cantidadPersonas: number;
}
