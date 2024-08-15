import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISetting, NewSetting } from '../setting.model';

export type PartialUpdateSetting = Partial<ISetting> & Pick<ISetting, 'id'>;

export type EntityResponseType = HttpResponse<ISetting>;
export type EntityArrayResponseType = HttpResponse<ISetting[]>;

@Injectable({ providedIn: 'root' })
export class SettingService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/settings');

  create(setting: NewSetting): Observable<EntityResponseType> {
    return this.http.post<ISetting>(this.resourceUrl, setting, { observe: 'response' });
  }

  update(setting: ISetting): Observable<EntityResponseType> {
    return this.http.put<ISetting>(`${this.resourceUrl}/${this.getSettingIdentifier(setting)}`, setting, { observe: 'response' });
  }

  partialUpdate(setting: PartialUpdateSetting): Observable<EntityResponseType> {
    return this.http.patch<ISetting>(`${this.resourceUrl}/${this.getSettingIdentifier(setting)}`, setting, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ISetting>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISetting[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSettingIdentifier(setting: Pick<ISetting, 'id'>): string {
    return setting.id;
  }

  compareSetting(o1: Pick<ISetting, 'id'> | null, o2: Pick<ISetting, 'id'> | null): boolean {
    return o1 && o2 ? this.getSettingIdentifier(o1) === this.getSettingIdentifier(o2) : o1 === o2;
  }

  addSettingToCollectionIfMissing<Type extends Pick<ISetting, 'id'>>(
    settingCollection: Type[],
    ...settingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const settings: Type[] = settingsToCheck.filter(isPresent);
    if (settings.length > 0) {
      const settingCollectionIdentifiers = settingCollection.map(settingItem => this.getSettingIdentifier(settingItem));
      const settingsToAdd = settings.filter(settingItem => {
        const settingIdentifier = this.getSettingIdentifier(settingItem);
        if (settingCollectionIdentifiers.includes(settingIdentifier)) {
          return false;
        }
        settingCollectionIdentifiers.push(settingIdentifier);
        return true;
      });
      return [...settingsToAdd, ...settingCollection];
    }
    return settingCollection;
  }
}
