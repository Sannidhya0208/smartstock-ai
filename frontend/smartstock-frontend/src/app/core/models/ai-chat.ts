export interface AiChatRequest {
  question: string;
}

export interface AiChatResponse {
  question: string;
  answer: string;
  model: string;
  generatedAt: string;
}

export interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
  createdAt: string;
  model?: string;
}
