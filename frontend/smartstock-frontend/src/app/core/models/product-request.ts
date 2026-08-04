export interface ProductRequest {
  name: string;
  sku: string;
  price: number;
    quantity: number;
  categoryId: number;
  supplierId: number;
  addToInventory: boolean;
  stockLevel: number | null;
  minimumStock: number | null;
}
