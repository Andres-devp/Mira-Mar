import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Account, AccountItem, HotelService, Reservation } from '../../../../core/models/entities';
import { AccountItemService } from '../../../../core/services/account-item.service';
import { HotelServiceService } from '../../../../core/services/hotel-service.service';
import { ReservationService } from '../../../../core/services/reservation.service';

@Component({
  selector: 'app-reservation-detail',
  templateUrl: './reservation-detail.component.html',
  styleUrls: ['./reservation-detail.component.css']
})
export class ReservationDetailComponent implements OnInit {
  reservation?: Reservation;
  actionLoading = false;
  actionError = '';

  account?: Account;
  payLoading = false;
  payError = '';

  items: AccountItem[] = [];
  itemsError = '';

  paidItems: AccountItem[] = [];
  paidItemsError = '';

  showModal = false;
  availableServices: HotelService[] = [];
  serviceQuantities: Record<number, number> = {};
  modalLoading = false;
  modalError = '';

  get canEditServices(): boolean {
    return (this.reservation?.estado === 'PENDING' || this.reservation?.estado === 'ACTIVE')
      && this.account?.estado !== 'CLOSED';
  }

  get canInactivar(): boolean {
    return !this.account || this.account.estado === 'CLOSED';
  }

  constructor(
    private route: ActivatedRoute,
    private reservationService: ReservationService,
    private accountItemService: AccountItemService,
    private hotelServiceService: HotelServiceService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = Number(params.get('id'));
      if (!id) return;
      this.reservationService.getById(id).subscribe({
        next: (data) => {
          this.reservation = data;
          this.loadItems(id);
          this.loadPaidItems(id);
          this.loadAccount(id);
        },
        error: (err) => console.error('Error loading reservation detail:', err)
      });
    });
  }

  loadItems(reservationId: number): void {
    this.accountItemService.getByReservation(reservationId).subscribe({
      next: (data) => { this.items = data; },
      error: () => { this.itemsError = 'No se pudieron cargar los servicios adicionales.'; }
    });
  }

  loadPaidItems(reservationId: number): void {
    this.accountItemService.getPaidByReservation(reservationId).subscribe({
      next: (data) => { this.paidItems = data; },
      error: () => { this.paidItemsError = 'No se pudieron cargar los servicios pagados.'; }
    });
  }

  loadAccount(reservationId: number): void {
    this.reservationService.getAccount(reservationId).subscribe({
      next: (data) => { this.account = data; },
      error: (err) => { if (err.status !== 404) console.error('Error loading account:', err); }
    });
  }

  get totalSubtotal(): number {
    return this.items.reduce((sum, item) => sum + item.subtotal, 0);
  }

  activar(): void { this.changeEstado('ACTIVE'); }
  cancelar(): void { this.changeEstado('CANCELED'); }
  inactivar(): void { this.changeEstado('INACTIVE'); }

  private changeEstado(nuevoEstado: string): void {
    if (!this.reservation) return;
    this.actionLoading = true;
    this.actionError = '';
    this.reservationService.updateEstado(this.reservation.id, nuevoEstado).subscribe({
      next: (updated) => {
        this.actionLoading = false;
        this.reservation = updated;
      },
      error: (err) => {
        this.actionLoading = false;
        this.actionError = err.error?.error || 'No se pudo actualizar el estado.';
      }
    });
  }

  updateCantidad(item: AccountItem, delta: number): void {
    if (!this.reservation) return;
    const nueva = item.cantidad + delta;
    if (nueva <= 0) return;
    this.accountItemService.updateCantidad(this.reservation.id, item.id, nueva).subscribe({
      next: (updated) => {
        const idx = this.items.findIndex(i => i.id === updated.id);
        if (idx !== -1) this.items[idx] = updated;
        this.loadPaidItems(this.reservation!.id);
      },
      error: () => { this.itemsError = 'No se pudo actualizar la cantidad.'; }
    });
  }

  removeItem(itemId: number): void {
    if (!this.reservation) return;
    this.accountItemService.remove(this.reservation.id, itemId).subscribe({
      next: () => {
        this.items = this.items.filter(i => i.id !== itemId);
        this.loadPaidItems(this.reservation!.id);
      },
      error: () => { this.itemsError = 'No se pudo eliminar el servicio.'; }
    });
  }

  openModal(): void {
    this.modalError = '';
    this.showModal = true;
    this.hotelServiceService.getAll().subscribe({
      next: (services) => {
        this.availableServices = services;
        const qtys: Record<number, number> = {};
        services.forEach(s => { qtys[s.id] = 1; });
        this.serviceQuantities = qtys;
      },
      error: () => { this.modalError = 'No se pudieron cargar los servicios.'; }
    });
  }

  closeModal(): void {
    this.showModal = false;
    this.availableServices = [];
  }

  incrementQty(serviceId: number): void {
    this.serviceQuantities[serviceId] = (this.serviceQuantities[serviceId] || 1) + 1;
  }

  decrementQty(serviceId: number): void {
    const current = this.serviceQuantities[serviceId] || 1;
    if (current > 1) this.serviceQuantities[serviceId] = current - 1;
  }

  addService(service: HotelService): void {
    if (!this.reservation) return;
    const cantidad = this.serviceQuantities[service.id] || 1;
    this.modalLoading = true;
    this.accountItemService.add(this.reservation.id, service.id, cantidad).subscribe({
      next: (newItem) => {
        this.items.push(newItem);
        this.modalLoading = false;
        this.loadAccount(this.reservation!.id);
        this.loadPaidItems(this.reservation!.id);
        this.closeModal();
      },
      error: (err) => {
        this.modalLoading = false;
        this.modalError = err.error?.error || 'No se pudo agregar el servicio.';
      }
    });
  }

  pay(): void {
    if (!this.reservation) return;
    this.payLoading = true;
    this.payError = '';
    this.reservationService.payAccount(this.reservation.id).subscribe({
      next: (updated) => {
        this.payLoading = false;
        this.account = updated;
        this.loadItems(this.reservation!.id);
        this.loadPaidItems(this.reservation!.id);
      },
      error: (err) => {
        this.payLoading = false;
        this.payError = err.error?.error || 'No se pudo procesar el pago.';
      }
    });
  }
}
