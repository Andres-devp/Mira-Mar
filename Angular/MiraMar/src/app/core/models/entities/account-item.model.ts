import type { HotelService } from './hotel-service.model';

export interface AccountItem {
  id: number;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  createdAt: string;
  eliminado: boolean;
  accountId?: number;
  hotelServiceId?: number;
  hotelServiceNombre?: string;
  hotelServicePrecio?: number;
  hotelService?: HotelService;
}
