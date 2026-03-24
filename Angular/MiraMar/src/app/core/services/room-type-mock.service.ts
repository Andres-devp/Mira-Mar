import { Injectable } from '@angular/core';
import { RoomType, RoomTypeDeleteResult, RoomTypeFormValue } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class RoomTypeMockService {
  private roomTypes: RoomType[] = [
    {
      id: 1,
      codigo: 'ESTANDAR',
      nombre: 'Habitación Estándar',
      descripcion: 'Habitación básica confortable para una o dos personas',
      urlImagen: '/images/Habitacion1.avif',
      precioNoche: 80,
      capacidad: 2
    },
    {
      id: 2,
      codigo: 'DOBLE',
      nombre: 'Habitación Doble',
      descripcion: 'Amplia habitación con cama doble y amenidades',
      urlImagen: '/images/Habitacion2.avif',
      precioNoche: 120,
      capacidad: 2
    },
    {
      id: 3,
      codigo: 'SUITE',
      nombre: 'Suite Ejecutiva',
      descripcion: 'Suite de lujo con sala de estar y vistas al mar',
      urlImagen: '/images/Habitacion3.avif',
      precioNoche: 200,
      capacidad: 3
    },
    {
      id: 4,
      codigo: 'FAMILIAR',
      nombre: 'Habitación Familiar',
      descripcion: 'Espaciosa habitación para familias con múltiples camas',
      urlImagen: '/images/habitacionFamiliar.jpg',
      precioNoche: 180,
      capacidad: 4
    },
    {
      id: 5,
      codigo: 'PRESIDENCIAL',
      nombre: 'Suite Presidencial',
      descripcion: 'La mejor suite del hotel con lujos y servicios premium',
      urlImagen: '/images/Ocean View Interior.avif',
      precioNoche: 350,
      capacidad: 4
    }
  ];

  private protectedTypeIds = new Set<number>([1]);

  listar(): RoomType[] {
    return this.roomTypes;
  }

  listarDestacados(limit = 3): RoomType[] {
    return this.roomTypes.slice(0, limit);
  }

  buscarPorId(id: number): RoomType | undefined {
    return this.roomTypes.find((type) => type.id === id);
  }

  agregar(payload: RoomTypeFormValue): void {
    const nextId = this.roomTypes.length
      ? Math.max(...this.roomTypes.map((type) => type.id)) + 1
      : 1;

    const created: RoomType = {
      id: nextId,
      ...payload
    };

    this.roomTypes.push(created);
  }

  editar(id: number, payload: RoomTypeFormValue): boolean {
    const index = this.roomTypes.findIndex((type) => type.id === id);

    if (index < 0) {
      return false;
    }

    const updated: RoomType = {
      id,
      ...payload
    };

    this.roomTypes[index] = updated;

    return true;
  }

  eliminar(id: number): RoomTypeDeleteResult {
    if (this.protectedTypeIds.has(id)) {
      return {
        success: false,
        message: 'No se puede eliminar el tipo porque tiene habitaciones asociadas.'
      };
    }

    const exists = this.roomTypes.some((type) => type.id === id);

    if (!exists) {
      return {
        success: false,
        message: 'No se encontró el tipo de habitación seleccionado.'
      };
    }

    this.roomTypes = this.roomTypes.filter((type) => type.id !== id);

    return {
      success: true
    };
  }
}
