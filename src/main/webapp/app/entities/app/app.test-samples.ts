import { IApp, NewApp } from './app.model';

export const sampleWithRequiredData: IApp = {
  id: '27e9e96a-5108-40ab-afc6-df001a1166ba',
  type: 'soupy',
  amount: 'hidden',
  schema: 'aha',
  model: 'unlike excepting compost',
  title: 'wetly unaccountably lonely',
  isDefault: false,
  isActive: false,
  isFree: false,
};

export const sampleWithPartialData: IApp = {
  id: '065c97f0-78c3-4f2d-843c-72dd2e116170',
  type: 'the corner newsstand',
  amount: 'luminous huzzah grill',
  schema: 'anaesthetise',
  model: 'repose off',
  title: 'why speculate',
  isDefault: false,
  isActive: true,
  isFree: true,
};

export const sampleWithFullData: IApp = {
  id: 'e0eb4ea1-5749-41c6-8aeb-7dc7c8c38fa9',
  type: 'round regarding',
  amount: 'faithfully pace',
  schema: 'dickey minimum',
  model: 'night knowledgeable',
  title: 'beyond',
  isDefault: true,
  isActive: false,
  isFree: false,
};

export const sampleWithNewData: NewApp = {
  type: 'brutalise campaigning',
  amount: 'after',
  schema: 'blackbird ack',
  model: 'through as',
  title: 'huddle',
  isDefault: true,
  isActive: false,
  isFree: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
