import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { RoomTypeFormValue } from '../../../../core/models/entities';
import { RoomTypeMockService } from '../../../../core/services/room-type-mock.service';

@Component({
  selector: 'app-room-type-form',
  templateUrl: './room-type-form.component.html',
  styleUrls: ['./room-type-form.component.css']
})
export class RoomTypeFormComponent implements OnInit, OnDestroy {
  isEditMode = false;
  currentId: number | null = null;
  notFound = false;

  readonly tipoForm = this.fb.nonNullable.group({
    codigo: ['', [Validators.required, Validators.maxLength(50)]],
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    descripcion: ['', [Validators.maxLength(500)]],
    precioNoche: [0, [Validators.required, Validators.min(0)]],
    capacidad: [1, [Validators.required, Validators.min(1), Validators.max(10)]],
    urlImagen: ['', [Validators.maxLength(255)]]
  });

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly roomTypeService: RoomTypeMockService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = params.get('id');
      this.isEditMode = !!id;
      this.currentId = id ? Number(id) : null;

      if (!this.isEditMode || this.currentId === null) {
        this.tipoForm.controls.codigo.enable();
        return;
      }

      this.roomTypeService
        .getById(this.currentId)
        .pipe(takeUntil(this.destroy$))
        .subscribe((tipo) => {
          if (!tipo) {
            this.notFound = true;
            return;
          }

          this.notFound = false;
          this.tipoForm.patchValue({
            codigo: tipo.codigo,
            nombre: tipo.nombre,
            descripcion: tipo.descripcion || '',
            precioNoche: tipo.precioNoche,
            capacidad: tipo.capacidad,
            urlImagen: tipo.urlImagen || ''
          });
          this.tipoForm.controls.codigo.disable();
        });
    });
  }

  get pageTitle(): string {
    return this.isEditMode ? 'Editar Tipo' : 'Nuevo Tipo';
  }

  get submitLabel(): string {
    return this.isEditMode ? 'Guardar cambios' : 'Crear tipo';
  }

  save(): void {
    if (this.tipoForm.invalid) {
      this.tipoForm.markAllAsTouched();
      return;
    }

    const raw = this.tipoForm.getRawValue();
    const payload: RoomTypeFormValue = {
      codigo: raw.codigo.trim(),
      nombre: raw.nombre.trim(),
      descripcion: raw.descripcion.trim(),
      precioNoche: Number(raw.precioNoche),
      capacidad: Number(raw.capacidad),
      urlImagen: raw.urlImagen.trim() || '/images/Habitacion1.avif'
    };

    if (this.isEditMode && this.currentId !== null) {
      this.roomTypeService.update(this.currentId, payload);
    } else {
      this.roomTypeService.create(payload);
    }

    this.router.navigate(['/roomtypes']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
