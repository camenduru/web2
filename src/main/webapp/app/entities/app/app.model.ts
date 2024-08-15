export interface IApp {
  id: string;
  type?: string | null;
  amount?: string | null;
  schema?: string | null;
  model?: string | null;
  title?: string | null;
  isDefault?: boolean | null;
  isActive?: boolean | null;
  isFree?: boolean | null;
  cooldown?: string | null;
}

export type NewApp = Omit<IApp, 'id'> & { id: null };
