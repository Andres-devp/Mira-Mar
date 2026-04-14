export interface AccountItem {
  id: number;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  createdAt: string;
  eliminado: boolean;
  accountId?: number;
  hotelServiceId?: number;
}
