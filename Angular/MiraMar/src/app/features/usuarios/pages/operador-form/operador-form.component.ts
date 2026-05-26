import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OperatorService } from '../../../../core/services/operator.service';

@Component({
  selector: 'app-operador-form',
  templateUrl: './operador-form.component.html',
  styleUrls: ['./operador-form.component.css']
})
export class OperadorFormComponent {
  operadorForm: FormGroup;
  isNew = true;
  currentId: number | null = null;
  pageTitle = 'Nuevo Operador';
  submitLabel = 'Crear operador';

  constructor(
    private fb: FormBuilder,
    private operatorService: OperatorService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.operadorForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      usuario: ['', Validators.required],
      email: ['', Validators.required],
      contrasena: ['', Validators.required],
      cedula: ['', Validators.required],
      telefono: ['']
    });

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isNew = false;
        this.currentId = Number(id);
        this.pageTitle = 'Editar Operador';
        this.submitLabel = 'Guardar cambios';
        this.operadorForm.get('contrasena')!.clearValidators();
        this.operadorForm.get('contrasena')!.updateValueAndValidity();

        this.operatorService.getById(this.currentId).subscribe({
          next: (op) => {
            this.operadorForm.patchValue({
              nombre: op.nombre,
              apellido: op.apellido,
              usuario: op.usuario,
              email: op.email,
              contrasena: '',
              cedula: op.cedula,
              telefono: op.telefono || ''
            });
          },
          error: (err) => console.error('Error loading operator:', err)
        });
      }
    });
  }

  save(): void {
    if (this.operadorForm.valid) {
      const payload = this.operadorForm.value;
      if (this.isNew) {
        this.operatorService.create(payload).subscribe({
          next: () => this.router.navigate(['/operadores']),
          error: (err) => console.error('Error creating operator:', err)
        });
      } else if (this.currentId !== null) {
        this.operatorService.update(this.currentId, payload).subscribe({
          next: () => this.router.navigate(['/operadores']),
          error: (err) => console.error('Error updating operator:', err)
        });
      }
    }
  }
}
