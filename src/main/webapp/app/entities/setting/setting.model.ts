import { Membership } from 'app/entities/enumerations/membership.model';

export interface ISetting {
  id: string;
  login?: string | null;
  total?: string | null;
  membership?: keyof typeof Membership | null;
  notifyUri?: string | null;
  notifyToken?: string | null;
  discordUsername?: string | null;
  discordId?: string | null;
  discordChannel?: string | null;
  discordToken?: string | null;
}

export type NewSetting = Omit<ISetting, 'id'> & { id: null };
