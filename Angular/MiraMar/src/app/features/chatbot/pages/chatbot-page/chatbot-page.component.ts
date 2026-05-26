import { Component, OnInit } from '@angular/core';
import { ChatbotService, ChatbotResponse } from 'src/app/core/services/chatbot.service';

interface ChatMessage {
  text?: string;
  html?: string;
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
    this.messages.push(this.createBotMessage(
      'Hola. Soy el concierge digital de Mira Mar. En que puedo ayudarte?'
    ));
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
        this.messages.push(this.createBotMessage(response.response));
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
        
        this.messages.push(this.createBotMessage(errorMsg));
        this.isLoading = false;
      }
    );
  }


  clearChat(): void {
    this.messages = [
      this.createBotMessage('Hola. Soy el concierge digital de Mira Mar. En que puedo ayudarte?')
    ];
  }

  private createBotMessage(raw: string): ChatMessage {
    return {
      html: this.formatBotMessage(raw),
      sender: 'bot',
      timestamp: new Date()
    };
  }

  private formatBotMessage(raw: string): string {
    if (!raw) {
      return '<p></p>';
    }

    const normalized = raw.replace(/\r\n/g, '\n');
    const withoutBold = normalized.replace(/\*\*(.*?)\*\*/g, '$1');
    const lines = withoutBold
      .split('\n')
      .map(line => line.trim())
      .filter(line => line.length > 0);

    const blocks: Array<{ type: 'text' | 'list'; value?: string; items?: string[] }> = [];
    let listItems: string[] = [];
    let textLines: string[] = [];

    const flushText = () => {
      if (textLines.length) {
        blocks.push({ type: 'text', value: textLines.join(' ') });
        textLines = [];
      }
    };

    const flushList = () => {
      if (listItems.length) {
        blocks.push({ type: 'list', items: listItems });
        listItems = [];
      }
    };

    for (const line of lines) {
      const listMatch = line.match(/^[-*]\s+(.*)/);
      if (listMatch) {
        flushText();
        listItems.push(listMatch[1].replace(/\*/g, ''));
      } else {
        flushList();
        textLines.push(line.replace(/\*/g, ''));
      }
    }

    flushText();
    flushList();

    if (!blocks.length) {
      return '<p></p>';
    }

    return blocks
      .map(block => {
        if (block.type === 'list') {
          const items = (block.items || [])
            .map(item => `<li>${this.escapeHtml(item)}</li>`)
            .join('');
          return `<ul class="message-list">${items}</ul>`;
        }
        return `<p>${this.escapeHtml(block.value || '')}</p>`;
      })
      .join('');
  }

  private escapeHtml(value: string): string {
    return value
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;');
  }
}
