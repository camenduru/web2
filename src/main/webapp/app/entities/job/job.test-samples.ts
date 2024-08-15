import dayjs from 'dayjs/esm';

import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: '69c2a070-5eab-4722-94c0-c1c7d5508873',
  login: 'immediately',
  date: dayjs('2024-08-14T22:51'),
  status: 'DONE',
  type: 'before',
  command: 'seldom',
  amount: 'satiate',
  notifyUri: 'supplement vibrant brightly',
  notifyToken: 'clearly',
  discordUsername: 'briskly',
  discordId: 'however till deflect',
  discordChannel: 'than concrete',
  discordToken: 'hence',
  source: 'IOS',
  result: 'unaccountably beneath after',
};

export const sampleWithPartialData: IJob = {
  id: 'd3f47333-22d7-4faa-8003-3356935b09cb',
  login: 'whoever',
  date: dayjs('2024-08-14T20:17'),
  status: 'FAILED',
  type: 'mmm',
  command: 'pro discourse throughout',
  amount: 'but eek gee',
  notifyUri: 'dry bah snappy',
  notifyToken: 'tenet',
  discordUsername: 'reluctantly mmm',
  discordId: 'embed boo',
  discordChannel: 'against',
  discordToken: 'awful into apud',
  source: 'WEB',
  result: 'ah',
};

export const sampleWithFullData: IJob = {
  id: '668cc787-d61c-49c8-8f0e-7e64bd12f545',
  login: 'apropos webbed fisherman',
  date: dayjs('2024-08-15T01:59'),
  status: 'WAITING',
  type: 'who',
  command: 'acidic woot sole',
  amount: 'obediently deviation compulsion',
  notifyUri: 'ick while',
  notifyToken: 'dart worse boldly',
  discordUsername: 'provided resent',
  discordId: 'before',
  discordChannel: 'glorious',
  discordToken: 'however speedy',
  source: 'IOS',
  result: 'sure-footed gee tidy',
};

export const sampleWithNewData: NewJob = {
  login: 'kooky cruelly wearily',
  date: dayjs('2024-08-15T06:07'),
  status: 'WORKING',
  type: 'throughout now',
  command: 'excepting unabashedly officially',
  amount: 'calculus muted',
  notifyUri: 'provided ha',
  notifyToken: 'emphasise reader ferociously',
  discordUsername: 'aboard idolized',
  discordId: 'brr',
  discordChannel: 'yuck mete',
  discordToken: 'gosh',
  source: 'IOS',
  result: 'notwithstanding as tired',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
