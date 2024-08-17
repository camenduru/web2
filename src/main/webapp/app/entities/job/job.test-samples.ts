import dayjs from 'dayjs/esm';

import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: '9c2a0705-eab7-4224-8c0c-1c7d55088735',
  login: 'what manufacturer',
  date: dayjs('2024-08-15T10:40'),
  status: 'WAITING',
  type: 'seldom',
  command: 'satiate',
  amount: 'supplement vibrant brightly',
  notifyUri: 'clearly',
  notifyToken: 'briskly',
  discordUsername: 'however till deflect',
  discordId: 'than concrete',
  discordChannel: 'hence',
  discordToken: 'disguised',
  source: 'API',
  total: 'hmph hmph pfft',
  result: 'altruistic',
};

export const sampleWithPartialData: IJob = {
  id: '03335693-5b09-4cb2-b0fd-5467ed93b616',
  login: 'seriously',
  date: dayjs('2024-08-15T08:16'),
  status: 'CANCELED',
  type: 'nor mmm',
  command: 'plane zowie to',
  amount: 'per',
  notifyUri: 'geez manicure',
  notifyToken: 'monsoon telemeter',
  discordUsername: 'cloud',
  discordId: 'whenever',
  discordChannel: 'emulsify',
  discordToken: 'hm meaningfully',
  source: 'API',
  total: 'vast',
  result: 'wassail',
};

export const sampleWithFullData: IJob = {
  id: 'b764668c-c787-4d61-bc9c-8f0e7e64bd12',
  login: 'tightly',
  date: dayjs('2024-08-15T02:01'),
  status: 'CANCELED',
  type: 'whose',
  command: 'fisherman indeed witter',
  amount: 'cauliflower funny',
  notifyUri: 'gadzooks nocturnal',
  notifyToken: 'yippee',
  discordUsername: 'aw badly',
  discordId: 'winner short-term',
  discordChannel: 'extremely coffee even',
  discordToken: 'able',
  source: 'API',
  total: 'unaccountably',
  result: 'disinherit pitcher',
};

export const sampleWithNewData: NewJob = {
  login: 'inconsequential unless night',
  date: dayjs('2024-08-14T17:10'),
  status: 'EXPIRED',
  type: 'kooky cruelly wearily',
  command: 'respect supposing',
  amount: 'where',
  notifyUri: 'frantically',
  notifyToken: 'officially unloose mostly',
  discordUsername: 'fatherly rope',
  discordId: 'liquidize meh if',
  discordChannel: 'unethically',
  discordToken: 'idolized',
  source: 'WEB',
  total: 'gadzooks yuck mete',
  result: 'gosh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
