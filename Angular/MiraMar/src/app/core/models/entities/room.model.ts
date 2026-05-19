import type { RoomType } from './room-type.model';

export interface Room {
  id: number;
  nombre: string;
  tipoHabitacion?: RoomType;
  tipoHabitacionId?: number;
  roomTypeId?: number;
  roomTypeName?: string;
  capacidad?: number;
  precioNoche?: number;
}
