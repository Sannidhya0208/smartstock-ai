import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  ViewChild,
  inject
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { finalize } from 'rxjs';

import {
  ChatMessage
} from '../../core/models/ai-chat';

import {
  AiService
} from '../../core/services/ai';

@Component({
  selector: 'app-ai-assistant',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './ai-assistant.html',
  styleUrl: './ai-assistant.css'
})
export class AiAssistant {
  private readonly aiService =
    inject(AiService);

  private readonly formBuilder =
    inject(FormBuilder);

  private readonly changeDetector =
    inject(ChangeDetectorRef);

  @ViewChild('chatContainer')
  private chatContainer?: ElementRef<HTMLDivElement>;

  isLoading = false;
  errorMessage = '';

  messages: ChatMessage[] = [
    {
      role: 'assistant',
      content:
        'Hello! I am SmartStock AI. Ask me about stock risks, restocking priorities, inventory levels, demand forecasts or recent stock movements.',
      createdAt: new Date().toISOString()
    }
  ];

  readonly suggestedQuestions = [
    'Which products need restocking?',
    'Which products are currently out of stock?',
    'What are the biggest inventory risks?',
    'Which products had the most stock-out activity?',
    'Give me three practical inventory recommendations.'
  ];

  questionForm =
    this.formBuilder.nonNullable.group({
      question: [
        '',
        [
          Validators.required,
          Validators.maxLength(500)
        ]
      ]
    });

  sendQuestion(): void {
    if (
      this.questionForm.invalid ||
      this.isLoading
    ) {
      this.questionForm.markAllAsTouched();
      return;
    }

    const question =
      this.questionForm.controls
        .question.value.trim();

    if (!question) {
      return;
    }

    this.messages.push({
      role: 'user',
      content: question,
      createdAt: new Date().toISOString()
    });

    this.questionForm.reset({
      question: ''
    });

    this.errorMessage = '';
    this.isLoading = true;

    this.scrollToBottom();

    this.aiService
      .askQuestion(question)
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.changeDetector.detectChanges();
          this.scrollToBottom();
        })
      )
      .subscribe({
        next: response => {
          this.messages.push({
            role: 'assistant',
            content: response.answer,
            createdAt: response.generatedAt,
            model: response.model
          });
        },

        error: error => {
          console.error(
            'AI assistant request failed:',
            error
          );

          if (error.status === 503) {
            this.errorMessage =
              'The AI service is unavailable. Make sure Ollama is running.';
          } else if (
            error.status === 401 ||
            error.status === 403
          ) {
            this.errorMessage =
              'Your session has expired. Please log in again.';
          } else if (error.status === 0) {
            this.errorMessage =
              'Unable to connect to the backend server.';
          } else {
            this.errorMessage =
              error.error?.message ??
              'Unable to generate an AI response.';
          }
        }
      });
  }

  useSuggestion(question: string): void {
    if (this.isLoading) {
      return;
    }

    this.questionForm.setValue({
      question
    });

    this.sendQuestion();
  }

  clearConversation(): void {
    if (this.isLoading) {
      return;
    }

    this.messages = [
      {
        role: 'assistant',
        content:
          'Conversation cleared. What would you like to know about your inventory?',
        createdAt: new Date().toISOString()
      }
    ];

    this.errorMessage = '';
    this.changeDetector.detectChanges();
  }

  handleKeydown(event: KeyboardEvent): void {
    if (
      event.key === 'Enter' &&
      !event.shiftKey
    ) {
      event.preventDefault();
      this.sendQuestion();
    }
  }

  trackMessage(
    index: number,
    message: ChatMessage
  ): string {
    return `${message.role}-${message.createdAt}-${index}`;
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const container =
        this.chatContainer?.nativeElement;

      if (!container) {
        return;
      }

      container.scrollTop =
        container.scrollHeight;
    });
  }
}
