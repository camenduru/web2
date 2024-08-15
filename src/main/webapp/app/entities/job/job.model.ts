import dayjs from 'dayjs/esm';
import { JobStatus } from 'app/entities/enumerations/job-status.model';
import { JobSource } from 'app/entities/enumerations/job-source.model';

export interface IJob {
  id: string;
  login?: string | null;
  date?: dayjs.Dayjs | null;
  status?: keyof typeof JobStatus | null;
  type?: string | null;
  command?: string | null;
  amount?: string | null;
  notifyUri?: string | null;
  notifyToken?: string | null;
  discordUsername?: string | null;
  discordId?: string | null;
  discordChannel?: string | null;
  discordToken?: string | null;
  source?: keyof typeof JobSource | null;
  total?: string | null;
  result?: string | null;
}

export type NewJob = Omit<IJob, 'id'> & { id: null };
