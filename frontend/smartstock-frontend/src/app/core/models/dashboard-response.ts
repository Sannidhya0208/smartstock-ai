export interface DashboardResponse {
  totalProducts: number;
  totalInventoryItems: number;
  lowStockProducts: number;
  outOfStockProducts: number;
  totalInventoryValue: number;
  model: string;
  generatedAt: string;
}
