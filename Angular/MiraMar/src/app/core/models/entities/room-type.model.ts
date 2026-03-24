export interface RoomType {
  id: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  urlImagen?: string;
  precioNoche: number;
  capacidad: number;
}

export type RoomTypeFormValue = Omit<RoomType, 'id'>;

export interface RoomTypeDeleteResult {
  success: boolean;
  message?: string;
}
