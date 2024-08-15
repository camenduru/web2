import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'ef5cb807-8ef7-48dc-9ba0-378c54af89cb',
};

export const sampleWithPartialData: IAuthority = {
  name: 'bfd3615b-107e-47ea-8fcd-8b1af71cbabb',
};

export const sampleWithFullData: IAuthority = {
  name: '3703aae4-2614-4741-a5d8-1a60b28e0d42',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
