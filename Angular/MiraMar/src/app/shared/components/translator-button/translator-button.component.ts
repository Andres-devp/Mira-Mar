import { Component, OnInit } from '@angular/core';
import { TranslationService } from 'src/app/core/services/translator.service';

@Component({
  selector: 'app-translator-button',
  templateUrl: './translator-button.component.html',
  styleUrls: ['./translator-button.component.css']
})
export class TranslatorButtonComponent implements OnInit {
  currentLanguage: string = 'es';
  isTranslating: boolean = false;

  constructor(private translationService: TranslationService) {
    // Cargar idioma guardado
    const savedLanguage = localStorage.getItem('currentLanguage');
    if (savedLanguage) {
      this.currentLanguage = savedLanguage;
    }
  }

  ngOnInit(): void {
    // Aplicar idioma guardado
    document.documentElement.lang = this.currentLanguage;
  }

  toggleLanguage(): void {
    this.isTranslating = true;
    const newLanguage = this.currentLanguage === 'es' ? 'en' : 'es';
    
    // Cambiar el idioma de manera simple
    this.currentLanguage = newLanguage;
    document.documentElement.lang = newLanguage;
    
    // Guardar preferencia
    localStorage.setItem('currentLanguage', newLanguage);
    
    // Recargar página para aplicar cambios (opcional, pero asegura que todo se actualice)
    // Comentado para permitir cambio sin recarga, pero disponible si es necesario
    // window.location.reload();
    
    this.isTranslating = false;
    
    console.log('Idioma cambiado a: ' + (newLanguage === 'es' ? 'Español' : 'English'));
    
    // Emitir evento para que otros componentes puedan reaccionar
    window.dispatchEvent(new CustomEvent('languageChanged', { 
      detail: { language: newLanguage }
    }));
  }

  get languageLabel(): string {
    return this.currentLanguage === 'es' ? 'ES' : 'EN';
  }

  get languageTooltip(): string {
    return this.currentLanguage === 'es' ? 'Cambiar a inglés' : 'Cambiar a español';
  }
}
