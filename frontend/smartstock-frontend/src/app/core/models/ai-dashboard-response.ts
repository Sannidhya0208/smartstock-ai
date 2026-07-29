export interface AiDashboardResponse {
  totalProducts: number;
  totalInventoryItems: number;
  lowStockItems: number;
  outOfStockItems: number;
  totalInventoryValue: number;
  aiSummary: string;
  model: string;
  generatedAt: string;
}
