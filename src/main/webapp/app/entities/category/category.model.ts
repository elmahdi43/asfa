import { IProduct } from 'app/entities/product/product.model';

export interface ICategory {
  id: number;
  label?: string | null;
  summary?: string | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
