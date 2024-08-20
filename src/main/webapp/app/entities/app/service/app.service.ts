import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApp, NewApp } from '../app.model';

export type PartialUpdateApp = Partial<IApp> & Pick<IApp, 'id'>;

export type EntityResponseType = HttpResponse<IApp>;
export type EntityArrayResponseType = HttpResponse<IApp[]>;

@Injectable({ providedIn: 'root' })
export class AppService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apps');

  create(app: NewApp): Observable<EntityResponseType> {
    return this.http.post<IApp>(this.resourceUrl, app, { observe: 'response' });
  }

  update(app: IApp): Observable<EntityResponseType> {
    return this.http.put<IApp>(`${this.resourceUrl}/${this.getAppIdentifier(app)}`, app, { observe: 'response' });
  }

  partialUpdate(app: PartialUpdateApp): Observable<EntityResponseType> {
    return this.http.patch<IApp>(`${this.resourceUrl}/${this.getAppIdentifier(app)}`, app, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IApp>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApp[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppIdentifier(app: Pick<IApp, 'id'>): string {
    return app.id;
  }

  compareApp(o1: Pick<IApp, 'id'> | null, o2: Pick<IApp, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppIdentifier(o1) === this.getAppIdentifier(o2) : o1 === o2;
  }

  addAppToCollectionIfMissing<Type extends Pick<IApp, 'id'>>(appCollection: Type[], ...appsToCheck: (Type | null | undefined)[]): Type[] {
    const apps: Type[] = appsToCheck.filter(isPresent);
    if (apps.length > 0) {
      const appCollectionIdentifiers = appCollection.map(appItem => this.getAppIdentifier(appItem));
      const appsToAdd = apps.filter(appItem => {
        const appIdentifier = this.getAppIdentifier(appItem);
        if (appCollectionIdentifiers.includes(appIdentifier)) {
          return false;
        }
        appCollectionIdentifiers.push(appIdentifier);
        return true;
      });
      return [...appsToAdd, ...appCollection];
    }
    return appCollection;
  }
}
