import { ISetting, NewSetting } from './setting.model';

export const sampleWithRequiredData: ISetting = {
  id: 'e11fd165-4cf5-4565-be5f-015f2b27ef25',
  login: 'whoa while',
  total: 'twirl astride following',
  membership: 'FREE',
  notifyUri: 'gosh moult',
  notifyToken: 'outset',
  discordUsername: 'soil',
  discordId: 'gripping flaky',
  discordChannel: 'whose',
  discordToken: 'grubby uselessly',
};

export const sampleWithPartialData: ISetting = {
  id: '2e029381-80c0-4ad7-a489-b26963a3b517',
  login: 'mostly',
  total: 'yawningly',
  membership: 'PAID',
  notifyUri: 'judgementally',
  notifyToken: 'doubtfully closely',
  discordUsername: 'even at',
  discordId: 'miniaturise',
  discordChannel: 'that dilate',
  discordToken: 'oh',
};

export const sampleWithFullData: ISetting = {
  id: '5db0bdb8-ccbe-487b-aa67-e420d6d58302',
  login: 'mmm',
  total: 'until boohoo',
  membership: 'PAID',
  notifyUri: 'throughout ack worth',
  notifyToken: 'specify weasel',
  discordUsername: 'following tan why',
  discordId: 'zowie interestingly',
  discordChannel: 'dual',
  discordToken: 'a',
};

export const sampleWithNewData: NewSetting = {
  login: 'abaft',
  total: 'where stress glance',
  membership: 'PAID',
  notifyUri: 'what',
  notifyToken: 'during',
  discordUsername: 'mountainous',
  discordId: 'hm jive stalk',
  discordChannel: 'frankly throughout',
  discordToken: 'boohoo unless',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
