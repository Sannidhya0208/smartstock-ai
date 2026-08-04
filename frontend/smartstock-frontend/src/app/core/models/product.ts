export interface Product {
    id: number;
    name: string;
    sku: string;
    price: number;
    quantity: number;

    categoryId: number | null;
    category: string | null;

    supplierId: number | null;
    supplier: string | null;
    addToInventory: boolean;
    stockLevel: number | null;
    minimumStock: number | null;

}