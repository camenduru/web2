import dayjs from 'dayjs/esm';

import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: '84010a82-fca9-4fc2-8c94-77f50330a7e2',
  login: 'sans',
  date: dayjs('2024-08-14T23:40'),
  status: 'WORKING',
  type: 'selfishly jagged',
  command: 'woot gee stem',
  amount: 'crawl photodiode',
  notifyUri: 'briskly ultimately questionably',
  notifyToken: 'patiently process',
  discordUsername: 'ew ick',
  discordId: 'private an within',
  discordChannel: 'off saucer',
  discordToken: 'measurement entree',
  source: 'DISCORD',
  result: 'substantial vice uncomfortable',
};

export const sampleWithPartialData: IJob = {
  id: 'f3a0cb00-ba6f-4152-9c28-2060f6693ed4',
  login: 'enrage defiantly blocker',
  date: dayjs('2024-08-14T21:50'),
  status: 'WORKING',
  type: 'or into',
  command: 'wrong lumberman ripple',
  amount: 'handmade',
  notifyUri: 'sedately even',
  notifyToken: 'wonder onto sharply',
  discordUsername: 'bale except except',
  discordId: 'undercut',
  discordChannel: 'kindly whether',
  discordToken: 'noxious once chop',
  source: 'WEB',
  result: 'dull',
};

export const sampleWithFullData: IJob = {
  id: 'c1d4d5fb-716e-472e-a42c-b848efe3b98a',
  login: 'ugh jeep',
  date: dayjs('2024-08-14T18:52'),
  status: 'DONE',
  type: 'by among table',
  command: 'along yarn',
  amount: 'marry courageous',
  notifyUri: 'cloves for',
  notifyToken: 'intellect yieldingly mangle',
  discordUsername: 'up',
  discordId: 'documentation whoa outside',
  discordChannel: 'since',
  discordToken: 'aha ah',
  source: 'WEB',
  result: 'silently gracefully afore',
};

export const sampleWithNewData: NewJob = {
  login: 'modern uh-huh',
  date: dayjs('2024-08-15T14:52'),
  status: 'FAILED',
  type: 'into though undercharge',
  command: 'subsidy sweetly',
  amount: 'milky',
  notifyUri: 'austere',
  notifyToken: 'meh',
  discordUsername: 'because',
  discordId: 'vainly',
  discordChannel: 'record circa',
  discordToken: 'burn-out ethnicity even',
  source: 'IOS',
  result: 'ample',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
