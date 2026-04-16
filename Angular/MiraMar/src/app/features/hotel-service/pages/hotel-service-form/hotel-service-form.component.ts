import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelService } from '../../../../core/models/entities';
import { HotelServiceService } from '../../../../core/services/hotel-service.service';

@Component({
    selector: 'app-hotel-service-form',
    templateUrl: './hotel-service-form.component.html',
    styleUrls: ['./hotel-service-form.component.css']
})
export class HotelServiceFormComponent implements OnInit {
    isEditMode = false;
    currentId: number | null = null;
    notFound = false;

  servicioForm = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    descripcion: ['', [Validators.maxLength(500)]],
    price: [0, [Validators.min(0)]],
    imageUrl: ['', [Validators.maxLength(255)]]
  });

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private hotelServiceService: HotelServiceService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      this.isEditMode = !!id;
      this.currentId = id ? Number(id) : null;

      if (!this.isEditMode || this.currentId === null) {
        return;
      }

      this.hotelServiceService.getById(this.currentId).subscribe({
        next: (servicio) => {
          this.notFound = false;
          this.servicioForm.patchValue({
            nombre: servicio.nombre,
            descripcion: servicio.descripcion || '',
            price: servicio.price || 0,
            imageUrl: servicio.imageUrl || ''
          });
        },
        error: () => {
          this.notFound = true;
        }
      });
    });
  }

  get pageTitle(): string {
    return this.isEditMode ? 'Editar Servicio' : 'Nuevo Servicio';
  }

  get submitLabel(): string {
    return this.isEditMode ? 'Guardar cambios' : 'Crear servicio';
  }

  save(): void {
    if (this.servicioForm.invalid) {
      this.servicioForm.markAllAsTouched();
      return;
    }

    const raw = this.servicioForm.getRawValue();
    const payload: any = {
      nombre: raw.nombre.trim(),
      descripcion: raw.descripcion.trim(),
      price: Number(raw.price),
      imageUrl: raw.imageUrl.trim() || '/images/servicio.jpg'
    };

    if (this.isEditMode && this.currentId !== null) {
      this.hotelServiceService.update(this.currentId, payload).subscribe({
        next: () => this.router.navigate(['/services/table']),
        error: (err) => console.error('Error updating service:', err)
      });
    } else {
      this.hotelServiceService.create(payload).subscribe({
        next: () => this.router.navigate(['/services/table']),
        error: (err) => console.error('Error creating service:', err)
      });
    }
  }
}