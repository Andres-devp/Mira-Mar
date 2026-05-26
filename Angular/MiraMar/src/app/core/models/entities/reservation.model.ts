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
  roomTypeName?: string;
  clientId?: number;
  clientNombre?: string;
  clientEmail?: string;
  roomId?: number;
  roomNombre?: string;
  roomTypeId?: number;
}

export interface ReservationCreateRequest {
  clientId?: number;
  roomTypeId: number;
  fechaInicio: string;
  fechaFin: string;
  roomTypeName?: string;
  cantidadPersonas: number;
}
