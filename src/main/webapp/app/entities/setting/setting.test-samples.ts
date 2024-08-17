import { ISetting, NewSetting } from './setting.model';

export const sampleWithRequiredData: ISetting = {
  id: 'e9dc53e7-bb82-44c5-86d9-a73e100a5b54',
  login: 'unacceptable',
  total: 'psst',
  membership: 'PAID',
  notifyUri: 'yearningly outside',
  notifyToken: 'subdued widget',
  discordUsername: 'underneath wordy gift',
  discordId: 'ah',
  discordChannel: 'yuck hog sieve',
  discordToken: 'fray obi against',
  apiKey: 'ha yowza',
};

export const sampleWithPartialData: ISetting = {
  id: 'f20625cb-e403-43bd-b02c-daf1d7d38df0',
  login: 'inasmuch',
  total: 'officially',
  membership: 'PAID',
  notifyUri: 'jaggedly readdress',
  notifyToken: 'smoothly',
  discordUsername: 'ouch',
  discordId: 'treat',
  discordChannel: 'shocked furthermore',
  discordToken: 'instructive',
  apiKey: 'absent for',
};

export const sampleWithFullData: ISetting = {
  id: '287bd8fc-d191-4218-a8f8-99a7ae149680',
  login: 'meanwhile yippee adept',
  total: 'attest punctually commonsense',
  membership: 'FREE',
  notifyUri: 'gracefully bewitched',
  notifyToken: 'alarmed',
  discordUsername: 'ugh strap',
  discordId: 'likewise certainly eliminate',
  discordChannel: 'esteemed prowl old',
  discordToken: 'by',
  apiKey: 'ah',
};

export const sampleWithNewData: NewSetting = {
  login: 'atrium candid',
  total: 'vainly poorly',
  membership: 'FREE',
  notifyUri: 'ew',
  notifyToken: 'hand-holding beside',
  discordUsername: 'than indeed despite',
  discordId: 'amid analyst fairly',
  discordChannel: 'robust',
  discordToken: 'down beside unless',
  apiKey: 'unlike',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
