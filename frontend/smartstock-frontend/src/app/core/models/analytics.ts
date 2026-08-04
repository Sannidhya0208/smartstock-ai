export interface AnalyticsResponse {
  totalProducts: number;
  totalInventoryValue: number;
  lowStockProducts: number;
  outOfStockProducts: number;
  averageProductPrice: number;
  averageStockLevel: number;
}

export interface DemandForecastResponse {
  productId: number;
  productName: string;
  currentStock: number;
  averageDailyDemand: number;
  forecastDays: number;
  predictedDemand: number;
  recommendedReorderQuantity: number;
  stockRisk: string;
}
