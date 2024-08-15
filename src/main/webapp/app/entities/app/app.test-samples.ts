import { IApp, NewApp } from './app.model';

export const sampleWithRequiredData: IApp = {
  id: '3b4218bf-a0f6-4b11-aaec-426d3e9aca58',
  type: 'cruelly recite',
  amount: 'an aside',
  schema: 'decimal noisily',
  model: 'mode',
  title: 'nippy bravely yippee',
  isDefault: false,
  isActive: false,
  isFree: false,
  cooldown: 'decelerate',
};

export const sampleWithPartialData: IApp = {
  id: '9e0bdee4-cfa6-4f73-9b44-fc4149da19cb',
  type: 'pony unto',
  amount: 'including',
  schema: 'friendly before',
  model: 'even voyage pfft',
  title: 'violently aha up',
  isDefault: false,
  isActive: false,
  isFree: false,
  cooldown: 'on',
};

export const sampleWithFullData: IApp = {
  id: '338ff8ac-d4d4-49e1-903e-4bc448f52163',
  type: 'downstairs',
  amount: 'brr which proliferate',
  schema: 'behind cultivated daffodil',
  model: 'excepting',
  title: 'upright enormously rigidly',
  isDefault: false,
  isActive: false,
  isFree: false,
  cooldown: 'across',
};

export const sampleWithNewData: NewApp = {
  type: 'beside pleasant contour',
  amount: 'insistent hair',
  schema: 'supposing impressionable',
  model: 'hemorrhage blindly',
  title: 'facsimile striped freely',
  isDefault: false,
  isActive: true,
  isFree: true,
  cooldown: 'for compartmentalize',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
