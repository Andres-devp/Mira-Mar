export interface HotelService {
  id: number;
  nombre: string;
  descripcion?: string;
  imageUrl?: string;
  price?: number;
}

export type HotelServiceFormValue = Omit<HotelService, 'id'>;

export interface HotelServiceDeleteResult {
  success: boolean;
  message?: string;
}
