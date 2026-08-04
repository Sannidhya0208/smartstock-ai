export interface Inventory {
  id: number;
  productId: number;
  productName: string;
  stockLevel: number;
  minimumStock: number;
}

export interface InventoryRequest {
  productId: number;
  stockLevel: number;
  minimumStock: number;
}

export interface StockRequest {
  quantity: number;
}

export interface StockResponse {
  inventoryId: number;
  productName: string;
  stockLevel: number;
  message: string;
}
