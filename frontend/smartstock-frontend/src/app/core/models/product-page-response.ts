
import { Product } from "./product";

export interface ProductPageResponse {
  content: Product[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}