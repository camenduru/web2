import { IApp, NewApp } from './app.model';

export const sampleWithRequiredData: IApp = {
  id: '13b4218b-fa0f-46b1-a1ae-c426d3e9aca5',
  type: 'earn gosh',
  amount: 'transfix sanctify shy',
  schema: 'captor but opposite',
  model: 'whose aw',
  title: 'snaffle which',
  isDefault: true,
  isActive: false,
  isFree: false,
};

export const sampleWithPartialData: IApp = {
  id: 'ee4cfa6f-73b4-44fc-b414-9da19cb47c75',
  type: 'yahoo cancel notify',
  amount: 'whoa publicise',
  schema: 'surge',
  model: 'if',
  title: 'between well',
  isDefault: true,
  isActive: true,
  isFree: false,
};

export const sampleWithFullData: IApp = {
  id: 'b168eeeb-d071-44bd-bea3-38ff8acd4d49',
  type: 'enforce',
  amount: 'decision',
  schema: 'bah wherever',
  model: 'playfully gah yippee',
  title: 'out dull yuck',
  isDefault: false,
  isActive: true,
  isFree: false,
};

export const sampleWithNewData: NewApp = {
  type: 'whoa scarcely',
  amount: 'off boy oh',
  schema: 'that pro',
  model: 'jump roll solid',
  title: 'gazunder fooey',
  isDefault: true,
  isActive: false,
  isFree: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
