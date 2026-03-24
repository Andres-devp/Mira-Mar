import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { RoomType, RoomTypeDeleteResult, RoomTypeFormValue } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class RoomTypeMockService {
  private readonly roomTypesSubject = new BehaviorSubject<RoomType[]>([
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
  ]);

  private readonly protectedTypeIds = new Set<number>([1]);

  private get currentValue(): RoomType[] {
    return this.roomTypesSubject.value;
  }

  getAll(): Observable<RoomType[]> {
    return this.roomTypesSubject.asObservable();
  }

  getFeatured(limit = 3): Observable<RoomType[]> {
    return this.getAll().pipe(map((types) => types.slice(0, limit)));
  }

  getById(id: number): Observable<RoomType | undefined> {
    return this.getAll().pipe(map((types) => types.find((type) => type.id === id)));
  }

  create(payload: RoomTypeFormValue): void {
    const nextId = this.currentValue.length
      ? Math.max(...this.currentValue.map((type) => type.id)) + 1
      : 1;

    const created: RoomType = {
      id: nextId,
      ...payload
    };

    this.roomTypesSubject.next([...this.currentValue, created]);
  }

  update(id: number, payload: RoomTypeFormValue): boolean {
    const index = this.currentValue.findIndex((type) => type.id === id);

    if (index < 0) {
      return false;
    }

    const updated: RoomType = {
      id,
      ...payload
    };

    const copy = [...this.currentValue];
    copy[index] = updated;
    this.roomTypesSubject.next(copy);

    return true;
  }

  delete(id: number): RoomTypeDeleteResult {
    if (this.protectedTypeIds.has(id)) {
      return {
        success: false,
        message: 'No se puede eliminar el tipo porque tiene habitaciones asociadas.'
      };
    }

    const exists = this.currentValue.some((type) => type.id === id);

    if (!exists) {
      return {
        success: false,
        message: 'No se encontró el tipo de habitación seleccionado.'
      };
    }

    this.roomTypesSubject.next(this.currentValue.filter((type) => type.id !== id));

    return {
      success: true
    };
  }
}
