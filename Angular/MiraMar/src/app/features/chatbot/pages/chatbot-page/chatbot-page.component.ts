import { Component, OnInit } from '@angular/core';
import { ChatbotService, ChatbotResponse } from 'src/app/core/services/chatbot.service';

interface ChatMessage {
  text: string;
  sender: 'user' | 'bot';
  timestamp: Date;
}

@Component({
  selector: 'app-chatbot-page',
  templateUrl: './chatbot-page.component.html',
  styleUrls: ['./chatbot-page.component.css']
})
export class ChatbotPageComponent implements OnInit {
  messages: ChatMessage[] = [];
  userInput: string = '';
  selectedContext: string = '';
  isLoading: boolean = false;
  contexts = [
    { value: '', label: 'General' },
    { value: 'rooms', label: 'Habitaciones' },
    { value: 'prices', label: 'Precios' },
    { value: 'services', label: 'Servicios' },
    { value: 'reservations', label: 'Reservaciones' }
  ];

  constructor(private chatbotService: ChatbotService) { }

  ngOnInit(): void {
    // Mensaje de bienvenida
    this.messages.push({
      text: '¡Hola! Bienvenido al chat inteligente de Mira Mar. ¿En qué puedo ayudarte hoy?',
      sender: 'bot',
      timestamp: new Date()
    });
  }

  sendMessage(): void {
    if (!this.userInput.trim()) {
      return;
    }

    // Agregar mensaje del usuario
    this.messages.push({
      text: this.userInput,
      sender: 'user',
      timestamp: new Date()
    });

    this.isLoading = true;
    const message = this.userInput;
    this.userInput = '';

    // Hacer la llamada al servicio
    this.chatbotService.ask({
      message: message,
      context: this.selectedContext
    }).subscribe(
      (response: ChatbotResponse) => {
        this.messages.push({
          text: response.response,
          sender: 'bot',
          timestamp: new Date()
        });
        this.isLoading = false;
      },
      (error) => {
        console.error('Error completo:', error);
        let errorMsg = 'Disculpa, ocurrió un error. Por favor intenta de nuevo.';
        
        // Mostrar detalles del error para debug
        if (error.error && typeof error.error === 'object') {
          errorMsg = error.error.message || error.error;
        } else if (error.error) {
          errorMsg = error.error;
        } else if (error.message) {
          errorMsg = error.message;
        } else if (error.status === 401) {
          errorMsg = 'No autorizado. Por favor inicia sesión.';
        } else if (error.status === 403) {
          errorMsg = 'Acceso denegado. No tienes permisos para usar el chatbot.';
        } else if (error.status === 0) {
          errorMsg = 'No se puede conectar al servidor. Verifica que el backend esté corriendo en localhost:8080.';
        }
        
        this.messages.push({
          text: errorMsg,
          sender: 'bot',
          timestamp: new Date()
        });
        this.isLoading = false;
      }
    );
  }

  clearChat(): void {
    this.messages = [{
      text: '¡Hola! Bienvenido al chat inteligente de Mira Mar. ¿En qué puedo ayudarte hoy?',
      sender: 'bot',
      timestamp: new Date()
    }];
  }
}
