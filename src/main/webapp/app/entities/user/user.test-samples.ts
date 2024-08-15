import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: '7a29b8ac-c3a4-4ae0-86c4-74a7e0864369',
  login: '5',
};

export const sampleWithPartialData: IUser = {
  id: 'fb75f9e4-7d8a-4899-8eb7-bdd0f06168cc',
  login: '1@Bzw\\60eY6S\\3CdEgaz\\iK\\Kaf-A9\\\\K',
};

export const sampleWithFullData: IUser = {
  id: 'b96749e9-081c-4d0e-a8bf-db4ecd84d777',
  login: 'rm@2\\Mj\\rwURcr1',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
