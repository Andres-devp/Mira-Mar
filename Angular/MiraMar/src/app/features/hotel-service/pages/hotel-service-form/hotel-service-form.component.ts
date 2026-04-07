import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelServiceFormValue } from '../../../../core/models/entities';
import { HotelServiceMockService } from '../../../../core/services/hotel-service-mock.service';

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
    private hotelServiceService: HotelServiceMockService
) {}

ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
        const id = params.get('id');
        this.isEditMode = !!id;
        this.currentId = id ? Number(id) : null;

        if (!this.isEditMode || this.currentId === null) {
            return;
        }

    const servicio = this.hotelServiceService.buscarPorId(this.currentId);

    if (!servicio) {
        this.notFound = true;
        return;
    }

    this.notFound = false;
    this.servicioForm.patchValue({
        nombre: servicio.nombre,
        descripcion: servicio.descripcion || '',
        price: servicio.price || 0,
        imageUrl: servicio.imageUrl || ''
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
    const payload: HotelServiceFormValue = {
        nombre: raw.nombre.trim(),
        descripcion: raw.descripcion.trim(),
        price: Number(raw.price),
        imageUrl: raw.imageUrl.trim() || '/images/servicio.jpg'
    };

    if (this.isEditMode && this.currentId !== null) {
        this.hotelServiceService.editar(this.currentId, payload);
    } else {
        this.hotelServiceService.agregar(payload);
    }

    this.router.navigate(['/services']);
}
}