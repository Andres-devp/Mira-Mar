import { Component } from '@angular/core';
import { TranslationService } from 'src/app/core/services/translator.service';

@Component({
  selector: 'app-translator-button',
  templateUrl: './translator-button.component.html',
  styleUrls: ['./translator-button.component.css']
})
export class TranslatorButtonComponent {
  currentLanguage: string = 'es';
  isTranslating: boolean = false;

  constructor(private translationService: TranslationService) { }

  toggleLanguage(): void {
    this.isTranslating = true;
    const pageText = document.body.innerText;
    
    // Nota: Para una verdadera traducción de página, necesitarías integrar
    // con una solución como Google Translate o i18n
    // Por ahora, esto es una demostración de la API
    
    const newLanguage = this.currentLanguage === 'es' ? 'en' : 'es';
    
    this.translationService.toggleLanguage(pageText.substring(0, 500), this.currentLanguage)
      .subscribe(
        (response) => {
          // En una implementación real, aplicarías la traducción al DOM
          console.log('Traducción:', response);
          this.currentLanguage = newLanguage;
          this.isTranslating = false;
          
          // Cambiar el idioma en el localStorage si es necesario
          localStorage.setItem('currentLanguage', this.currentLanguage);
        },
        (error) => {
          console.error('Error en traducción:', error);
          this.isTranslating = false;
        }
      );
  }

  get languageLabel(): string {
    return this.currentLanguage === 'es' ? 'ES' : 'EN';
  }

  get languageTooltip(): string {
    return this.currentLanguage === 'es' ? 'Cambiar a inglés' : 'Cambiar a español';
  }
}
