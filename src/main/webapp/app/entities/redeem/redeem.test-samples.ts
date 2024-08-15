import dayjs from 'dayjs/esm';

import { IRedeem, NewRedeem } from './redeem.model';

export const sampleWithRequiredData: IRedeem = {
  id: '77cc2174-07e9-4ee0-8922-341abe087410',
  login: 'unsteady delightfully definite',
  date: dayjs('2024-08-14T20:23'),
  status: 'USED',
  type: 'sneeze',
  author: 'objection upliftingly',
  amount: 'gah whoa',
  code: 'shipper pish hmph',
};

export const sampleWithPartialData: IRedeem = {
  id: '72a7161a-ad53-4955-aad9-05e8fb3e1b3e',
  login: 'excitedly',
  date: dayjs('2024-08-15T08:26'),
  status: 'FAILED',
  type: 'costly',
  author: 'mmm aggravating',
  amount: 'forceful',
  code: 'boohoo',
};

export const sampleWithFullData: IRedeem = {
  id: 'be28db2d-0819-46a8-afe1-9833679ff006',
  login: 'until after yet',
  date: dayjs('2024-08-14T20:42'),
  status: 'CANCELED',
  type: 'shyly sunny feline',
  author: 'yearly',
  amount: 'or',
  code: 'for',
};

export const sampleWithNewData: NewRedeem = {
  login: 'unlike',
  date: dayjs('2024-08-15T02:50'),
  status: 'EXPIRED',
  type: 'scare gadzooks',
  author: 'though wonderful',
  amount: 'blue unfolded',
  code: 'mockingly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
