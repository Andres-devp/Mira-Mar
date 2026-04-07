import { Injectable } from '@angular/core';
import { HotelService, HotelServiceDeleteResult, HotelServiceFormValue } from '../models/entities';

@Injectable({
providedIn: 'root'
})
export class HotelServiceMockService {
private hotelServices: HotelService[] = [
    {
    id: 1,
    nombre: 'Servicio de Limpieza',
    descripcion: 'Limpieza diaria de habitaciones',
    imageUrl: '/images/servicio-limpieza.jpg',
    price: 0
    },
    {
    id: 2,
    nombre: 'Desayuno Buffet',
    descripcion: 'Desayuno incluido con variedad de opciones',
    imageUrl: '/images/desayuno.jpg',
    price: 15
    },
    {
    id: 3,
    nombre: 'Servicio de Lavandería',
    descripcion: 'Lavado y planchado de ropa',
    imageUrl: '/images/lavanderia.jpg',
    price: 10
    }
];

private protectedServiceIds = new Set<number>([1]);

listar(): HotelService[] {
    return this.hotelServices;
}

buscarPorId(id: number): HotelService | undefined {
    return this.hotelServices.find((service) => service.id === id);
}

agregar(payload: HotelServiceFormValue): void {
    const nextId = this.hotelServices.length
    ? Math.max(...this.hotelServices.map((service) => service.id)) + 1
    : 1;

    const created: HotelService = {
    id: nextId,
    ...payload
    };

    this.hotelServices.push(created);
}

editar(id: number, payload: HotelServiceFormValue): boolean {
    const index = this.hotelServices.findIndex((service) => service.id === id);

    if (index < 0) {
    return false;
    }

    const updated: HotelService = {
    id,
    ...payload
    };

    this.hotelServices[index] = updated;

    return true;
}

eliminar(id: number): HotelServiceDeleteResult {
    if (this.protectedServiceIds.has(id)) {
    return {
        success: false,
        message: 'No se puede eliminar el servicio porque es requerido por el sistema.'
    };
    }

    const exists = this.hotelServices.some((service) => service.id === id);

    if (!exists) {
    return {
        success: false,
        message: 'No se encontró el servicio seleccionado.'
    };
    }

    this.hotelServices = this.hotelServices.filter((service) => service.id !== id);

    return {
    success: true
    };
}
}