import dayjs from 'dayjs/esm';
import { RedeemStatus } from 'app/entities/enumerations/redeem-status.model';

export interface IRedeem {
  id: string;
  login?: string | null;
  date?: dayjs.Dayjs | null;
  status?: keyof typeof RedeemStatus | null;
  type?: string | null;
  author?: string | null;
  amount?: string | null;
  code?: string | null;
}

export type NewRedeem = Omit<IRedeem, 'id'> & { id: null };
