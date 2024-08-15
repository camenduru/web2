import { ISetting, NewSetting } from './setting.model';

export const sampleWithRequiredData: ISetting = {
  id: 'fe9dc53e-7bb8-424c-956d-9a73e100a5b5',
  login: 'overdue',
  total: 'pamphlet track ah',
  membership: 'FREE',
  notifyUri: 'periodic ha',
  notifyToken: 'hm hurtful punctuate',
  discordUsername: 'few carefree glorify',
  discordId: 'since supposing',
  discordChannel: 'suffuse boom centre',
  discordToken: 'frightfully coolly faithfully',
};

export const sampleWithPartialData: ISetting = {
  id: '3bd02cda-f1d7-4d38-adf0-d41f347154fe',
  login: 'unity',
  total: 'now pish enormously',
  membership: 'FREE',
  notifyUri: 'harmonize frenetically',
  notifyToken: 'well-off salivate flee',
  discordUsername: 'amidst',
  discordId: 'into teeming',
  discordChannel: 'oof',
  discordToken: 'bubbly pro',
};

export const sampleWithFullData: ISetting = {
  id: 'cf42877a-e729-499e-9367-7e0b76f37f06',
  login: 'yowza though',
  total: 'whoever seemingly or',
  membership: 'FREE',
  notifyUri: 'authentic cold grass',
  notifyToken: 'likewise',
  discordUsername: 'whispered entangle yippee',
  discordId: 'italicise',
  discordChannel: 'although',
  discordToken: 'vase easily',
};

export const sampleWithNewData: NewSetting = {
  login: 'lest reason as',
  total: 'mariachi',
  membership: 'FREE',
  notifyUri: 'mist which',
  notifyToken: 'of',
  discordUsername: 'though multicolored why',
  discordId: 'attach unbearably famous',
  discordChannel: 'than',
  discordToken: 'participant next loyally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
