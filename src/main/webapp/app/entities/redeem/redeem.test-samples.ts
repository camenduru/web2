import dayjs from 'dayjs/esm';

import { IRedeem, NewRedeem } from './redeem.model';

export const sampleWithRequiredData: IRedeem = {
  id: '7705a858-c231-406e-be23-6e4cca1d1af7',
  login: 'however thrifty',
  date: dayjs('2024-08-14T17:55'),
  status: 'WAITING',
  type: 'tingling shocking lest',
  author: 'even phooey critical',
  amount: 'retire knottily',
  code: 'forenenst unless privilege',
};

export const sampleWithPartialData: IRedeem = {
  id: '32468d85-b2ef-40f7-afd2-696b7eaa3693',
  login: 'weakness through profit',
  date: dayjs('2024-08-15T10:40'),
  status: 'USED',
  type: 'via yippee whenever',
  author: 'tights',
  amount: 'uh-huh or',
  code: 'furthermore',
};

export const sampleWithFullData: IRedeem = {
  id: 'e42971bb-b39e-461d-a938-6e7d0f22b82b',
  login: 'silly sender indeed',
  date: dayjs('2024-08-15T14:43'),
  status: 'FAILED',
  type: 'motivation',
  author: 'doorstep shell false',
  amount: 'that whereas rest',
  code: 'yet',
};

export const sampleWithNewData: NewRedeem = {
  login: 'entrance lest',
  date: dayjs('2024-08-15T06:55'),
  status: 'FAILED',
  type: 'nor righteously',
  author: 'scarily ick',
  amount: 'excluding',
  code: 'pro',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
