export interface StockTransaction {
  id: number;
  productId: number;
  productName: string;
  transactionType: 'STOCK_IN' | 'STOCK_OUT';
  quantity: number;
  stockBefore: number;
  stockAfter: number;
  createdAt: string;
}
