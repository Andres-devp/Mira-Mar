import { RoomType } from './room-type.model';

export interface Room {
  id: number;
  nombre: string;
  tipoHabitacion?: RoomType;
}
